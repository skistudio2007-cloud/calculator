package com.example.calcora.engine

data class MathStep(
    val stepNumber: Int,
    val operationType: String,
    val subExpression: String,
    val result: String,
    val intermediateExpression: String
)

data class EvaluationResult(
    val resultString: String,
    val numericValue: Double?,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val steps: List<MathStep> = emptyList()
)
