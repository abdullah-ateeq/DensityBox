package com.example.densitybox.domain.validator

import java.math.BigDecimal

object DensityBoxValidator {
    fun validateMass(mass: BigDecimal): String? {
        if (mass < BigDecimal.ZERO) {
            return "Mass must not be negative."
        }
        return null
    }

    fun validateVolume(volume: BigDecimal): String? {
        if (volume < BigDecimal.ZERO) {
            return "Volume must not be negative."
        }
        if (volume.compareTo(BigDecimal.ZERO) == 0) {
            return "Volume must be greater than zero."
        }
        return null
    }

    fun validateDensity(density: BigDecimal): String? {
        if (density < BigDecimal.ZERO) {
            return "Density must not be negative."
        }
        if (density.compareTo(BigDecimal.ZERO) == 0) {
            return "Density must be greater than zero."
        }
        return null
    }
}
