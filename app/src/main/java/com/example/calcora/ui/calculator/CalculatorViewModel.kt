package com.example.calcora.ui.calculator

import androidx.lifecycle.ViewModel
import com.example.calcora.data.CalculatorHistoryRepository
import com.example.calcora.data.CalculatorMode
import com.example.calcora.data.PreferencesRepository
import com.example.calcora.engine.BodmasEngine
import com.example.calcora.engine.MathStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CalculatorUiState(
    val expression: String = "",
    val previewResult: String = "",
    val finalResult: String = "",
    val isEvaluated: Boolean = false,
    val isDegreeMode: Boolean = true,
    val isScientific: Boolean = false,
    val steps: List<MathStep> = emptyList(),
    val errorMessage: String? = null
)

class CalculatorViewModel(
    private val historyRepository: CalculatorHistoryRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CalculatorUiState(
            isScientific = preferencesRepository.calculatorMode.value == CalculatorMode.SCIENTIFIC
        )
    )
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun toggleScientific() {
        val newMode = !_uiState.value.isScientific
        _uiState.value = _uiState.value.copy(isScientific = newMode)
        preferencesRepository.setCalculatorMode(
            if (newMode) CalculatorMode.SCIENTIFIC else CalculatorMode.STANDARD
        )
    }

    fun toggleDegreeMode() {
        _uiState.value = _uiState.value.copy(isDegreeMode = !_uiState.value.isDegreeMode)
        updateLivePreview(_uiState.value.expression)
    }

    fun onInput(char: String) {
        val current = _uiState.value
        val newExpr = if (current.isEvaluated) {
            if (isOperator(char)) {
                current.finalResult + char
            } else {
                char
            }
        } else {
            current.expression + char
        }

        _uiState.value = current.copy(
            expression = newExpr,
            isEvaluated = false,
            errorMessage = null
        )
        updateLivePreview(newExpr)
    }

    fun onBackspace() {
        val current = _uiState.value
        if (current.isEvaluated) {
            clear()
            return
        }
        if (current.expression.isNotEmpty()) {
            val newExpr = current.expression.dropLast(1)
            _uiState.value = current.copy(
                expression = newExpr,
                errorMessage = null
            )
            updateLivePreview(newExpr)
        }
    }

    fun clear() {
        _uiState.value = _uiState.value.copy(
            expression = "",
            previewResult = "",
            finalResult = "",
            isEvaluated = false,
            steps = emptyList(),
            errorMessage = null
        )
    }

    fun onParentheses() {
        val current = _uiState.value
        val expr = current.expression
        val openCount = expr.count { it == '(' }
        val closeCount = expr.count { it == ')' }

        val toAppend = if (openCount > closeCount) {
            val lastChar = expr.lastOrNull()
            if (lastChar != null && (lastChar.isDigit() || lastChar == ')')) ")" else "("
        } else {
            "("
        }
        onInput(toAppend)
    }

    fun onFunction(fn: String) {
        onInput("$fn(")
    }

    fun onPower(exp: String) {
        onInput("^$exp")
    }

    fun onInverse() {
        onInput("1/")
    }

    fun toggleSign() {
        val current = _uiState.value
        val expr = current.expression
        if (expr.isEmpty()) return

        // If ends with a number, wrap in - or negate
        val regex = Regex("""(-?\d+(\.\d+)?)$""")
        val match = regex.find(expr)
        if (match != null) {
            val numStr = match.value
            val replaced = if (numStr.startsWith("-")) {
                numStr.drop(1)
            } else {
                "-$numStr"
            }
            val newExpr = expr.replaceRange(match.range, replaced)
            _uiState.value = current.copy(expression = newExpr)
            updateLivePreview(newExpr)
        }
    }

    fun evaluate() {
        val current = _uiState.value
        val expr = current.expression.trim()
        if (expr.isEmpty()) return

        val result = BodmasEngine.evaluate(
            expression = expr,
            isDegreeMode = current.isDegreeMode,
            generateSteps = preferencesRepository.bodmasStepsEnabled.value
        )

        if (result.isError) {
            _uiState.value = current.copy(
                errorMessage = result.errorMessage ?: "Invalid expression",
                previewResult = ""
            )
        } else {
            _uiState.value = current.copy(
                finalResult = result.resultString,
                previewResult = "",
                isEvaluated = true,
                steps = result.steps,
                errorMessage = null
            )
            historyRepository.addCalculation(expr, result.resultString)
        }
    }

    fun reuseCalculation(expression: String, result: String) {
        _uiState.value = _uiState.value.copy(
            expression = result,
            finalResult = result,
            previewResult = "",
            isEvaluated = true,
            errorMessage = null
        )
    }

    private fun updateLivePreview(expr: String) {
        if (expr.isEmpty() || isOperator(expr.takeLast(1))) {
            _uiState.value = _uiState.value.copy(previewResult = "")
            return
        }
        val preview = BodmasEngine.evaluate(
            expression = expr,
            isDegreeMode = _uiState.value.isDegreeMode,
            generateSteps = false
        )
        if (!preview.isError && preview.numericValue != null) {
            _uiState.value = _uiState.value.copy(previewResult = preview.resultString)
        } else {
            _uiState.value = _uiState.value.copy(previewResult = "")
        }
    }

    private fun isOperator(s: String): Boolean {
        return s in listOf("+", "−", "-", "×", "*", "÷", "/", "^", "%")
    }
}
