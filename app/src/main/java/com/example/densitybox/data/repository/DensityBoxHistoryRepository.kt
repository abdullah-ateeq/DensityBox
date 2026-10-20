package com.example.densitybox.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.densitybox.data.model.DensityBoxHistory
import com.example.densitybox.domain.model.QuantityType

class DensityBoxHistoryRepository(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val PREF_NAME = "density_box_history_prefs"
        private const val KEY_HISTORY = "history_entries"
        const val MAX_HISTORY_ITEMS = 10

        fun create(context: Context): DensityBoxHistoryRepository {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return DensityBoxHistoryRepository(prefs)
        }
    }

    @Synchronized
    fun getHistory(): List<DensityBoxHistory> {
        val raw = sharedPreferences.getString(KEY_HISTORY, null) ?: return emptyList()
        return deserialize(raw)
    }

    @Synchronized
    fun addEntry(entry: DensityBoxHistory): Boolean {
        val current = getHistory().toMutableList()
        if (current.isNotEmpty()) {
            val latest = current.first()
            if (isSameCalculation(latest, entry)) {
                return false
            }
        }
        current.add(0, entry)
        while (current.size > MAX_HISTORY_ITEMS) {
            current.removeAt(current.size - 1)
        }
        saveHistory(current)
        return true
    }

    @Synchronized
    fun deleteEntry(id: String) {
        val current = getHistory().filterNot { it.id == id }
        saveHistory(current)
    }

    @Synchronized
    fun clearHistory() {
        sharedPreferences.edit().remove(KEY_HISTORY).apply()
    }

    private fun isSameCalculation(a: DensityBoxHistory, b: DensityBoxHistory): Boolean {
        return a.mode == b.mode &&
            a.firstValue == b.firstValue &&
            a.firstUnit == b.firstUnit &&
            a.secondValue == b.secondValue &&
            a.secondUnit == b.secondUnit &&
            a.resultValue == b.resultValue &&
            a.resultUnit == b.resultUnit
    }

    private fun saveHistory(list: List<DensityBoxHistory>) {
        val serialized = serialize(list)
        sharedPreferences.edit().putString(KEY_HISTORY, serialized).apply()
    }

    internal fun serialize(list: List<DensityBoxHistory>): String {
        return list.joinToString("\n") { item ->
            listOf(
                item.id,
                item.mode.name,
                item.firstValue,
                item.firstUnit,
                item.secondValue,
                item.secondUnit,
                item.resultValue,
                item.resultUnit,
                item.calculatedAt.toString()
            ).joinToString("\t")
        }
    }

    internal fun deserialize(raw: String): List<DensityBoxHistory> {
        if (raw.isBlank()) return emptyList()
        return raw.lines().filter { it.isNotBlank() }.mapNotNull { line ->
            val parts = line.split("\t")
            if (parts.size >= 9) {
                try {
                    DensityBoxHistory(
                        id = parts[0],
                        mode = QuantityType.valueOf(parts[1]),
                        firstValue = parts[2],
                        firstUnit = parts[3],
                        secondValue = parts[4],
                        secondUnit = parts[5],
                        resultValue = parts[6],
                        resultUnit = parts[7],
                        calculatedAt = parts[8].toLongOrNull() ?: 0L
                    )
                } catch (_: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }
}
