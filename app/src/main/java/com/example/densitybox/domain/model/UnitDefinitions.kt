package com.example.densitybox.domain.model

import java.math.BigDecimal

data class MassUnitDefinition(
    val unit: MassUnit,
    val name: String,
    val symbol: String,
    val toBaseFactor: BigDecimal
)

data class VolumeUnitDefinition(
    val unit: VolumeUnit,
    val name: String,
    val symbol: String,
    val toBaseFactor: BigDecimal
)

data class DensityUnitDefinition(
    val unit: DensityUnit,
    val name: String,
    val symbol: String,
    val massUnit: MassUnit,
    val volumeUnit: VolumeUnit,
    val massFactor: BigDecimal,
    val volumeFactor: BigDecimal
) {
    /**
     * Exact dimensional toBaseFactor (g/cm³) = massFactor / volumeFactor
     */
    val toBaseFactor: BigDecimal
        get() = massFactor.divide(volumeFactor, java.math.MathContext.DECIMAL128)
}
