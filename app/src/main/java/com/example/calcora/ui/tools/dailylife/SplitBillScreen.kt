package com.example.calcora.ui.tools.dailylife

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.tools.CalcoraInputField
import com.example.calcora.ui.tools.ResultMetric
import com.example.calcora.ui.tools.ToolScaffold
import java.text.DecimalFormat

@Composable
fun SplitBillScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var totalBill by remember { mutableStateOf("120") }
    var peopleCount by remember { mutableStateOf("4") }
    var tipPercent by remember { mutableStateOf("15") }

    val formatter = remember { DecimalFormat("#,##0.00") }

    val bill = totalBill.toDoubleOrNull() ?: 0.0
    val people = (peopleCount.toIntOrNull() ?: 1).coerceAtLeast(1)
    val tipPct = tipPercent.toDoubleOrNull() ?: 0.0

    val (tipAmount, grandTotal, perPerson) = remember(bill, people, tipPct) {
        if (bill > 0) {
            val tip = (bill * tipPct) / 100.0
            val total = bill + tip
            val split = total / people
            Triple(tip, total, split)
        } else {
            Triple(0.0, 0.0, 0.0)
        }
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Split Bill",
        subtitle = "Share restaurant bills and tips evenly",
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
                label = "Total Bill Amount",
                value = totalBill,
                onValueChange = { totalBill = it },
                placeholder = "120"
            )

            CalcoraInputField(
                label = "Number of People",
                value = peopleCount,
                onValueChange = { peopleCount = it },
                placeholder = "4",
                keyboardType = KeyboardType.Number
            )

            CalcoraInputField(
                label = "Tip Percentage (%)",
                value = tipPercent,
                onValueChange = { tipPercent = it },
                placeholder = "15",
                suffix = "%"
            )

            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ResultMetric(
                        label = "Amount Per Person",
                        value = formatter.format(perPerson),
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
                            label = "Total Tip",
                            value = formatter.format(tipAmount),
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetric(
                            label = "Total Bill Payable",
                            value = formatter.format(grandTotal),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
