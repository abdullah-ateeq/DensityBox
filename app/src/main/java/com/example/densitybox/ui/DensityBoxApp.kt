package com.example.densitybox.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.densitybox.ui.about.AboutScreen
import com.example.densitybox.ui.history.HistoryScreen
import com.example.densitybox.ui.home.HomeScreen
import com.example.densitybox.ui.navigation.Screen
import com.example.densitybox.ui.settings.SettingsScreen
import com.example.densitybox.ui.splash.SplashScreen
import com.example.densitybox.ui.theme.DensityBoxTheme
import com.example.densitybox.util.HapticFeedbackHelper

@Composable
fun DensityBoxApp(
    viewModel: DensityBoxViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    DensityBoxTheme(
        appTheme = uiState.theme,
        dynamicColor = uiState.dynamicColor
    ) {
        val showBottomBar = currentRoute != null && currentRoute != Screen.Splash.route

        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    NavigationBar {
                        Screen.bottomNavItems.forEach { screen ->
                            val isSelected = currentRoute == screen.route
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    if (currentRoute != screen.route) {
                                        HapticFeedbackHelper.performLightHaptic(context, uiState.hapticFeedback)
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    screen.icon?.let { icon ->
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = screen.title
                                        )
                                    }
                                },
                                label = { Text(screen.title) }
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onSplashFinished = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Home.route) {
                    HomeScreen(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }

                composable(Screen.History.route) {
                    HistoryScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onNavigateToHome = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }

                composable(Screen.About.route) {
                    AboutScreen()
                }
            }
        }
    }
}
