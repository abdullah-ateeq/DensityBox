package com.example.densitybox.domain.calculator

import com.example.densitybox.domain.converter.DensityConverter
import com.example.densitybox.domain.model.DensityBoxResult
import com.example.densitybox.domain.model.DensityCalculationDetails
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.VolumeUnit
import com.example.densitybox.domain.registry.DensityUnitRegistry
import com.example.densitybox.domain.registry.MassUnitRegistry
import com.example.densitybox.domain.registry.VolumeUnitRegistry
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object DensityCalculator {
    val DEFAULT_MATH_CONTEXT = MathContext(34, RoundingMode.HALF_UP)

    fun calculateDensity(
        mass: BigDecimal,
        massUnit: MassUnit,
        volume: BigDecimal,
        volumeUnit: VolumeUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        return calculateDensity(
            mass = mass,
            massUnit = massUnit,
            volume = volume,
            volumeUnit = volumeUnit,
            targetDensityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            mathContext = mathContext
        )
    }

    fun calculateDensity(
        mass: BigDecimal,
        massUnit: MassUnit,
        volume: BigDecimal,
        volumeUnit: VolumeUnit,
        targetDensityUnit: DensityUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        if (mass < BigDecimal.ZERO) {
            throw IllegalArgumentException("Mass must not be negative.")
        }
        if (volume < BigDecimal.ZERO) {
            throw IllegalArgumentException("Volume must not be negative.")
        }
        if (volume.compareTo(BigDecimal.ZERO) == 0) {
            throw IllegalArgumentException("Volume must be greater than zero.")
        }

        val massInGrams = mass.multiply(MassUnitRegistry.get(massUnit).toBaseFactor, mathContext)
        val volumeInCm3 = volume.multiply(VolumeUnitRegistry.get(volumeUnit).toBaseFactor, mathContext)
        val baseDensity = massInGrams.divide(volumeInCm3, mathContext)

        return DensityConverter.convert(
            value = baseDensity,
            from = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            to = targetDensityUnit,
            mathContext = mathContext
        )
    }

    fun calculateMass(
        density: BigDecimal,
        densityUnit: DensityUnit,
        volume: BigDecimal,
        volumeUnit: VolumeUnit,
        targetMassUnit: MassUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        if (density < BigDecimal.ZERO) {
            throw IllegalArgumentException("Density must not be negative.")
        }
        if (density.compareTo(BigDecimal.ZERO) == 0) {
            throw IllegalArgumentException("Density must be greater than zero.")
        }
        if (volume < BigDecimal.ZERO) {
            throw IllegalArgumentException("Volume must not be negative.")
        }

        val densityInBase = DensityConverter.convert(
            value = density,
            from = densityUnit,
            to = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        val volumeInCm3 = volume.multiply(VolumeUnitRegistry.get(volumeUnit).toBaseFactor, mathContext)
        val massInGrams = densityInBase.multiply(volumeInCm3, mathContext)

        return massInGrams.divide(MassUnitRegistry.get(targetMassUnit).toBaseFactor, mathContext)
    }

    fun calculateVolume(
        mass: BigDecimal,
        massUnit: MassUnit,
        density: BigDecimal,
        densityUnit: DensityUnit,
        targetVolumeUnit: VolumeUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        if (mass < BigDecimal.ZERO) {
            throw IllegalArgumentException("Mass must not be negative.")
        }
        if (density < BigDecimal.ZERO) {
            throw IllegalArgumentException("Density must not be negative.")
        }
        if (density.compareTo(BigDecimal.ZERO) == 0) {
            throw IllegalArgumentException("Density must be greater than zero.")
        }

        val massInGrams = mass.multiply(MassUnitRegistry.get(massUnit).toBaseFactor, mathContext)
        val densityInBase = DensityConverter.convert(
            value = density,
            from = densityUnit,
            to = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
            mathContext = mathContext
        )
        val volumeInCm3 = massInGrams.divide(densityInBase, mathContext)

        return volumeInCm3.divide(VolumeUnitRegistry.get(targetVolumeUnit).toBaseFactor, mathContext)
    }

    fun solveDensity(
        mass: BigDecimal,
        massUnit: MassUnit,
        volume: BigDecimal,
        volumeUnit: VolumeUnit,
        targetDensityUnit: DensityUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): DensityBoxResult.DensityResult {
        val density = calculateDensity(mass, massUnit, volume, volumeUnit, targetDensityUnit, mathContext)
        val massDef = MassUnitRegistry.get(massUnit)
        val volumeDef = VolumeUnitRegistry.get(volumeUnit)
        val densityDef = DensityUnitRegistry.get(targetDensityUnit)

        val formula = "Density = Mass ÷ Volume"
        val calculation = "${mass.toPlainString()} ${massDef.symbol} ÷ ${volume.toPlainString()} ${volumeDef.symbol} = ${density.toPlainString()} ${densityDef.symbol}"

        return DensityBoxResult.DensityResult(
            mass = mass,
            volume = volume,
            density = density,
            massUnit = massUnit,
            volumeUnit = volumeUnit,
            densityUnit = targetDensityUnit,
            details = DensityCalculationDetails(formula, calculation)
        )
    }

    fun solveMass(
        density: BigDecimal,
        densityUnit: DensityUnit,
        volume: BigDecimal,
        volumeUnit: VolumeUnit,
        targetMassUnit: MassUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): DensityBoxResult.MassResult {
        val mass = calculateMass(density, densityUnit, volume, volumeUnit, targetMassUnit, mathContext)
        val densityDef = DensityUnitRegistry.get(densityUnit)
        val volumeDef = VolumeUnitRegistry.get(volumeUnit)
        val massDef = MassUnitRegistry.get(targetMassUnit)

        val formula = "Mass = Density × Volume"
        val calculation = "${density.toPlainString()} ${densityDef.symbol} × ${volume.toPlainString()} ${volumeDef.symbol} = ${mass.toPlainString()} ${massDef.symbol}"

        return DensityBoxResult.MassResult(
            density = density,
            volume = volume,
            mass = mass,
            densityUnit = densityUnit,
            volumeUnit = volumeUnit,
            massUnit = targetMassUnit,
            details = DensityCalculationDetails(formula, calculation)
        )
    }

    fun solveVolume(
        mass: BigDecimal,
        massUnit: MassUnit,
        density: BigDecimal,
        densityUnit: DensityUnit,
        targetVolumeUnit: VolumeUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): DensityBoxResult.VolumeResult {
        val volume = calculateVolume(mass, massUnit, density, densityUnit, targetVolumeUnit, mathContext)
        val massDef = MassUnitRegistry.get(massUnit)
        val densityDef = DensityUnitRegistry.get(densityUnit)
        val volumeDef = VolumeUnitRegistry.get(targetVolumeUnit)

        val formula = "Volume = Mass ÷ Density"
        val calculation = "${mass.toPlainString()} ${massDef.symbol} ÷ ${density.toPlainString()} ${densityDef.symbol} = ${volume.toPlainString()} ${volumeDef.symbol}"

        return DensityBoxResult.VolumeResult(
            mass = mass,
            density = density,
            volume = volume,
            massUnit = massUnit,
            densityUnit = densityUnit,
            volumeUnit = targetVolumeUnit,
            details = DensityCalculationDetails(formula, calculation)
        )
    }
}
