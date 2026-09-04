package com.example.calcora.ui.tools.converters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.data.CurrencyRepository
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.tools.CalcoraInputField
import com.example.calcora.ui.tools.ResultMetric
import com.example.calcora.ui.tools.ToolScaffold
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(
    currencyRepository: CurrencyRepository,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var amount by remember { mutableStateOf("100") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }

    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }

    val currencies by currencyRepository.currencies.collectAsState()
    val lastUpdated by currencyRepository.lastUpdated.collectAsState()

    val formatter = remember { DecimalFormat("#,##0.00") }

    val numAmount = amount.toDoubleOrNull() ?: 0.0
    val convertedAmount = remember(numAmount, fromCurrency, toCurrency, currencies) {
        if (numAmount > 0) {
            currencyRepository.convert(numAmount, fromCurrency, toCurrency)
        } else 0.0
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Currency Converter",
        subtitle = "Offline cached rates with live refresh",
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
            // Last Updated Pill with Refresh Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(glassColors.operatorButtonBg)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cached Rates: $lastUpdated",
                    style = MaterialTheme.typography.bodySmall,
                    color = glassColors.secondaryText
                )
                IconButton(
                    onClick = { currencyRepository.refreshRates() },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Rates",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            CalcoraInputField(
                label = "Amount to Convert",
                value = amount,
                onValueChange = { amount = it },
                placeholder = "100"
            )

            // From Currency Selector
            Column {
                Text(
                    text = "From Currency",
                    style = MaterialTheme.typography.labelMedium,
                    color = glassColors.secondaryText,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedFrom,
                    onExpandedChange = { expandedFrom = !expandedFrom }
                ) {
                    OutlinedTextField(
                        value = "$fromCurrency — ${currencyRepository.currencyNames[fromCurrency]?.first ?: ""}",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFrom) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = glassColors.cardBackground,
                            unfocusedContainerColor = glassColors.cardBackground,
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = glassColors.cardBorder
                        ),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedFrom,
                        onDismissRequest = { expandedFrom = false }
                    ) {
                        currencies.forEach { item ->
                            DropdownMenuItem(
                                text = { Text("${item.code} (${item.symbol}) - ${item.name}") },
                                onClick = {
                                    fromCurrency = item.code
                                    expandedFrom = false
                                }
                            )
                        }
                    }
                }
            }

            // Swap Currencies Button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        val temp = fromCurrency
                        fromCurrency = toCurrency
                        toCurrency = temp
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(glassColors.operatorButtonBg)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Swap Currencies",
                        tint = PrimaryBlue
                    )
                }
            }

            // To Currency Selector
            Column {
                Text(
                    text = "To Currency",
                    style = MaterialTheme.typography.labelMedium,
                    color = glassColors.secondaryText,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedTo,
                    onExpandedChange = { expandedTo = !expandedTo }
                ) {
                    OutlinedTextField(
                        value = "$toCurrency — ${currencyRepository.currencyNames[toCurrency]?.first ?: ""}",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTo) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = glassColors.cardBackground,
                            unfocusedContainerColor = glassColors.cardBackground,
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = glassColors.cardBorder
                        ),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTo,
                        onDismissRequest = { expandedTo = false }
                    ) {
                        currencies.forEach { item ->
                            DropdownMenuItem(
                                text = { Text("${item.code} (${item.symbol}) - ${item.name}") },
                                onClick = {
                                    toCurrency = item.code
                                    expandedTo = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Conversion Result Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    val toSymbol = currencyRepository.currencyNames[toCurrency]?.second ?: toCurrency
                    ResultMetric(
                        label = "Converted Amount ($toCurrency)",
                        value = "$toSymbol ${formatter.format(convertedAmount)}",
                        highlight = true
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = glassColors.cardBorder
                    )

                    val rateOne = currencyRepository.convert(1.0, fromCurrency, toCurrency)
                    Text(
                        text = "1 $fromCurrency = ${DecimalFormat("#,##0.0000").format(rateOne)} $toCurrency",
                        style = MaterialTheme.typography.bodyMedium,
                        color = glassColors.secondaryText
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
