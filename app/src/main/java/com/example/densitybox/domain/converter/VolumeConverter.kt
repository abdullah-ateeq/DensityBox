package com.example.densitybox.domain.converter

import com.example.densitybox.domain.model.VolumeUnit
import com.example.densitybox.domain.registry.VolumeUnitRegistry
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object VolumeConverter {
    val DEFAULT_MATH_CONTEXT = MathContext(34, RoundingMode.HALF_UP)

    fun convert(
        value: BigDecimal,
        from: VolumeUnit,
        to: VolumeUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        if (from == to) return value
        val fromFactor = VolumeUnitRegistry.get(from).toBaseFactor
        val toFactor = VolumeUnitRegistry.get(to).toBaseFactor
        val inBaseCm3 = value.multiply(fromFactor, mathContext)
        return inBaseCm3.divide(toFactor, mathContext)
    }
}
