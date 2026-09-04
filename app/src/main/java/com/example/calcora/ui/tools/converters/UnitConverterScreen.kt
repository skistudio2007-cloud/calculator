package com.example.calcora.ui.tools.converters

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.model.UnitCategory
import com.example.calcora.model.UnitItem
import com.example.calcora.model.UnitRegistry
import com.example.calcora.theme.AmoledOperatorRed
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.tools.CalcoraInputField
import com.example.calcora.ui.tools.ToolScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    category: UnitCategory,
    isTemperature: Boolean = false,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var inputValue by remember { mutableStateOf("1") }
    var fromIndex by remember { mutableStateOf(0) }
    var toIndex by remember { mutableStateOf(if (category.units.size > 1) 1 else 0) }

    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }

    val fromUnit = category.units.getOrElse(fromIndex) { category.units.first() }
    val toUnit = category.units.getOrElse(toIndex) { category.units.first() }

    val num = inputValue.toDoubleOrNull() ?: 0.0

    val convertedValue = remember(num, fromUnit, toUnit, isTemperature) {
        if (isTemperature) {
            UnitRegistry.convertTemperature(num, fromUnit.name, toUnit.name)
        } else {
            UnitRegistry.convertStandard(num, fromUnit, toUnit)
        }
    }

    val singleRate = remember(fromUnit, toUnit, isTemperature) {
        if (isTemperature) {
            UnitRegistry.convertTemperature(1.0, fromUnit.name, toUnit.name)
        } else {
            UnitRegistry.convertStandard(1.0, fromUnit, toUnit)
        }
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = category.title,
        subtitle = "Accurate scientific & standard conversion",
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
            // Premium Input Field
            CalcoraInputField(
                label = "VALUE TO CONVERT",
                value = inputValue,
                onValueChange = { inputValue = it },
                placeholder = "1",
                suffix = fromUnit.symbol
            )

            // Selector Container Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 22.dp,
                elevation = 2.dp,
                backgroundColor = Color(0xFF14171D)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // From Unit Selector
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "FROM",
                                style = MaterialTheme.typography.labelSmall,
                                color = glassColors.secondaryText,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = fromUnit.symbol,
                                style = MaterialTheme.typography.labelSmall,
                                color = AmoledOperatorRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        ExposedDropdownMenuBox(
                            expanded = expandedFrom,
                            onExpandedChange = { expandedFrom = !expandedFrom }
                        ) {
                            OutlinedTextField(
                                value = "${fromUnit.name} (${fromUnit.symbol})",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFrom) },
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF0C0E12),
                                    unfocusedContainerColor = Color(0xFF0C0E12),
                                    focusedBorderColor = AmoledOperatorRed,
                                    unfocusedBorderColor = Color(0x22FFFFFF),
                                    focusedTextColor = Color(0xFFEDEDED),
                                    unfocusedTextColor = Color(0xFFEDEDED)
                                ),
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedFrom,
                                onDismissRequest = { expandedFrom = false },
                                modifier = Modifier.background(Color(0xFF1A1D24))
                            ) {
                                category.units.forEachIndexed { index, unit ->
                                    val isSelected = index == fromIndex
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "${unit.name} (${unit.symbol})",
                                                    color = if (isSelected) AmoledOperatorRed else Color(0xFFEDEDED),
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Check,
                                                        contentDescription = null,
                                                        tint = AmoledOperatorRed,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            fromIndex = index
                                            expandedFrom = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Swap Button Row with subtle divider lines
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0x1AFFFFFF)
                        )
                        IconButton(
                            onClick = {
                                val temp = fromIndex
                                fromIndex = toIndex
                                toIndex = temp
                            },
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1F232B))
                                .border(1.dp, Color(0x2AFFFFFF), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap Units",
                                tint = AmoledOperatorRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0x1AFFFFFF)
                        )
                    }

                    // To Unit Selector
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TO",
                                style = MaterialTheme.typography.labelSmall,
                                color = glassColors.secondaryText,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = toUnit.symbol,
                                style = MaterialTheme.typography.labelSmall,
                                color = AmoledOperatorRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        ExposedDropdownMenuBox(
                            expanded = expandedTo,
                            onExpandedChange = { expandedTo = !expandedTo }
                        ) {
                            OutlinedTextField(
                                value = "${toUnit.name} (${toUnit.symbol})",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTo) },
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF0C0E12),
                                    unfocusedContainerColor = Color(0xFF0C0E12),
                                    focusedBorderColor = AmoledOperatorRed,
                                    unfocusedBorderColor = Color(0x22FFFFFF),
                                    focusedTextColor = Color(0xFFEDEDED),
                                    unfocusedTextColor = Color(0xFFEDEDED)
                                ),
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedTo,
                                onDismissRequest = { expandedTo = false },
                                modifier = Modifier.background(Color(0xFF1A1D24))
                            ) {
                                category.units.forEachIndexed { index, unit ->
                                    val isSelected = index == toIndex
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "${unit.name} (${unit.symbol})",
                                                    color = if (isSelected) AmoledOperatorRed else Color(0xFFEDEDED),
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Check,
                                                        contentDescription = null,
                                                        tint = AmoledOperatorRed,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            toIndex = index
                                            expandedTo = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Luxury Result Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1B1F27),
                                Color(0xFF121419)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Brush.verticalGradient(
                            listOf(
                                AmoledOperatorRed.copy(alpha = 0.45f),
                                Color(0x1AFFFFFF)
                            )
                        ),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CONVERTED RESULT",
                            style = MaterialTheme.typography.labelSmall,
                            color = glassColors.secondaryText,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AmoledOperatorRed.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = toUnit.symbol,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmoledOperatorRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    AnimatedContent(
                        targetState = convertedValue,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "convertedValue"
                    ) { targetVal ->
                        Text(
                            text = "${UnitRegistry.formatAccurate(targetVal)} ${toUnit.symbol}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            ),
                            color = Color(0xFFEDEDED),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        color = Color(0x1AFFFFFF)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rate Calculation",
                            style = MaterialTheme.typography.bodySmall,
                            color = glassColors.secondaryText
                        )
                        Text(
                            text = "1 ${fromUnit.symbol} = ${UnitRegistry.formatAccurate(singleRate)} ${toUnit.symbol}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = AmoledOperatorRed
                        )
                    }
                }
            }

            // Quick Reference List: Equivalent Across All Units
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Equivalent Across All Units",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEDEDED),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "${category.units.size} Units",
                    style = MaterialTheme.typography.labelSmall,
                    color = glassColors.secondaryText
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                category.units.forEach { u ->
                    val isCurrentTarget = u.name == toUnit.name
                    val isCurrentSource = u.name == fromUnit.name
                    val equiv = if (isTemperature) {
                        UnitRegistry.convertTemperature(num, fromUnit.name, u.name)
                    } else {
                        UnitRegistry.convertStandard(num, fromUnit, u)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isCurrentTarget) Color(0xFF1D222B)
                                else Color(0xFF13151A)
                            )
                            .border(
                                1.dp,
                                if (isCurrentTarget) AmoledOperatorRed.copy(alpha = 0.5f)
                                else if (isCurrentSource) Color(0x33FFFFFF)
                                else Color(0x14FFFFFF),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = u.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isCurrentTarget) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isCurrentTarget) Color(0xFFEDEDED) else glassColors.secondaryText
                            )
                            if (isCurrentSource) {
                                Text(
                                    text = "Current Input",
                                    fontSize = 10.sp,
                                    color = Color(0xFF888E99)
                                )
                            }
                        }

                        Text(
                            text = "${UnitRegistry.formatAccurate(equiv)} ${u.symbol}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isCurrentTarget) AmoledOperatorRed else Color(0xFFEDEDED)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
