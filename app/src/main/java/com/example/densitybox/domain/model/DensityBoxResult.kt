package com.example.densitybox.domain.model

import java.math.BigDecimal

sealed interface DensityBoxResult {
    val details: DensityCalculationDetails

    data class DensityResult(
        val mass: BigDecimal,
        val volume: BigDecimal,
        val density: BigDecimal,
        val massUnit: MassUnit,
        val volumeUnit: VolumeUnit,
        val densityUnit: DensityUnit,
        override val details: DensityCalculationDetails
    ) : DensityBoxResult

    data class MassResult(
        val density: BigDecimal,
        val volume: BigDecimal,
        val mass: BigDecimal,
        val densityUnit: DensityUnit,
        val volumeUnit: VolumeUnit,
        val massUnit: MassUnit,
        override val details: DensityCalculationDetails
    ) : DensityBoxResult

    data class VolumeResult(
        val mass: BigDecimal,
        val density: BigDecimal,
        val volume: BigDecimal,
        val massUnit: MassUnit,
        val densityUnit: DensityUnit,
        val volumeUnit: VolumeUnit,
        override val details: DensityCalculationDetails
    ) : DensityBoxResult
}
