package com.example.calcora.ui.tools.dailylife

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.tools.ResultMetric
import com.example.calcora.ui.tools.ToolScaffold
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeCalculatorScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var birthDateMillis by remember {
        val cal = Calendar.getInstance()
        cal.set(2000, Calendar.JANUARY, 1)
        mutableStateOf(cal.timeInMillis)
    }
    var targetDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    var showBirthDatePicker by remember { mutableStateOf(false) }
    var showTargetDatePicker by remember { mutableStateOf(false) }

    val sdf = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    val ageResult = remember(birthDateMillis, targetDateMillis) {
        val birthCal = Calendar.getInstance().apply { timeInMillis = birthDateMillis }
        val targetCal = Calendar.getInstance().apply { timeInMillis = targetDateMillis }

        if (birthCal.after(targetCal)) {
            AgeBreakdown(0, 0, 0, 0L, 0L, 0L)
        } else {
            var y = targetCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
            var m = targetCal.get(Calendar.MONTH) - birthCal.get(Calendar.MONTH)
            var d = targetCal.get(Calendar.DAY_OF_MONTH) - birthCal.get(Calendar.DAY_OF_MONTH)

            if (d < 0) {
                m--
                val prevMonthCal = (targetCal.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                d += prevMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)
            }
            if (m < 0) {
                y--
                m += 12
            }

            val diffMillis = targetDateMillis - birthDateMillis
            val totDays = diffMillis / (1000L * 60 * 60 * 24)
            val totWeeks = totDays / 7
            val totMonths = (y * 12) + m

            AgeBreakdown(y, m, d, totMonths.toLong(), totWeeks, totDays)
        }
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Age Calculator",
        subtitle = "Calculate chronological age and days lived",
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
            // Date of Birth Card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showBirthDatePicker = true },
                cornerRadius = 18.dp,
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Date of Birth",
                            style = MaterialTheme.typography.labelMedium,
                            color = glassColors.secondaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = sdf.format(Date(birthDateMillis)),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Pick Birth Date",
                        tint = glassColors.primaryBlue
                    )
                }
            }

            // Target Date Card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTargetDatePicker = true },
                cornerRadius = 18.dp,
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Age as of Date",
                            style = MaterialTheme.typography.labelMedium,
                            color = glassColors.secondaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = sdf.format(Date(targetDateMillis)),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Pick Target Date",
                        tint = glassColors.primaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Primary Result Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ResultMetric(
                        label = "Exact Age",
                        value = "${ageResult.years} Years, ${ageResult.months} Months, ${ageResult.days} Days",
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
                            label = "Total Months",
                            value = "${ageResult.totalMonths}",
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetric(
                            label = "Total Weeks",
                            value = "${ageResult.totalWeeks}",
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetric(
                            label = "Total Days",
                            value = "${ageResult.totalDays}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showBirthDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = birthDateMillis)
        DatePickerDialog(
            onDismissRequest = { showBirthDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { birthDateMillis = it }
                    showBirthDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBirthDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTargetDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = targetDateMillis)
        DatePickerDialog(
            onDismissRequest = { showTargetDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { targetDateMillis = it }
                    showTargetDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTargetDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

data class AgeBreakdown(
    val years: Int,
    val months: Int,
    val days: Int,
    val totalMonths: Long,
    val totalWeeks: Long,
    val totalDays: Long
)

