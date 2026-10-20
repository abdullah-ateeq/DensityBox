package com.example.densitybox.domain.registry

import com.example.densitybox.domain.model.VolumeUnit
import com.example.densitybox.domain.model.VolumeUnitDefinition
import java.math.BigDecimal

object VolumeUnitRegistry {
    val CUBIC_MILLIMETER = VolumeUnitDefinition(
        unit = VolumeUnit.CUBIC_MILLIMETER,
        name = "Cubic Millimeter",
        symbol = "mm³",
        toBaseFactor = BigDecimal("0.001")
    )

    val CUBIC_CENTIMETER = VolumeUnitDefinition(
        unit = VolumeUnit.CUBIC_CENTIMETER,
        name = "Cubic Centimeter",
        symbol = "cm³",
        toBaseFactor = BigDecimal.ONE
    )

    val MILLILITER = VolumeUnitDefinition(
        unit = VolumeUnit.MILLILITER,
        name = "Milliliter",
        symbol = "mL",
        toBaseFactor = BigDecimal.ONE
    )

    val LITER = VolumeUnitDefinition(
        unit = VolumeUnit.LITER,
        name = "Liter",
        symbol = "L",
        toBaseFactor = BigDecimal("1000")
    )

    val CUBIC_METER = VolumeUnitDefinition(
        unit = VolumeUnit.CUBIC_METER,
        name = "Cubic Meter",
        symbol = "m³",
        toBaseFactor = BigDecimal("1000000")
    )

    val CUBIC_INCH = VolumeUnitDefinition(
        unit = VolumeUnit.CUBIC_INCH,
        name = "Cubic Inch",
        symbol = "in³",
        toBaseFactor = BigDecimal("16.387064")
    )

    val CUBIC_FOOT = VolumeUnitDefinition(
        unit = VolumeUnit.CUBIC_FOOT,
        name = "Cubic Foot",
        symbol = "ft³",
        toBaseFactor = BigDecimal("28316.846592")
    )

    val US_FLUID_OUNCE = VolumeUnitDefinition(
        unit = VolumeUnit.US_FLUID_OUNCE,
        name = "US Fluid Ounce",
        symbol = "US fl oz",
        toBaseFactor = BigDecimal("29.5735295625")
    )

    val US_GALLON = VolumeUnitDefinition(
        unit = VolumeUnit.US_GALLON,
        name = "US Gallon",
        symbol = "US gal",
        toBaseFactor = BigDecimal("3785.411784")
    )

    val all: List<VolumeUnitDefinition> = listOf(
        CUBIC_MILLIMETER,
        CUBIC_CENTIMETER,
        MILLILITER,
        LITER,
        CUBIC_METER,
        CUBIC_INCH,
        CUBIC_FOOT,
        US_FLUID_OUNCE,
        US_GALLON
    )

    private val map: Map<VolumeUnit, VolumeUnitDefinition> = all.associateBy { it.unit }

    fun get(unit: VolumeUnit): VolumeUnitDefinition =
        map[unit] ?: throw IllegalArgumentException("Unknown volume unit: $unit")

    fun search(query: String): List<VolumeUnitDefinition> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return all
        return all.filter {
            it.name.lowercase().contains(trimmed) ||
                it.symbol.lowercase().contains(trimmed) ||
                it.unit.name.lowercase().contains(trimmed)
        }
    }
}
