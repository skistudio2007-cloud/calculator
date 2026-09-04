package com.example.calcora.ui.tools.dailylife

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.tools.ResultMetric
import com.example.calcora.ui.tools.ToolScaffold
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDiffScreen(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    var startDateMillis by remember { mutableStateOf(System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)) }
    var endDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val sdf = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    val diffResult = remember(startDateMillis, endDateMillis) {
        val start = minOf(startDateMillis, endDateMillis)
        val end = maxOf(startDateMillis, endDateMillis)

        val calStart = Calendar.getInstance().apply { timeInMillis = start }
        val calEnd = Calendar.getInstance().apply { timeInMillis = end }

        var y = calEnd.get(Calendar.YEAR) - calStart.get(Calendar.YEAR)
        var m = calEnd.get(Calendar.MONTH) - calStart.get(Calendar.MONTH)
        var d = calEnd.get(Calendar.DAY_OF_MONTH) - calStart.get(Calendar.DAY_OF_MONTH)

        if (d < 0) {
            m--
            val prevMonth = (calEnd.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
            d += prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        if (m < 0) {
            y--
            m += 12
        }

        val diffMillis = end - start
        val tDays = diffMillis / (1000L * 60 * 60 * 24)
        val tWeeks = tDays / 7
        val tMonths = (y * 12) + m

        DateDiffBreakdown(tDays, tWeeks, tMonths.toLong(), y.toLong(), m.toLong(), d.toLong())
    }

    val glassColors = LocalGlassColors.current

    ToolScaffold(
        title = "Date Difference",
        subtitle = "Calculate the exact duration between dates",
        isFavorite = isFavorite,
        onToggleFavorite = onToggleFavorite,
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Start Date
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showStartDatePicker = true },
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
                        Text("Start Date", style = MaterialTheme.typography.labelMedium, color = glassColors.secondaryText)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(sdf.format(Date(startDateMillis)), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                    }
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = glassColors.primaryBlue)
                }
            }

            // Swap Button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        val temp = startDateMillis
                        startDateMillis = endDateMillis
                        endDateMillis = temp
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(glassColors.operatorButtonBg)
                ) {
                    Icon(Icons.Default.SwapVert, contentDescription = "Swap Dates", tint = PrimaryBlue)
                }
            }

            // End Date
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEndDatePicker = true },
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
                        Text("End Date", style = MaterialTheme.typography.labelMedium, color = glassColors.secondaryText)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(sdf.format(Date(endDateMillis)), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                    }
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = glassColors.primaryBlue)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Result
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ResultMetric(
                        label = "Total Days",
                        value = "${diffResult.totalDays} Days",
                        highlight = true
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = glassColors.cardBorder)

                    ResultMetric(
                        label = "Breakdown",
                        value = "${diffResult.years} Years, ${diffResult.months} Months, ${diffResult.days} Days"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = glassColors.cardBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ResultMetric(label = "Total Weeks", value = "${diffResult.totalWeeks} wks", modifier = Modifier.weight(1f))
                        ResultMetric(label = "Total Months", value = "${diffResult.totalMonths} mos", modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showStartDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = startDateMillis)
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { startDateMillis = it }
                    showStartDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = state) }
    }

    if (showEndDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = endDateMillis)
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { endDateMillis = it }
                    showEndDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = state) }
    }
}

data class DateDiffBreakdown(
    val totalDays: Long,
    val totalWeeks: Long,
    val totalMonths: Long,
    val years: Long,
    val months: Long,
    val days: Long
)

