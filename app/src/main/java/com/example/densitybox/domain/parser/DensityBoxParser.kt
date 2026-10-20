package com.example.densitybox.domain.parser

import java.math.BigDecimal

object DensityBoxParser {
    const val MAX_INPUT_LENGTH = 500

    fun parse(input: String): Result<BigDecimal> {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("Enter a valid number."))
        }
        if (trimmed.length > MAX_INPUT_LENGTH) {
            return Result.failure(IllegalArgumentException("Number is too long. Maximum 500 digits."))
        }

        // Normalize leading decimal points like ".5", "+.5", "-.5"
        val normalized = when {
            trimmed.startsWith(".") -> "0$trimmed"
            trimmed.startsWith("+.") -> "+0${trimmed.substring(2)}"
            trimmed.startsWith("-.") -> "-0${trimmed.substring(2)}"
            else -> trimmed
        }

        // Normalize trailing decimal points like "10."
        val cleaned = if (normalized.endsWith(".") && !normalized.contains('e', ignoreCase = true)) {
            normalized.dropLast(1)
        } else {
            normalized
        }

        if (cleaned.isEmpty() || cleaned == "+" || cleaned == "-") {
            return Result.failure(IllegalArgumentException("Enter a valid number."))
        }

        return try {
            val value = BigDecimal(cleaned)
            Result.success(value)
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("Enter a valid number."))
        }
    }

    fun parseMass(input: String): Result<BigDecimal> = parse(input)
    fun parseVolume(input: String): Result<BigDecimal> = parse(input)
    fun parseDensity(input: String): Result<BigDecimal> = parse(input)
}
