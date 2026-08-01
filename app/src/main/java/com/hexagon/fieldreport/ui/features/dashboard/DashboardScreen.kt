package com.hexagon.fieldreport.ui.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ScheduleTask(
    val dayLabel: String,
    val date: String,
    val planned: String,
    val actual: String,
    val status: TaskStatus
)

enum class TaskStatus { COMPLETED, PENDING, DELAYED }

@Composable
fun DashboardScreen() {
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) Color.White else Color.Black

    val scheduleItems = listOf(
        ScheduleTask("Day 1", "Jul 26", "Site Prep & Safety Brief", "Completed on time", TaskStatus.COMPLETED),
        ScheduleTask("Day 2", "Jul 27", "Material Delivery (Steel)", "Completed on time", TaskStatus.COMPLETED),
        ScheduleTask("Day 3", "Jul 28", "Foundation Pouring", "Weather delay - Partial pour", TaskStatus.DELAYED),
        ScheduleTask("Day 4", "Jul 29", "Foundation Curing", "In progress", TaskStatus.PENDING),
        ScheduleTask("Day 5", "Jul 30", "First Floor Framework", "Pending", TaskStatus.PENDING),
        ScheduleTask("Day 6", "Jul 31", "Plumbing Rough-in", "Pending", TaskStatus.PENDING),
        ScheduleTask("Day 7", "Aug 01", "Weekly Inspection", "Pending", TaskStatus.PENDING)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp)
    ) {
        Text(
            text = "Schedule Tracker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Current 7-Day Cycle",
            fontSize = 16.sp,
            color = textColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            // FIX: Generous bottom padding forces the list to stop scrolling higher up,
            // creating the empty background gap around the floating nav bar corners.
            contentPadding = PaddingValues(top = 8.dp, bottom = 140.dp)
        ) {
            items(scheduleItems) { task ->
                TaskTimelineCard(task = task, isDark = isDark, textColor = textColor)
            }
        }
    }
}

@Composable
fun TaskTimelineCard(task: ScheduleTask, isDark: Boolean, textColor: Color) {
    val cardBg = if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.03f)

    val (statusColor, statusIcon) = when (task.status) {
        TaskStatus.COMPLETED -> Pair(Color(0xFF4CAF50), Icons.Default.Check)
        TaskStatus.DELAYED -> Pair(Color(0xFFF44336), Icons.Default.Warning)
        TaskStatus.PENDING -> Pair(Color.Gray, null)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(statusColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (statusIcon != null) {
                    Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(20.dp))
                } else {
                    Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(statusColor))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${task.dayLabel} • ${task.date}", fontSize = 12.sp, color = textColor.copy(alpha = 0.6f))
                    Text(text = task.status.name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusColor)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Planned: ${task.planned}", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = textColor)
                Text(text = "Actual: ${task.actual}", fontSize = 13.sp, color = textColor.copy(alpha = 0.8f))
            }
        }
    }
}