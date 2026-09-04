package com.example.calcora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.engine.MathStep
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PremiumLightBlue
import com.example.calcora.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodmasStepSheet(
    originalExpression: String,
    finalResult: String,
    steps: List<MathStep>,
    onDismiss: () -> Unit
) {
    val glassColors = LocalGlassColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        scrimColor = Color.Black.copy(alpha = 0.45f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(glassColors.cardBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(glassColors.operatorButtonBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Functions,
                            contentDescription = null,
                            tint = glassColors.primaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "BODMAS Steps",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Order of Operations Explanation",
                            style = MaterialTheme.typography.bodySmall,
                            color = glassColors.secondaryText
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = glassColors.secondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Original Expression Box
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 16.dp,
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Original Expression",
                        style = MaterialTheme.typography.labelMedium,
                        color = glassColors.secondaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = originalExpression,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (steps.isEmpty()) {
                Text(
                    text = "Single-step evaluation: $originalExpression = $finalResult",
                    style = MaterialTheme.typography.bodyMedium,
                    color = glassColors.secondaryText,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(steps) { step ->
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            cornerRadius = 16.dp,
                            elevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryBlue.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${step.stepNumber}",
                                        color = PrimaryBlue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Step ${step.stepNumber} — ${step.operationType}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = PrimaryBlue,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${step.subExpression} = ${step.result}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Final Result Card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                cornerRadius = 18.dp,
                backgroundColor = PrimaryBlue.copy(alpha = 0.1f),
                borderColor = PrimaryBlue.copy(alpha = 0.3f),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Final Answer",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = finalResult,
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
