package com.hexagon.fieldreport.ui.features.export

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExportScreen() {
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.03f)
    val accentColor = if (isDark) Color.Cyan else Color(0xFF0000FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Prevents content overflow
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 140.dp) // Generous bottom clearance matching the nav bar height + gap
    ) {
        Text(
            text = "Export Reports",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Timeframe", color = textColor.copy(alpha = 0.6f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Current 7-Day Cycle", color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text("Jul 26 - Aug 01, 2026", color = textColor.copy(alpha = 0.8f), fontSize = 14.sp)
                }
                Icon(Icons.Default.DateRange, contentDescription = "Select Cycle", tint = accentColor)
            }
        }

        Text(
            text = "Format Options",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(
            onClick = { /* Backend Devs will link PPTX Generation here */ },
            modifier = Modifier.fillMaxWidth().height(60.dp).padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = "PPTX", tint = if (isDark) Color.Black else Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Generate Presentation (.pptx)",
                color = if (isDark) Color.Black else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }

        OutlinedButton(
            onClick = { /* Backend Devs will link Database CSV dump here */ },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor)
        ) {
            Text("Export Raw Data (.csv)")
        }
    }
}