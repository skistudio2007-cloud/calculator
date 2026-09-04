package com.example.calcora

import com.example.calcora.engine.BodmasEngine
import org.junit.Assert.*
import org.junit.Test

class BodmasEngineTest {

    @Test
    fun testBODMASPrecedence() {
        // 2 + 3 * 4 must be 14, not 20
        val res1 = BodmasEngine.evaluate("2 + 3 * 4")
        assertFalse(res1.isError)
        assertEquals("14", res1.resultString)

        // 10 - 2 + 5 must be 13
        val res2 = BodmasEngine.evaluate("10 - 2 + 5")
        assertFalse(res2.isError)
        assertEquals("13", res2.resultString)

        // Division before multiplication / addition
        val res3 = BodmasEngine.evaluate("20 / 4 * 2 + 1")
        assertFalse(res3.isError)
        assertEquals("11", res3.resultString)
    }

    @Test
    fun testBracketsAndNesting() {
        // (5 + 3) * 2 = 16
        val res1 = BodmasEngine.evaluate("(5 + 3) * 2")
        assertFalse(res1.isError)
        assertEquals("16", res1.resultString)

        // Nested brackets: ((2 + 3) * (4 - 1)) / 3 = 5
        val res2 = BodmasEngine.evaluate("((2 + 3) * (4 - 1)) / 3")
        assertFalse(res2.isError)
        assertEquals("5", res2.resultString)
    }

    @Test
    fun testFloatingPointAccuracy() {
        // 0.1 + 0.2 must display 0.3 without IEEE 754 precision artifacts
        val res = BodmasEngine.evaluate("0.1 + 0.2")
        assertFalse(res.isError)
        assertEquals("0.3", res.resultString)
    }

    @Test
    fun testDivisionByZero() {
        val res = BodmasEngine.evaluate("10 / 0")
        assertTrue(res.isError)
        assertEquals("Cannot divide by zero", res.errorMessage)
    }

    @Test
    fun testBracketValidation() {
        val res1 = BodmasEngine.evaluate("(5 + 2")
        assertTrue(res1.isError)
        assertEquals("Check your brackets", res1.errorMessage)

        val res2 = BodmasEngine.evaluate("5 + 2)")
        assertTrue(res2.isError)
        assertEquals("Check your brackets", res2.errorMessage)
    }

    @Test
    fun testScientificFunctions() {
        // sin(90) in degrees = 1
        val sinRes = BodmasEngine.evaluate("sin(90)", isDegreeMode = true)
        assertFalse(sinRes.isError)
        assertEquals("1", sinRes.resultString)

        // cos(0) = 1
        val cosRes = BodmasEngine.evaluate("cos(0)", isDegreeMode = true)
        assertFalse(cosRes.isError)
        assertEquals("1", cosRes.resultString)

        // sqrt(144) = 12
        val sqrtRes = BodmasEngine.evaluate("sqrt(144)")
        assertFalse(sqrtRes.isError)
        assertEquals("12", sqrtRes.resultString)

        // 2^3 = 8
        val powRes = BodmasEngine.evaluate("2 ^ 3")
        assertFalse(powRes.isError)
        assertEquals("8", powRes.resultString)

        // 5! = 120
        val factRes = BodmasEngine.evaluate("fact(5)")
        assertFalse(factRes.isError)
        assertEquals("120", factRes.resultString)
    }

    @Test
    fun testStepDecomposition() {
        val res = BodmasEngine.evaluate("10 + 5 * 2", generateSteps = true)
        assertFalse(res.isError)
        assertEquals("20", res.resultString)
        assertTrue(res.steps.isNotEmpty())
        assertEquals(2, res.steps.size)
        assertEquals("Multiplication", res.steps[0].operationType)
        assertEquals("Addition", res.steps[1].operationType)
    }
}
