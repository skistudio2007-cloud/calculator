package com.example.calcora.ui.tools.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import kotlin.math.pow

@Composable
fun CompoundInterestScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var principal by remember { mutableStateOf("10000") }
    var rate by remember { mutableStateOf("6.0") }
    var timeYears by remember { mutableStateOf("3") }
    var frequency by remember { mutableStateOf("Yearly") }

    val formatter = remember { DecimalFormat("#,##0.00") }

    val p = principal.toDoubleOrNull() ?: 0.0
    val r = (rate.toDoubleOrNull() ?: 0.0) / 100.0
    val t = timeYears.toDoubleOrNull() ?: 0.0

    val n = when (frequency) {
        "Monthly" -> 12.0
        "Quarterly" -> 4.0
        "Half-yearly" -> 2.0
        else -> 1.0
    }

    val (compoundInterest, finalAmount) = remember(p, r, t, n) {
        if (p > 0 && r >= 0 && t >= 0) {
            val amount = p * (1.0 + (r / n)).pow(n * t)
            val ci = amount - p
            Pair(ci, amount)
        } else {
            Pair(0.0, 0.0)
        }
    }

    val frequencies = listOf("Yearly", "Half-yearly", "Quarterly", "Monthly")
    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Compound Interest",
        subtitle = "Formula: A = P(1 + r/n)^(nt)",
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
            CalcoraInputField(
                label = "Principal (P)",
                value = principal,
                onValueChange = { principal = it },
                placeholder = "10000"
            )

            CalcoraInputField(
                label = "Annual Interest Rate (R)",
                value = rate,
                onValueChange = { rate = it },
                placeholder = "6.0",
                suffix = "%"
            )

            CalcoraInputField(
                label = "Time Period (Years)",
                value = timeYears,
                onValueChange = { timeYears = it },
                placeholder = "3",
                suffix = "Years"
            )

            // Compounding Frequency selector
            Column {
                Text(
                    text = "Compounding Frequency",
                    style = MaterialTheme.typography.labelMedium,
                    color = glassColors.secondaryText,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    frequencies.forEach { freq ->
                        val isSelected = frequency == freq
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PrimaryBlue else glassColors.cardBackground)
                                .clickable { frequency = freq }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = freq,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else glassColors.secondaryText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ResultMetric(
                        label = "Compound Interest",
                        value = formatter.format(compoundInterest),
                        highlight = true
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = glassColors.cardBorder
                    )

                    ResultMetric(
                        label = "Final Maturity Amount",
                        value = formatter.format(finalAmount)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
