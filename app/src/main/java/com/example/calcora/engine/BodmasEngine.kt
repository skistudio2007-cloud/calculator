package com.example.calcora.engine

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.*

object BodmasEngine {

    private val decimalFormat = DecimalFormat("#,##0.##########")
    private val scientificFormat = DecimalFormat("0.######E0")

    fun evaluate(
        expression: String,
        isDegreeMode: Boolean = true,
        generateSteps: Boolean = true
    ): EvaluationResult {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) {
            return EvaluationResult("", null)
        }

        // Bracket validation
        var openCount = 0
        for (ch in trimmed) {
            if (ch == '(') openCount++
            else if (ch == ')') openCount--
            if (openCount < 0) {
                return EvaluationResult("Check your brackets", null, isError = true, errorMessage = "Check your brackets")
            }
        }
        if (openCount > 0) {
            return EvaluationResult("Check your brackets", null, isError = true, errorMessage = "Check your brackets")
        }

        try {
            val normalized = normalizeExpression(trimmed)
            val steps = if (generateSteps) generateBodmasSteps(normalized, isDegreeMode) else emptyList()
            val finalVal = evaluateInternal(normalized, isDegreeMode)

            if (finalVal.isInfinite() || finalVal.isNaN()) {
                return EvaluationResult("Cannot divide by zero", null, isError = true, errorMessage = "Cannot divide by zero")
            }

            val formatted = formatNumber(finalVal)
            return EvaluationResult(
                resultString = formatted,
                numericValue = finalVal,
                isError = false,
                steps = steps
            )
        } catch (e: ArithmeticException) {
            val msg = if (e.message?.contains("zero", ignoreCase = true) == true) "Cannot divide by zero" else "Invalid expression"
            return EvaluationResult(msg, null, isError = true, errorMessage = msg)
        } catch (e: Exception) {
            val msg = if (e.message?.contains("zero", ignoreCase = true) == true) "Cannot divide by zero" else "Invalid expression"
            return EvaluationResult(msg, null, isError = true, errorMessage = msg)
        }
    }

    fun formatNumber(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        val absVal = abs(value)
        if (absVal != 0.0 && (absVal >= 1e12 || absVal <= 1e-6)) {
            return scientificFormat.format(value)
        }
        // Round to 10 decimal places to eliminate floating point issues (0.1 + 0.2 -> 0.3)
        val bd = BigDecimal(value, MathContext.DECIMAL64).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros()
        return bd.toPlainString()
    }

    private fun normalizeExpression(expr: String): String {
        return expr
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("π", Math.PI.toString())
            .replace("e", Math.E.toString())
    }

    private sealed class Token {
        data class Num(val value: Double) : Token()
        data class Op(val symbol: Char, val precedence: Int, val isRightAssoc: Boolean = false) : Token()
        data class Func(val name: String) : Token()
        object OpenParen : Token()
        object CloseParen : Token()
    }

    private fun tokenize(expr: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        var expectUnary = true

        while (i < expr.length) {
            val ch = expr[i]
            when {
                ch.isWhitespace() -> i++
                ch.isDigit() || ch == '.' -> {
                    val sb = StringBuilder()
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.' || expr[i] == 'E' || expr[i] == 'e')) {
                        if ((expr[i] == 'E' || expr[i] == 'e') && i + 1 < expr.length && (expr[i + 1] == '+' || expr[i + 1] == '-')) {
                            sb.append(expr[i])
                            sb.append(expr[i + 1])
                            i += 2
                        } else {
                            sb.append(expr[i])
                            i++
                        }
                    }
                    val num = sb.toString().toDouble()
                    tokens.add(Token.Num(num))
                    expectUnary = false
                }
                ch == '+' || ch == '-' -> {
                    if (expectUnary && ch == '-') {
                        // Unary minus: represented as 0 - x or negate
                        tokens.add(Token.Num(0.0))
                        tokens.add(Token.Op('-', 1))
                    } else {
                        tokens.add(Token.Op(ch, 1))
                    }
                    expectUnary = true
                    i++
                }
                ch == '*' || ch == '/' || ch == '%' -> {
                    tokens.add(Token.Op(ch, 2))
                    expectUnary = true
                    i++
                }
                ch == '^' -> {
                    tokens.add(Token.Op('^', 3, isRightAssoc = true))
                    expectUnary = true
                    i++
                }
                ch == '(' -> {
                    tokens.add(Token.OpenParen)
                    expectUnary = true
                    i++
                }
                ch == ')' -> {
                    tokens.add(Token.CloseParen)
                    expectUnary = false
                    i++
                }
                ch.isLetter() || ch == '√' -> {
                    val sb = StringBuilder()
                    if (ch == '√') {
                        sb.append("sqrt")
                        i++
                    } else {
                        while (i < expr.length && (expr[i].isLetter() || expr[i] == '√')) {
                            sb.append(expr[i])
                            i++
                        }
                    }
                    tokens.add(Token.Func(sb.toString()))
                    expectUnary = true
                }
                else -> {
                    i++
                }
            }
        }
        return tokens
    }

    private fun evaluateInternal(expr: String, isDegreeMode: Boolean): Double {
        val tokens = tokenize(expr)
        if (tokens.isEmpty()) return 0.0

        // Shunting-Yard to Reverse Polish Notation (RPN)
        val outputQueue = mutableListOf<Token>()
        val opStack = mutableListOf<Token>()

        for (token in tokens) {
            when (token) {
                is Token.Num -> outputQueue.add(token)
                is Token.Func -> opStack.add(token)
                is Token.Op -> {
                    while (opStack.isNotEmpty()) {
                        val top = opStack.last()
                        if (top is Token.Op && (
                                    (!token.isRightAssoc && token.precedence <= top.precedence) ||
                                            (token.isRightAssoc && token.precedence < top.precedence)
                                    )) {
                            outputQueue.add(opStack.removeAt(opStack.lastIndex))
                        } else if (top is Token.Func) {
                            outputQueue.add(opStack.removeAt(opStack.lastIndex))
                        } else {
                            break
                        }
                    }
                    opStack.add(token)
                }
                is Token.OpenParen -> opStack.add(token)
                is Token.CloseParen -> {
                    var foundOpen = false
                    while (opStack.isNotEmpty()) {
                        val top = opStack.removeAt(opStack.lastIndex)
                        if (top is Token.OpenParen) {
                            foundOpen = true
                            break
                        } else {
                            outputQueue.add(top)
                        }
                    }
                    if (!foundOpen) throw IllegalArgumentException("Check your brackets")
                    if (opStack.isNotEmpty() && opStack.last() is Token.Func) {
                        outputQueue.add(opStack.removeAt(opStack.lastIndex))
                    }
                }
            }
        }

        while (opStack.isNotEmpty()) {
            val top = opStack.removeAt(opStack.lastIndex)
            if (top is Token.OpenParen || top is Token.CloseParen) {
                throw IllegalArgumentException("Check your brackets")
            }
            outputQueue.add(top)
        }

        // Evaluate RPN
        val valStack = mutableListOf<Double>()
        for (token in outputQueue) {
            when (token) {
                is Token.Num -> valStack.add(token.value)
                is Token.Op -> {
                    if (valStack.size < 2) throw IllegalArgumentException("Invalid expression")
                    val b = valStack.removeAt(valStack.lastIndex)
                    val a = valStack.removeAt(valStack.lastIndex)
                    val res = when (token.symbol) {
                        '+' -> a + b
                        '-' -> a - b
                        '*' -> a * b
                        '/' -> {
                            if (abs(b) < 1e-12) throw ArithmeticException("Cannot divide by zero")
                            a / b
                        }
                        '%' -> (a * b) / 100.0
                        '^' -> a.pow(b)
                        else -> throw IllegalArgumentException("Unknown operator: ${token.symbol}")
                    }
                    valStack.add(res)
                }
                is Token.Func -> {
                    if (valStack.isEmpty()) throw IllegalArgumentException("Invalid expression")
                    val arg = valStack.removeAt(valStack.lastIndex)
                    val res = applyFunction(token.name, arg, isDegreeMode)
                    valStack.add(res)
                }
                else -> Unit
            }
        }

        if (valStack.size != 1) throw IllegalArgumentException("Invalid expression")
        return valStack[0]
    }

    private fun applyFunction(name: String, arg: Double, isDegreeMode: Boolean): Double {
        val rad = if (isDegreeMode) Math.toRadians(arg) else arg
        return when (name.lowercase()) {
            "sin" -> sin(rad)
            "cos" -> cos(rad)
            "tan" -> {
                val cosVal = cos(rad)
                if (abs(cosVal) < 1e-12) throw ArithmeticException("Tangent undefined")
                tan(rad)
            }
            "asin" -> {
                if (arg < -1.0 || arg > 1.0) throw ArithmeticException("Domain error for asin")
                val res = asin(arg)
                if (isDegreeMode) Math.toDegrees(res) else res
            }
            "acos" -> {
                if (arg < -1.0 || arg > 1.0) throw ArithmeticException("Domain error for acos")
                val res = acos(arg)
                if (isDegreeMode) Math.toDegrees(res) else res
            }
            "atan" -> {
                val res = atan(arg)
                if (isDegreeMode) Math.toDegrees(res) else res
            }
            "log" -> {
                if (arg <= 0) throw ArithmeticException("Domain error for log")
                log10(arg)
            }
            "ln" -> {
                if (arg <= 0) throw ArithmeticException("Domain error for ln")
                ln(arg)
            }
            "sqrt" -> {
                if (arg < 0) throw ArithmeticException("Domain error for sqrt")
                sqrt(arg)
            }
            "abs" -> abs(arg)
            "fact" -> factorial(arg)
            else -> throw IllegalArgumentException("Unknown function: $name")
        }
    }

    fun factorial(n: Double): Double {
        if (n < 0 || n != floor(n)) throw ArithmeticException("Factorial only for non-negative integers")
        if (n > 170) return Double.POSITIVE_INFINITY
        var res = 1.0
        val count = n.toLong()
        for (i in 2..count) {
            res *= i
        }
        return res
    }

    // Step-by-step BODMAS decomposition
    private fun generateBodmasSteps(expr: String, isDegreeMode: Boolean): List<MathStep> {
        val steps = mutableListOf<MathStep>()
        var current = expr
        var stepNum = 1

        // 1. Solve Parentheses innermost first
        val parenRegex = Regex("""\(([^()]+)\)""")
        while (parenRegex.containsMatchIn(current)) {
            val match = parenRegex.find(current) ?: break
            val inner = match.groupValues[1]
            val innerVal = evaluateInternal(inner, isDegreeMode)
            val formatted = formatNumber(innerVal)
            steps.add(
                MathStep(
                    stepNumber = stepNum++,
                    operationType = "Brackets (BODMAS)",
                    subExpression = "(${inner})",
                    result = formatted,
                    intermediateExpression = current.replaceRange(match.range, formatted)
                )
            )
            current = current.replaceRange(match.range, formatted)
        }

        // 2. Orders: Powers, Roots, Functions
        val funcRegex = Regex("""(sin|cos|tan|asin|acos|atan|log|ln|sqrt|abs)\((-?\d+(\.\d+)?)\)""")
        while (funcRegex.containsMatchIn(current)) {
            val match = funcRegex.find(current) ?: break
            val funcName = match.groupValues[1]
            val arg = match.groupValues[2].toDouble()
            val valRes = applyFunction(funcName, arg, isDegreeMode)
            val formatted = formatNumber(valRes)
            steps.add(
                MathStep(
                    stepNumber = stepNum++,
                    operationType = "Orders (Scientific Function)",
                    subExpression = "${funcName}($arg)",
                    result = formatted,
                    intermediateExpression = current.replaceRange(match.range, formatted)
                )
            )
            current = current.replaceRange(match.range, formatted)
        }

        val powerRegex = Regex("""(-?\d+(\.\d+)?)\s*\^\s*(-?\d+(\.\d+)?)""")
        while (powerRegex.containsMatchIn(current)) {
            val match = powerRegex.find(current) ?: break
            val a = match.groupValues[1].toDouble()
            val b = match.groupValues[3].toDouble()
            val res = a.pow(b)
            val formatted = formatNumber(res)
            steps.add(
                MathStep(
                    stepNumber = stepNum++,
                    operationType = "Orders (Power)",
                    subExpression = "$a ^ $b",
                    result = formatted,
                    intermediateExpression = current.replaceRange(match.range, formatted)
                )
            )
            current = current.replaceRange(match.range, formatted)
        }

        // 3. Division & Multiplication left to right
        val multDivRegex = Regex("""(-?\d+(\.\d+)?)\s*([*/%])\s*(-?\d+(\.\d+)?)""")
        while (multDivRegex.containsMatchIn(current)) {
            val match = multDivRegex.find(current) ?: break
            val a = match.groupValues[1].toDouble()
            val op = match.groupValues[3]
            val b = match.groupValues[4].toDouble()
            val res = when (op) {
                "*" -> a * b
                "/" -> {
                    if (abs(b) < 1e-12) throw ArithmeticException("Cannot divide by zero")
                    a / b
                }
                "%" -> (a * b) / 100.0
                else -> 0.0
            }
            val formatted = formatNumber(res)
            val opName = when (op) {
                "*" -> "Multiplication"
                "/" -> "Division"
                else -> "Percentage"
            }
            val displayOp = if (op == "*") "×" else if (op == "/") "÷" else "%"
            steps.add(
                MathStep(
                    stepNumber = stepNum++,
                    operationType = opName,
                    subExpression = "$a $displayOp $b",
                    result = formatted,
                    intermediateExpression = current.replaceRange(match.range, formatted)
                )
            )
            current = current.replaceRange(match.range, formatted)
        }

        // 4. Addition & Subtraction left to right
        val addSubRegex = Regex("""(-?\d+(\.\d+)?)\s*([+\-])\s*(-?\d+(\.\d+)?)""")
        while (addSubRegex.containsMatchIn(current)) {
            val match = addSubRegex.find(current) ?: break
            val a = match.groupValues[1].toDouble()
            val op = match.groupValues[3]
            val b = match.groupValues[4].toDouble()
            val res = when (op) {
                "+" -> a + b
                "-" -> a - b
                else -> 0.0
            }
            val formatted = formatNumber(res)
            val opName = if (op == "+") "Addition" else "Subtraction"
            val displayOp = if (op == "-") "−" else "+"
            steps.add(
                MathStep(
                    stepNumber = stepNum++,
                    operationType = opName,
                    subExpression = "$a $displayOp $b",
                    result = formatted,
                    intermediateExpression = current.replaceRange(match.range, formatted)
                )
            )
            current = current.replaceRange(match.range, formatted)
        }

        return steps
    }
}
