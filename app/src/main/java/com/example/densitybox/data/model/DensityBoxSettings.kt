package com.example.densitybox.data.model

import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.QuantityType
import com.example.densitybox.domain.model.VolumeUnit
import java.math.RoundingMode

enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK
}

data class DensityBoxSettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val dynamicColor: Boolean = true,
    val hapticFeedback: Boolean = true,
    val animations: Boolean = true,
    val decimalPlaces: Int = 6,
    val roundingMode: RoundingMode = RoundingMode.HALF_UP,
    val showFormula: Boolean = true,
    val showCalculationSteps: Boolean = true,
    val confirmHistoryDelete: Boolean = true,
    val preserveLastCalculation: Boolean = true,
    val lastMode: QuantityType = QuantityType.DENSITY,
    val lastMassInput: String = "100",
    val lastMassUnit: MassUnit = MassUnit.GRAM,
    val lastVolumeInput: String = "50",
    val lastVolumeUnit: VolumeUnit = VolumeUnit.CUBIC_CENTIMETER,
    val lastDensityInput: String = "2",
    val lastDensityUnit: DensityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER
)
