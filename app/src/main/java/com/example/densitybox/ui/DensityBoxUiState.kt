package com.example.densitybox.ui

import com.example.densitybox.data.model.AppTheme
import com.example.densitybox.data.model.DensityBoxHistory
import com.example.densitybox.domain.model.DensityBoxResult
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.QuantityType
import com.example.densitybox.domain.model.VolumeUnit
import java.math.RoundingMode

sealed interface CalculationState {
    data object Idle : CalculationState
    data object Calculated : CalculationState
    data class Error(val message: String) : CalculationState
}

data class DensityBoxUiState(
    val mode: QuantityType = QuantityType.DENSITY,
    val massInput: String = "100",
    val massUnit: MassUnit = MassUnit.GRAM,
    val volumeInput: String = "50",
    val volumeUnit: VolumeUnit = VolumeUnit.CUBIC_CENTIMETER,
    val densityInput: String = "2",
    val densityUnit: DensityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
    val result: DensityBoxResult? = null,
    val history: List<DensityBoxHistory> = emptyList(),
    val decimalPlaces: Int = 6,
    val roundingMode: RoundingMode = RoundingMode.HALF_UP,
    val showFormula: Boolean = true,
    val showCalculationSteps: Boolean = true,
    val errorMessage: String? = null,
    val isCalculating: Boolean = false,
    val calculationState: CalculationState = CalculationState.Idle,
    val theme: AppTheme = AppTheme.SYSTEM,
    val dynamicColor: Boolean = true,
    val hapticFeedback: Boolean = true,
    val animations: Boolean = true,
    val confirmHistoryDelete: Boolean = true,
    val preserveLastCalculation: Boolean = true
)
