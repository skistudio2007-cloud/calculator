package com.example.calcora.ui.tools.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Remove
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
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.tools.CalcoraInputField
import com.example.calcora.ui.tools.ResultMetric
import com.example.calcora.ui.tools.ToolScaffold
import java.text.DecimalFormat
import kotlin.math.abs

@Composable
fun ProfitLossScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var costPrice by remember { mutableStateOf("1500") }
    var sellingPrice by remember { mutableStateOf("1800") }

    val formatter = remember { DecimalFormat("#,##0.00") }
    val percentFormatter = remember { DecimalFormat("0.00") }

    val cp = costPrice.toDoubleOrNull() ?: 0.0
    val sp = sellingPrice.toDoubleOrNull() ?: 0.0

    val diff = sp - cp
    val percent = if (cp > 0) (abs(diff) / cp) * 100.0 else 0.0

    val statusText = when {
        diff > 0 -> "PROFIT"
        diff < 0 -> "LOSS"
        else -> "BREAK EVEN"
    }

    val statusColor = when {
        diff > 0 -> Color(0xFF2E7D32)
        diff < 0 -> Color(0xFFC62828)
        else -> Color(0xFF657085)
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Profit & Loss",
        subtitle = "Calculate trade margins and yield",
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
                label = "Cost Price (CP)",
                value = costPrice,
                onValueChange = { costPrice = it },
                placeholder = "1500"
            )

            CalcoraInputField(
                label = "Selling Price (SP)",
                value = sellingPrice,
                onValueChange = { sellingPrice = it },
                placeholder = "1800"
            )

            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Status Badge with icon and text
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(statusColor.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when {
                                diff > 0 -> Icons.Default.ArrowUpward
                                diff < 0 -> Icons.Default.ArrowDownward
                                else -> Icons.Default.Remove
                            },
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$statusText OF ${formatter.format(abs(diff))}",
                            color = statusColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ResultMetric(
                        label = "Profit / Loss Amount",
                        value = "${if (diff > 0) "+" else if (diff < 0) "-" else ""}${formatter.format(abs(diff))}",
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
                            label = "Margin Percentage",
                            value = "${percentFormatter.format(percent)}%",
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetric(
                            label = "Net Return Ratio",
                            value = if (cp > 0) "${percentFormatter.format(sp / cp)}x" else "0x",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
