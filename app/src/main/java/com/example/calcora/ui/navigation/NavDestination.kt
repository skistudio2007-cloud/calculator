package com.example.calcora.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainTab(val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    CALCULATOR("Calculator", Icons.Filled.Calculate, Icons.Outlined.Calculate),
    TOOLS("Tools", Icons.Filled.Widgets, Icons.Outlined.Widgets),
    HISTORY("History", Icons.Filled.History, Icons.Outlined.History),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
}
