package com.example.calcora.data

import android.content.Context
import android.content.SharedPreferences
import com.example.calcora.model.CalculationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class CalculatorHistoryRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("calcora_history", Context.MODE_PRIVATE)
    private val _history = MutableStateFlow<List<CalculationItem>>(emptyList())
    val history: StateFlow<List<CalculationItem>> = _history.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        val jsonStr = prefs.getString("items", "[]") ?: "[]"
        val list = mutableListOf<CalculationItem>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    CalculationItem(
                        id = obj.getString("id"),
                        expression = obj.getString("expression"),
                        result = obj.getString("result"),
                        timestamp = obj.getLong("timestamp")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _history.value = list
    }

    private fun saveHistory(list: List<CalculationItem>) {
        val arr = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("expression", item.expression)
            obj.put("result", item.result)
            obj.put("timestamp", item.timestamp)
            arr.put(obj)
        }
        prefs.edit().putString("items", arr.toString()).apply()
        _history.value = list
    }

    fun addCalculation(expression: String, result: String) {
        val newItem = CalculationItem(expression = expression, result = result)
        // Put newest at the top, limit to 100 entries
        val updated = listOf(newItem) + _history.value.take(99)
        saveHistory(updated)
    }

    fun deleteCalculation(id: String) {
        val updated = _history.value.filterNot { it.id == id }
        saveHistory(updated)
    }

    fun clearAll() {
        saveHistory(emptyList())
    }
}
