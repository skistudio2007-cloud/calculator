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

@Composable
fun GstCalculatorScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var amount by remember { mutableStateOf("1000") }
    var gstRate by remember { mutableStateOf("18") }
    var isAddGst by remember { mutableStateOf(true) }

    val formatter = remember { DecimalFormat("#,##0.00") }

    val totalAmt = amount.toDoubleOrNull() ?: 0.0
    val rate = gstRate.toDoubleOrNull() ?: 0.0

    val (original, gstAmount, finalAmount) = remember(totalAmt, rate, isAddGst) {
        if (totalAmt > 0 && rate >= 0) {
            if (isAddGst) {
                val gst = (totalAmt * rate) / 100.0
                Triple(totalAmt, gst, totalAmt + gst)
            } else {
                val orig = (totalAmt * 100.0) / (100.0 + rate)
                val gst = totalAmt - orig
                Triple(orig, gst, totalAmt)
            }
        } else {
            Triple(0.0, 0.0, 0.0)
        }
    }

    val quickRates = listOf("3", "5", "12", "18", "28")
    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "GST Calculator",
        subtitle = "Goods and Services Tax calculation with quick slabs",
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
            // Mode switch: Add GST vs Remove GST
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(glassColors.cardBackground)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isAddGst) PrimaryBlue else Color.Transparent)
                        .clickable { isAddGst = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add GST (+)",
                        color = if (isAddGst) Color.White else glassColors.secondaryText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isAddGst) PrimaryBlue else Color.Transparent)
                        .clickable { isAddGst = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Remove GST (-)",
                        color = if (!isAddGst) Color.White else glassColors.secondaryText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            CalcoraInputField(
                label = if (isAddGst) "Net Amount" else "Gross Amount (Including GST)",
                value = amount,
                onValueChange = { amount = it },
                placeholder = "1000"
            )

            CalcoraInputField(
                label = "GST Rate (%)",
                value = gstRate,
                onValueChange = { gstRate = it },
                placeholder = "18",
                suffix = "%"
            )

            // Quick Slabs
            Column {
                Text(
                    text = "Standard Tax Slabs",
                    style = MaterialTheme.typography.labelMedium,
                    color = glassColors.secondaryText,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    quickRates.forEach { r ->
                        val isSelected = gstRate == r
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PrimaryBlue.copy(alpha = 0.2f) else glassColors.cardBackground)
                                .clickable { gstRate = r }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$r%",
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
                        label = "Final Amount",
                        value = formatter.format(finalAmount),
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
                            label = "Original Cost",
                            value = formatter.format(original),
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetric(
                            label = "GST Amount",
                            value = formatter.format(gstAmount),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
