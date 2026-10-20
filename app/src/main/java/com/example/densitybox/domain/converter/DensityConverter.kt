package com.example.densitybox.domain.converter

import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.registry.DensityUnitRegistry
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object DensityConverter {
    val DEFAULT_MATH_CONTEXT = MathContext(34, RoundingMode.HALF_UP)

    fun convert(
        value: BigDecimal,
        from: DensityUnit,
        to: DensityUnit,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        if (from == to) return value
        val fromDef = DensityUnitRegistry.get(from)
        val toDef = DensityUnitRegistry.get(to)

        // Normalize to base g/cm³: value * fromMassFactor / fromVolumeFactor
        val inBase = value.multiply(fromDef.massFactor, mathContext)
            .divide(fromDef.volumeFactor, mathContext)

        // Convert from base g/cm³ to target: inBase * toVolumeFactor / toMassFactor
        return inBase.multiply(toDef.volumeFactor, mathContext)
            .divide(toDef.massFactor, mathContext)
    }
}
