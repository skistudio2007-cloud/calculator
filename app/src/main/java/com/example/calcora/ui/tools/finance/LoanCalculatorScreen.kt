package com.example.calcora.ui.tools.finance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.tools.CalcoraInputField
import com.example.calcora.ui.tools.ResultMetric
import com.example.calcora.ui.tools.ToolScaffold
import java.text.DecimalFormat
import kotlin.math.pow

@Composable
fun LoanCalculatorScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var principal by remember { mutableStateOf("50000") }
    var interestRate by remember { mutableStateOf("7.0") }
    var tenureYears by remember { mutableStateOf("3") }

    val formatter = remember { DecimalFormat("#,##0.00") }

    val p = principal.toDoubleOrNull() ?: 0.0
    val r = (interestRate.toDoubleOrNull() ?: 0.0) / (12.0 * 100.0)
    val n = (tenureYears.toDoubleOrNull() ?: 0.0) * 12.0

    val (monthlyPayment, totalInterest, totalAmount) = remember(p, r, n) {
        if (p > 0 && r > 0 && n > 0) {
            val emi = (p * r * (1.0 + r).pow(n)) / ((1.0 + r).pow(n) - 1.0)
            val total = emi * n
            Triple(emi, total - p, total)
        } else if (p > 0 && r == 0.0 && n > 0) {
            Triple(p / n, 0.0, p)
        } else {
            Triple(0.0, 0.0, 0.0)
        }
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Loan Calculator",
        subtitle = "Compute monthly payments and total payoff",
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
                label = "Principal Amount",
                value = principal,
                onValueChange = { principal = it },
                placeholder = "50000"
            )

            CalcoraInputField(
                label = "Annual Interest Rate",
                value = interestRate,
                onValueChange = { interestRate = it },
                placeholder = "7.0",
                suffix = "%"
            )

            CalcoraInputField(
                label = "Loan Tenure (Years)",
                value = tenureYears,
                onValueChange = { tenureYears = it },
                placeholder = "3",
                suffix = "Years"
            )

            // Reset Button
            OutlinedButton(
                onClick = {
                    principal = ""
                    interestRate = ""
                    tenureYears = ""
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.RestartAlt, contentDescription = null, tint = glassColors.secondaryText)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset All Values", color = glassColors.secondaryText, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Result Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ResultMetric(
                        label = "Monthly Payment",
                        value = formatter.format(monthlyPayment),
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
                            label = "Total Interest",
                            value = formatter.format(totalInterest),
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetric(
                            label = "Total Amount Payable",
                            value = formatter.format(totalAmount),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
