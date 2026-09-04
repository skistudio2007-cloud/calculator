package com.example.calcora.ui.tools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.data.PreferencesRepository
import com.example.calcora.model.ToolCategory
import com.example.calcora.model.ToolItem
import com.example.calcora.model.ToolRegistry
import com.example.calcora.theme.LocalGlassColors
import com.example.calcora.theme.PrimaryBlue
import com.example.calcora.ui.components.GlassCard
import com.example.calcora.ui.components.TopHeader

@Composable
fun ToolsScreen(
    preferencesRepository: PreferencesRepository,
    onSelectTool: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val favorites by preferencesRepository.favorites.collectAsState()
    val glassColors = LocalGlassColors.current

    val allTools = remember { ToolRegistry.allTools }

    val filteredTools = remember(searchQuery, allTools) {
        if (searchQuery.isBlank()) allTools
        else allTools.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
        }
    }

    val favoriteTools = remember(favorites, allTools) {
        allTools.filter { favorites.contains(it.id) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopHeader(
            title = "Tools & Converters",
            modeLabel = "All-in-One Utility Hub",
            onBack = onBack
        )

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search tools and converters...",
                        color = glassColors.secondaryText.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = glassColors.secondaryText
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search",
                                tint = glassColors.secondaryText
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = glassColors.cardBackground,
                    unfocusedContainerColor = glassColors.cardBackground,
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = glassColors.cardBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Tools Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Favorites Section (only when not searching and favorites exist)
            if (searchQuery.isBlank() && favoriteTools.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    CategoryHeader(
                        title = "FAVORITES",
                        icon = Icons.Filled.Favorite,
                        iconTint = Color(0xFFE91E63)
                    )
                }

                items(favoriteTools, key = { "fav_${it.id}" }) { tool ->
                    ToolCard(
                        tool = tool,
                        isFavorite = true,
                        onToggleFavorite = { preferencesRepository.toggleFavorite(tool.id) },
                        onClick = { onSelectTool(tool.id) }
                    )
                }

                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Categorized Sections
            ToolCategory.values().forEach { category ->
                val categoryTools = filteredTools.filter { it.category == category }
                if (categoryTools.isNotEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        CategoryHeader(title = category.title)
                    }

                    items(categoryTools, key = { it.id }) { tool ->
                        val isFav = favorites.contains(tool.id)
                        ToolCard(
                            tool = tool,
                            isFavorite = isFav,
                            onToggleFavorite = { preferencesRepository.toggleFavorite(tool.id) },
                            onClick = { onSelectTool(tool.id) }
                        )
                    }

                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    title: String,
    icon: ImageVector? = null,
    iconTint: Color = PrimaryBlue
) {
    val glassColors = LocalGlassColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue,
            letterSpacing = 1.sp,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun ToolCard(
    tool: ToolItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    val glassColors = LocalGlassColors.current
    val icon = getToolIcon(tool.iconName)

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        cornerRadius = 20.dp,
        elevation = 2.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(glassColors.operatorButtonBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = glassColors.primaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Toggle favorite",
                        tint = if (isFavorite) Color(0xFFE91E63) else glassColors.secondaryText.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column {
                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tool.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = glassColors.secondaryText,
                    fontSize = 11.sp,
                    maxLines = 2,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

private fun getToolIcon(name: String): ImageVector {
    return when (name) {
        "AccountBalance" -> Icons.Default.AccountBalance
        "Payments" -> Icons.Default.Payments
        "TrendingUp" -> Icons.Default.TrendingUp
        "ShowChart" -> Icons.Default.ShowChart
        "ReceiptLong" -> Icons.Default.ReceiptLong
        "LocalOffer" -> Icons.Default.LocalOffer
        "Percent" -> Icons.Default.Percent
        "Cake" -> Icons.Default.Cake
        "FitnessCenter" -> Icons.Default.FitnessCenter
        "PieChart" -> Icons.Default.PieChart
        "CalendarMonth" -> Icons.Default.CalendarMonth
        "Group" -> Icons.Default.Group
        "Paid" -> Icons.Default.Paid
        "CurrencyExchange" -> Icons.Default.CurrencyExchange
        "Straighten" -> Icons.Default.Straighten
        "Scale" -> Icons.Default.Scale
        "Thermostat" -> Icons.Default.Thermostat
        "SquareFoot" -> Icons.Default.SquareFoot
        "WaterDrop" -> Icons.Default.WaterDrop
        "Speed" -> Icons.Default.Speed
        "Schedule" -> Icons.Default.Schedule
        "Storage" -> Icons.Default.Storage
        else -> Icons.Default.Calculate
    }
}
