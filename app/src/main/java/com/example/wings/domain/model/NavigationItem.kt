package com.example.wings.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.wings.ui.navigation.Screen

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen,
)