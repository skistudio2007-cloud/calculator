package com.example.calcora.ui.tools.dailylife

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.tools.CalcoraInputField
import com.example.calcora.ui.tools.ResultMetric
import com.example.calcora.ui.tools.ToolScaffold
import java.text.DecimalFormat

enum class PercentMode(val title: String, val formulaDesc: String) {
    PERCENT_OF_VALUE("X% of Y", "Calculates the exact portion of a total"),
    VALUE_IS_WHAT_PERCENT("X is what % of Y?", "Calculates the ratio of X relative to Y"),
    PERCENT_INCREASE("Increase from X to Y", "Percentage gain from initial value X to Y"),
    PERCENT_DECREASE("Decrease from X to Y", "Percentage loss from initial value X to Y")
}

@Composable
fun PercentageCalculatorScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var selectedMode by remember { mutableStateOf(PercentMode.PERCENT_OF_VALUE) }
    var inputX by remember { mutableStateOf("20") }
    var inputY by remember { mutableStateOf("150") }

    val formatter = remember { DecimalFormat("#,##0.00") }

    val x = inputX.toDoubleOrNull() ?: 0.0
    val y = inputY.toDoubleOrNull() ?: 0.0

    val (resultString, label) = remember(selectedMode, x, y) {
        when (selectedMode) {
            PercentMode.PERCENT_OF_VALUE -> {
                val res = (x * y) / 100.0
                Pair(formatter.format(res), "$x% of $y")
            }
            PercentMode.VALUE_IS_WHAT_PERCENT -> {
                if (y != 0.0) {
                    val res = (x / y) * 100.0
                    Pair("${formatter.format(res)}%", "$x out of $y")
                } else Pair("--", "Cannot divide by 0")
            }
            PercentMode.PERCENT_INCREASE -> {
                if (x != 0.0) {
                    val diff = y - x
                    val res = (diff / x) * 100.0
                    Pair("${formatter.format(res)}%", "Increase from $x to $y")
                } else Pair("--", "Initial value cannot be 0")
            }
            PercentMode.PERCENT_DECREASE -> {
                if (x != 0.0) {
                    val diff = x - y
                    val res = (diff / x) * 100.0
                    Pair("${formatter.format(res)}%", "Decrease from $x to $y")
                } else Pair("--", "Initial value cannot be 0")
            }
        }
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Percentage Calculator",
        subtitle = "Multi-mode percentage operations",
        isFavorite = isFavorite,
        onToggleFavorite = onToggleFavorite,
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mode Selector horizontally scrollable chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PercentMode.values().forEach { mode ->
                    val isSelected = selectedMode == mode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) PrimaryBlue else glassColors.cardBackground)
                            .clickable { selectedMode = mode }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.title,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else glassColors.secondaryText
                        )
                    }
                }
            }

            Text(
                text = selectedMode.formulaDesc,
                style = MaterialTheme.typography.bodySmall,
                color = glassColors.secondaryText
            )

            CalcoraInputField(
                label = when (selectedMode) {
                    PercentMode.PERCENT_OF_VALUE -> "Percentage (X%)"
                    PercentMode.VALUE_IS_WHAT_PERCENT -> "Value (X)"
                    PercentMode.PERCENT_INCREASE, PercentMode.PERCENT_DECREASE -> "Initial Value (X)"
                },
                value = inputX,
                onValueChange = { inputX = it },
                placeholder = "20"
            )

            CalcoraInputField(
                label = when (selectedMode) {
                    PercentMode.PERCENT_OF_VALUE -> "Of Total Value (Y)"
                    PercentMode.VALUE_IS_WHAT_PERCENT -> "Total (Y)"
                    PercentMode.PERCENT_INCREASE, PercentMode.PERCENT_DECREASE -> "Final Value (Y)"
                },
                value = inputY,
                onValueChange = { inputY = it },
                placeholder = "150"
            )

            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ResultMetric(
                        label = label,
                        value = resultString,
                        highlight = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
