package com.example.densitybox.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.densitybox.data.model.AppTheme
import com.example.densitybox.ui.DensityBoxUiState
import com.example.densitybox.ui.DensityBoxViewModel
import com.example.densitybox.util.HapticFeedbackHelper
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: DensityBoxUiState,
    viewModel: DensityBoxViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showThemeDialog by remember { mutableStateOf(false) }
    var showDecimalDialog by remember { mutableStateOf(false) }
    var showRoundingDialog by remember { mutableStateOf(false) }
    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showResetSettingsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // APPEARANCE SECTION
            SettingsSectionHeader(title = "APPEARANCE")
            SettingsCard {
                SettingsClickableRow(
                    title = "Theme",
                    subtitle = when (uiState.theme) {
                        AppTheme.SYSTEM -> "System"
                        AppTheme.LIGHT -> "Light"
                        AppTheme.DARK -> "Dark"
                    },
                    onClick = { showThemeDialog = true }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsToggleRow(
                    title = "Dynamic Color",
                    subtitle = "Use colors derived from system wallpaper",
                    checked = uiState.dynamicColor,
                    onCheckedChange = {
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        viewModel.updateDynamicColor(it)
                    }
                )
            }

            // BEHAVIOR SECTION
            SettingsSectionHeader(title = "BEHAVIOR")
            SettingsCard {
                SettingsToggleRow(
                    title = "Haptic Feedback",
                    subtitle = "Vibrate on interactions and calculations",
                    checked = uiState.hapticFeedback,
                    onCheckedChange = {
                        viewModel.updateHapticFeedback(it)
                        HapticFeedbackHelper.performLightHaptic(context, it)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsToggleRow(
                    title = "Animations",
                    subtitle = "Enable fluid UI state transitions",
                    checked = uiState.animations,
                    onCheckedChange = {
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        viewModel.updateAnimations(it)
                    }
                )
            }

            // NUMBER DISPLAY SECTION
            SettingsSectionHeader(title = "NUMBER DISPLAY")
            SettingsCard {
                SettingsClickableRow(
                    title = "Decimal Places",
                    subtitle = "${uiState.decimalPlaces} decimal places (trailing zeros trimmed)",
                    onClick = { showDecimalDialog = true }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsClickableRow(
                    title = "Rounding Mode",
                    subtitle = uiState.roundingMode.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() },
                    onClick = { showRoundingDialog = true }
                )
            }

            // DISPLAY SECTION
            SettingsSectionHeader(title = "DISPLAY")
            SettingsCard {
                SettingsToggleRow(
                    title = "Show Formula",
                    subtitle = "Display formula card beneath result",
                    checked = uiState.showFormula,
                    onCheckedChange = {
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        viewModel.updateShowFormula(it)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsToggleRow(
                    title = "Show Calculation Steps",
                    subtitle = "Display step-by-step substitution breakdown",
                    checked = uiState.showCalculationSteps,
                    onCheckedChange = {
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        viewModel.updateShowCalculationSteps(it)
                    }
                )
            }

            // HISTORY SECTION
            SettingsSectionHeader(title = "HISTORY")
            SettingsCard {
                SettingsToggleRow(
                    title = "Confirm History Delete",
                    subtitle = "Prompt before deleting calculations",
                    checked = uiState.confirmHistoryDelete,
                    onCheckedChange = {
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        viewModel.updateConfirmHistoryDelete(it)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsToggleRow(
                    title = "Preserve Last Calculation",
                    subtitle = "Restore input values and mode on app reopen",
                    checked = uiState.preserveLastCalculation,
                    onCheckedChange = {
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        viewModel.updatePreserveLastCalculation(it)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsClickableRow(
                    title = "Clear History",
                    subtitle = "Remove all ${uiState.history.size} saved calculations",
                    onClick = { showClearHistoryDialog = true },
                    titleColor = MaterialTheme.colorScheme.error
                )
            }

            // RESET SETTINGS SECTION
            SettingsCard {
                SettingsClickableRow(
                    title = "Reset Settings",
                    subtitle = "Restore all settings to original defaults",
                    onClick = { showResetSettingsDialog = true },
                    titleColor = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // THEME DIALOG
        if (showThemeDialog) {
            AlertDialog(
                onDismissRequest = { showThemeDialog = false },
                title = { Text("Choose Theme") },
                text = {
                    Column {
                        listOf(
                            AppTheme.SYSTEM to "System Default",
                            AppTheme.LIGHT to "Light",
                            AppTheme.DARK to "Dark"
                        ).forEach { (theme, label) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                                        viewModel.updateTheme(theme)
                                        showThemeDialog = false
                                    }
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = uiState.theme == theme,
                                    onClick = {
                                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                                        viewModel.updateTheme(theme)
                                        showThemeDialog = false
                                    }
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showThemeDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // DECIMAL PLACES DIALOG
        if (showDecimalDialog) {
            val decimalOptions = listOf(0, 1, 2, 3, 4, 6, 8, 10, 12)
            AlertDialog(
                onDismissRequest = { showDecimalDialog = false },
                title = { Text("Decimal Places") },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        decimalOptions.forEach { places ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                                        viewModel.updateDecimalPlaces(places)
                                        showDecimalDialog = false
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = uiState.decimalPlaces == places,
                                    onClick = {
                                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                                        viewModel.updateDecimalPlaces(places)
                                        showDecimalDialog = false
                                    }
                                )
                                Text(
                                    text = "$places decimal places",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDecimalDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // ROUNDING MODE DIALOG
        if (showRoundingDialog) {
            val roundingOptions = listOf(
                RoundingMode.HALF_UP to "Half Up",
                RoundingMode.HALF_DOWN to "Half Down",
                RoundingMode.HALF_EVEN to "Half Even",
                RoundingMode.UP to "Up",
                RoundingMode.DOWN to "Down",
                RoundingMode.CEILING to "Ceiling",
                RoundingMode.FLOOR to "Floor"
            )
            AlertDialog(
                onDismissRequest = { showRoundingDialog = false },
                title = { Text("Rounding Mode") },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        roundingOptions.forEach { (mode, label) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                                        viewModel.updateRoundingMode(mode)
                                        showRoundingDialog = false
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = uiState.roundingMode == mode,
                                    onClick = {
                                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                                        viewModel.updateRoundingMode(mode)
                                        showRoundingDialog = false
                                    }
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showRoundingDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // CLEAR HISTORY DIALOG
        if (showClearHistoryDialog) {
            AlertDialog(
                onDismissRequest = { showClearHistoryDialog = false },
                title = { Text("Clear History") },
                text = { Text("Clear all recent density calculations?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                            viewModel.clearHistory()
                            showClearHistoryDialog = false
                        }
                    ) {
                        Text("Clear", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearHistoryDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // RESET SETTINGS DIALOG
        if (showResetSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showResetSettingsDialog = false },
                title = { Text("Reset Settings") },
                text = { Text("Reset all preferences to default values?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                            viewModel.resetSettings()
                            showResetSettingsDialog = false
                        }
                    ) {
                        Text("Reset", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetSettingsDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
private fun SettingsClickableRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
