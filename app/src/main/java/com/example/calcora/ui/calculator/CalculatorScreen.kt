package com.example.calcora.ui.calculator

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.theme.*
import com.example.calcora.ui.components.*

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    hapticEnabled: Boolean,
    onNavigateToHistory: () -> Unit = {},
    onNavigateToTools: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onSelectTool: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var showConverterSheet by remember { mutableStateOf(false) }
    var showStepSheet by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showOverflowMenu by remember { mutableStateOf(false) }
    var isInvMode by remember { mutableStateOf(false) }

    // Blinking red cursor animation
    val infiniteTransition = rememberInfiniteTransition(label = "cursorBlink")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 550, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // TOP APP BAR: History & Tools on Left (No Calcora text, No Icons) + 3-Dot Overflow Menu (Top Right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Clean Minimalist Text Links (No Icons)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "History",
                    color = AmoledSecondaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigateToHistory() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )

                Text(
                    text = "Tools",
                    color = AmoledSecondaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigateToTools() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }

            // Top Right: 3-Dot Overflow Menu (Settings & Privacy Policy inside)
            Box {
                IconButton(
                    onClick = { showOverflowMenu = !showOverflowMenu },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = AmoledSecondaryText
                    )
                }

                DropdownMenu(
                    expanded = showOverflowMenu,
                    onDismissRequest = { showOverflowMenu = false },
                    modifier = Modifier.background(AmoledSurface)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (state.isScientific) "Standard Mode" else "Scientific Mode",
                                color = AmoledDigitText,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Calculate,
                                contentDescription = null,
                                tint = if (state.isScientific) AmoledOperatorRed else AmoledSecondaryText
                            )
                        },
                        onClick = {
                            showOverflowMenu = false
                            viewModel.toggleScientific()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Settings",
                                color = AmoledDigitText,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = null,
                                tint = AmoledSecondaryText
                            )
                        },
                        onClick = {
                            showOverflowMenu = false
                            onNavigateToSettings()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Privacy Policy",
                                color = AmoledDigitText,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Security,
                                contentDescription = null,
                                tint = AmoledOperatorRed
                            )
                        },
                        onClick = {
                            showOverflowMenu = false
                            showPrivacyDialog = true
                        }
                    )
                }
            }
        }

        // TOP DISPLAY AREA (Serif font + Red cursor line)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Calculation Step preview or expression
                if (state.expression.isNotEmpty() && state.isEvaluated) {
                    Text(
                        text = state.expression,
                        color = AmoledSecondaryText,
                        fontSize = 22.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // Error message banner
                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage!!,
                        color = AmoledOperatorRed,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Default,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                // Main Number / Input with red blinking cursor
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    val displayText = when {
                        state.isEvaluated -> state.finalResult
                        state.expression.isNotEmpty() -> state.expression
                        else -> "0"
                    }

                    Text(
                        text = displayText,
                        color = AmoledDigitText,
                        fontSize = if (displayText.length > 9) 40.sp else 58.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.End,
                        maxLines = 2
                    )

                    // Vertical Red Cursor
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .width(2.5.dp)
                            .height(if (displayText.length > 9) 36.dp else 48.dp)
                            .background(AmoledCursorRed.copy(alpha = cursorAlpha))
                    )
                }

                // Step breakdown chip
                if (state.steps.isNotEmpty() && state.isEvaluated) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Show Steps (${state.steps.size})",
                        color = AmoledOperatorRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AmoledOperatorRed.copy(alpha = 0.12f))
                            .clickable { showStepSheet = true }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // MID TOOLBAR ROW: Scientific Toggle on Left | Backspace on Right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Scientific Keypad Toggle Button ([√ π e =]) on Left
            IconButton(
                onClick = { viewModel.toggleScientific() },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = if (state.isScientific) Icons.Outlined.Exposure else Icons.Outlined.Calculate,
                    contentDescription = "Toggle Scientific Keypad",
                    tint = if (state.isScientific) AmoledOperatorRed else AmoledSecondaryText,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Red Backspace Key Button on right
            IconButton(
                onClick = { viewModel.onBackspace() },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Backspace,
                    contentDescription = "Backspace",
                    tint = AmoledOperatorRed,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // KEYPAD CONTAINER (Exact 4-column Standard or 5-column Scientific)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            if (state.isScientific) {
                Scientific5ColumnKeypad(
                    viewModel = viewModel,
                    hapticEnabled = hapticEnabled,
                    isDegreeMode = state.isDegreeMode,
                    isInvMode = isInvMode,
                    onToggleInv = { isInvMode = !isInvMode }
                )
            } else {
                Standard4ColumnKeypad(
                    viewModel = viewModel,
                    hapticEnabled = hapticEnabled
                )
            }
        }
    }

    // Unit Converter Modal Grid Sheet (Image 2)
    if (showConverterSheet) {
        UnitConverterGridSheet(
            onDismiss = { showConverterSheet = false },
            onSelectTool = { toolId ->
                onSelectTool(toolId)
            }
        )
    }

    // BODMAS Steps Sheet
    if (showStepSheet) {
        BodmasStepSheet(
            originalExpression = state.expression,
            finalResult = state.finalResult,
            steps = state.steps,
            onDismiss = { showStepSheet = false }
        )
    }

    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        PrivacyPolicyDialog(
            onDismiss = { showPrivacyDialog = false }
        )
    }
}

// -------------------------------------------------------------
// STANDARD 4-COLUMN KEYPAD (Image 3)
// Row 1: C | () | % | ÷
// Row 2: 7 | 8 | 9 | ×
// Row 3: 4 | 5 | 6 | −
// Row 4: 1 | 2 | 3 | +
// Row 5: 0 | 00 | . | =
// -------------------------------------------------------------
@Composable
private fun Standard4ColumnKeypad(
    viewModel: CalculatorViewModel,
    hapticEnabled: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            CalcoraButton("C", role = ButtonRole.CLEAR, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.clear() }
            CalcoraButton("()", role = ButtonRole.OPERATOR, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onParentheses() }
            CalcoraButton("%", role = ButtonRole.OPERATOR, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("%") }
            CalcoraButton("÷", role = ButtonRole.OPERATOR, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("÷") }
        }

        // Row 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            CalcoraButton("7", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("7") }
            CalcoraButton("8", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("8") }
            CalcoraButton("9", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("9") }
            CalcoraButton("×", role = ButtonRole.OPERATOR, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("×") }
        }

        // Row 3
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            CalcoraButton("4", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("4") }
            CalcoraButton("5", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("5") }
            CalcoraButton("6", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("6") }
            CalcoraButton("−", role = ButtonRole.OPERATOR, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("−") }
        }

        // Row 4
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            CalcoraButton("1", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("1") }
            CalcoraButton("2", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("2") }
            CalcoraButton("3", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("3") }
            CalcoraButton("+", role = ButtonRole.OPERATOR, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("+") }
        }

        // Row 5
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            CalcoraButton("0", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("0") }
            CalcoraButton("00", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("00") }
            CalcoraButton(".", role = ButtonRole.NUMBER, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput(".") }
            CalcoraButton("=", role = ButtonRole.EQUALS, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.evaluate() }
        }
    }
}

// -------------------------------------------------------------
// SCIENTIFIC 5-COLUMN KEYPAD (Image 1)
// Row 1: ⇄ | Rad | sin | cos | tan
// Row 2: √ | ln | log | 1/x | eˣ
// Row 3: x² | C | () | % | ÷
// Row 4: xʸ | 7 | 8 | 9 | ×
// Row 5: |x| | 4 | 5 | 6 | −
// Row 6: π | 1 | 2 | 3 | +
// Row 7: e | 0 | 00 | . | =
// -------------------------------------------------------------
@Composable
private fun Scientific5ColumnKeypad(
    viewModel: CalculatorViewModel,
    hapticEnabled: Boolean,
    isDegreeMode: Boolean,
    isInvMode: Boolean,
    onToggleInv: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcoraButton("⇄", role = ButtonRole.FUNCTION, fontSize = 17.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { onToggleInv() }
            CalcoraButton(if (isDegreeMode) "Deg" else "Rad", role = ButtonRole.FUNCTION, fontSize = 16.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.toggleDegreeMode() }
            CalcoraButton(if (isInvMode) "asin" else "sin", role = ButtonRole.FUNCTION, fontSize = 16.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onFunction(if (isInvMode) "asin" else "sin") }
            CalcoraButton(if (isInvMode) "acos" else "cos", role = ButtonRole.FUNCTION, fontSize = 16.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onFunction(if (isInvMode) "acos" else "cos") }
            CalcoraButton(if (isInvMode) "atan" else "tan", role = ButtonRole.FUNCTION, fontSize = 16.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onFunction(if (isInvMode) "atan" else "tan") }
        }

        // Row 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcoraButton("√", role = ButtonRole.FUNCTION, fontSize = 18.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onFunction("sqrt") }
            CalcoraButton("ln", role = ButtonRole.FUNCTION, fontSize = 16.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onFunction("ln") }
            CalcoraButton("log", role = ButtonRole.FUNCTION, fontSize = 16.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onFunction("log") }
            CalcoraButton("1/x", role = ButtonRole.FUNCTION, fontSize = 16.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInverse() }
            CalcoraButton("eˣ", role = ButtonRole.FUNCTION, fontSize = 16.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("e^") }
        }

        // Row 3
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcoraButton("x²", role = ButtonRole.FUNCTION, fontSize = 17.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onPower("2") }
            CalcoraButton("C", role = ButtonRole.CLEAR, fontSize = 21.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.clear() }
            CalcoraButton("()", role = ButtonRole.OPERATOR, fontSize = 20.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onParentheses() }
            CalcoraButton("%", role = ButtonRole.OPERATOR, fontSize = 20.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("%") }
            CalcoraButton("÷", role = ButtonRole.OPERATOR, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("÷") }
        }

        // Row 4
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcoraButton("xʸ", role = ButtonRole.FUNCTION, fontSize = 17.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("^") }
            CalcoraButton("7", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("7") }
            CalcoraButton("8", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("8") }
            CalcoraButton("9", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("9") }
            CalcoraButton("×", role = ButtonRole.OPERATOR, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("×") }
        }

        // Row 5
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcoraButton("|x|", role = ButtonRole.FUNCTION, fontSize = 16.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onFunction("abs") }
            CalcoraButton("4", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("4") }
            CalcoraButton("5", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("5") }
            CalcoraButton("6", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("6") }
            CalcoraButton("−", role = ButtonRole.OPERATOR, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("−") }
        }

        // Row 6
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcoraButton("π", role = ButtonRole.FUNCTION, fontSize = 18.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("π") }
            CalcoraButton("1", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("1") }
            CalcoraButton("2", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("2") }
            CalcoraButton("3", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("3") }
            CalcoraButton("+", role = ButtonRole.OPERATOR, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("+") }
        }

        // Row 7
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcoraButton("e", role = ButtonRole.FUNCTION, fontSize = 18.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("e") }
            CalcoraButton("0", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("0") }
            CalcoraButton("00", role = ButtonRole.NUMBER, fontSize = 20.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput("00") }
            CalcoraButton(".", role = ButtonRole.NUMBER, fontSize = 22.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.onInput(".") }
            CalcoraButton("=", role = ButtonRole.EQUALS, fontSize = 24.sp, hapticEnabled = hapticEnabled, modifier = Modifier.weight(1f)) { viewModel.evaluate() }
        }
    }
}
