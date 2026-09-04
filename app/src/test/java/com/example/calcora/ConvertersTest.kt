package com.example.calcora

import com.example.calcora.model.UnitRegistry
import org.junit.Assert.*
import org.junit.Test

class ConvertersTest {

    @Test
    fun testLengthConversion() {
        val meter = UnitRegistry.lengthCategory.units.first { it.symbol == "m" }
        val km = UnitRegistry.lengthCategory.units.first { it.symbol == "km" }
        val cm = UnitRegistry.lengthCategory.units.first { it.symbol == "cm" }

        // 1000 meters = 1 km
        val resKm = UnitRegistry.convertStandard(1000.0, meter, km)
        assertEquals(1.0, resKm, 0.0001)

        // 1 meter = 100 cm
        val resCm = UnitRegistry.convertStandard(1.0, meter, cm)
        assertEquals(100.0, resCm, 0.0001)
    }

    @Test
    fun testWeightConversion() {
        val kg = UnitRegistry.weightCategory.units.first { it.symbol == "kg" }
        val g = UnitRegistry.weightCategory.units.first { it.symbol == "g" }

        // 1 kg = 1000 grams
        val res = UnitRegistry.convertStandard(1.0, kg, g)
        assertEquals(1000.0, res, 0.0001)
    }

    @Test
    fun testTemperatureConversion() {
        // 0 C = 32 F
        val fFromC = UnitRegistry.convertTemperature(0.0, "Celsius", "Fahrenheit")
        assertEquals(32.0, fFromC, 0.0001)

        // 100 C = 212 F
        val boilingF = UnitRegistry.convertTemperature(100.0, "Celsius", "Fahrenheit")
        assertEquals(212.0, boilingF, 0.0001)

        // 0 C = 273.15 K
        val kFromC = UnitRegistry.convertTemperature(0.0, "Celsius", "Kelvin")
        assertEquals(273.15, kFromC, 0.0001)
    }

    @Test
    fun testStorageConversion() {
        val byte = UnitRegistry.storageCategory.units.first { it.symbol == "B" }
        val kb = UnitRegistry.storageCategory.units.first { it.symbol == "KB" }
        val mb = UnitRegistry.storageCategory.units.first { it.symbol == "MB" }

        // 1024 B = 1 KB
        val resKb = UnitRegistry.convertStandard(1024.0, byte, kb)
        assertEquals(1.0, resKb, 0.0001)

        // 1 MB = 1024 KB
        val resMb = UnitRegistry.convertStandard(1.0, mb, kb)
        assertEquals(1024.0, resMb, 0.0001)
    }

    @Test
    fun testSpeedConversion() {
        val kmh = UnitRegistry.speedCategory.units.first { it.symbol == "km/h" }
        val ms = UnitRegistry.speedCategory.units.first { it.symbol == "m/s" }

        // 36 km/h = 10 m/s
        val res = UnitRegistry.convertStandard(36.0, kmh, ms)
        assertEquals(10.0, res, 0.001)
    }
}
