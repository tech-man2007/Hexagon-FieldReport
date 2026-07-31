package com.hexagon.fieldreport.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object DailyReport : Screen("daily_report")
}