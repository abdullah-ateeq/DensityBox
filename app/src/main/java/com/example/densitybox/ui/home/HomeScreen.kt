package com.example.densitybox.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.densitybox.domain.formatter.DensityBoxFormatter
import com.example.densitybox.domain.model.DensityBoxResult
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.QuantityType
import com.example.densitybox.domain.model.VolumeUnit
import com.example.densitybox.domain.registry.DensityUnitRegistry
import com.example.densitybox.domain.registry.MassUnitRegistry
import com.example.densitybox.domain.registry.VolumeUnitRegistry
import com.example.densitybox.ui.DensityBoxUiState
import com.example.densitybox.ui.DensityBoxViewModel
import com.example.densitybox.util.ClipboardHelper
import com.example.densitybox.util.HapticFeedbackHelper
import com.example.densitybox.util.ShareHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: DensityBoxUiState,
    viewModel: DensityBoxViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var unitPickerQuantityType by remember { mutableStateOf<QuantityType?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "DensityBox",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Calculate density, mass, or volume.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                            viewModel.reset()
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Values reset to default")
                            }
                        },
                        modifier = Modifier.semantics { contentDescription = "Reset values" }
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // MODE SELECTOR
            ModeSelectorSection(
                currentMode = uiState.mode,
                onModeSelected = { mode ->
                    HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                    viewModel.setMode(mode)
                }
            )

            // DYNAMIC INPUT FIELDS
            when (uiState.mode) {
                QuantityType.DENSITY -> {
                    DensityModeInputs(
                        uiState = uiState,
                        onMassChanged = viewModel::onMassInputChanged,
                        onVolumeChanged = viewModel::onVolumeInputChanged,
                        onPickMassUnit = { unitPickerQuantityType = QuantityType.MASS },
                        onPickVolumeUnit = { unitPickerQuantityType = QuantityType.VOLUME },
                        onPickTargetDensityUnit = { unitPickerQuantityType = QuantityType.DENSITY },
                        onDone = { focusManager.clearFocus() }
                    )
                }
                QuantityType.MASS -> {
                    MassModeInputs(
                        uiState = uiState,
                        onDensityChanged = viewModel::onDensityInputChanged,
                        onVolumeChanged = viewModel::onVolumeInputChanged,
                        onPickDensityUnit = { unitPickerQuantityType = QuantityType.DENSITY },
                        onPickVolumeUnit = { unitPickerQuantityType = QuantityType.VOLUME },
                        onPickTargetMassUnit = { unitPickerQuantityType = QuantityType.MASS },
                        onDone = { focusManager.clearFocus() }
                    )
                }
                QuantityType.VOLUME -> {
                    VolumeModeInputs(
                        uiState = uiState,
                        onMassChanged = viewModel::onMassInputChanged,
                        onDensityChanged = viewModel::onDensityInputChanged,
                        onPickMassUnit = { unitPickerQuantityType = QuantityType.MASS },
                        onPickDensityUnit = { unitPickerQuantityType = QuantityType.DENSITY },
                        onPickTargetVolumeUnit = { unitPickerQuantityType = QuantityType.VOLUME },
                        onDone = { focusManager.clearFocus() }
                    )
                }
            }

            // ERROR DISPLAY OR RESULT CARD
            if (uiState.errorMessage != null) {
                ErrorCard(message = uiState.errorMessage)
            } else if (uiState.result != null) {
                ResultCard(
                    result = uiState.result,
                    decimalPlaces = uiState.decimalPlaces,
                    roundingMode = uiState.roundingMode,
                    onCopyValue = { valueWithUnit ->
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        ClipboardHelper.copyToClipboard(context, "DensityBox Result", valueWithUnit)
                    },
                    onCopyAll = {
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        val copyText = DensityBoxFormatter.formatCopyText(
                            uiState.result,
                            uiState.decimalPlaces,
                            uiState.roundingMode
                        )
                        ClipboardHelper.copyToClipboard(context, "DensityBox Calculation", copyText)
                    },
                    onSave = {
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        val saved = viewModel.saveCalculation()
                        coroutineScope.launch {
                            if (saved) {
                                snackbarHostState.showSnackbar("Calculation saved to history")
                            } else {
                                snackbarHostState.showSnackbar("Calculation already in history")
                            }
                        }
                    },
                    onShare = {
                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                        val shareText = DensityBoxFormatter.formatShareText(
                            uiState.result,
                            uiState.decimalPlaces,
                            uiState.roundingMode
                        )
                        ShareHelper.shareText(context, shareText)
                    }
                )
            }

            // FORMULA CARD
            if (uiState.showFormula && uiState.result != null) {
                FormulaCard(formula = uiState.result.details.formula)
            }

            // CALCULATION STEPS
            if (uiState.showCalculationSteps && uiState.result != null) {
                CalculationStepsCard(
                    result = uiState.result,
                    decimalPlaces = uiState.decimalPlaces,
                    roundingMode = uiState.roundingMode
                )
            }

            // QUICK PRESETS
            PresetChips(
                mode = uiState.mode,
                onMassPreset = { v, u ->
                    HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                    viewModel.setMassPreset(v, u)
                },
                onVolumePreset = { v, u ->
                    HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                    viewModel.setVolumePreset(v, u)
                },
                onDensityPreset = { v, u ->
                    HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                    viewModel.setDensityPreset(v, u)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // UNIT PICKER BOTTOM SHEET
        unitPickerQuantityType?.let { quantityType ->
            val currentSelectedId = when (quantityType) {
                QuantityType.MASS -> uiState.massUnit.name
                QuantityType.VOLUME -> uiState.volumeUnit.name
                QuantityType.DENSITY -> uiState.densityUnit.name
            }

            UnitPickerSheet(
                quantityType = quantityType,
                selectedUnitId = currentSelectedId,
                onMassUnitSelected = {
                    HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                    viewModel.onMassUnitChanged(it)
                },
                onVolumeUnitSelected = {
                    HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                    viewModel.onVolumeUnitChanged(it)
                },
                onDensityUnitSelected = {
                    HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                    viewModel.onDensityUnitChanged(it)
                },
                onDismiss = { unitPickerQuantityType = null }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSelectorSection(
    currentMode: QuantityType,
    onModeSelected: (QuantityType) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        val modes = listOf(
            Triple(QuantityType.DENSITY, "DENSITY", "Calculate density"),
            Triple(QuantityType.MASS, "MASS", "Calculate mass"),
            Triple(QuantityType.VOLUME, "VOLUME", "Calculate volume")
        )

        modes.forEachIndexed { index, (mode, label, desc) ->
            val isSelected = currentMode == mode
            Surface(
                onClick = { onModeSelected(mode) },
                shape = when (index) {
                    0 -> RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    modes.lastIndex -> RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                    else -> RoundedCornerShape(0.dp)
                },
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .semantics { contentDescription = desc }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun DensityModeInputs(
    uiState: DensityBoxUiState,
    onMassChanged: (String) -> Unit,
    onVolumeChanged: (String) -> Unit,
    onPickMassUnit: () -> Unit,
    onPickVolumeUnit: () -> Unit,
    onPickTargetDensityUnit: () -> Unit,
    onDone: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InputFieldWithUnit(
            label = "MASS",
            accessibilityDesc = "Mass value.",
            value = uiState.massInput,
            onValueChange = onMassChanged,
            unitSymbol = MassUnitRegistry.get(uiState.massUnit).symbol,
            onUnitClick = onPickMassUnit,
            imeAction = ImeAction.Next
        )

        InputFieldWithUnit(
            label = "VOLUME",
            accessibilityDesc = "Volume value.",
            value = uiState.volumeInput,
            onValueChange = onVolumeChanged,
            unitSymbol = VolumeUnitRegistry.get(uiState.volumeUnit).symbol,
            onUnitClick = onPickVolumeUnit,
            imeAction = ImeAction.Done,
            onImeAction = onDone
        )

        TargetUnitSelectorRow(
            label = "Result Density Unit:",
            unitSymbol = DensityUnitRegistry.get(uiState.densityUnit).symbol,
            unitName = DensityUnitRegistry.get(uiState.densityUnit).name,
            onClick = onPickTargetDensityUnit
        )
    }
}

@Composable
private fun MassModeInputs(
    uiState: DensityBoxUiState,
    onDensityChanged: (String) -> Unit,
    onVolumeChanged: (String) -> Unit,
    onPickDensityUnit: () -> Unit,
    onPickVolumeUnit: () -> Unit,
    onPickTargetMassUnit: () -> Unit,
    onDone: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InputFieldWithUnit(
            label = "DENSITY",
            accessibilityDesc = "Density value.",
            value = uiState.densityInput,
            onValueChange = onDensityChanged,
            unitSymbol = DensityUnitRegistry.get(uiState.densityUnit).symbol,
            onUnitClick = onPickDensityUnit,
            imeAction = ImeAction.Next
        )

        InputFieldWithUnit(
            label = "VOLUME",
            accessibilityDesc = "Volume value.",
            value = uiState.volumeInput,
            onValueChange = onVolumeChanged,
            unitSymbol = VolumeUnitRegistry.get(uiState.volumeUnit).symbol,
            onUnitClick = onPickVolumeUnit,
            imeAction = ImeAction.Done,
            onImeAction = onDone
        )

        TargetUnitSelectorRow(
            label = "Result Mass Unit:",
            unitSymbol = MassUnitRegistry.get(uiState.massUnit).symbol,
            unitName = MassUnitRegistry.get(uiState.massUnit).name,
            onClick = onPickTargetMassUnit
        )
    }
}

@Composable
private fun VolumeModeInputs(
    uiState: DensityBoxUiState,
    onMassChanged: (String) -> Unit,
    onDensityChanged: (String) -> Unit,
    onPickMassUnit: () -> Unit,
    onPickDensityUnit: () -> Unit,
    onPickTargetVolumeUnit: () -> Unit,
    onDone: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InputFieldWithUnit(
            label = "MASS",
            accessibilityDesc = "Mass value.",
            value = uiState.massInput,
            onValueChange = onMassChanged,
            unitSymbol = MassUnitRegistry.get(uiState.massUnit).symbol,
            onUnitClick = onPickMassUnit,
            imeAction = ImeAction.Next
        )

        InputFieldWithUnit(
            label = "DENSITY",
            accessibilityDesc = "Density value.",
            value = uiState.densityInput,
            onValueChange = onDensityChanged,
            unitSymbol = DensityUnitRegistry.get(uiState.densityUnit).symbol,
            onUnitClick = onPickDensityUnit,
            imeAction = ImeAction.Done,
            onImeAction = onDone
        )

        TargetUnitSelectorRow(
            label = "Result Volume Unit:",
            unitSymbol = VolumeUnitRegistry.get(uiState.volumeUnit).symbol,
            unitName = VolumeUnitRegistry.get(uiState.volumeUnit).name,
            onClick = onPickTargetVolumeUnit
        )
    }
}

@Composable
private fun InputFieldWithUnit(
    label: String,
    accessibilityDesc: String,
    value: String,
    onValueChange: (String) -> Unit,
    unitSymbol: String,
    onUnitClick: () -> Unit,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: () -> Unit = {}
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .semantics { contentDescription = accessibilityDesc },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(
                    onNext = { onImeAction() },
                    onDone = { onImeAction() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Surface(
                onClick = onUnitClick,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .height(56.dp)
                    .semantics { contentDescription = "Change unit from $unitSymbol" }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = unitSymbol,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TargetUnitSelectorRow(
    label: String,
    unitSymbol: String,
    unitName: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$unitName ($unitSymbol)",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select target unit",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun ResultCard(
    result: DensityBoxResult,
    decimalPlaces: Int,
    roundingMode: java.math.RoundingMode,
    onCopyValue: (String) -> Unit,
    onCopyAll: () -> Unit,
    onSave: () -> Unit,
    onShare: () -> Unit
) {
    val (label, formattedValue, unitSymbol, calculationString) = when (result) {
        is DensityBoxResult.DensityResult -> {
            val dStr = DensityBoxFormatter.formatNumber(result.density, decimalPlaces, roundingMode)
            val sym = DensityUnitRegistry.get(result.densityUnit).symbol
            val mStr = DensityBoxFormatter.formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)
            val vStr = DensityBoxFormatter.formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)
            Quad(
                "Density",
                "$dStr $sym",
                sym,
                "$mStr ÷ $vStr = $dStr $sym"
            )
        }
        is DensityBoxResult.MassResult -> {
            val mStr = DensityBoxFormatter.formatNumber(result.mass, decimalPlaces, roundingMode)
            val sym = MassUnitRegistry.get(result.massUnit).symbol
            val dStr = DensityBoxFormatter.formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)
            val vStr = DensityBoxFormatter.formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)
            Quad(
                "Mass",
                "$mStr $sym",
                sym,
                "$dStr × $vStr = $mStr $sym"
            )
        }
        is DensityBoxResult.VolumeResult -> {
            val vStr = DensityBoxFormatter.formatNumber(result.volume, decimalPlaces, roundingMode)
            val sym = VolumeUnitRegistry.get(result.volumeUnit).symbol
            val mStr = DensityBoxFormatter.formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)
            val dStr = DensityBoxFormatter.formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)
            Quad(
                "Volume",
                "$vStr $sym",
                sym,
                "$mStr ÷ $dStr = $vStr $sym"
            )
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RESULT",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Large result value + small copy icon beside
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formattedValue,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            contentDescription = "Calculated $label, $formattedValue"
                        }
                )

                IconButton(
                    onClick = { onCopyValue(formattedValue) },
                    modifier = Modifier.semantics { contentDescription = "Copy result value" }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Value",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sub calculation line
            Text(
                text = calculationString,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons: COPY, SAVE, SHARE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilledTonalButton(
                    onClick = onCopyAll,
                    modifier = Modifier
                        .weight(1f)
                        .semantics { contentDescription = "Copy calculation result." },
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 2.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "COPY",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .weight(1f)
                        .semantics { contentDescription = "Save calculation to history" },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 2.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "SAVE",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                OutlinedButton(
                    onClick = onShare,
                    modifier = Modifier
                        .weight(1f)
                        .semantics { contentDescription = "Share calculation" },
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 2.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "SHARE",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
private fun FormulaCard(formula: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "FORMULA",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = formula,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CalculationStepsCard(
    result: DensityBoxResult,
    decimalPlaces: Int,
    roundingMode: java.math.RoundingMode
) {
    val (step1, step2) = when (result) {
        is DensityBoxResult.DensityResult -> {
            val m = DensityBoxFormatter.formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)
            val v = DensityBoxFormatter.formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)
            val d = DensityBoxFormatter.formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)
            Pair("$m ÷ $v", "= $d")
        }
        is DensityBoxResult.MassResult -> {
            val d = DensityBoxFormatter.formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)
            val v = DensityBoxFormatter.formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)
            val m = DensityBoxFormatter.formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)
            Pair("$d × $v", "= $m")
        }
        is DensityBoxResult.VolumeResult -> {
            val m = DensityBoxFormatter.formatMass(result.mass, result.massUnit, decimalPlaces, roundingMode)
            val d = DensityBoxFormatter.formatDensity(result.density, result.densityUnit, decimalPlaces, roundingMode)
            val v = DensityBoxFormatter.formatVolume(result.volume, result.volumeUnit, decimalPlaces, roundingMode)
            Pair("$m ÷ $d", "= $v")
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "CALCULATION STEPS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = step1,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = step2,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private data class Quad(
    val label: String,
    val formattedValue: String,
    val unitSymbol: String,
    val calculationString: String
)
