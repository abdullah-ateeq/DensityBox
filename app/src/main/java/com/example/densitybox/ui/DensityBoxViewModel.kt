package com.example.densitybox.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.densitybox.data.model.AppTheme
import com.example.densitybox.data.model.DensityBoxHistory
import com.example.densitybox.data.repository.DensityBoxHistoryRepository
import com.example.densitybox.data.repository.SettingsRepository
import com.example.densitybox.domain.calculator.DensityCalculator
import com.example.densitybox.domain.formatter.DensityBoxFormatter
import com.example.densitybox.domain.model.DensityBoxResult
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.QuantityType
import com.example.densitybox.domain.model.VolumeUnit
import com.example.densitybox.domain.parser.DensityBoxParser
import com.example.densitybox.domain.registry.DensityUnitRegistry
import com.example.densitybox.domain.registry.MassUnitRegistry
import com.example.densitybox.domain.registry.VolumeUnitRegistry
import com.example.densitybox.domain.validator.DensityBoxValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.RoundingMode
import java.util.UUID

class DensityBoxViewModel(
    private val settingsRepository: SettingsRepository,
    private val historyRepository: DensityBoxHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<DensityBoxUiState> = _uiState.asStateFlow()

    init {
        recalculate()
    }

    private fun createInitialState(): DensityBoxUiState {
        val settings = settingsRepository.getSettings()
        val history = historyRepository.getHistory()

        return if (settings.preserveLastCalculation) {
            DensityBoxUiState(
                mode = settings.lastMode,
                massInput = settings.lastMassInput,
                massUnit = settings.lastMassUnit,
                volumeInput = settings.lastVolumeInput,
                volumeUnit = settings.lastVolumeUnit,
                densityInput = settings.lastDensityInput,
                densityUnit = settings.lastDensityUnit,
                history = history,
                decimalPlaces = settings.decimalPlaces,
                roundingMode = settings.roundingMode,
                showFormula = settings.showFormula,
                showCalculationSteps = settings.showCalculationSteps,
                theme = settings.theme,
                dynamicColor = settings.dynamicColor,
                hapticFeedback = settings.hapticFeedback,
                animations = settings.animations,
                confirmHistoryDelete = settings.confirmHistoryDelete,
                preserveLastCalculation = settings.preserveLastCalculation
            )
        } else {
            DensityBoxUiState(
                mode = QuantityType.DENSITY,
                massInput = "100",
                massUnit = MassUnit.GRAM,
                volumeInput = "50",
                volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
                densityInput = "2",
                densityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER,
                history = history,
                decimalPlaces = settings.decimalPlaces,
                roundingMode = settings.roundingMode,
                showFormula = settings.showFormula,
                showCalculationSteps = settings.showCalculationSteps,
                theme = settings.theme,
                dynamicColor = settings.dynamicColor,
                hapticFeedback = settings.hapticFeedback,
                animations = settings.animations,
                confirmHistoryDelete = settings.confirmHistoryDelete,
                preserveLastCalculation = settings.preserveLastCalculation
            )
        }
    }

    fun setMode(newMode: QuantityType) {
        if (_uiState.value.mode == newMode) return

        val current = _uiState.value
        val currentResult = current.result

        when (newMode) {
            QuantityType.DENSITY -> {
                var mInput = current.massInput
                var mUnit = current.massUnit
                var vInput = current.volumeInput
                var vUnit = current.volumeUnit

                if (current.mode == QuantityType.MASS && currentResult is DensityBoxResult.MassResult) {
                    mInput = DensityBoxFormatter.formatNumber(currentResult.mass, current.decimalPlaces, current.roundingMode)
                    mUnit = currentResult.massUnit
                    vInput = DensityBoxFormatter.formatNumber(currentResult.volume, current.decimalPlaces, current.roundingMode)
                    vUnit = currentResult.volumeUnit
                } else if (current.mode == QuantityType.VOLUME && currentResult is DensityBoxResult.VolumeResult) {
                    mInput = DensityBoxFormatter.formatNumber(currentResult.mass, current.decimalPlaces, current.roundingMode)
                    mUnit = currentResult.massUnit
                    vInput = DensityBoxFormatter.formatNumber(currentResult.volume, current.decimalPlaces, current.roundingMode)
                    vUnit = currentResult.volumeUnit
                }

                _uiState.update {
                    it.copy(
                        mode = newMode,
                        massInput = mInput,
                        massUnit = mUnit,
                        volumeInput = vInput,
                        volumeUnit = vUnit
                    )
                }
            }
            QuantityType.MASS -> {
                var dInput = current.densityInput
                var dUnit = current.densityUnit
                var vInput = current.volumeInput
                var vUnit = current.volumeUnit

                if (current.mode == QuantityType.DENSITY && currentResult is DensityBoxResult.DensityResult) {
                    dInput = DensityBoxFormatter.formatNumber(currentResult.density, current.decimalPlaces, current.roundingMode)
                    dUnit = currentResult.densityUnit
                    vInput = DensityBoxFormatter.formatNumber(currentResult.volume, current.decimalPlaces, current.roundingMode)
                    vUnit = currentResult.volumeUnit
                } else if (current.mode == QuantityType.VOLUME && currentResult is DensityBoxResult.VolumeResult) {
                    dInput = DensityBoxFormatter.formatNumber(currentResult.density, current.decimalPlaces, current.roundingMode)
                    dUnit = currentResult.densityUnit
                    vInput = DensityBoxFormatter.formatNumber(currentResult.volume, current.decimalPlaces, current.roundingMode)
                    vUnit = currentResult.volumeUnit
                }

                _uiState.update {
                    it.copy(
                        mode = newMode,
                        densityInput = dInput,
                        densityUnit = dUnit,
                        volumeInput = vInput,
                        volumeUnit = vUnit
                    )
                }
            }
            QuantityType.VOLUME -> {
                var mInput = current.massInput
                var mUnit = current.massUnit
                var dInput = current.densityInput
                var dUnit = current.densityUnit

                if (current.mode == QuantityType.DENSITY && currentResult is DensityBoxResult.DensityResult) {
                    mInput = DensityBoxFormatter.formatNumber(currentResult.mass, current.decimalPlaces, current.roundingMode)
                    mUnit = currentResult.massUnit
                    dInput = DensityBoxFormatter.formatNumber(currentResult.density, current.decimalPlaces, current.roundingMode)
                    dUnit = currentResult.densityUnit
                } else if (current.mode == QuantityType.MASS && currentResult is DensityBoxResult.MassResult) {
                    mInput = DensityBoxFormatter.formatNumber(currentResult.mass, current.decimalPlaces, current.roundingMode)
                    mUnit = currentResult.massUnit
                    dInput = DensityBoxFormatter.formatNumber(currentResult.density, current.decimalPlaces, current.roundingMode)
                    dUnit = currentResult.densityUnit
                }

                _uiState.update {
                    it.copy(
                        mode = newMode,
                        massInput = mInput,
                        massUnit = mUnit,
                        densityInput = dInput,
                        densityUnit = dUnit
                    )
                }
            }
        }
        recalculate()
        persistCurrentState()
    }

    fun onMassInputChanged(input: String) {
        _uiState.update { it.copy(massInput = input) }
        recalculate()
        persistCurrentState()
    }

    fun onVolumeInputChanged(input: String) {
        _uiState.update { it.copy(volumeInput = input) }
        recalculate()
        persistCurrentState()
    }

    fun onDensityInputChanged(input: String) {
        _uiState.update { it.copy(densityInput = input) }
        recalculate()
        persistCurrentState()
    }

    fun onMassUnitChanged(unit: MassUnit) {
        _uiState.update { it.copy(massUnit = unit) }
        recalculate()
        persistCurrentState()
    }

    fun onVolumeUnitChanged(unit: VolumeUnit) {
        _uiState.update { it.copy(volumeUnit = unit) }
        recalculate()
        persistCurrentState()
    }

    fun onDensityUnitChanged(unit: DensityUnit) {
        _uiState.update { it.copy(densityUnit = unit) }
        recalculate()
        persistCurrentState()
    }

    fun setMassPreset(value: String, unit: MassUnit) {
        _uiState.update { it.copy(massInput = value, massUnit = unit) }
        recalculate()
        persistCurrentState()
    }

    fun setVolumePreset(value: String, unit: VolumeUnit) {
        _uiState.update { it.copy(volumeInput = value, volumeUnit = unit) }
        recalculate()
        persistCurrentState()
    }

    fun setDensityPreset(value: String, unit: DensityUnit) {
        _uiState.update { it.copy(densityInput = value, densityUnit = unit) }
        recalculate()
        persistCurrentState()
    }

    fun recalculate() {
        val state = _uiState.value
        when (state.mode) {
            QuantityType.DENSITY -> calculateDensityMode(state)
            QuantityType.MASS -> calculateMassMode(state)
            QuantityType.VOLUME -> calculateVolumeMode(state)
        }
    }

    private fun calculateDensityMode(state: DensityBoxUiState) {
        val massParsed = DensityBoxParser.parseMass(state.massInput)
        if (massParsed.isFailure) {
            val err = massParsed.exceptionOrNull()?.message ?: "Enter a valid number."
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = err,
                    calculationState = CalculationState.Error(err)
                )
            }
            return
        }

        val volumeParsed = DensityBoxParser.parseVolume(state.volumeInput)
        if (volumeParsed.isFailure) {
            val err = volumeParsed.exceptionOrNull()?.message ?: "Enter a valid number."
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = err,
                    calculationState = CalculationState.Error(err)
                )
            }
            return
        }

        val mass = massParsed.getOrThrow()
        val volume = volumeParsed.getOrThrow()

        val massErr = DensityBoxValidator.validateMass(mass)
        if (massErr != null) {
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = massErr,
                    calculationState = CalculationState.Error(massErr)
                )
            }
            return
        }

        val volErr = DensityBoxValidator.validateVolume(volume)
        if (volErr != null) {
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = volErr,
                    calculationState = CalculationState.Error(volErr)
                )
            }
            return
        }

        try {
            val result = DensityCalculator.solveDensity(
                mass = mass,
                massUnit = state.massUnit,
                volume = volume,
                volumeUnit = state.volumeUnit,
                targetDensityUnit = state.densityUnit
            )
            _uiState.update {
                it.copy(
                    result = result,
                    errorMessage = null,
                    calculationState = CalculationState.Calculated
                )
            }
        } catch (e: Exception) {
            val err = e.message ?: "Calculation error"
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = err,
                    calculationState = CalculationState.Error(err)
                )
            }
        }
    }

    private fun calculateMassMode(state: DensityBoxUiState) {
        val densityParsed = DensityBoxParser.parseDensity(state.densityInput)
        if (densityParsed.isFailure) {
            val err = densityParsed.exceptionOrNull()?.message ?: "Enter a valid number."
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = err,
                    calculationState = CalculationState.Error(err)
                )
            }
            return
        }

        val volumeParsed = DensityBoxParser.parseVolume(state.volumeInput)
        if (volumeParsed.isFailure) {
            val err = volumeParsed.exceptionOrNull()?.message ?: "Enter a valid number."
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = err,
                    calculationState = CalculationState.Error(err)
                )
            }
            return
        }

        val density = densityParsed.getOrThrow()
        val volume = volumeParsed.getOrThrow()

        val densityErr = DensityBoxValidator.validateDensity(density)
        if (densityErr != null) {
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = densityErr,
                    calculationState = CalculationState.Error(densityErr)
                )
            }
            return
        }

        val volErr = DensityBoxValidator.validateVolume(volume)
        if (volErr != null) {
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = volErr,
                    calculationState = CalculationState.Error(volErr)
                )
            }
            return
        }

        try {
            val result = DensityCalculator.solveMass(
                density = density,
                densityUnit = state.densityUnit,
                volume = volume,
                volumeUnit = state.volumeUnit,
                targetMassUnit = state.massUnit
            )
            _uiState.update {
                it.copy(
                    result = result,
                    errorMessage = null,
                    calculationState = CalculationState.Calculated
                )
            }
        } catch (e: Exception) {
            val err = e.message ?: "Calculation error"
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = err,
                    calculationState = CalculationState.Error(err)
                )
            }
        }
    }

    private fun calculateVolumeMode(state: DensityBoxUiState) {
        val massParsed = DensityBoxParser.parseMass(state.massInput)
        if (massParsed.isFailure) {
            val err = massParsed.exceptionOrNull()?.message ?: "Enter a valid number."
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = err,
                    calculationState = CalculationState.Error(err)
                )
            }
            return
        }

        val densityParsed = DensityBoxParser.parseDensity(state.densityInput)
        if (densityParsed.isFailure) {
            val err = densityParsed.exceptionOrNull()?.message ?: "Enter a valid number."
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = err,
                    calculationState = CalculationState.Error(err)
                )
            }
            return
        }

        val mass = massParsed.getOrThrow()
        val density = densityParsed.getOrThrow()

        val massErr = DensityBoxValidator.validateMass(mass)
        if (massErr != null) {
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = massErr,
                    calculationState = CalculationState.Error(massErr)
                )
            }
            return
        }

        val densityErr = DensityBoxValidator.validateDensity(density)
        if (densityErr != null) {
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = densityErr,
                    calculationState = CalculationState.Error(densityErr)
                )
            }
            return
        }

        try {
            val result = DensityCalculator.solveVolume(
                mass = mass,
                massUnit = state.massUnit,
                density = density,
                densityUnit = state.densityUnit,
                targetVolumeUnit = state.volumeUnit
            )
            _uiState.update {
                it.copy(
                    result = result,
                    errorMessage = null,
                    calculationState = CalculationState.Calculated
                )
            }
        } catch (e: Exception) {
            val err = e.message ?: "Calculation error"
            _uiState.update {
                it.copy(
                    result = null,
                    errorMessage = err,
                    calculationState = CalculationState.Error(err)
                )
            }
        }
    }

    fun saveCalculation(): Boolean {
        val state = _uiState.value
        val result = state.result ?: return false

        val (firstVal, firstUnit, secondVal, secondUnit, resVal, resUnit) = when (result) {
            is DensityBoxResult.DensityResult -> {
                HexaTuple(
                    state.massInput,
                    MassUnitRegistry.get(result.massUnit).symbol,
                    state.volumeInput,
                    VolumeUnitRegistry.get(result.volumeUnit).symbol,
                    DensityBoxFormatter.formatNumber(result.density, state.decimalPlaces, state.roundingMode),
                    DensityUnitRegistry.get(result.densityUnit).symbol
                )
            }
            is DensityBoxResult.MassResult -> {
                HexaTuple(
                    state.densityInput,
                    DensityUnitRegistry.get(result.densityUnit).symbol,
                    state.volumeInput,
                    VolumeUnitRegistry.get(result.volumeUnit).symbol,
                    DensityBoxFormatter.formatNumber(result.mass, state.decimalPlaces, state.roundingMode),
                    MassUnitRegistry.get(result.massUnit).symbol
                )
            }
            is DensityBoxResult.VolumeResult -> {
                HexaTuple(
                    state.massInput,
                    MassUnitRegistry.get(result.massUnit).symbol,
                    state.densityInput,
                    DensityUnitRegistry.get(result.densityUnit).symbol,
                    DensityBoxFormatter.formatNumber(result.volume, state.decimalPlaces, state.roundingMode),
                    VolumeUnitRegistry.get(result.volumeUnit).symbol
                )
            }
        }

        val entry = DensityBoxHistory(
            id = UUID.randomUUID().toString(),
            mode = state.mode,
            firstValue = firstVal,
            firstUnit = firstUnit,
            secondValue = secondVal,
            secondUnit = secondUnit,
            resultValue = resVal,
            resultUnit = resUnit,
            calculatedAt = System.currentTimeMillis()
        )

        val added = historyRepository.addEntry(entry)
        if (added) {
            _uiState.update { it.copy(history = historyRepository.getHistory()) }
        }
        return added
    }

    fun deleteHistoryItem(id: String) {
        historyRepository.deleteEntry(id)
        _uiState.update { it.copy(history = historyRepository.getHistory()) }
    }

    fun clearHistory() {
        historyRepository.clearHistory()
        _uiState.update { it.copy(history = emptyList()) }
    }

    fun restoreHistoryItem(item: DensityBoxHistory) {
        when (item.mode) {
            QuantityType.DENSITY -> {
                val massUnit = MassUnitRegistry.all.firstOrNull { it.symbol == item.firstUnit }?.unit ?: MassUnit.GRAM
                val volumeUnit = VolumeUnitRegistry.all.firstOrNull { it.symbol == item.secondUnit }?.unit ?: VolumeUnit.CUBIC_CENTIMETER
                val densityUnit = DensityUnitRegistry.all.firstOrNull { it.symbol == item.resultUnit }?.unit ?: DensityUnit.GRAM_PER_CUBIC_CENTIMETER

                _uiState.update {
                    it.copy(
                        mode = QuantityType.DENSITY,
                        massInput = item.firstValue,
                        massUnit = massUnit,
                        volumeInput = item.secondValue,
                        volumeUnit = volumeUnit,
                        densityUnit = densityUnit
                    )
                }
            }
            QuantityType.MASS -> {
                val densityUnit = DensityUnitRegistry.all.firstOrNull { it.symbol == item.firstUnit }?.unit ?: DensityUnit.GRAM_PER_CUBIC_CENTIMETER
                val volumeUnit = VolumeUnitRegistry.all.firstOrNull { it.symbol == item.secondUnit }?.unit ?: VolumeUnit.CUBIC_CENTIMETER
                val massUnit = MassUnitRegistry.all.firstOrNull { it.symbol == item.resultUnit }?.unit ?: MassUnit.GRAM

                _uiState.update {
                    it.copy(
                        mode = QuantityType.MASS,
                        densityInput = item.firstValue,
                        densityUnit = densityUnit,
                        volumeInput = item.secondValue,
                        volumeUnit = volumeUnit,
                        massUnit = massUnit
                    )
                }
            }
            QuantityType.VOLUME -> {
                val massUnit = MassUnitRegistry.all.firstOrNull { it.symbol == item.firstUnit }?.unit ?: MassUnit.GRAM
                val densityUnit = DensityUnitRegistry.all.firstOrNull { it.symbol == item.secondUnit }?.unit ?: DensityUnit.GRAM_PER_CUBIC_CENTIMETER
                val volumeUnit = VolumeUnitRegistry.all.firstOrNull { it.symbol == item.resultUnit }?.unit ?: VolumeUnit.CUBIC_CENTIMETER

                _uiState.update {
                    it.copy(
                        mode = QuantityType.VOLUME,
                        massInput = item.firstValue,
                        massUnit = massUnit,
                        densityInput = item.secondValue,
                        densityUnit = densityUnit,
                        volumeUnit = volumeUnit
                    )
                }
            }
        }
        recalculate()
        persistCurrentState()
    }

    fun reset() {
        _uiState.update {
            it.copy(
                mode = QuantityType.DENSITY,
                massInput = "100",
                massUnit = MassUnit.GRAM,
                volumeInput = "50",
                volumeUnit = VolumeUnit.CUBIC_CENTIMETER,
                densityInput = "2",
                densityUnit = DensityUnit.GRAM_PER_CUBIC_CENTIMETER
            )
        }
        recalculate()
        persistCurrentState()
    }

    fun updateTheme(theme: AppTheme) {
        _uiState.update { it.copy(theme = theme) }
        persistSettings()
    }

    fun updateDynamicColor(enabled: Boolean) {
        _uiState.update { it.copy(dynamicColor = enabled) }
        persistSettings()
    }

    fun updateHapticFeedback(enabled: Boolean) {
        _uiState.update { it.copy(hapticFeedback = enabled) }
        persistSettings()
    }

    fun updateAnimations(enabled: Boolean) {
        _uiState.update { it.copy(animations = enabled) }
        persistSettings()
    }

    fun updateDecimalPlaces(places: Int) {
        _uiState.update { it.copy(decimalPlaces = places) }
        persistSettings()
    }

    fun updateRoundingMode(mode: RoundingMode) {
        _uiState.update { it.copy(roundingMode = mode) }
        persistSettings()
    }

    fun updateShowFormula(show: Boolean) {
        _uiState.update { it.copy(showFormula = show) }
        persistSettings()
    }

    fun updateShowCalculationSteps(show: Boolean) {
        _uiState.update { it.copy(showCalculationSteps = show) }
        persistSettings()
    }

    fun updateConfirmHistoryDelete(confirm: Boolean) {
        _uiState.update { it.copy(confirmHistoryDelete = confirm) }
        persistSettings()
    }

    fun updatePreserveLastCalculation(preserve: Boolean) {
        _uiState.update { it.copy(preserveLastCalculation = preserve) }
        persistSettings()
    }

    fun resetSettings() {
        settingsRepository.resetSettings()
        val defaultSettings = settingsRepository.getSettings()
        _uiState.update {
            it.copy(
                theme = defaultSettings.theme,
                dynamicColor = defaultSettings.dynamicColor,
                hapticFeedback = defaultSettings.hapticFeedback,
                animations = defaultSettings.animations,
                decimalPlaces = defaultSettings.decimalPlaces,
                roundingMode = defaultSettings.roundingMode,
                showFormula = defaultSettings.showFormula,
                showCalculationSteps = defaultSettings.showCalculationSteps,
                confirmHistoryDelete = defaultSettings.confirmHistoryDelete,
                preserveLastCalculation = defaultSettings.preserveLastCalculation
            )
        }
    }

    private fun persistCurrentState() {
        val state = _uiState.value
        val currentSettings = settingsRepository.getSettings()
        settingsRepository.saveSettings(
            currentSettings.copy(
                lastMode = state.mode,
                lastMassInput = state.massInput,
                lastMassUnit = state.massUnit,
                lastVolumeInput = state.volumeInput,
                lastVolumeUnit = state.volumeUnit,
                lastDensityInput = state.densityInput,
                lastDensityUnit = state.densityUnit
            )
        )
    }

    private fun persistSettings() {
        val state = _uiState.value
        val currentSettings = settingsRepository.getSettings()
        settingsRepository.saveSettings(
            currentSettings.copy(
                theme = state.theme,
                dynamicColor = state.dynamicColor,
                hapticFeedback = state.hapticFeedback,
                animations = state.animations,
                decimalPlaces = state.decimalPlaces,
                roundingMode = state.roundingMode,
                showFormula = state.showFormula,
                showCalculationSteps = state.showCalculationSteps,
                confirmHistoryDelete = state.confirmHistoryDelete,
                preserveLastCalculation = state.preserveLastCalculation,
                lastMode = state.mode,
                lastMassInput = state.massInput,
                lastMassUnit = state.massUnit,
                lastVolumeInput = state.volumeInput,
                lastVolumeUnit = state.volumeUnit,
                lastDensityInput = state.densityInput,
                lastDensityUnit = state.densityUnit
            )
        )
    }

    private data class HexaTuple(
        val firstVal: String,
        val firstUnit: String,
        val secondVal: String,
        val secondUnit: String,
        val resVal: String,
        val resUnit: String
    )

    companion object {
        fun provideFactory(
            settingsRepository: SettingsRepository,
            historyRepository: DensityBoxHistoryRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DensityBoxViewModel(settingsRepository, historyRepository) as T
            }
        }
    }
}
