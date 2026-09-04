package com.example.calcora.ui.tools.finance

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
fun EmiCalculatorScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var loanAmount by remember { mutableStateOf("100000") }
    var interestRate by remember { mutableStateOf("8.5") }
    var tenure by remember { mutableStateOf("5") }
    var isTenureInYears by remember { mutableStateOf(true) }

    val formatter = remember { DecimalFormat("#,##0.00") }

    val p = loanAmount.toDoubleOrNull() ?: 0.0
    val annualRate = interestRate.toDoubleOrNull() ?: 0.0
    val tenureNum = tenure.toDoubleOrNull() ?: 0.0
    val totalMonths = if (isTenureInYears) tenureNum * 12.0 else tenureNum

    val monthlyRate = annualRate / (12.0 * 100.0)

    val (monthlyEmi, totalInterest, totalPayment) = remember(p, monthlyRate, totalMonths) {
        if (p > 0 && monthlyRate > 0 && totalMonths > 0) {
            val emi = (p * monthlyRate * (1.0 + monthlyRate).pow(totalMonths)) /
                    ((1.0 + monthlyRate).pow(totalMonths) - 1.0)
            val payment = emi * totalMonths
            val interest = payment - p
            Triple(emi, interest, payment)
        } else if (p > 0 && monthlyRate == 0.0 && totalMonths > 0) {
            val emi = p / totalMonths
            Triple(emi, 0.0, p)
        } else {
            Triple(0.0, 0.0, 0.0)
        }
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "EMI Calculator",
        subtitle = "Calculate your monthly EMI and total interest",
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
                label = "Loan Amount",
                value = loanAmount,
                onValueChange = { loanAmount = it },
                placeholder = "100000"
            )

            CalcoraInputField(
                label = "Annual Interest Rate",
                value = interestRate,
                onValueChange = { interestRate = it },
                placeholder = "8.5",
                suffix = "%"
            )

            // Tenure input with Years / Months toggle
            Column {
                Text(
                    text = "Loan Tenure",
                    style = MaterialTheme.typography.labelMedium,
                    color = glassColors.secondaryText,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CalcoraInputField(
                        label = "",
                        value = tenure,
                        onValueChange = { tenure = it },
                        placeholder = "5",
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        modifier = Modifier
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(glassColors.cardBackground)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isTenureInYears) PrimaryBlue else Color.Transparent)
                                .clickable { isTenureInYears = true }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Years",
                                color = if (isTenureInYears) Color.White else glassColors.secondaryText,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (!isTenureInYears) PrimaryBlue else Color.Transparent)
                                .clickable { isTenureInYears = false }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Months",
                                color = if (!isTenureInYears) Color.White else glassColors.secondaryText,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
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
                        label = "Monthly EMI",
                        value = formatter.format(monthlyEmi),
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
                            label = "Total Payment",
                            value = formatter.format(totalPayment),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
