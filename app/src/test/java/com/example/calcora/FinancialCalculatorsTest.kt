package com.example.calcora

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.abs
import kotlin.math.pow

class FinancialCalculatorsTest {

    @Test
    fun testEmiCalculation() {
        val p = 100000.0
        val annualRate = 12.0
        val tenureMonths = 12.0

        val r = annualRate / (12.0 * 100.0) // 0.01
        val emi = (p * r * (1.0 + r).pow(tenureMonths)) / ((1.0 + r).pow(tenureMonths) - 1.0)

        // For 100,000 at 12% for 1 year, monthly EMI is approx 8884.88
        assertEquals(8884.88, emi, 0.05)
    }

    @Test
    fun testSimpleInterest() {
        val p = 10000.0
        val r = 5.0
        val t = 2.0
        val si = (p * r * t) / 100.0
        val total = p + si

        assertEquals(1000.0, si, 0.001)
        assertEquals(11000.0, total, 0.001)
    }

    @Test
    fun testCompoundInterest() {
        val p = 10000.0
        val r = 0.10 // 10%
        val t = 2.0
        val n = 1.0 // yearly
        val amount = p * (1.0 + (r / n)).pow(n * t)
        val ci = amount - p

        assertEquals(12100.0, amount, 0.001)
        assertEquals(2100.0, ci, 0.001)
    }

    @Test
    fun testGstCalculations() {
        val net = 1000.0
        val rate = 18.0

        // Add GST
        val gstAdd = (net * rate) / 100.0
        val gross = net + gstAdd
        assertEquals(180.0, gstAdd, 0.001)
        assertEquals(1180.0, gross, 0.001)

        // Remove GST from 1180
        val originalCost = (gross * 100.0) / (100.0 + rate)
        val gstRemoved = gross - originalCost
        assertEquals(1000.0, originalCost, 0.001)
        assertEquals(180.0, gstRemoved, 0.001)
    }

    @Test
    fun testDiscountCalculation() {
        val original = 500.0
        val discount = 20.0
        val saved = (original * discount) / 100.0
        val finalPrice = original - saved

        assertEquals(100.0, saved, 0.001)
        assertEquals(400.0, finalPrice, 0.001)
    }

    @Test
    fun testProfitLoss() {
        val cp = 200.0
        val sp = 250.0
        val diff = sp - cp
        val percent = (abs(diff) / cp) * 100.0

        assertTrue(diff > 0)
        assertEquals(50.0, diff, 0.001)
        assertEquals(25.0, percent, 0.001)
    }
}
