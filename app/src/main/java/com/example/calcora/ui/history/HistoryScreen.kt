package com.example.calcora.ui.history

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.data.CalculatorHistoryRepository
import com.example.calcora.model.CalculationItem
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.EmptyState
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.components.TopHeader
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    historyRepository: CalculatorHistoryRepository,
    onReuseCalculation: (CalculationItem) -> Unit,
    onBack: () -> Unit = {}
) {
    val historyItems by historyRepository.history.collectAsState()
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val glassColors = LocalGlassColors.current
    val sdf = remember { SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "History",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${historyItems.size} recorded calculations",
                        style = MaterialTheme.typography.bodySmall,
                        color = glassColors.secondaryText
                    )
                }
            }

            if (historyItems.isNotEmpty()) {
                IconButton(
                    onClick = { showClearConfirmDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(glassColors.operatorButtonBg)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteSweep,
                        contentDescription = "Clear all history",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (historyItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    icon = Icons.Outlined.History,
                    title = "No calculations yet",
                    subtitle = "Your recent calculations will appear here."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(historyItems, key = { it.id }) { item ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 20.dp,
                        elevation = 2.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.expression,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = glassColors.secondaryText,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "= ${item.result}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Reuse button
                                    IconButton(
                                        onClick = { onReuseCalculation(item) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Input,
                                            contentDescription = "Reuse in calculator",
                                            tint = PrimaryBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    // Copy result button
                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("Calculation Result", item.result)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "Copied ${item.result}", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy result",
                                            tint = glassColors.secondaryText,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    // Delete button
                                    IconButton(
                                        onClick = { historyRepository.deleteCalculation(item.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Delete item",
                                            tint = glassColors.secondaryText.copy(alpha = 0.7f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = sdf.format(Date(item.timestamp)),
                                style = MaterialTheme.typography.labelSmall,
                                color = glassColors.secondaryText.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = {
                Text(
                    text = "Clear All History?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will permanently delete all recorded calculations. This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = glassColors.secondaryText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        historyRepository.clearAll()
                        showClearConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Clear All", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
