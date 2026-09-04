package com.example.calcora.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

enum class CalculatorMode {
    STANDARD, SCIENTIFIC
}

class PreferencesRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("calcora_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _calculatorMode = MutableStateFlow(loadCalculatorMode())
    val calculatorMode: StateFlow<CalculatorMode> = _calculatorMode.asStateFlow()

    private val _hapticEnabled = MutableStateFlow(prefs.getBoolean("haptic_enabled", true))
    val hapticEnabled: StateFlow<Boolean> = _hapticEnabled.asStateFlow()

    private val _soundEnabled = MutableStateFlow(prefs.getBoolean("sound_enabled", false))
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _bodmasStepsEnabled = MutableStateFlow(prefs.getBoolean("bodmas_steps_enabled", true))
    val bodmasStepsEnabled: StateFlow<Boolean> = _bodmasStepsEnabled.asStateFlow()

    private val _favorites = MutableStateFlow(loadFavorites())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private fun loadThemeMode(): ThemeMode {
        val name = prefs.getString("theme_mode", ThemeMode.DARK.name) ?: ThemeMode.DARK.name
        return try {
            ThemeMode.valueOf(name)
        } catch (e: Exception) {
            ThemeMode.DARK
        }
    }

    private fun loadCalculatorMode(): CalculatorMode {
        val name = prefs.getString("calc_mode", CalculatorMode.STANDARD.name) ?: CalculatorMode.STANDARD.name
        return try {
            CalculatorMode.valueOf(name)
        } catch (e: Exception) {
            CalculatorMode.STANDARD
        }
    }

    private fun loadFavorites(): Set<String> {
        val defaultFavs = setOf("emi", "currency", "percentage", "bmi")
        return prefs.getStringSet("favorites", defaultFavs) ?: defaultFavs
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _themeMode.value = mode
    }

    fun setCalculatorMode(mode: CalculatorMode) {
        prefs.edit().putString("calc_mode", mode.name).apply()
        _calculatorMode.value = mode
    }

    fun setHapticEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("haptic_enabled", enabled).apply()
        _hapticEnabled.value = enabled
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
        _soundEnabled.value = enabled
    }

    fun setBodmasStepsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("bodmas_steps_enabled", enabled).apply()
        _bodmasStepsEnabled.value = enabled
    }

    fun toggleFavorite(toolId: String) {
        val current = _favorites.value.toMutableSet()
        if (current.contains(toolId)) {
            current.remove(toolId)
        } else {
            current.add(toolId)
        }
        prefs.edit().putStringSet("favorites", current).apply()
        _favorites.value = current
    }

    fun isFavorite(toolId: String): Boolean {
        return _favorites.value.contains(toolId)
    }
}
