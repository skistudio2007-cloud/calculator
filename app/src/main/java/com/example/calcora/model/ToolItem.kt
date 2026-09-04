package com.example.calcora.model

enum class ToolCategory(val title: String) {
    FINANCE("FINANCE"),
    DAILY_LIFE("DAILY LIFE"),
    CONVERTERS("CONVERTERS")
}

data class ToolItem(
    val id: String,
    val name: String,
    val description: String,
    val category: ToolCategory,
    val iconName: String
)

object ToolRegistry {
    val allTools = listOf(
        // Finance
        ToolItem("emi", "EMI Calculator", "Calculate your monthly EMI and amortization", ToolCategory.FINANCE, "AccountBalance"),
        ToolItem("loan", "Loan Calculator", "Compute payments, interest, and payoff", ToolCategory.FINANCE, "Payments"),
        ToolItem("simple_interest", "Simple Interest", "Standard (P × R × T) / 100 calculation", ToolCategory.FINANCE, "TrendingUp"),
        ToolItem("compound_interest", "Compound Interest", "Calculate CI with multiple compounding cycles", ToolCategory.FINANCE, "ShowChart"),
        ToolItem("gst", "GST Calculator", "Add or remove GST with standard tax slabs", ToolCategory.FINANCE, "ReceiptLong"),
        ToolItem("discount", "Discount Calculator", "Compute final price and total money saved", ToolCategory.FINANCE, "LocalOffer"),
        ToolItem("profit_loss", "Profit & Loss", "Calculate profit/loss margins and returns", ToolCategory.FINANCE, "Percent"),

        // Daily Life
        ToolItem("age", "Age Calculator", "Exact age in years, months, and days", ToolCategory.DAILY_LIFE, "Cake"),
        ToolItem("bmi", "BMI Calculator", "Body Mass Index and health category", ToolCategory.DAILY_LIFE, "FitnessCenter"),
        ToolItem("percentage", "Percentage Calculator", "Multi-mode percentage operations", ToolCategory.DAILY_LIFE, "PieChart"),
        ToolItem("date_diff", "Date Difference", "Total span between two dates", ToolCategory.DAILY_LIFE, "CalendarMonth"),
        ToolItem("split_bill", "Split Bill", "Divide group expenses and tips fairly", ToolCategory.DAILY_LIFE, "Group"),
        ToolItem("tip", "Tip Calculator", "Quick tip calculation and bill total", ToolCategory.DAILY_LIFE, "Paid"),

        // Converters
        ToolItem("currency", "Currency Converter", "Live and offline cached global exchange rates", ToolCategory.CONVERTERS, "CurrencyExchange"),
        ToolItem("length", "Length Converter", "Meters, feet, inches, kilometers, miles", ToolCategory.CONVERTERS, "Straighten"),
        ToolItem("weight", "Weight Converter", "Kilograms, pounds, grams, ounces, tonnes", ToolCategory.CONVERTERS, "Scale"),
        ToolItem("temperature", "Temperature Converter", "Celsius, Fahrenheit, and Kelvin conversions", ToolCategory.CONVERTERS, "Thermostat"),
        ToolItem("area", "Area Converter", "Square meters, acres, hectares, sq ft", ToolCategory.CONVERTERS, "SquareFoot"),
        ToolItem("volume", "Volume Converter", "Liters, milliliters, gallons, cups, cu m", ToolCategory.CONVERTERS, "WaterDrop"),
        ToolItem("speed", "Speed Converter", "km/h, mph, m/s, and knots", ToolCategory.CONVERTERS, "Speed"),
        ToolItem("time", "Time Converter", "Seconds, minutes, hours, days, weeks", ToolCategory.CONVERTERS, "Schedule"),
        ToolItem("storage", "Digital Storage", "Bits, bytes, KB, MB, GB, and TB", ToolCategory.CONVERTERS, "Storage")
    )

    fun getToolById(id: String): ToolItem? = allTools.find { it.id == id }
}
