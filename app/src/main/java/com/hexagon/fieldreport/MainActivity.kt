package com.hexagon.fieldreport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
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
            val isDark = isSystemInDarkTheme()
            val view = LocalView.current

            // Forces dark status bar icons when in light mode so they are visible
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as android.app.Activity).window
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
                }
            }

            Surface(modifier = Modifier.fillMaxSize()) {
                AppEntryPoint()
            }
        }
    }
}

@Composable
fun AppEntryPoint() {
    val context = androidx.compose.ui.platform.LocalContext.current

    val sharedPrefs = remember {
        context.getSharedPreferences("hexagon_prefs", android.content.Context.MODE_PRIVATE)
    }

    var isLoggedIn by remember {
        mutableStateOf(sharedPrefs.getBoolean("is_logged_in", false))
    }

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = {
                sharedPrefs.edit().putBoolean("is_logged_in", true).apply()
                isLoggedIn = true
            }
        )
    } else {
        MainAppScaffold(
            onLogout = {
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

    val isDark = isSystemInDarkTheme()

    val backgroundModifier = if (isDark) {
        Modifier.background(Brush.verticalGradient(listOf(Color(0xFF263238), Color(0xFF101416))))
    } else {
        Modifier.background(Color.White)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(backgroundModifier)
    ) {
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "dashboard",
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                composable("dashboard") {
                    // Applying backgroundModifier to each individual screen blocks the overlap bug
                    Box(modifier = Modifier.fillMaxSize().then(backgroundModifier), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Dashboard / Overview",
                            color = if (isDark) Color.White else Color.Black
                        )
                    }
                }
                composable("daily_report") {
                    Box(modifier = Modifier.fillMaxSize().then(backgroundModifier)) {
                        DailyReportScreen()
                    }
                }
                composable("settings") {
                    Box(modifier = Modifier.fillMaxSize().then(backgroundModifier)) {
                        SettingsScreen(onLogoutClick = { onLogout() })
                    }
                }
            }
        }

        // True Floating Nav Bar, sits entirely on top of the list
        FloatingNavBar(
            currentRoute = currentRoute,
            onNavigate = { route ->
                if (route != currentRoute) {
                    currentRoute = route
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}