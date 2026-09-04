package com.example.calcora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.theme.AmoledOperatorRed
import com.example.calcora.theme.AmoledSurface

data class ConverterGridItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val isPrimary: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterGridSheet(
    onDismiss: () -> Unit,
    onSelectTool: (String) -> Unit
) {
    val items = remember {
        listOf(
            ConverterGridItem("currency", "Currency", Icons.Outlined.MonetizationOn, isPrimary = true),
            ConverterGridItem("area", "Area", Icons.Outlined.GridView),
            ConverterGridItem("length", "Length", Icons.Outlined.Straighten),
            ConverterGridItem("temperature", "Temperature", Icons.Outlined.Thermostat),
            ConverterGridItem("volume", "Volume", Icons.Outlined.ViewInAr),
            ConverterGridItem("weight", "Mass", Icons.Outlined.Scale),
            ConverterGridItem("storage", "Data", Icons.Outlined.DonutLarge),
            ConverterGridItem("speed", "Speed", Icons.Outlined.Speed),
            ConverterGridItem("time", "Time", Icons.Outlined.Schedule),
            ConverterGridItem("pressure", "Pressure", Icons.Outlined.Speed),
            ConverterGridItem("force", "Force", Icons.Outlined.TrendingUp),
            ConverterGridItem("power", "Power", Icons.Outlined.Bolt),
            ConverterGridItem("energy", "Energy", Icons.Outlined.LocalFireDepartment),
            ConverterGridItem("frequency", "Frequency", Icons.Outlined.GraphicEq),
            ConverterGridItem("angle", "Angle", Icons.Outlined.ChangeHistory),
            ConverterGridItem("fuel", "Fuel", Icons.Outlined.LocalGasStation),
            ConverterGridItem("age", "Age", Icons.Outlined.Cake),
            ConverterGridItem("numeral", "Numeral System", Icons.Outlined.Pin),
            ConverterGridItem("gst", "GST", Icons.Outlined.ReceiptLong),
            ConverterGridItem("split_bill", "Split bill", Icons.Outlined.Group),
            ConverterGridItem("date_diff", "Date", Icons.Outlined.CalendarMonth),
            ConverterGridItem("bmi", "BMI", Icons.Outlined.FitnessCenter),
            ConverterGridItem("discount", "Discount", Icons.Outlined.Sell),
            ConverterGridItem("loan", "Loan", Icons.Outlined.AccountBalance),
            ConverterGridItem("emi", "EMI", Icons.Outlined.Payments),
            ConverterGridItem("simple_interest", "Investment", Icons.Outlined.TrendingUp),
            ConverterGridItem("compound_interest", "Savings", Icons.Outlined.Savings),
            ConverterGridItem("percentage", "Percentage", Icons.Outlined.Percent)
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AmoledSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF333333))
            )
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "UNIT CONVERTER",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.5.sp
                ),
                color = Color(0xFFC4C4C4),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF14171D))
                            .border(1.dp, Color(0x18FFFFFF), RoundedCornerShape(16.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onSelectTool(item.id)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (item.isPrimary) AmoledOperatorRed.copy(alpha = 0.15f) else Color(0xFF1B1E26)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (item.isPrimary) AmoledOperatorRed else Color(0xFFCFD3DC),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (item.isPrimary) AmoledOperatorRed else Color(0xFFC4C8D2),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
