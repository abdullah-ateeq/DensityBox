package com.example.densitybox.domain.registry

import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.DensityUnitDefinition
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.VolumeUnit

object DensityUnitRegistry {
    val GRAM_PER_CUBIC_CENTIMETER = DensityUnitDefinition(
        unit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
        name = "Gram per cubic centimeter",
        symbol = "g/cm³",
        massUnit = MassUnit.GRAM,
        volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
        massFactor = MassUnitRegistry.GRAM.toBaseFactor,
        volumeFactor = VolumeUnitRegistry.CUBIC_CENTIMETER.toBaseFactor
    )

    val KILOGRAM_PER_CUBIC_METER = DensityUnitDefinition(
        unit = DensityUnit.KILOGRAM_PER_CUBIC_METER,
        name = "Kilogram per cubic meter",
        symbol = "kg/m³",
        massUnit = MassUnit.KILOGRAM,
        volumeUnit = VolumeUnit.CUBIC_METER,
        massFactor = MassUnitRegistry.KILOGRAM.toBaseFactor,
        volumeFactor = VolumeUnitRegistry.CUBIC_METER.toBaseFactor
    )

    val GRAM_PER_MILLILITER = DensityUnitDefinition(
        unit = DensityUnit.GRAM_PER_MILLILITER,
        name = "Gram per milliliter",
        symbol = "g/mL",
        massUnit = MassUnit.GRAM,
        volumeUnit = VolumeUnit.MILLILITER,
        massFactor = MassUnitRegistry.GRAM.toBaseFactor,
        volumeFactor = VolumeUnitRegistry.MILLILITER.toBaseFactor
    )

    val KILOGRAM_PER_LITER = DensityUnitDefinition(
        unit = DensityUnit.KILOGRAM_PER_LITER,
        name = "Kilogram per liter",
        symbol = "kg/L",
        massUnit = MassUnit.KILOGRAM,
        volumeUnit = VolumeUnit.LITER,
        massFactor = MassUnitRegistry.KILOGRAM.toBaseFactor,
        volumeFactor = VolumeUnitRegistry.LITER.toBaseFactor
    )

    val POUND_PER_CUBIC_FOOT = DensityUnitDefinition(
        unit = DensityUnit.POUND_PER_CUBIC_FOOT,
        name = "Pound per cubic foot",
        symbol = "lb/ft³",
        massUnit = MassUnit.POUND,
        volumeUnit = VolumeUnit.CUBIC_FOOT,
        massFactor = MassUnitRegistry.POUND.toBaseFactor,
        volumeFactor = VolumeUnitRegistry.CUBIC_FOOT.toBaseFactor
    )

    val POUND_PER_CUBIC_INCH = DensityUnitDefinition(
        unit = DensityUnit.POUND_PER_CUBIC_INCH,
        name = "Pound per cubic inch",
        symbol = "lb/in³",
        massUnit = MassUnit.POUND,
        volumeUnit = VolumeUnit.CUBIC_INCH,
        massFactor = MassUnitRegistry.POUND.toBaseFactor,
        volumeFactor = VolumeUnitRegistry.CUBIC_INCH.toBaseFactor
    )

    val OUNCE_PER_CUBIC_INCH = DensityUnitDefinition(
        unit = DensityUnit.OUNCE_PER_CUBIC_INCH,
        name = "Ounce per cubic inch",
        symbol = "oz/in³",
        massUnit = MassUnit.OUNCE,
        volumeUnit = VolumeUnit.CUBIC_INCH,
        massFactor = MassUnitRegistry.OUNCE.toBaseFactor,
        volumeFactor = VolumeUnitRegistry.CUBIC_INCH.toBaseFactor
    )

    val all: List<DensityUnitDefinition> = listOf(
        GRAM_PER_CUBIC_CENTIMETER,
        KILOGRAM_PER_CUBIC_METER,
        GRAM_PER_MILLILITER,
        KILOGRAM_PER_LITER,
        POUND_PER_CUBIC_FOOT,
        POUND_PER_CUBIC_INCH,
        OUNCE_PER_CUBIC_INCH
    )

    private val map: Map<DensityUnit, DensityUnitDefinition> = all.associateBy { it.unit }

    fun get(unit: DensityUnit): DensityUnitDefinition =
        map[unit] ?: throw IllegalArgumentException("Unknown density unit: $unit")

    fun search(query: String): List<DensityUnitDefinition> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return all
        return all.filter {
            it.name.lowercase().contains(trimmed) ||
                it.symbol.lowercase().contains(trimmed) ||
                it.unit.name.lowercase().contains(trimmed)
        }
    }
}
