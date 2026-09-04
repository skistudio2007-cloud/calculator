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
fun BmiCalculatorScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var isMetric by remember { mutableStateOf(true) }

    // Metric inputs
    var heightCm by remember { mutableStateOf("175") }
    var weightKg by remember { mutableStateOf("70") }

    // Imperial inputs
    var heightFt by remember { mutableStateOf("5") }
    var heightIn by remember { mutableStateOf("9") }
    var weightLbs by remember { mutableStateOf("154") }

    val formatter = remember { DecimalFormat("0.0") }

    val (bmi, category, categoryColor) = remember(isMetric, heightCm, weightKg, heightFt, heightIn, weightLbs) {
        val calculatedBmi = if (isMetric) {
            val cm = heightCm.toDoubleOrNull() ?: 0.0
            val kg = weightKg.toDoubleOrNull() ?: 0.0
            if (cm > 0 && kg > 0) {
                val meters = cm / 100.0
                kg / (meters * meters)
            } else 0.0
        } else {
            val ft = heightFt.toDoubleOrNull() ?: 0.0
            val inch = heightIn.toDoubleOrNull() ?: 0.0
            val lbs = weightLbs.toDoubleOrNull() ?: 0.0
            val totalInches = (ft * 12.0) + inch
            if (totalInches > 0 && lbs > 0) {
                (lbs / (totalInches * totalInches)) * 703.0
            } else 0.0
        }

        val cat = when {
            calculatedBmi <= 0 -> "Enter measurements" to Color(0xFF657085)
            calculatedBmi < 18.5 -> "Underweight" to Color(0xFF1E88E5)
            calculatedBmi < 25.0 -> "Normal weight" to Color(0xFF2E7D32)
            calculatedBmi < 30.0 -> "Overweight" to Color(0xFFF57C00)
            else -> "Obese" to Color(0xFFC62828)
        }
        Triple(calculatedBmi, cat.first, cat.second)
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "BMI Calculator",
        subtitle = "Body Mass Index and nutritional status",
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
            // Unit Switcher
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
                        .background(if (isMetric) PrimaryBlue else Color.Transparent)
                        .clickable { isMetric = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Metric (cm, kg)",
                        color = if (isMetric) Color.White else glassColors.secondaryText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isMetric) PrimaryBlue else Color.Transparent)
                        .clickable { isMetric = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Imperial (ft, lbs)",
                        color = if (!isMetric) Color.White else glassColors.secondaryText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            if (isMetric) {
                CalcoraInputField(
                    label = "Height (cm)",
                    value = heightCm,
                    onValueChange = { heightCm = it },
                    placeholder = "175",
                    suffix = "cm"
                )

                CalcoraInputField(
                    label = "Weight (kg)",
                    value = weightKg,
                    onValueChange = { weightKg = it },
                    placeholder = "70",
                    suffix = "kg"
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CalcoraInputField(
                        label = "Height (Feet)",
                        value = heightFt,
                        onValueChange = { heightFt = it },
                        placeholder = "5",
                        suffix = "ft",
                        modifier = Modifier.weight(1f)
                    )
                    CalcoraInputField(
                        label = "Inches",
                        value = heightIn,
                        onValueChange = { heightIn = it },
                        placeholder = "9",
                        suffix = "in",
                        modifier = Modifier.weight(1f)
                    )
                }

                CalcoraInputField(
                    label = "Weight (Pounds)",
                    value = weightLbs,
                    onValueChange = { weightLbs = it },
                    placeholder = "154",
                    suffix = "lbs"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ResultMetric(
                        label = "BMI Value",
                        value = if (bmi > 0) formatter.format(bmi) else "--",
                        highlight = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(categoryColor.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.uppercase(),
                            color = categoryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = glassColors.cardBorder
                    )

                    Text(
                        text = "Healthy BMI range: 18.5 – 24.9 kg/m²",
                        style = MaterialTheme.typography.bodySmall,
                        color = glassColors.secondaryText
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
