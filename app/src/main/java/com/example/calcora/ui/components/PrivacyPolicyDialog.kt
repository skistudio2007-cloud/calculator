package com.example.calcora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AmoledSurface,
        scrimColor = Color.Black.copy(alpha = 0.65f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF333842))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AmoledOperatorRed.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Security,
                            contentDescription = null,
                            tint = AmoledOperatorRed,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = "Privacy Policy",
                        color = AmoledDigitText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CalcoraSerif
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = AmoledSecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PolicyItem(
                    icon = Icons.Outlined.Lock,
                    title = "100% Offline & Private",
                    description = "Calcora operates entirely on your local device. None of your calculations, financial calculations, unit conversions, or inputs are transmitted to external servers."
                )

                PolicyItem(
                    icon = Icons.Outlined.PrivacyTip,
                    title = "Zero Data Tracking",
                    description = "We do not use analytics SDKs, advertising IDs, or behavioral tracking libraries. Your usage patterns stay completely confidential."
                )

                PolicyItem(
                    icon = Icons.Outlined.Vibration,
                    title = "Permissions Usage",
                    description = "The VIBRATE permission is used exclusively to generate subtle haptic tactile feedback when tapping keypad buttons. No camera, location, contacts, or storage permissions are required."
                )

                PolicyItem(
                    icon = Icons.Outlined.Security,
                    title = "Local History & Settings",
                    description = "Your calculation history and customized preferences are stored in Android private app storage. You can purge all calculation history at any time from Settings."
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Dismiss Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmoledOperatorRed),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "I Understand",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun PolicyItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF181B22))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AmoledOperatorRed,
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Column {
            Text(
                text = title,
                color = AmoledDigitText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = AmoledSecondaryText,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}
