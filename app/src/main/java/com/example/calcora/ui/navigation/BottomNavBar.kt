package com.example.calcora.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.GlassCard

@Composable
fun BottomNavBar(
    selectedTab: MainTab,
    onSelectTab: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColors.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            cornerRadius = 34.dp,
            elevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainTab.values().forEach { tab ->
                    val isSelected = tab == selectedTab

                    val iconScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 1.0f,
                        animationSpec = spring(),
                        label = "tabIconScale"
                    )

                    val tintColor by animateColorAsState(
                        targetValue = if (isSelected) PrimaryBlue else glassColors.secondaryText,
                        label = "tabTintColor"
                    )

                    val bgTint by animateColorAsState(
                        targetValue = if (isSelected) PrimaryBlue.copy(alpha = 0.12f) else Color.Transparent,
                        label = "tabBgTint"
                    )

                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(bgTint)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onSelectTab(tab) }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.title,
                            tint = tintColor,
                            modifier = Modifier
                                .size(22.dp)
                                .scale(iconScale)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = tab.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = tintColor
                        )
                    }
                }
            }
        }
    }
}
