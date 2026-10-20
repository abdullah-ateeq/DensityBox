package com.example.densitybox

import com.example.densitybox.data.model.AppTheme
import com.example.densitybox.data.model.DensityBoxHistory
import com.example.densitybox.data.repository.DensityBoxHistoryRepository
import com.example.densitybox.data.repository.SettingsRepository
import com.example.densitybox.domain.model.DensityBoxResult
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.QuantityType
import com.example.densitybox.domain.model.VolumeUnit
import com.example.densitybox.testutil.FakeSharedPreferences
import com.example.densitybox.ui.CalculationState
import com.example.densitybox.ui.DensityBoxViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class DensityBoxViewModelAndHistoryTest {

    private lateinit var settingsPrefs: FakeSharedPreferences
    private lateinit var historyPrefs: FakeSharedPreferences
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var historyRepository: DensityBoxHistoryRepository
    private lateinit var viewModel: DensityBoxViewModel

    @Before
    fun setup() {
        settingsPrefs = FakeSharedPreferences()
        historyPrefs = FakeSharedPreferences()
        settingsRepository = SettingsRepository(settingsPrefs)
        historyRepository = DensityBoxHistoryRepository(historyPrefs)
        viewModel = DensityBoxViewModel(settingsRepository, historyRepository)
    }

    // 51. Mode switching
    @Test
    fun test51_modeSwitchingPreservesValues() {
        // Start in Density mode: 100 g, 50 cm³ -> Result 2 g/cm³
        assertEquals(QuantityType.DENSITY, viewModel.uiState.value.mode)
        assertEquals("100", viewModel.uiState.value.massInput)
        assertEquals("50", viewModel.uiState.value.volumeInput)
        val densityResult = viewModel.uiState.value.result as? DensityBoxResult.DensityResult
        assertNotNull(densityResult)
        assertEquals(0, BigDecimal("2").compareTo(densityResult!!.density))

        // Switch to Mass: should take calculated density (2 g/cm³) and volume (50 cm³) -> Result 100 g
        viewModel.setMode(QuantityType.MASS)
        assertEquals(QuantityType.MASS, viewModel.uiState.value.mode)
        assertEquals("2", viewModel.uiState.value.densityInput)
        assertEquals("50", viewModel.uiState.value.volumeInput)
        val massResult = viewModel.uiState.value.result as? DensityBoxResult.MassResult
        assertNotNull(massResult)
        assertEquals(0, BigDecimal("100").compareTo(massResult!!.mass))

        // Switch to Volume: should take mass (100 g) and density (2 g/cm³) -> Result 50 cm³
        viewModel.setMode(QuantityType.VOLUME)
        assertEquals(QuantityType.VOLUME, viewModel.uiState.value.mode)
        assertEquals("100", viewModel.uiState.value.massInput)
        assertEquals("2", viewModel.uiState.value.densityInput)
        val volumeResult = viewModel.uiState.value.result as? DensityBoxResult.VolumeResult
        assertNotNull(volumeResult)
        assertEquals(0, BigDecimal("50").compareTo(volumeResult!!.volume))
    }

    // 54. History save
    @Test
    fun test54_historySave() {
        val saved = viewModel.saveCalculation()
        assertTrue(saved)
        val history = viewModel.uiState.value.history
        assertEquals(1, history.size)
        assertEquals(QuantityType.DENSITY, history.first().mode)
        assertEquals("2", history.first().resultValue)
    }

    // 55. History load
    @Test
    fun test55_historyLoad() {
        viewModel.saveCalculation()
        // Create fresh repository and viewmodel pointing to same prefs
        val freshHistoryRepo = DensityBoxHistoryRepository(historyPrefs)
        val items = freshHistoryRepo.getHistory()
        assertEquals(1, items.size)
        assertEquals("100", items.first().firstValue)
    }

    // 56. History limit (max 10 entries, 11th removes oldest)
    @Test
    fun test56_historyLimitTenEntries() {
        for (i in 1..11) {
            viewModel.onMassInputChanged("${i * 10}")
            viewModel.saveCalculation()
        }
        val history = historyRepository.getHistory()
        assertEquals(10, history.size)
        // Newest entry should be for 110
        assertEquals("110", history.first().firstValue)
        // Oldest entry (10) should have been removed, oldest remaining should be 20
        assertEquals("20", history.last().firstValue)
    }

    // 57. History deletion
    @Test
    fun test57_historyDeletion() {
        viewModel.saveCalculation()
        val item = viewModel.uiState.value.history.first()
        viewModel.deleteHistoryItem(item.id)
        assertTrue(viewModel.uiState.value.history.isEmpty())
    }

    // 58. History clear
    @Test
    fun test58_historyClear() {
        viewModel.onMassInputChanged("100")
        viewModel.saveCalculation()
        viewModel.onMassInputChanged("200")
        viewModel.saveCalculation()
        assertEquals(2, viewModel.uiState.value.history.size)

        viewModel.clearHistory()
        assertTrue(viewModel.uiState.value.history.isEmpty())
        assertTrue(historyRepository.getHistory().isEmpty())
    }

    // 59. History restore
    @Test
    fun test59_historyRestore() {
        // Calculate in Mass mode
        viewModel.setMode(QuantityType.MASS)
        viewModel.onDensityInputChanged("4")
        viewModel.onVolumeInputChanged("25")
        viewModel.saveCalculation()

        // Change values
        viewModel.reset()
        assertEquals(QuantityType.DENSITY, viewModel.uiState.value.mode)

        // Restore saved calculation
        val saved = historyRepository.getHistory().first()
        viewModel.restoreHistoryItem(saved)

        // Verify restored state
        assertEquals(QuantityType.MASS, viewModel.uiState.value.mode)
        assertEquals("4", viewModel.uiState.value.densityInput)
        assertEquals("25", viewModel.uiState.value.volumeInput)
        val result = viewModel.uiState.value.result as? DensityBoxResult.MassResult
        assertNotNull(result)
        assertEquals(0, BigDecimal("100").compareTo(result!!.mass))
    }

    // 60. Settings persistence
    @Test
    fun test60_settingsPersistence() {
        viewModel.updateTheme(AppTheme.DARK)
        viewModel.updateDecimalPlaces(4)
        viewModel.updateRoundingMode(RoundingMode.FLOOR)

        val freshSettings = SettingsRepository(settingsPrefs).getSettings()
        assertEquals(AppTheme.DARK, freshSettings.theme)
        assertEquals(4, freshSettings.decimalPlaces)
        assertEquals(RoundingMode.FLOOR, freshSettings.roundingMode)
    }

    // 61. Last calculation persistence
    @Test
    fun test61_lastCalculationPersistence() {
        viewModel.setMode(QuantityType.MASS)
        viewModel.onDensityInputChanged("3.5")
        viewModel.onVolumeInputChanged("40")

        val freshSettings = SettingsRepository(settingsPrefs).getSettings()
        assertEquals(QuantityType.MASS, freshSettings.lastMode)
        assertEquals("3.5", freshSettings.lastDensityInput)
        assertEquals("40", freshSettings.lastVolumeInput)
    }

    // 62. Reset
    @Test
    fun test62_reset() {
        viewModel.setMode(QuantityType.MASS)
        viewModel.onDensityInputChanged("5")
        viewModel.saveCalculation()

        viewModel.reset()

        assertEquals(QuantityType.DENSITY, viewModel.uiState.value.mode)
        assertEquals("100", viewModel.uiState.value.massInput)
        assertEquals("50", viewModel.uiState.value.volumeInput)
        assertEquals("2", viewModel.uiState.value.densityInput)

        // History remains untouched!
        assertEquals(1, viewModel.uiState.value.history.size)
    }

    // 63. Duplicate history protection
    @Test
    fun test63_duplicateHistoryProtection() {
        val firstSave = viewModel.saveCalculation()
        assertTrue(firstSave)
        assertEquals(1, viewModel.uiState.value.history.size)

        // Immediate duplicate save
        val secondSave = viewModel.saveCalculation()
        assertFalse(secondSave)
        assertEquals(1, viewModel.uiState.value.history.size)
    }

    // 64. Rapid input protection
    @Test
    fun test64_rapidInputProtection() {
        val inputs = listOf("1", "10", "100", "100.", "100.5", "abc", "100.5")
        for (inp in inputs) {
            viewModel.onMassInputChanged(inp)
        }
        assertEquals(CalculationState.Calculated, viewModel.uiState.value.calculationState)
        assertNotNull(viewModel.uiState.value.result)
    }
}
