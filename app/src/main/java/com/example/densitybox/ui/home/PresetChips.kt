package com.example.densitybox.ui.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.QuantityType
import com.example.densitybox.domain.model.VolumeUnit

@Composable
fun PresetChips(
    mode: QuantityType,
    onMassPreset: (String, MassUnit) -> Unit,
    onVolumePreset: (String, VolumeUnit) -> Unit,
    onDensityPreset: (String, DensityUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "QUICK PRESETS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (mode) {
                QuantityType.DENSITY -> {
                    // Presets for Mass & Volume
                    PresetChip(label = "Mass: 100 g") { onMassPreset("100", MassUnit.GRAM) }
                    PresetChip(label = "Mass: 500 g") { onMassPreset("500", MassUnit.GRAM) }
                    PresetChip(label = "Mass: 1 kg") { onMassPreset("1", MassUnit.KILOGRAM) }
                    PresetChip(label = "Mass: 5 kg") { onMassPreset("5", MassUnit.KILOGRAM) }

                    PresetChip(label = "Vol: 10 mL") { onVolumePreset("10", VolumeUnit.MILLILITER) }
                    PresetChip(label = "Vol: 50 cm³") { onVolumePreset("50", VolumeUnit.CUBIC_CENTIMETER) }
                    PresetChip(label = "Vol: 100 mL") { onVolumePreset("100", VolumeUnit.MILLILITER) }
                    PresetChip(label = "Vol: 500 mL") { onVolumePreset("500", VolumeUnit.MILLILITER) }
                    PresetChip(label = "Vol: 1 L") { onVolumePreset("1", VolumeUnit.LITER) }
                }
                QuantityType.MASS -> {
                    // Presets for Density & Volume
                    PresetChip(label = "Density: 1 g/cm³") { onDensityPreset("1", DensityUnit.GRAM_PER_CUBIC_CENTIMETER) }
                    PresetChip(label = "Density: 2 g/cm³") { onDensityPreset("2", DensityUnit.GRAM_PER_CUBIC_CENTIMETER) }
                    PresetChip(label = "Density: 7.85 g/cm³ (Steel)") { onDensityPreset("7.85", DensityUnit.GRAM_PER_CUBIC_CENTIMETER) }
                    PresetChip(label = "Density: 10 g/cm³") { onDensityPreset("10", DensityUnit.GRAM_PER_CUBIC_CENTIMETER) }

                    PresetChip(label = "Vol: 50 cm³") { onVolumePreset("50", VolumeUnit.CUBIC_CENTIMETER) }
                    PresetChip(label = "Vol: 100 mL") { onVolumePreset("100", VolumeUnit.MILLILITER) }
                    PresetChip(label = "Vol: 1 L") { onVolumePreset("1", VolumeUnit.LITER) }
                }
                QuantityType.VOLUME -> {
                    // Presets for Mass & Density
                    PresetChip(label = "Mass: 100 g") { onMassPreset("100", MassUnit.GRAM) }
                    PresetChip(label = "Mass: 500 g") { onMassPreset("500", MassUnit.GRAM) }
                    PresetChip(label = "Mass: 1 kg") { onMassPreset("1", MassUnit.KILOGRAM) }

                    PresetChip(label = "Density: 1 g/cm³") { onDensityPreset("1", DensityUnit.GRAM_PER_CUBIC_CENTIMETER) }
                    PresetChip(label = "Density: 2 g/cm³") { onDensityPreset("2", DensityUnit.GRAM_PER_CUBIC_CENTIMETER) }
                    PresetChip(label = "Density: 7.85 g/cm³ (Steel)") { onDensityPreset("7.85", DensityUnit.GRAM_PER_CUBIC_CENTIMETER) }
                    PresetChip(label = "Density: 10 g/cm³") { onDensityPreset("10", DensityUnit.GRAM_PER_CUBIC_CENTIMETER) }
                }
            }
        }
    }
}

@Composable
private fun PresetChip(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        )
    }
}
