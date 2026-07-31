package com.hexagon.fieldreport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hexagon.fieldreport.ui.features.dailyreport.DailyReportScreen
import com.hexagon.fieldreport.ui.features.login.LoginScreen
import com.hexagon.fieldreport.ui.features.settings.SettingsScreen
import com.hexagon.fieldreport.ui.navigation.FloatingNavBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                AppEntryPoint()
            }
        }
    }
}

@Composable
fun AppEntryPoint() {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Persistent storage instance (survives app background kills)
    val sharedPrefs = remember {
        context.getSharedPreferences("hexagon_prefs", android.content.Context.MODE_PRIVATE)
    }

    // Check if user was previously logged in (defaults to false)
    var isLoggedIn by remember {
        mutableStateOf(sharedPrefs.getBoolean("is_logged_in", false))
    }

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = {
                // Save state permanently when login succeeds
                sharedPrefs.edit().putBoolean("is_logged_in", true).apply()
                isLoggedIn = true
            }
        )
    } else {
        // Main App with Floating Nav Bar & Scaffolds
        MainAppScaffold(
            onLogout = {
                // Clear state permanently on logout
                sharedPrefs.edit().putBoolean("is_logged_in", false).apply()
                isLoggedIn = false
            }
        )
    }
}

@Composable
fun MainAppScaffold(onLogout: () -> Unit) {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf("dashboard") }

    // Industrial / Construction theme background gradient matching your login page
    val isDark = isSystemInDarkTheme()
    val backgroundGradient = if (isDark) {
        Brush.verticalGradient(listOf(Color(0xFF263238), Color(0xFF101416)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFECEFF1), Color(0xFFB0BEC5)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                FloatingNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        currentRoute = route
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "dashboard",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("dashboard") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Dashboard / Overview",
                            color = if (isDark) Color.White else Color.Black
                        )
                    }
                }
                composable("daily_report") {
                    DailyReportScreen()
                }
                composable("settings") {
                    SettingsScreen(
                        onLogoutClick = {
                            onLogout() // Triggers session wipe and returns to login screen
                        }
                    )
                }
            }
        }
    }
}