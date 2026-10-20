package com.example.densitybox

import com.example.densitybox.domain.parser.DensityBoxParser
import com.example.densitybox.domain.validator.DensityBoxValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class DensityBoxParserAndValidatorTest {

    // 30. Decimal values
    @Test
    fun test30_parseStandardDecimal() {
        val result = DensityBoxParser.parse("100.5")
        assertTrue(result.isSuccess)
        assertEquals(0, BigDecimal("100.5").compareTo(result.getOrThrow()))
    }

    // 31. Leading decimal
    @Test
    fun test31_parseLeadingDecimal() {
        val result = DensityBoxParser.parse(".5")
        assertTrue(result.isSuccess)
        assertEquals(0, BigDecimal("0.5").compareTo(result.getOrThrow()))
    }

    // 32. Trailing decimal
    @Test
    fun test32_parseTrailingDecimal() {
        val result = DensityBoxParser.parse("10.")
        assertTrue(result.isSuccess)
        assertEquals(0, BigDecimal("10").compareTo(result.getOrThrow()))
    }

    // 33. Scientific notation
    @Test
    fun test33_parseScientificNotation() {
        val r1 = DensityBoxParser.parse("1E+5")
        assertTrue(r1.isSuccess)
        assertEquals(0, BigDecimal("100000").compareTo(r1.getOrThrow()))

        val r2 = DensityBoxParser.parse("1.5E-2")
        assertTrue(r2.isSuccess)
        assertEquals(0, BigDecimal("0.015").compareTo(r2.getOrThrow()))
    }

    // 38. Invalid input
    @Test
    fun test38_parseInvalidInput() {
        val result = DensityBoxParser.parse("abc")
        assertTrue(result.isFailure)
        assertEquals("Enter a valid number.", result.exceptionOrNull()?.message)
    }

    // 39. Empty input
    @Test
    fun test39_parseEmptyInput() {
        val r1 = DensityBoxParser.parse("")
        assertTrue(r1.isFailure)
        assertEquals("Enter a valid number.", r1.exceptionOrNull()?.message)

        val r2 = DensityBoxParser.parse("   ")
        assertTrue(r2.isFailure)
        assertEquals("Enter a valid number.", r2.exceptionOrNull()?.message)
    }

    // 40. Multiple decimal points
    @Test
    fun test40_parseMultipleDecimalPoints() {
        val r1 = DensityBoxParser.parse("12..5")
        assertTrue(r1.isFailure)
        assertEquals("Enter a valid number.", r1.exceptionOrNull()?.message)

        val r2 = DensityBoxParser.parse("1.2.3")
        assertTrue(r2.isFailure)
        assertEquals("Enter a valid number.", r2.exceptionOrNull()?.message)
    }

    // 41. Invalid signs
    @Test
    fun test41_parseInvalidSigns() {
        val r1 = DensityBoxParser.parse("--")
        assertTrue(r1.isFailure)
        assertEquals("Enter a valid number.", r1.exceptionOrNull()?.message)

        val r2 = DensityBoxParser.parse("-")
        assertTrue(r2.isFailure)
        assertEquals("Enter a valid number.", r2.exceptionOrNull()?.message)

        val r3 = DensityBoxParser.parse("+")
        assertTrue(r3.isFailure)
        assertEquals("Enter a valid number.", r3.exceptionOrNull()?.message)
    }

    // 42. Maximum input length
    @Test
    fun test42_maxInputLengthExceeded() {
        val long501 = "9".repeat(501)
        val result = DensityBoxParser.parse(long501)
        assertTrue(result.isFailure)
        assertEquals("Number is too long. Maximum 500 digits.", result.exceptionOrNull()?.message)

        val valid500 = "9".repeat(500)
        val validResult = DensityBoxParser.parse(valid500)
        assertTrue(validResult.isSuccess)
    }

    // Validator Tests
    @Test
    fun test_validatorMass() {
        assertNull(DensityBoxValidator.validateMass(BigDecimal.ZERO))
        assertNull(DensityBoxValidator.validateMass(BigDecimal("100")))
        assertEquals("Mass must not be negative.", DensityBoxValidator.validateMass(BigDecimal("-0.1")))
    }

    @Test
    fun test_validatorVolume() {
        assertNull(DensityBoxValidator.validateVolume(BigDecimal("50")))
        assertEquals("Volume must be greater than zero.", DensityBoxValidator.validateVolume(BigDecimal.ZERO))
        assertEquals("Volume must not be negative.", DensityBoxValidator.validateVolume(BigDecimal("-1")))
    }

    @Test
    fun test_validatorDensity() {
        assertNull(DensityBoxValidator.validateDensity(BigDecimal("2")))
        assertEquals("Density must be greater than zero.", DensityBoxValidator.validateDensity(BigDecimal.ZERO))
        assertEquals("Density must not be negative.", DensityBoxValidator.validateDensity(BigDecimal("-2")))
    }
}
