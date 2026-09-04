package com.example.calcora.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.GlassCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolScaffold(
    title: String,
    subtitle: String? = null,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val glassColors = LocalGlassColors.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = glassColors.secondaryText
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    if (onToggleFavorite != null) {
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color(0xFFE91E63) else glassColors.secondaryText
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            content = content
        )
    }
}

@Composable
fun CalcoraInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "0",
    suffix: String? = null,
    keyboardType: KeyboardType = KeyboardType.Decimal
) {
    val glassColors = LocalGlassColors.current

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = glassColors.secondaryText,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = glassColors.secondaryText.copy(alpha = 0.5f)) },
            trailingIcon = if (suffix != null) {
                {
                    Text(
                        text = suffix,
                        style = MaterialTheme.typography.bodyMedium,
                        color = glassColors.secondaryText,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = glassColors.cardBackground,
                unfocusedContainerColor = glassColors.cardBackground,
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = glassColors.cardBorder,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ResultMetric(
    label: String,
    value: String,
    highlight: Boolean = false,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColors.current

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (highlight) PrimaryBlue.copy(alpha = 0.85f) else glassColors.secondaryText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = if (highlight) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.titleLarge,
            color = if (highlight) PrimaryBlue else MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
    }
}
