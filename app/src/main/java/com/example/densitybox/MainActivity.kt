package com.example.densitybox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.densitybox.data.repository.DensityBoxHistoryRepository
import com.example.densitybox.data.repository.SettingsRepository
import com.example.densitybox.ui.DensityBoxApp
import com.example.densitybox.ui.DensityBoxViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DensityBoxViewModel by viewModels {
        val settingsRepo = SettingsRepository.create(applicationContext)
        val historyRepo = DensityBoxHistoryRepository.create(applicationContext)
        DensityBoxViewModel.provideFactory(settingsRepo, historyRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DensityBoxApp(viewModel = viewModel)
        }
    }
}
