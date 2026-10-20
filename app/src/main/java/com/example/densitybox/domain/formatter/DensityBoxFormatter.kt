package com.example.densitybox.domain.formatter

import com.example.densitybox.domain.model.DensityBoxResult
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.VolumeUnit
import com.example.densitybox.domain.registry.DensityUnitRegistry
import com.example.densitybox.domain.registry.MassUnitRegistry
import com.example.densitybox.domain.registry.VolumeUnitRegistry
import java.math.BigDecimal
import java.math.RoundingMode

object DensityBoxFormatter {

    fun formatNumber(
        value: BigDecimal,
        decimalPlaces: Int = 6,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        val scaled = value.setScale(decimalPlaces, roundingMode)
        val plain = scaled.toPlainString()
        val result = if (plain.contains('.')) {
            val trimmed = plain.trimEnd('0').trimEnd('.')
            if (trimmed.isEmpty() || trimmed == "-") "0" else trimmed
        } else {
            plain
        }
        // Normalize "-0" to "0"
        return if (result == "-0") "0" else result
    }

    fun formatMass(
        value: BigDecimal,
        unit: MassUnit,
        decimalPlaces: Int = 6,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        val symbol = MassUnitRegistry.get(unit).symbol
        return "${formatNumber(value, decimalPlaces, roundingMode)} $symbol"
    }

    fun formatVolume(
        value: BigDecimal,
        unit: VolumeUnit,
        decimalPlaces: Int = 6,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        val symbol = VolumeUnitRegistry.get(unit).symbol
        return "${formatNumber(value, decimalPlaces, roundingMode)} $symbol"
    }

    fun formatDensity(
        value: BigDecimal,
        unit: DensityUnit,
        decimalPlaces: Int = 6,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        val symbol = DensityUnitRegistry.get(unit).symbol
        return "${formatNumber(value, decimalPlaces, roundingMode)} $symbol"
    }

    fun formatCalculation(
        result: DensityBoxResult,
        decimalPlaces: Int = 6,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return when (result) {
            is DensityBoxResult.DensityResult -> {
                val m = formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)
                val v = formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)
                val d = formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)
                "$m ÷ $v = $d"
            }
            is DensityBoxResult.MassResult -> {
                val d = formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)
                val v = formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)
                val m = formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)
                "$d × $v = $m"
            }
            is DensityBoxResult.VolumeResult -> {
                val m = formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)
                val d = formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)
                val v = formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)
                "$m ÷ $d = $v"
            }
        }
    }

    fun formatCopyText(
        result: DensityBoxResult,
        decimalPlaces: Int = 6,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return buildString {
            appendLine("DensityBox")
            appendLine()
            when (result) {
                is DensityBoxResult.DensityResult -> {
                    appendLine("Mass: ${formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)}")
                    appendLine("Volume: ${formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)}")
                    append("Density: ${formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)}")
                }
                is DensityBoxResult.MassResult -> {
                    appendLine("Density: ${formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)}")
                    appendLine("Volume: ${formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)}")
                    append("Mass: ${formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)}")
                }
                is DensityBoxResult.VolumeResult -> {
                    appendLine("Mass: ${formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)}")
                    appendLine("Density: ${formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)}")
                    append("Volume: ${formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)}")
                }
            }
        }
    }

    fun formatShareText(
        result: DensityBoxResult,
        decimalPlaces: Int = 6,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String = formatCopyText(result, decimalPlaces, roundingMode)
}
