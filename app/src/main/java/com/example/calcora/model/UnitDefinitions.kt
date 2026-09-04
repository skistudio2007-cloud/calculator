package com.example.calcora.model

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

data class UnitItem(
    val name: String,
    val symbol: String,
    val toBaseRatio: Double = 1.0 // Ratio to multiply to convert to base unit
)

data class UnitCategory(
    val id: String,
    val title: String,
    val baseUnitName: String,
    val units: List<UnitItem>
)

object UnitRegistry {

    // 1. Digital Storage (Data)
    val storageCategory = UnitCategory(
        id = "storage",
        title = "Data Storage Converter",
        baseUnitName = "Byte",
        units = listOf(
            UnitItem("Bit", "b", 0.125),
            UnitItem("Byte", "B", 1.0),
            UnitItem("Kilobyte (KB)", "KB", 1024.0),
            UnitItem("Megabyte (MB)", "MB", 1024.0 * 1024.0),
            UnitItem("Gigabyte (GB)", "GB", 1024.0 * 1024.0 * 1024.0),
            UnitItem("Terabyte (TB)", "TB", 1024.0 * 1024.0 * 1024.0 * 1024.0),
            UnitItem("Petabyte (PB)", "PB", 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0)
        )
    )

    // 2. Length
    val lengthCategory = UnitCategory(
        id = "length",
        title = "Length Converter",
        baseUnitName = "Meter",
        units = listOf(
            UnitItem("Millimeter", "mm", 0.001),
            UnitItem("Centimeter", "cm", 0.01),
            UnitItem("Meter", "m", 1.0),
            UnitItem("Kilometer", "km", 1000.0),
            UnitItem("Inch", "in", 0.0254),
            UnitItem("Foot", "ft", 0.3048),
            UnitItem("Yard", "yd", 0.9144),
            UnitItem("Mile", "mi", 1609.344),
            UnitItem("Nautical Mile", "NM", 1852.0)
        )
    )

    // 3. Weight & Mass
    val weightCategory = UnitCategory(
        id = "weight",
        title = "Mass & Weight Converter",
        baseUnitName = "Gram",
        units = listOf(
            UnitItem("Milligram", "mg", 0.001),
            UnitItem("Gram", "g", 1.0),
            UnitItem("Kilogram", "kg", 1000.0),
            UnitItem("Metric Ton", "t", 1000000.0),
            UnitItem("Ounce", "oz", 28.349523125),
            UnitItem("Pound", "lb", 453.59237),
            UnitItem("Stone", "st", 6350.29318),
            UnitItem("Carat", "ct", 0.2)
        )
    )

    // 4. Area
    val areaCategory = UnitCategory(
        id = "area",
        title = "Area Converter",
        baseUnitName = "Square Meter",
        units = listOf(
            UnitItem("Square Centimeter", "cm²", 0.0001),
            UnitItem("Square Meter", "m²", 1.0),
            UnitItem("Square Kilometer", "km²", 1_000_000.0),
            UnitItem("Square Inch", "in²", 0.00064516),
            UnitItem("Square Foot", "ft²", 0.09290304),
            UnitItem("Square Yard", "yd²", 0.83612736),
            UnitItem("Acre", "ac", 4046.8564224),
            UnitItem("Hectare", "ha", 10000.0),
            UnitItem("Square Mile", "mi²", 2589988.110336)
        )
    )

    // 5. Volume
    val volumeCategory = UnitCategory(
        id = "volume",
        title = "Volume Converter",
        baseUnitName = "Liter",
        units = listOf(
            UnitItem("Milliliter", "mL", 0.001),
            UnitItem("Liter", "L", 1.0),
            UnitItem("Cubic Meter", "m³", 1000.0),
            UnitItem("US Fluid Ounce", "fl oz", 0.0295735295625),
            UnitItem("US Cup", "cup", 0.2365882365),
            UnitItem("US Pint", "pt", 0.473176473),
            UnitItem("US Quart", "qt", 0.946352946),
            UnitItem("US Gallon", "gal", 3.785411784),
            UnitItem("Imperial Gallon", "UK gal", 4.54609)
        )
    )

    // 6. Temperature
    val temperatureCategory = UnitCategory(
        id = "temperature",
        title = "Temperature Converter",
        baseUnitName = "Celsius",
        units = listOf(
            UnitItem("Celsius", "°C"),
            UnitItem("Fahrenheit", "°F"),
            UnitItem("Kelvin", "K")
        )
    )

    // 7. Speed
    val speedCategory = UnitCategory(
        id = "speed",
        title = "Speed Converter",
        baseUnitName = "Meter per second",
        units = listOf(
            UnitItem("Meter/sec", "m/s", 1.0),
            UnitItem("Km/hour", "km/h", 1.0 / 3.6),
            UnitItem("Miles/hour", "mph", 0.44704),
            UnitItem("Knots", "kn", 0.5144444444444444),
            UnitItem("Foot/sec", "ft/s", 0.3048)
        )
    )

    // 8. Time
    val timeCategory = UnitCategory(
        id = "time",
        title = "Time Converter",
        baseUnitName = "Second",
        units = listOf(
            UnitItem("Milliseconds", "ms", 0.001),
            UnitItem("Seconds", "s", 1.0),
            UnitItem("Minutes", "min", 60.0),
            UnitItem("Hours", "h", 3600.0),
            UnitItem("Days", "d", 86400.0),
            UnitItem("Weeks", "wk", 604800.0),
            UnitItem("Months (30d)", "mo", 2592000.0),
            UnitItem("Years (365d)", "yr", 31536000.0)
        )
    )

    // 9. Pressure
    val pressureCategory = UnitCategory(
        id = "pressure",
        title = "Pressure Converter",
        baseUnitName = "Pascal",
        units = listOf(
            UnitItem("Pascal", "Pa", 1.0),
            UnitItem("Kilopascal", "kPa", 1000.0),
            UnitItem("Bar", "bar", 100000.0),
            UnitItem("PSI", "psi", 6894.757293168),
            UnitItem("Atmosphere", "atm", 101325.0),
            UnitItem("Torr / mmHg", "mmHg", 133.322368421)
        )
    )

    // 10. Force
    val forceCategory = UnitCategory(
        id = "force",
        title = "Force Converter",
        baseUnitName = "Newton",
        units = listOf(
            UnitItem("Newton", "N", 1.0),
            UnitItem("Kilonewton", "kN", 1000.0),
            UnitItem("Dyne", "dyn", 0.00001),
            UnitItem("Pound-force", "lbf", 4.4482216152605),
            UnitItem("Kilogram-force", "kgf", 9.80665)
        )
    )

    // 11. Power
    val powerCategory = UnitCategory(
        id = "power",
        title = "Power Converter",
        baseUnitName = "Watt",
        units = listOf(
            UnitItem("Watt", "W", 1.0),
            UnitItem("Kilowatt", "kW", 1000.0),
            UnitItem("Megawatt", "MW", 1000000.0),
            UnitItem("Horsepower (hp)", "hp", 745.69987158227),
            UnitItem("BTU/hour", "BTU/h", 0.29307107)
        )
    )

    // 12. Energy
    val energyCategory = UnitCategory(
        id = "energy",
        title = "Energy Converter",
        baseUnitName = "Joule",
        units = listOf(
            UnitItem("Joule", "J", 1.0),
            UnitItem("Kilojoule", "kJ", 1000.0),
            UnitItem("Calorie", "cal", 4.184),
            UnitItem("Kilocalorie", "kcal", 4184.0),
            UnitItem("Watt-hour", "Wh", 3600.0),
            UnitItem("Kilowatt-hour", "kWh", 3600000.0),
            UnitItem("BTU", "BTU", 1055.05585262)
        )
    )

    // 13. Frequency
    val frequencyCategory = UnitCategory(
        id = "frequency",
        title = "Frequency Converter",
        baseUnitName = "Hertz",
        units = listOf(
            UnitItem("Hertz", "Hz", 1.0),
            UnitItem("Kilohertz", "kHz", 1000.0),
            UnitItem("Megahertz", "MHz", 1000000.0),
            UnitItem("Gigahertz", "GHz", 1000000000.0),
            UnitItem("RPM", "rpm", 1.0 / 60.0)
        )
    )

    // 14. Angle
    val angleCategory = UnitCategory(
        id = "angle",
        title = "Angle Converter",
        baseUnitName = "Degree",
        units = listOf(
            UnitItem("Degree", "°", 1.0),
            UnitItem("Radian", "rad", 180.0 / Math.PI),
            UnitItem("Gradian", "grad", 0.9),
            UnitItem("Arcminute", "'", 1.0 / 60.0),
            UnitItem("Arcsecond", "\"", 1.0 / 3600.0)
        )
    )

    // 15. Fuel Economy
    val fuelCategory = UnitCategory(
        id = "fuel",
        title = "Fuel Economy Converter",
        baseUnitName = "km/L",
        units = listOf(
            UnitItem("Km per liter", "km/L", 1.0),
            UnitItem("Miles per gallon (US)", "mpg", 0.4251437),
            UnitItem("Miles per gallon (UK)", "mpg UK", 0.354006)
        )
    )

    fun convertStandard(value: Double, fromUnit: UnitItem, toUnit: UnitItem): Double {
        if (fromUnit == toUnit) return value
        val baseVal = value * fromUnit.toBaseRatio
        return baseVal / toUnit.toBaseRatio
    }

    fun convertTemperature(value: Double, from: String, to: String): Double {
        if (from == to) return value
        val celsius = when (from) {
            "Celsius" -> value
            "Fahrenheit" -> (value - 32.0) * (5.0 / 9.0)
            "Kelvin" -> value - 273.15
            else -> value
        }
        return when (to) {
            "Celsius" -> celsius
            "Fahrenheit" -> (celsius * (9.0 / 5.0)) + 32.0
            "Kelvin" -> celsius + 273.15
            else -> celsius
        }
    }

    fun formatAccurate(value: Double): String {
        if (value == 0.0) return "0"
        if (value.isNaN() || value.isInfinite()) return "Error"
        val absVal = abs(value)
        return when {
            absVal >= 1e12 || (absVal < 1e-6 && absVal > 0) -> {
                java.text.DecimalFormat("0.########E0").format(value)
            }
            else -> {
                try {
                    BigDecimal(value)
                        .setScale(8, RoundingMode.HALF_UP)
                        .stripTrailingZeros()
                        .toPlainString()
                } catch (e: Exception) {
                    java.text.DecimalFormat("#,##0.########").format(value)
                }
            }
        }
    }
}
