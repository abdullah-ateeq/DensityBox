package com.example.densitybox.data.model

import com.example.densitybox.domain.model.QuantityType

data class DensityBoxHistory(
    val id: String,
    val mode: QuantityType,
    val firstValue: String,
    val firstUnit: String,
    val secondValue: String,
    val secondUnit: String,
    val resultValue: String,
    val resultUnit: String,
    val calculatedAt: Long
)
