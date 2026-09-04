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
fun DiscountCalculatorScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var originalPrice by remember { mutableStateOf("1200") }
    var discountPercent by remember { mutableStateOf("25") }

    val formatter = remember { DecimalFormat("#,##0.00") }

    val price = originalPrice.toDoubleOrNull() ?: 0.0
    val disc = discountPercent.toDoubleOrNull() ?: 0.0

    val (finalPrice, moneySaved) = remember(price, disc) {
        if (price > 0 && disc >= 0) {
            val saved = (price * disc) / 100.0
            val finalP = maxOf(0.0, price - saved)
            Pair(finalP, saved)
        } else {
            Pair(0.0, 0.0)
        }
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Discount Calculator",
        subtitle = "Calculate final price and savings",
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
                label = "Original Price",
                value = originalPrice,
                onValueChange = { originalPrice = it },
                placeholder = "1200"
            )

            CalcoraInputField(
                label = "Discount Percentage (%)",
                value = discountPercent,
                onValueChange = { discountPercent = it },
                placeholder = "25",
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
                        label = "Final Discounted Price",
                        value = formatter.format(finalPrice),
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
                            label = "Money Saved",
                            value = formatter.format(moneySaved),
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetric(
                            label = "Effective Rate",
                            value = "${discountPercent}% OFF",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
