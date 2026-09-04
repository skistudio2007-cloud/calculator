package com.example.calcora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PremiumLightBlue
import com.example.calcora.theme.PrimaryBlue

@Composable
fun TopHeader(
    title: String = "Calcora",
    modeLabel: String? = null,
    isScientific: Boolean = false,
    onBack: (() -> Unit)? = null,
    onToggleScientific: (() -> Unit)? = null,
    onHistoryClick: (() -> Unit)? = null,
    onToolsClick: (() -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null
) {
    val glassColors = LocalGlassColors.current
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Area: Back Button or Logo + Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
            } else {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(PremiumLightBlue, PrimaryBlue))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "C",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                if (modeLabel != null) {
                    Text(
                        text = modeLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = glassColors.secondaryText
                    )
                }
            }
        }

        // Right Area: Action shortcuts + 3-Dot (⋮) Overflow Menu
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onToggleScientific != null) {
                IconButton(
                    onClick = onToggleScientific,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isScientific) PrimaryBlue.copy(alpha = 0.15f)
                            else Color.Transparent
                        )
                ) {
                    Icon(
                        imageVector = if (isScientific) Icons.Outlined.Calculate else Icons.Default.Science,
                        contentDescription = if (isScientific) "Standard Mode" else "Scientific Mode",
                        tint = if (isScientific) PrimaryBlue else glassColors.secondaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 3-Dot (⋮) Menu Button
            Box {
                IconButton(
                    onClick = { menuExpanded = true },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options Menu",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(22.dp)
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    if (onToolsClick != null) {
                        DropdownMenuItem(
                            text = { Text("Tools & Converters", fontWeight = FontWeight.Medium) },
                            leadingIcon = {
                                Icon(Icons.Default.Widgets, contentDescription = null, tint = PrimaryBlue)
                            },
                            onClick = {
                                menuExpanded = false
                                onToolsClick()
                            }
                        )
                    }

                    if (onHistoryClick != null) {
                        DropdownMenuItem(
                            text = { Text("History", fontWeight = FontWeight.Medium) },
                            leadingIcon = {
                                Icon(Icons.Default.History, contentDescription = null, tint = PrimaryBlue)
                            },
                            onClick = {
                                menuExpanded = false
                                onHistoryClick()
                            }
                        )
                    }

                    if (onSettingsClick != null) {
                        DropdownMenuItem(
                            text = { Text("Settings", fontWeight = FontWeight.Medium) },
                            leadingIcon = {
                                Icon(Icons.Default.Settings, contentDescription = null, tint = PrimaryBlue)
                            },
                            onClick = {
                                menuExpanded = false
                                onSettingsClick()
                            }
                        )
                    }

                    if (onToggleScientific != null) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (isScientific) "Standard Mode" else "Scientific Mode",
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    if (isScientific) Icons.Outlined.Calculate else Icons.Default.Science,
                                    contentDescription = null,
                                    tint = PrimaryBlue
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onToggleScientific()
                            }
                        )
                    }
                }
            }
        }
    }
}
