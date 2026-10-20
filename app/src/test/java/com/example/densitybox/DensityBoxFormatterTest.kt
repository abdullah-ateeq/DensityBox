package com.example.densitybox

import com.example.densitybox.domain.calculator.DensityCalculator
import com.example.densitybox.domain.formatter.DensityBoxFormatter
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.VolumeUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class DensityBoxFormatterTest {

    // 43. HALF_UP
    @Test
    fun test43_halfUp() {
        val value = BigDecimal("2.345")
        val formatted = DensityBoxFormatter.formatNumber(value, decimalPlaces = 2, roundingMode = RoundingMode.HALF_UP)
        assertEquals("2.35", formatted)
    }

    // 44. HALF_DOWN
    @Test
    fun test44_halfDown() {
        val value = BigDecimal("2.345")
        val formatted = DensityBoxFormatter.formatNumber(value, decimalPlaces = 2, roundingMode = RoundingMode.HALF_DOWN)
        assertEquals("2.34", formatted)
    }

    // 45. HALF_EVEN
    @Test
    fun test45_halfEven() {
        val v1 = BigDecimal("2.345")
        val f1 = DensityBoxFormatter.formatNumber(v1, decimalPlaces = 2, roundingMode = RoundingMode.HALF_EVEN)
        assertEquals("2.34", f1) // 4 is even

        val v2 = BigDecimal("2.355")
        val f2 = DensityBoxFormatter.formatNumber(v2, decimalPlaces = 2, roundingMode = RoundingMode.HALF_EVEN)
        assertEquals("2.36", f2) // 6 is even
    }

    // 46. UP
    @Test
    fun test46_up() {
        val value = BigDecimal("2.341")
        val formatted = DensityBoxFormatter.formatNumber(value, decimalPlaces = 2, roundingMode = RoundingMode.UP)
        assertEquals("2.35", formatted)
    }

    // 47. DOWN
    @Test
    fun test47_down() {
        val value = BigDecimal("2.349")
        val formatted = DensityBoxFormatter.formatNumber(value, decimalPlaces = 2, roundingMode = RoundingMode.DOWN)
        assertEquals("2.34", formatted)
    }

    // 48. CEILING
    @Test
    fun test48_ceiling() {
        val pos = BigDecimal("2.341")
        assertEquals("2.35", DensityBoxFormatter.formatNumber(pos, decimalPlaces = 2, roundingMode = RoundingMode.CEILING))

        val neg = BigDecimal("-2.349")
        assertEquals("-2.34", DensityBoxFormatter.formatNumber(neg, decimalPlaces = 2, roundingMode = RoundingMode.CEILING))
    }

    // 49. FLOOR
    @Test
    fun test49_floor() {
        val pos = BigDecimal("2.349")
        assertEquals("2.34", DensityBoxFormatter.formatNumber(pos, decimalPlaces = 2, roundingMode = RoundingMode.FLOOR))

        val neg = BigDecimal("-2.341")
        assertEquals("-2.35", DensityBoxFormatter.formatNumber(neg, decimalPlaces = 2, roundingMode = RoundingMode.FLOOR))
    }

    // 50. Trailing-zero formatting
    @Test
    fun test50_trailingZeroFormatting() {
        assertEquals("2", DensityBoxFormatter.formatNumber(BigDecimal("2.000000"), decimalPlaces = 6))
        assertEquals("2.5", DensityBoxFormatter.formatNumber(BigDecimal("2.500000"), decimalPlaces = 6))
        assertEquals("1.234567", DensityBoxFormatter.formatNumber(BigDecimal("1.234567"), decimalPlaces = 6))
        assertEquals("0", DensityBoxFormatter.formatNumber(BigDecimal("0.000000"), decimalPlaces = 6))
    }

    @Test
    fun test_formatUnitFunctions() {
        assertEquals("100 g", DensityBoxFormatter.formatMass(BigDecimal("100"), MassUnit.GRAM))
        assertEquals("50 cm³", DensityBoxFormatter.formatVolume(BigDecimal("50"), VolumeUnit.CUBIC_CENTIMETER))
        assertEquals("2 g/cm³", DensityBoxFormatter.formatDensity(BigDecimal("2"), DensityUnit.GRAM_PER_CUBIC_CENTIMETER))
    }

    @Test
    fun test_formatCalculationAndCopyText() {
        val result = DensityCalculator.solveDensity(
            mass = BigDecimal("100"),
            massUnit = MassUnit.GRAM,
            volume = BigDecimal("50"),
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            targetDensityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER
        )

        val calc = DensityBoxFormatter.formatCalculation(result)
        assertEquals("100 g ÷ 50 cm³ = 2 g/cm³", calc)

        val copy = DensityBoxFormatter.formatCopyText(result)
        assertTrue(copy.contains("DensityBox"))
        assertTrue(copy.contains("Mass: 100 g"))
        assertTrue(copy.contains("Volume: 50 cm³"))
        assertTrue(copy.contains("Density: 2 g/cm³"))

        val share = DensityBoxFormatter.formatShareText(result)
        assertEquals(copy, share)
    }
}
