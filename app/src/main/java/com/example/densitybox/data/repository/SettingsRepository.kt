package com.example.densitybox.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.densitybox.data.model.AppTheme
import com.example.densitybox.data.model.DensityBoxSettings
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.QuantityType
import com.example.densitybox.domain.model.VolumeUnit
import java.math.RoundingMode

class SettingsRepository(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val PREF_NAME = "density_box_settings_prefs"

        private const val KEY_THEME = "theme"
        private const val KEY_DYNAMIC_COLOR = "dynamic_color"
        private const val KEY_HAPTIC_FEEDBACK = "haptic_feedback"
        private const val KEY_ANIMATIONS = "animations"
        private const val KEY_DECIMAL_PLACES = "decimal_places"
        private const val KEY_ROUNDING_MODE = "rounding_mode"
        private const val KEY_SHOW_FORMULA = "show_formula"
        private const val KEY_SHOW_STEPS = "show_steps"
        private const val KEY_CONFIRM_DELETE = "confirm_history_delete"
        private const val KEY_PRESERVE_LAST = "preserve_last_calculation"

        private const val KEY_LAST_MODE = "last_mode"
        private const val KEY_LAST_MASS_INPUT = "last_mass_input"
        private const val KEY_LAST_MASS_UNIT = "last_mass_unit"
        private const val KEY_LAST_VOLUME_INPUT = "last_volume_input"
        private const val KEY_LAST_VOLUME_UNIT = "last_volume_unit"
        private const val KEY_LAST_DENSITY_INPUT = "last_density_input"
        private const val KEY_LAST_DENSITY_UNIT = "last_density_unit"

        fun create(context: Context): SettingsRepository {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return SettingsRepository(prefs)
        }
    }

    fun getSettings(): DensityBoxSettings {
        val themeStr = sharedPreferences.getString(KEY_THEME, AppTheme.SYSTEM.name) ?: AppTheme.SYSTEM.name
        val theme = runCatching { AppTheme.valueOf(themeStr) }.getOrDefault(AppTheme.SYSTEM)

        val roundingStr = sharedPreferences.getString(KEY_ROUNDING_MODE, RoundingMode.HALF_UP.name) ?: RoundingMode.HALF_UP.name
        val roundingMode = runCatching { RoundingMode.valueOf(roundingStr) }.getOrDefault(RoundingMode.HALF_UP)

        val modeStr = sharedPreferences.getString(KEY_LAST_MODE, QuantityType.DENSITY.name) ?: QuantityType.DENSITY.name
        val lastMode = runCatching { QuantityType.valueOf(modeStr) }.getOrDefault(QuantityType.DENSITY)

        val massUnitStr = sharedPreferences.getString(KEY_LAST_MASS_UNIT, MassUnit.GRAM.name) ?: MassUnit.GRAM.name
        val lastMassUnit = runCatching { MassUnit.valueOf(massUnitStr) }.getOrDefault(MassUnit.GRAM)

        val volumeUnitStr = sharedPreferences.getString(KEY_LAST_VOLUME_UNIT, VolumeUnit.CUBIC_CENTIMETER.name) ?: VolumeUnit.CUBIC_CENTIMETER.name
        val lastVolumeUnit = runCatching { VolumeUnit.valueOf(volumeUnitStr) }.getOrDefault(VolumeUnit.CUBIC_CENTIMETER)

        val densityUnitStr = sharedPreferences.getString(KEY_LAST_DENSITY_UNIT, DensityUnit.GRAM_PER_CUBIC_CENTIMETER.name) ?: DensityUnit.GRAM_PER_CUBIC_CENTIMETER.name
        val lastDensityUnit = runCatching { DensityUnit.valueOf(densityUnitStr) }.getOrDefault(DensityUnit.GRAM_PER_CUBIC_CENTIMETER)

        return DensityBoxSettings(
            theme = theme,
            dynamicColor = sharedPreferences.getBoolean(KEY_DYNAMIC_COLOR, true),
            hapticFeedback = sharedPreferences.getBoolean(KEY_HAPTIC_FEEDBACK, true),
            animations = sharedPreferences.getBoolean(KEY_ANIMATIONS, true),
            decimalPlaces = sharedPreferences.getInt(KEY_DECIMAL_PLACES, 6),
            roundingMode = roundingMode,
            showFormula = sharedPreferences.getBoolean(KEY_SHOW_FORMULA, true),
            showCalculationSteps = sharedPreferences.getBoolean(KEY_SHOW_STEPS, true),
            confirmHistoryDelete = sharedPreferences.getBoolean(KEY_CONFIRM_DELETE, true),
            preserveLastCalculation = sharedPreferences.getBoolean(KEY_PRESERVE_LAST, true),
            lastMode = lastMode,
            lastMassInput = sharedPreferences.getString(KEY_LAST_MASS_INPUT, "100") ?: "100",
            lastMassUnit = lastMassUnit,
            lastVolumeInput = sharedPreferences.getString(KEY_LAST_VOLUME_INPUT, "50") ?: "50",
            lastVolumeUnit = lastVolumeUnit,
            lastDensityInput = sharedPreferences.getString(KEY_LAST_DENSITY_INPUT, "2") ?: "2",
            lastDensityUnit = lastDensityUnit
        )
    }

    fun saveSettings(settings: DensityBoxSettings) {
        sharedPreferences.edit()
            .putString(KEY_THEME, settings.theme.name)
            .putBoolean(KEY_DYNAMIC_COLOR, settings.dynamicColor)
            .putBoolean(KEY_HAPTIC_FEEDBACK, settings.hapticFeedback)
            .putBoolean(KEY_ANIMATIONS, settings.animations)
            .putInt(KEY_DECIMAL_PLACES, settings.decimalPlaces)
            .putString(KEY_ROUNDING_MODE, settings.roundingMode.name)
            .putBoolean(KEY_SHOW_FORMULA, settings.showFormula)
            .putBoolean(KEY_SHOW_STEPS, settings.showCalculationSteps)
            .putBoolean(KEY_CONFIRM_DELETE, settings.confirmHistoryDelete)
            .putBoolean(KEY_PRESERVE_LAST, settings.preserveLastCalculation)
            .putString(KEY_LAST_MODE, settings.lastMode.name)
            .putString(KEY_LAST_MASS_INPUT, settings.lastMassInput)
            .putString(KEY_LAST_MASS_UNIT, settings.lastMassUnit.name)
            .putString(KEY_LAST_VOLUME_INPUT, settings.lastVolumeInput)
            .putString(KEY_LAST_VOLUME_UNIT, settings.lastVolumeUnit.name)
            .putString(KEY_LAST_DENSITY_INPUT, settings.lastDensityInput)
            .putString(KEY_LAST_DENSITY_UNIT, settings.lastDensityUnit.name)
            .apply()
    }

    fun resetSettings() {
        val defaultSettings = DensityBoxSettings()
        saveSettings(defaultSettings)
    }
}
