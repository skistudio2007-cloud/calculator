package com.example.calcora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcora.data.*
import com.example.calcora.model.UnitRegistry
import com.example.calcora.theme.*
import com.example.calcora.ui.calculator.CalculatorScreen
import com.example.calcora.ui.calculator.CalculatorViewModel
import com.example.calcora.ui.history.HistoryScreen
import com.example.calcora.ui.navigation.BottomNavBar
import com.example.calcora.ui.navigation.MainTab
import com.example.calcora.ui.settings.SettingsScreen
import com.example.calcora.ui.tools.ToolsScreen
import com.example.calcora.ui.tools.converters.CurrencyConverterScreen
import com.example.calcora.ui.tools.converters.UnitConverterScreen
import com.example.calcora.ui.tools.dailylife.*
import com.example.calcora.ui.tools.finance.*
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private lateinit var historyRepository: CalculatorHistoryRepository
    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var currencyRepository: CurrencyRepository
    private lateinit var calculatorViewModel: CalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())

        historyRepository = CalculatorHistoryRepository(applicationContext)
        preferencesRepository = PreferencesRepository(applicationContext)
        currencyRepository = CurrencyRepository(applicationContext)
        calculatorViewModel = CalculatorViewModel(historyRepository, preferencesRepository)

        setContent {
            val themeMode by preferencesRepository.themeMode.collectAsState()
            val isDark = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            CalcoraTheme(darkTheme = isDark) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalcoraApp(
                        calculatorViewModel = calculatorViewModel,
                        historyRepository = historyRepository,
                        preferencesRepository = preferencesRepository,
                        currencyRepository = currencyRepository
                    )
                }
            }
        }
    }
}

@Composable
fun CalcoraApp(
    calculatorViewModel: CalculatorViewModel,
    historyRepository: CalculatorHistoryRepository,
    preferencesRepository: PreferencesRepository,
    currencyRepository: CurrencyRepository
) {
    var showSplash by remember { mutableStateOf(true) }
    var currentTab by remember { mutableStateOf(MainTab.CALCULATOR) }
    var activeToolId by remember { mutableStateOf<String?>(null) }

    val hapticEnabled by preferencesRepository.hapticEnabled.collectAsState()
    val favorites by preferencesRepository.favorites.collectAsState()

    // Short premium splash screen (~700ms)
    LaunchedEffect(Unit) {
        delay(750)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        // Intercept hardware back button when inside a tool or non-calculator tab
        BackHandler(enabled = activeToolId != null || currentTab != MainTab.CALCULATOR) {
            if (activeToolId != null) {
                activeToolId = null
            } else {
                currentTab = MainTab.CALCULATOR
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (activeToolId != null) {
                val toolId = activeToolId!!
                val isFav = favorites.contains(toolId)
                val toggleFav = { preferencesRepository.toggleFavorite(toolId) }
                val back = { activeToolId = null }

                when (toolId) {
                    "emi" -> EmiCalculatorScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "loan" -> LoanCalculatorScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "simple_interest" -> SimpleInterestScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "compound_interest" -> CompoundInterestScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "gst" -> GstCalculatorScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "discount" -> DiscountCalculatorScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "profit_loss" -> ProfitLossScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "age" -> AgeCalculatorScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "bmi" -> BmiCalculatorScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "percentage" -> PercentageCalculatorScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "date_diff" -> DateDiffScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "split_bill" -> SplitBillScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "tip" -> TipCalculatorScreen(isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "currency" -> CurrencyConverterScreen(currencyRepository = currencyRepository, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "length" -> UnitConverterScreen(category = UnitRegistry.lengthCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "weight" -> UnitConverterScreen(category = UnitRegistry.weightCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "temperature" -> UnitConverterScreen(
                        category = com.example.calcora.model.UnitCategory(
                            id = "temperature",
                            title = "Temperature Converter",
                            baseUnitName = "Celsius",
                            units = listOf(
                                com.example.calcora.model.UnitItem("Celsius", "°C"),
                                com.example.calcora.model.UnitItem("Fahrenheit", "°F"),
                                com.example.calcora.model.UnitItem("Kelvin", "K")
                            )
                        ),
                        isTemperature = true,
                        isFavorite = isFav,
                        onToggleFavorite = toggleFav,
                        onBack = back
                    )
                    "area" -> UnitConverterScreen(category = UnitRegistry.areaCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "volume" -> UnitConverterScreen(category = UnitRegistry.volumeCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "speed" -> UnitConverterScreen(category = UnitRegistry.speedCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "time" -> UnitConverterScreen(category = UnitRegistry.timeCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "storage" -> UnitConverterScreen(category = UnitRegistry.storageCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "pressure" -> UnitConverterScreen(category = UnitRegistry.pressureCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "force" -> UnitConverterScreen(category = UnitRegistry.forceCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "power" -> UnitConverterScreen(category = UnitRegistry.powerCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "energy" -> UnitConverterScreen(category = UnitRegistry.energyCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "frequency" -> UnitConverterScreen(category = UnitRegistry.frequencyCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "angle" -> UnitConverterScreen(category = UnitRegistry.angleCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    "fuel" -> UnitConverterScreen(category = UnitRegistry.fuelCategory, isFavorite = isFav, onToggleFavorite = toggleFav, onBack = back)
                    else -> activeToolId = null
                }
            } else {
                // Main tab contents
                when (currentTab) {
                    MainTab.CALCULATOR -> {
                        CalculatorScreen(
                            viewModel = calculatorViewModel,
                            hapticEnabled = hapticEnabled,
                            onNavigateToHistory = { currentTab = MainTab.HISTORY },
                            onNavigateToTools = { currentTab = MainTab.TOOLS },
                            onNavigateToSettings = { currentTab = MainTab.SETTINGS },
                            onSelectTool = { toolId -> activeToolId = toolId }
                        )
                    }
                    MainTab.TOOLS -> {
                        ToolsScreen(
                            preferencesRepository = preferencesRepository,
                            onSelectTool = { toolId -> activeToolId = toolId },
                            onBack = { currentTab = MainTab.CALCULATOR }
                        )
                    }
                    MainTab.HISTORY -> {
                        HistoryScreen(
                            historyRepository = historyRepository,
                            onReuseCalculation = { item ->
                                calculatorViewModel.reuseCalculation(item.expression, item.result)
                                currentTab = MainTab.CALCULATOR
                            },
                            onBack = { currentTab = MainTab.CALCULATOR }
                        )
                    }
                    MainTab.SETTINGS -> {
                        SettingsScreen(
                            preferencesRepository = preferencesRepository,
                            historyRepository = historyRepository,
                            currencyRepository = currencyRepository,
                            onBack = { currentTab = MainTab.CALCULATOR }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0.2f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
        alpha.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 500)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale.value)
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(PremiumLightBlue, PrimaryBlue))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "C",
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Calcora",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Premium Calculator & Converter",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
