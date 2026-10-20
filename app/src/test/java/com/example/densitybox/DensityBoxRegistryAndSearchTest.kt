package com.example.densitybox

import com.example.densitybox.domain.model.DensityUnit
import com.example.densitybox.domain.model.MassUnit
import com.example.densitybox.domain.model.VolumeUnit
import com.example.densitybox.domain.registry.DensityUnitRegistry
import com.example.densitybox.domain.registry.MassUnitRegistry
import com.example.densitybox.domain.registry.VolumeUnitRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DensityBoxRegistryAndSearchTest {

    // 52. Unit search
    @Test
    fun test52_unitNameSearch() {
        // Search "gram" finds Gram and Milligram
        val massSearch = MassUnitRegistry.search("gram")
        assertTrue(massSearch.any { it.unit == MassUnit.GRAM })
        assertTrue(massSearch.any { it.unit == MassUnit.MILLIGRAM })

        // Search "liter" in volume finds Liter and Milliliter
        val volumeSearch = VolumeUnitRegistry.search("liter")
        assertTrue(volumeSearch.any { it.unit == VolumeUnit.LITER })
        assertTrue(volumeSearch.any { it.unit == VolumeUnit.MILLILITER })

        // Search "liter" in density finds Kilogram per liter and Gram per milliliter
        val densitySearch = DensityUnitRegistry.search("liter")
        assertTrue(densitySearch.any { it.unit == DensityUnit.KILOGRAM_PER_LITER })
        assertTrue(densitySearch.any { it.unit == DensityUnit.GRAM_PER_MILLILITER })
    }

    // 53. Unit symbol search
    @Test
    fun test53_unitSymbolSearch() {
        // Search "kg" in mass finds Kilogram
        val massSearch = MassUnitRegistry.search("kg")
        assertEquals(1, massSearch.size)
        assertEquals(MassUnit.KILOGRAM, massSearch.first().unit)

        // Search "kg" in density finds kg/m³ and kg/L
        val densitySearch = DensityUnitRegistry.search("kg")
        assertTrue(densitySearch.any { it.unit == DensityUnit.KILOGRAM_PER_CUBIC_METER })
        assertTrue(densitySearch.any { it.unit == DensityUnit.KILOGRAM_PER_LITER })

        // Search "cm³" finds Cubic Centimeter
        val volumeSearch = VolumeUnitRegistry.search("cm³")
        assertTrue(volumeSearch.any { it.unit == VolumeUnit.CUBIC_CENTIMETER })
    }

    @Test
    fun test_allRegistriesComplete() {
        assertEquals(6, MassUnitRegistry.all.size)
        assertEquals(9, VolumeUnitRegistry.all.size)
        assertEquals(7, DensityUnitRegistry.all.size)

        MassUnit.entries.forEach { unit ->
            assertNotNull(MassUnitRegistry.get(unit))
        }

        VolumeUnit.entries.forEach { unit ->
            assertNotNull(VolumeUnitRegistry.get(unit))
        }

        DensityUnit.entries.forEach { unit ->
            assertNotNull(DensityUnitRegistry.get(unit))
        }
    }
}
