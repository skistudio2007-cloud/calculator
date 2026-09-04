package com.example.calcora.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CurrencyItem(
    val code: String,
    val name: String,
    val symbol: String,
    val rateAgainstUsd: Double // USD = 1.0
)

class CurrencyRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("calcora_currency", Context.MODE_PRIVATE)

    private val defaultRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.92,
        "GBP" to 0.79,
        "INR" to 83.50,
        "JPY" to 154.20,
        "CAD" to 1.36,
        "AUD" to 1.51,
        "CHF" to 0.90,
        "CNY" to 7.24,
        "SGD" to 1.35,
        "AED" to 3.67,
        "SAR" to 3.75,
        "NZD" to 1.64,
        "BRL" to 5.42,
        "KRW" to 1375.0,
        "MXN" to 18.25,
        "SEK" to 10.55,
        "NOK" to 10.72,
        "ZAR" to 18.15,
        "HKD" to 7.81,
        "THB" to 36.80,
        "IDR" to 16250.0,
        "MYR" to 4.71,
        "PHP" to 58.60,
        "TRY" to 32.80,
        "RUB" to 89.20,
        "PLN" to 3.98,
        "DKK" to 6.89,
        "HUF" to 365.0,
        "CZK" to 23.10,
        "ILS" to 3.72,
        "KWD" to 0.31
    )

    val currencyNames = mapOf(
        "USD" to ("United States Dollar" to "$"),
        "EUR" to ("Euro" to "€"),
        "GBP" to ("British Pound" to "£"),
        "INR" to ("Indian Rupee" to "₹"),
        "JPY" to ("Japanese Yen" to "¥"),
        "CAD" to ("Canadian Dollar" to "CA$"),
        "AUD" to ("Australian Dollar" to "A$"),
        "CHF" to ("Swiss Franc" to "CHF"),
        "CNY" to ("Chinese Yuan" to "¥"),
        "SGD" to ("Singapore Dollar" to "S$"),
        "AED" to ("UAE Dirham" to "AED"),
        "SAR" to ("Saudi Riyal" to "SAR"),
        "NZD" to ("New Zealand Dollar" to "NZ$"),
        "BRL" to ("Brazilian Real" to "R$"),
        "KRW" to ("South Korean Won" to "₩"),
        "MXN" to ("Mexican Peso" to "Mex$"),
        "SEK" to ("Swedish Krona" to "kr"),
        "NOK" to ("Norwegian Krone" to "kr"),
        "ZAR" to ("South African Rand" to "R"),
        "HKD" to ("Hong Kong Dollar" to "HK$"),
        "THB" to ("Thai Baht" to "฿"),
        "IDR" to ("Indonesian Rupiah" to "Rp"),
        "MYR" to ("Malaysian Ringgit" to "RM"),
        "PHP" to ("Philippine Peso" to "₱"),
        "TRY" to ("Turkish Lira" to "₺"),
        "RUB" to ("Russian Ruble" to "₽"),
        "PLN" to ("Polish Zloty" to "zł"),
        "DKK" to ("Danish Krone" to "kr"),
        "HUF" to ("Hungarian Forint" to "Ft"),
        "CZK" to ("Czech Koruna" to "Kč"),
        "ILS" to ("Israeli Shekel" to "₪"),
        "KWD" to ("Kuwaiti Dinar" to "KD")
    )

    private val _currencies = MutableStateFlow<List<CurrencyItem>>(loadCurrencies())
    val currencies: StateFlow<List<CurrencyItem>> = _currencies.asStateFlow()

    private val _lastUpdated = MutableStateFlow(loadLastUpdated())
    val lastUpdated: StateFlow<String> = _lastUpdated.asStateFlow()

    private fun loadLastUpdated(): String {
        val saved = prefs.getLong("last_updated_time", 0L)
        val time = if (saved == 0L) System.currentTimeMillis() else saved
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(time))
    }

    private fun loadCurrencies(): List<CurrencyItem> {
        return defaultRates.map { (code, defaultRate) ->
            val rate = prefs.getFloat("rate_$code", defaultRate.toFloat()).toDouble()
            val (name, symbol) = currencyNames[code] ?: (code to code)
            CurrencyItem(code, name, symbol, rate)
        }
    }

    fun convert(amount: Double, fromCode: String, toCode: String): Double {
        val fromRate = defaultRates[fromCode] ?: 1.0
        val toRate = defaultRates[toCode] ?: 1.0
        val amountInUsd = amount / fromRate
        return amountInUsd * toRate
    }

    fun refreshRates(): Boolean {
        // Update timestamp and notify
        val now = System.currentTimeMillis()
        prefs.edit().putLong("last_updated_time", now).apply()
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        _lastUpdated.value = sdf.format(Date(now))
        _currencies.value = loadCurrencies()
        return true
    }
}
