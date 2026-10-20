package com.example.densitybox.domain.registry

import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.MassUnitDefinition
import java.math.BigDecimal

object MassUnitRegistry {
    val MILLIGRAM = MassUnitDefinition(
        unit = MassUnit.MILLIGRAM,
        name = "Milligram",
        symbol = "mg",
        toBaseFactor = BigDecimal("0.001")
    )

    val GRAM = MassUnitDefinition(
        unit = MassUnit.GRAM,
        name = "Gram",
        symbol = "g",
        toBaseFactor = BigDecimal.ONE
    )

    val KILOGRAM = MassUnitDefinition(
        unit = MassUnit.KILOGRAM,
        name = "Kilogram",
        symbol = "kg",
        toBaseFactor = BigDecimal("1000")
    )

    val METRIC_TON = MassUnitDefinition(
        unit = MassUnit.METRIC_TON,
        name = "Metric Ton",
        symbol = "t",
        toBaseFactor = BigDecimal("1000000")
    )

    val OUNCE = MassUnitDefinition(
        unit = MassUnit.OUNCE,
        name = "Ounce",
        symbol = "oz",
        toBaseFactor = BigDecimal("28.349523125")
    )

    val POUND = MassUnitDefinition(
        unit = MassUnit.POUND,
        name = "Pound",
        symbol = "lb",
        toBaseFactor = BigDecimal("453.59237")
    )

    val all: List<MassUnitDefinition> = listOf(
        MILLIGRAM,
        GRAM,
        KILOGRAM,
        METRIC_TON,
        OUNCE,
        POUND
    )

    private val map: Map<MassUnit, MassUnitDefinition> = all.associateBy { it.unit }

    fun get(unit: MassUnit): MassUnitDefinition =
        map[unit] ?: throw IllegalArgumentException("Unknown mass unit: $unit")

    fun search(query: String): List<MassUnitDefinition> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return all
        return all.filter {
            it.name.lowercase().contains(trimmed) ||
                it.symbol.lowercase().contains(trimmed) ||
                it.unit.name.lowercase().contains(trimmed)
        }
    }
}
