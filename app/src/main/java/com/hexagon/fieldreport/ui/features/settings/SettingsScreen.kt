package com.hexagon.fieldreport.ui.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onLogoutClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) Color.White else Color.Black
    val cardBgColor = if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.05f)
    val accentColor = if (isDark) Color.Cyan else Color(0xFF0000FF)
    val dangerColor = Color(0xFFE53935)

    val empName = "John Doe"
    val empDesignation = "Senior Site Engineer"
    val empId = "HEX-9482"
    val empEmail = "j.doe@hexagon.com"
    val empPhone = "+91 98765 43210"

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Profile & Settings", color = textColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = accentColor, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(empName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                        Text(empDesignation, fontSize = 14.sp, color = textColor.copy(alpha = 0.7f))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("ACCOUNT INFORMATION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accentColor, modifier = Modifier.padding(bottom = 8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBgColor)
                        .padding(16.dp)
                ) {
                    ProfileRow("Employee ID", empId, textColor)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = textColor.copy(alpha = 0.1f))
                    ProfileRow("Registered Email", empEmail, textColor)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = textColor.copy(alpha = 0.1f))
                    ProfileRow("Phone Number", empPhone, textColor)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Prominent Logout Button inside the scrollable content view
                Button(
                    onClick = onLogoutClick,
                    colors = ButtonDefaults.buttonColors(containerColor = dangerColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Logout", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out Securely", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String, textColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = textColor.copy(alpha = 0.6f))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}