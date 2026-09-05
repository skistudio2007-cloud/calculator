package com.example.calcora.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.data.*
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.components.PrivacyPolicyDialog
import com.example.calcora.ui.components.TopHeader

@Composable
fun SettingsScreen(
    preferencesRepository: PreferencesRepository,
    historyRepository: CalculatorHistoryRepository,
    currencyRepository: CurrencyRepository,
    onBack: () -> Unit = {}
) {
    val themeMode by preferencesRepository.themeMode.collectAsState()
    val calcMode by preferencesRepository.calculatorMode.collectAsState()
    val hapticEnabled by preferencesRepository.hapticEnabled.collectAsState()
    val soundEnabled by preferencesRepository.soundEnabled.collectAsState()
    val bodmasStepsEnabled by preferencesRepository.bodmasStepsEnabled.collectAsState()
    val lastUpdatedCurrency by currencyRepository.lastUpdated.collectAsState()

    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val glassColors = LocalGlassColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // APPEARANCE SECTION
            SettingsSection(title = "APPEARANCE") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeMode.values().forEach { mode ->
                        val isSelected = themeMode == mode
                        val label = when (mode) {
                            ThemeMode.SYSTEM -> "System"
                            ThemeMode.LIGHT -> "Light"
                            ThemeMode.DARK -> "Dark"
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PrimaryBlue else glassColors.cardBackground)
                                .clickable { preferencesRepository.setThemeMode(mode) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else glassColors.secondaryText,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // CALCULATOR PREFERENCES SECTION
            SettingsSection(title = "CALCULATOR DEFAULTS") {
                SettingsSwitchRow(
                    title = "BODMAS Step Explanations",
                    subtitle = "Generate step-by-step mathematical reasoning",
                    checked = bodmasStepsEnabled,
                    onCheckedChange = { preferencesRepository.setBodmasStepsEnabled(it) }
                )

                HorizontalDivider(color = glassColors.cardBorder, modifier = Modifier.padding(horizontal = 14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Default Calculator Layout",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Initial keypad style on app start",
                            style = MaterialTheme.typography.bodySmall,
                            color = glassColors.secondaryText
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(glassColors.operatorButtonBg)
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (calcMode == CalculatorMode.STANDARD) PrimaryBlue else Color.Transparent)
                                .clickable { preferencesRepository.setCalculatorMode(CalculatorMode.STANDARD) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Std",
                                color = if (calcMode == CalculatorMode.STANDARD) Color.White else glassColors.secondaryText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (calcMode == CalculatorMode.SCIENTIFIC) PrimaryBlue else Color.Transparent)
                                .clickable { preferencesRepository.setCalculatorMode(CalculatorMode.SCIENTIFIC) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Sci",
                                color = if (calcMode == CalculatorMode.SCIENTIFIC) Color.White else glassColors.secondaryText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // INTERACTION SECTION
            SettingsSection(title = "FEEDBACK & INTERACTION") {
                SettingsSwitchRow(
                    title = "Haptic Touch Feedback",
                    subtitle = "Tactile vibration settling on button press",
                    checked = hapticEnabled,
                    onCheckedChange = { preferencesRepository.setHapticEnabled(it) }
                )

                HorizontalDivider(color = glassColors.cardBorder, modifier = Modifier.padding(horizontal = 14.dp))

                SettingsSwitchRow(
                    title = "Audio Feedback",
                    subtitle = "Subtle auditory clicks on key interaction",
                    checked = soundEnabled,
                    onCheckedChange = { preferencesRepository.setSoundEnabled(it) }
                )
            }

            // CURRENCY SECTION
            SettingsSection(title = "CURRENCY CACHE") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Exchange Rates Last Updated",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 15.sp
                        )
                        Text(
                            text = lastUpdatedCurrency,
                            style = MaterialTheme.typography.bodySmall,
                            color = glassColors.secondaryText
                        )
                    }

                    TextButton(
                        onClick = {
                            currencyRepository.refreshRates()
                            Toast.makeText(context, "Currency rates refreshed", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryBlue)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Update")
                    }
                }
            }

            // DATA STORAGE SECTION
            SettingsSection(title = "DATA MANAGEMENT") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showClearHistoryDialog = true }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Clear Calculation History",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE53935),
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Permanently remove all saved calculations",
                            style = MaterialTheme.typography.bodySmall,
                            color = glassColors.secondaryText
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = null,
                        tint = Color(0xFFE53935)
                    )
                }
            }


            // ABOUT SECTION
            SettingsSection(title = "ABOUT CALCORA") {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("C", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Calcora — All-in-One Calculator & Converter",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Version 1.0.0 (Premium Edition)",
                                style = MaterialTheme.typography.bodySmall,
                                color = glassColors.secondaryText
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = { showPrivacyDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
                    ) {
                        Icon(Icons.Outlined.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Privacy Policy", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = { Text("Clear All History?", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently delete all recorded calculations.", color = glassColors.secondaryText) },
            confirmButton = {
                Button(
                    onClick = {
                        historyRepository.clearAll()
                        showClearHistoryDialog = false
                        Toast.makeText(context, "History cleared", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Clear All", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(20.dp)
        )
    }

    if (showPrivacyDialog) {
        PrivacyPolicyDialog(
            onDismiss = { showPrivacyDialog = false }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue,
            letterSpacing = 1.sp,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 6.dp, bottom = 6.dp)
        )
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 20.dp,
            elevation = 2.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth(), content = content)
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val glassColors = LocalGlassColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = glassColors.secondaryText
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryBlue,
                uncheckedThumbColor = glassColors.secondaryText,
                uncheckedTrackColor = glassColors.cardBackground
            )
        )
    }
}
