package com.example.calcora.ui.tools.finance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.tools.CalcoraInputField
import com.example.calcora.ui.tools.ResultMetric
import com.example.calcora.ui.tools.ToolScaffold
import java.text.DecimalFormat

@Composable
fun SimpleInterestScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var principal by remember { mutableStateOf("10000") }
    var rate by remember { mutableStateOf("5.0") }
    var timeYears by remember { mutableStateOf("2") }

    val formatter = remember { DecimalFormat("#,##0.00") }

    val p = principal.toDoubleOrNull() ?: 0.0
    val r = rate.toDoubleOrNull() ?: 0.0
    val t = timeYears.toDoubleOrNull() ?: 0.0

    val (interest, totalAmount) = remember(p, r, t) {
        if (p > 0 && r >= 0 && t >= 0) {
            val si = (p * r * t) / 100.0
            Pair(si, p + si)
        } else {
            Pair(0.0, 0.0)
        }
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Simple Interest",
        subtitle = "Formula: SI = (P × R × T) / 100",
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
                label = "Annual Rate of Interest (R)",
                value = rate,
                onValueChange = { rate = it },
                placeholder = "5.0",
                suffix = "%"
            )

            CalcoraInputField(
                label = "Time Period in Years (T)",
                value = timeYears,
                onValueChange = { timeYears = it },
                placeholder = "2",
                suffix = "Years"
            )

            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ResultMetric(
                        label = "Simple Interest",
                        value = formatter.format(interest),
                        highlight = true
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = glassColors.cardBorder
                    )

                    ResultMetric(
                        label = "Total Amount (P + SI)",
                        value = formatter.format(totalAmount)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
