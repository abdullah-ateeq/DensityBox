package com.example.densitybox

import com.example.densitybox.domain.calculator.DensityCalculator
import com.example.densitybox.domain.converter.DensityConverter
import com.example.densitybox.domain.converter.MassConverter
import com.example.densitybox.domain.converter.VolumeConverter
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.VolumeUnit
import com.example.densitybox.domain.registry.DensityUnitRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class DensityCalculatorTest {

    private val mathContext = MathContext(34, RoundingMode.HALF_UP)

    // 1. Density from mass and volume
    @Test
    fun test01_densityFromMassAndVolume() {
        val result = DensityCalculator.calculateDensity(
            mass = BigDecimal("100"),
            massUnit = MassUnit.GRAM,
            volume = BigDecimal("50"),
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal("2").compareTo(result))
    }

    // 2. Mass from density and volume
    @Test
    fun test02_massFromDensityAndVolume() {
        val result = DensityCalculator.calculateMass(
            density = BigDecimal("2"),
            densityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            volume = BigDecimal("50"),
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            targetMassUnit = MassUnit.GRAM,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal("100").compareTo(result))
    }

    // 3. Volume from mass and density
    @Test
    fun test03_volumeFromMassAndDensity() {
        val result = DensityCalculator.calculateVolume(
            mass = BigDecimal("100"),
            massUnit = MassUnit.GRAM,
            density = BigDecimal("2"),
            densityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            targetVolumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal("50").compareTo(result))
    }

    // 4. g → kg
    @Test
    fun test04_gramToKilogram() {
        val result = MassConverter.convert(BigDecimal("1000"), MassUnit.GRAM, MassUnit.KILOGRAM, mathContext)
        assertEquals(0, BigDecimal("1").compareTo(result))
    }

    // 5. kg → g
    @Test
    fun test05_kilogramToGram() {
        val result = MassConverter.convert(BigDecimal("1"), MassUnit.KILOGRAM, MassUnit.GRAM, mathContext)
        assertEquals(0, BigDecimal("1000").compareTo(result))
    }

    // 6. g → lb
    @Test
    fun test06_gramToPound() {
        val result = MassConverter.convert(BigDecimal("453.59237"), MassUnit.GRAM, MassUnit.POUND, mathContext)
        assertEquals(0, BigDecimal("1").compareTo(result))
    }

    // 7. lb → g
    @Test
    fun test07_poundToGram() {
        val result = MassConverter.convert(BigDecimal("1"), MassUnit.POUND, MassUnit.GRAM, mathContext)
        assertEquals(0, BigDecimal("453.59237").compareTo(result))
    }

    // 8. oz → g
    @Test
    fun test08_ounceToGram() {
        val result = MassConverter.convert(BigDecimal("1"), MassUnit.OUNCE, MassUnit.GRAM, mathContext)
        assertEquals(0, BigDecimal("28.349523125").compareTo(result))
    }

    // 9. mL → L
    @Test
    fun test09_milliliterToLiter() {
        val result = VolumeConverter.convert(BigDecimal("1000"), VolumeUnit.MILLILITER, VolumeUnit.LITER, mathContext)
        assertEquals(0, BigDecimal("1").compareTo(result))
    }

    // 10. L → mL
    @Test
    fun test10_literToMilliliter() {
        val result = VolumeConverter.convert(BigDecimal("1"), VolumeUnit.LITER, VolumeUnit.MILLILITER, mathContext)
        assertEquals(0, BigDecimal("1000").compareTo(result))
    }

    // 11. cm³ → mL
    @Test
    fun test11_cubicCentimeterToMilliliter() {
        val result = VolumeConverter.convert(BigDecimal("1"), VolumeUnit.CUBIC_CENTIMETER, VolumeUnit.MILLILITER, mathContext)
        assertEquals(0, BigDecimal("1").compareTo(result))
    }

    // 12. m³ → L
    @Test
    fun test12_cubicMeterToLiter() {
        val result = VolumeConverter.convert(BigDecimal("1"), VolumeUnit.CUBIC_METER, VolumeUnit.LITER, mathContext)
        assertEquals(0, BigDecimal("1000").compareTo(result))
    }

    // 13. in³ → cm³
    @Test
    fun test13_cubicInchToCubicCentimeter() {
        val result = VolumeConverter.convert(BigDecimal("1"), VolumeUnit.CUBIC_INCH, VolumeUnit.CUBIC_CENTIMETER, mathContext)
        assertEquals(0, BigDecimal("16.387064").compareTo(result))
    }

    // 14. ft³ → cm³
    @Test
    fun test14_cubicFootToCubicCentimeter() {
        val result = VolumeConverter.convert(BigDecimal("1"), VolumeUnit.CUBIC_FOOT, VolumeUnit.CUBIC_CENTIMETER, mathContext)
        assertEquals(0, BigDecimal("28316.846592").compareTo(result))
    }

    // 15. US fl oz → mL
    @Test
    fun test15_usFluidOunceToMilliliter() {
        val result = VolumeConverter.convert(BigDecimal("1"), VolumeUnit.US_FLUID_OUNCE, VolumeUnit.MILLILITER, mathContext)
        assertEquals(0, BigDecimal("29.5735295625").compareTo(result))
    }

    // 16. US gal → L
    @Test
    fun test16_usGallonToLiter() {
        val result = VolumeConverter.convert(BigDecimal("1"), VolumeUnit.US_GALLON, VolumeUnit.LITER, mathContext)
        assertEquals(0, BigDecimal("3.785411784").compareTo(result))
    }

    // 17. g/cm³ → kg/m³
    @Test
    fun test17_gramPerCm3ToKgPerM3() {
        val result = DensityConverter.convert(
            BigDecimal("1"),
            DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            DensityUnit.KILOGRAM_PER_CUBIC_METER,
            mathContext
        )
        assertEquals(0, BigDecimal("1000").compareTo(result))
    }

    // 18. kg/m³ → g/cm³
    @Test
    fun test18_kgPerM3ToGramPerCm3() {
        val result = DensityConverter.convert(
            BigDecimal("1000"),
            DensityUnit.KILOGRAM_PER_CUBIC_METER,
            DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            mathContext
        )
        assertEquals(0, BigDecimal("1").compareTo(result))
    }

    // 19. g/cm³ → g/mL
    @Test
    fun test19_gramPerCm3ToGramPerMl() {
        val result = DensityConverter.convert(
            BigDecimal("1"),
            DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            DensityUnit.GRAM_PER_MILLILITER,
            mathContext
        )
        assertEquals(0, BigDecimal("1").compareTo(result))
    }

    // 20. kg/L → g/cm³
    @Test
    fun test20_kgPerLToGramPerCm3() {
        val result = DensityConverter.convert(
            BigDecimal("1"),
            DensityUnit.KILOGRAM_PER_LITER,
            DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            mathContext
        )
        assertEquals(0, BigDecimal("1").compareTo(result))
    }

    // 21. lb/ft³ → g/cm³
    @Test
    fun test21_lbPerFt3ToGramPerCm3() {
        val result = DensityConverter.convert(
            BigDecimal("1"),
            DensityUnit.POUND_PER_CUBIC_FOOT,
            DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            mathContext
        )
        val expected = BigDecimal("453.59237").divide(BigDecimal("28316.846592"), mathContext)
        assertEquals(0, expected.compareTo(result))
    }

    // 22. lb/in³ → g/cm³
    @Test
    fun test22_lbPerIn3ToGramPerCm3() {
        val result = DensityConverter.convert(
            BigDecimal("1"),
            DensityUnit.POUND_PER_CUBIC_INCH,
            DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            mathContext
        )
        val expected = BigDecimal("453.59237").divide(BigDecimal("16.387064"), mathContext)
        assertEquals(0, expected.compareTo(result))
    }

    // 23. oz/in³ → g/cm³
    @Test
    fun test23_ozPerIn3ToGramPerCm3() {
        val result = DensityConverter.convert(
            BigDecimal("1"),
            DensityUnit.OUNCE_PER_CUBIC_INCH,
            DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            mathContext
        )
        val expected = BigDecimal("28.349523125").divide(BigDecimal("16.387064"), mathContext)
        assertEquals(0, expected.compareTo(result))
    }

    // 24. Zero mass
    @Test
    fun test24_zeroMass() {
        val result = DensityCalculator.calculateDensity(
            mass = BigDecimal.ZERO,
            massUnit = MassUnit.GRAM,
            volume = BigDecimal("50"),
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal.ZERO.compareTo(result))
    }

    // 25. Zero volume
    @Test
    fun test25_zeroVolume() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            DensityCalculator.calculateDensity(
                mass = BigDecimal("100"),
                massUnit = MassUnit.GRAM,
                volume = BigDecimal.ZERO,
                volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
                mathContext = mathContext
            )
        }
        assertEquals("Volume must be greater than zero.", ex.message)
    }

    // 26. Zero density
    @Test
    fun test26_zeroDensity() {
        val exDensityInMass = assertThrows(IllegalArgumentException::class.java) {
            DensityCalculator.calculateMass(
                density = BigDecimal.ZERO,
                densityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
                volume = BigDecimal("50"),
                volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
                targetMassUnit = MassUnit.GRAM,
                mathContext = mathContext
            )
        }
        assertEquals("Density must be greater than zero.", exDensityInMass.message)

        val exDensityInVol = assertThrows(IllegalArgumentException::class.java) {
            DensityCalculator.calculateVolume(
                mass = BigDecimal("100"),
                massUnit = MassUnit.GRAM,
                density = BigDecimal.ZERO,
                densityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
                targetVolumeUnit = VolumeUnit.CUBIC_CENTIMETER,
                mathContext = mathContext
            )
        }
        assertEquals("Density must be greater than zero.", exDensityInVol.message)
    }

    // 27. Negative mass
    @Test
    fun test27_negativeMass() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            DensityCalculator.calculateDensity(
                mass = BigDecimal("-100"),
                massUnit = MassUnit.GRAM,
                volume = BigDecimal("50"),
                volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
                mathContext = mathContext
            )
        }
        assertEquals("Mass must not be negative.", ex.message)
    }

    // 28. Negative volume
    @Test
    fun test28_negativeVolume() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            DensityCalculator.calculateDensity(
                mass = BigDecimal("100"),
                massUnit = MassUnit.GRAM,
                volume = BigDecimal("-50"),
                volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
                mathContext = mathContext
            )
        }
        assertEquals("Volume must not be negative.", ex.message)
    }

    // 29. Negative density
    @Test
    fun test29_negativeDensity() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            DensityCalculator.calculateMass(
                density = BigDecimal("-2"),
                densityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
                volume = BigDecimal("50"),
                volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
                targetMassUnit = MassUnit.GRAM,
                mathContext = mathContext
            )
        }
        assertEquals("Density must not be negative.", ex.message)
    }

    // 30. Decimal values
    @Test
    fun test30_decimalValues() {
        val result = DensityCalculator.calculateDensity(
            mass = BigDecimal("2.5"),
            massUnit = MassUnit.GRAM,
            volume = BigDecimal("0.5"),
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal("5").compareTo(result))
    }

    // 31. Leading decimal
    @Test
    fun test31_leadingDecimalCalculation() {
        val result = DensityCalculator.calculateDensity(
            mass = BigDecimal("0.5"),
            massUnit = MassUnit.GRAM,
            volume = BigDecimal("0.25"),
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal("2").compareTo(result))
    }

    // 32. Trailing decimal
    @Test
    fun test32_trailingDecimalCalculation() {
        val result = DensityCalculator.calculateDensity(
            mass = BigDecimal("10"),
            massUnit = MassUnit.GRAM,
            volume = BigDecimal("2"),
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal("5").compareTo(result))
    }

    // 33. Scientific notation
    @Test
    fun test33_scientificNotation() {
        val result = DensityCalculator.calculateDensity(
            mass = BigDecimal("1E+3"),
            massUnit = MassUnit.GRAM,
            volume = BigDecimal("1E+2"),
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal("10").compareTo(result))
    }

    // 34. Very small values
    @Test
    fun test34_verySmallValues() {
        val result = DensityCalculator.calculateDensity(
            mass = BigDecimal("0.000001"),
            massUnit = MassUnit.GRAM,
            volume = BigDecimal("0.000001"),
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal("1").compareTo(result))
    }

    // 35. Very large values
    @Test
    fun test35_veryLargeValues() {
        val mass = BigDecimal("999999999999999999999999")
        val volume = BigDecimal("1")
        val result = DensityCalculator.calculateDensity(
            mass = mass,
            massUnit = MassUnit.GRAM,
            volume = volume,
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        assertEquals(0, mass.compareTo(result))
    }

    // 36. BigDecimal precision
    @Test
    fun test36_bigDecimalPrecisionWithoutFloatingArtifacts() {
        val mass = BigDecimal("0.1")
        val volume = BigDecimal("0.3")
        val result = DensityCalculator.calculateDensity(
            mass = mass,
            massUnit = MassUnit.GRAM,
            volume = volume,
            volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        val expected = BigDecimal("1").divide(BigDecimal("3"), mathContext)
        assertEquals(0, expected.compareTo(result))
    }

    // 37. Same-unit conversion
    @Test
    fun test37_sameUnitConversion() {
        val m = MassConverter.convert(BigDecimal("42"), MassUnit.GRAM, MassUnit.GRAM)
        assertEquals(0, BigDecimal("42").compareTo(m))

        val v = VolumeConverter.convert(BigDecimal("77"), VolumeUnit.LITER, VolumeUnit.LITER)
        assertEquals(0, BigDecimal("77").compareTo(v))

        val d = DensityConverter.convert(BigDecimal("3.14"), DensityUnit.GRAM_PER_CUBIC_CENTIMETER, DensityUnit.GRAM_PER_CUBIC_CENTIMETER)
        assertEquals(0, BigDecimal("3.14").compareTo(d))
    }

    // Critical Test — Cross-Unit Density
    @Test
    fun testCritical_crossUnitDensity() {
        val result = DensityCalculator.calculateDensity(
            mass = BigDecimal("1"),
            massUnit = MassUnit.KILOGRAM,
            volume = BigDecimal("1"),
            volumeUnit = VolumeUnit.LITER,
            targetDensityUnit = DensityUnit.KILOGRAM_PER_LITER,
            mathContext = mathContext
        )
        assertEquals(0, BigDecimal("1").compareTo(result))
    }

    // Critical Test — Metric Ton
    @Test
    fun test_metricTonConversion() {
        val result = MassConverter.convert(BigDecimal("1"), MassUnit.METRIC_TON, MassUnit.GRAM, mathContext)
        assertEquals(0, BigDecimal("1000000").compareTo(result))
    }

    // Critical Test — Milligram
    @Test
    fun test_milligramConversion() {
        val result = MassConverter.convert(BigDecimal("1000"), MassUnit.MILLIGRAM, MassUnit.GRAM, mathContext)
        assertEquals(0, BigDecimal("1").compareTo(result))
    }
}
