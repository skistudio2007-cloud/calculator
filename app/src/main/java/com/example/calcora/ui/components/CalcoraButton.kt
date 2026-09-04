package com.example.calcora.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.theme.*

enum class ButtonRole {
    NUMBER,
    OPERATOR,
    EQUALS,
    CLEAR,
    FUNCTION
}

@Composable
fun CalcoraButton(
    text: String,
    role: ButtonRole = ButtonRole.NUMBER,
    modifier: Modifier = Modifier,
    isCircular: Boolean = true,
    cornerRadius: Dp = 20.dp,
    fontSize: TextUnit = if (role == ButtonRole.NUMBER) 26.sp else 24.sp,
    fontFamily: FontFamily = FontFamily.Default,
    hapticEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val view = LocalView.current
    val context = LocalContext.current
    var isPressed by remember { mutableStateOf(false) }

    val vibrator = remember {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (_: Exception) {
            null
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )

    val shape = if (isCircular) CircleShape else RoundedCornerShape(cornerRadius)

    val bgColor = when {
        isPressed -> when (role) {
            ButtonRole.EQUALS -> AmoledEqualsRed.copy(alpha = 0.85f)
            else -> AmoledButtonBgPressed
        }
        role == ButtonRole.EQUALS -> AmoledEqualsRed
        else -> AmoledButtonBg
    }

    val textColor = when (role) {
        ButtonRole.EQUALS -> Color.White
        ButtonRole.OPERATOR -> AmoledOperatorRed
        ButtonRole.CLEAR -> AmoledClearOrange
        ButtonRole.FUNCTION -> AmoledSecondaryText
        ButtonRole.NUMBER -> AmoledDigitText
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .clip(shape)
            .background(bgColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        if (hapticEnabled) {
                            try {
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
                                } else {
                                    @Suppress("DEPRECATION")
                                    vibrator?.vibrate(20)
                                }
                            } catch (_: Exception) {}
                        }
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontFamily = fontFamily,
            fontWeight = if (role == ButtonRole.EQUALS) FontWeight.Bold else FontWeight.Normal
        )
    }
}
