package com.example.densitybox.domain.converter

import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.registry.MassUnitRegistry
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object MassConverter {
    val DEFAULT_MATH_CONTEXT = MathContext(34, RoundingMode.HALF_UP)

    fun convert(
        value: BigDecimal,
        from: MassUnit,
        to: MassUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        if (from == to) return value
        val fromFactor = MassUnitRegistry.get(from).toBaseFactor
        val toFactor = MassUnitRegistry.get(to).toBaseFactor
        val inBaseGrams = value.multiply(fromFactor, mathContext)
        return inBaseGrams.divide(toFactor, mathContext)
    }
}
