package com.example.calcora.ui.tools.dailylife

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun TipCalculatorScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var billAmount by remember { mutableStateOf("80") }
    var tipPercent by remember { mutableStateOf("15") }

    val formatter = remember { DecimalFormat("#,##0.00") }

    val bill = billAmount.toDoubleOrNull() ?: 0.0
    val tipPct = tipPercent.toDoubleOrNull() ?: 0.0

    val (tipAmount, totalAmount) = remember(bill, tipPct) {
        if (bill > 0 && tipPct >= 0) {
            val tip = (bill * tipPct) / 100.0
            Pair(tip, bill + tip)
        } else {
            Pair(0.0, 0.0)
        }
    }

    val quickTips = listOf("10", "15", "18", "20", "25")
    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Tip Calculator",
        subtitle = "Calculate gratuity and total checkout",
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
                label = "Bill Amount",
                value = billAmount,
                onValueChange = { billAmount = it },
                placeholder = "80"
            )

            CalcoraInputField(
                label = "Tip Percentage (%)",
                value = tipPercent,
                onValueChange = { tipPercent = it },
                placeholder = "15",
                suffix = "%"
            )

            // Quick Tip Buttons
            Column {
                Text(
                    text = "Quick Percentage Options",
                    style = MaterialTheme.typography.labelMedium,
                    color = glassColors.secondaryText,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    quickTips.forEach { tip ->
                        val isSelected = tipPercent == tip
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PrimaryBlue.copy(alpha = 0.2f) else glassColors.cardBackground)
                                .clickable { tipPercent = tip }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$tip%",
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PrimaryBlue else glassColors.secondaryText,
                                fontSize = 13.sp
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
                        label = "Total Amount Payable",
                        value = formatter.format(totalAmount),
                        highlight = true
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = glassColors.cardBorder
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ResultMetric(
                            label = "Tip Amount",
                            value = formatter.format(tipAmount),
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetric(
                            label = "Effective Tip Rate",
                            value = "$tipPercent%",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
