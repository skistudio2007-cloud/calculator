package com.example.calcora.model

import java.util.UUID

data class CalculationItem(
    val id: String = UUID.randomUUID().toString(),
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)
