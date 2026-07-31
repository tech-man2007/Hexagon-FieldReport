package com.hexagon.fieldreport.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexagon.fieldreport.ui.components.GlassContainer

@Composable
fun FloatingNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 32.dp, end = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        GlassContainer(
            cornerRadius = 50.dp,
            modifier = Modifier.height(80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    // 8.dp padding on all sides gives the inner pill room to breathe
                    .padding(8.dp),
                // 4.dp spacedBy gives a tiny gap between the 1/3 sections
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(Icons.Default.Home, "Home", "dashboard", currentRoute, onNavigate)
                NavItem(Icons.Default.Add, "Report", "daily_report", currentRoute, onNavigate)
                NavItem(Icons.Default.Settings, "Settings", "settings", currentRoute, onNavigate)
            }
        }
    }
}

// FIX: Added RowScope to allow mathematical weight distribution
@Composable
private fun RowScope.NavItem(
    icon: ImageVector,
    label: String,
    route: String,
    currentRoute: String,
    onClick: (String) -> Unit
) {
    val isSelected = currentRoute == route
    val isDark = isSystemInDarkTheme()

    val accentColor = if (isDark) Color.Cyan else Color(0xFF0000FF)
    val unselectedColor = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)

    val contentColor = if (isSelected) accentColor else unselectedColor
    val backgroundColor = if (isSelected) accentColor.copy(alpha = 0.15f) else Color.Transparent

    Column(
        modifier = Modifier
            // FIX: This forces the engine to calculate exactly Width / 3
            .weight(1f)
            // FIX: Forces the pill to stretch fully top-to-bottom within the Row's padding
            .fillMaxHeight()
            .clip(RoundedCornerShape(50.dp))
            .background(backgroundColor)
            .clickable { onClick(route) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}