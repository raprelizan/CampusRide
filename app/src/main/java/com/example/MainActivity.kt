package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.CampusData
import com.example.model.VehicleType
import com.example.ui.screens.ActivityHistoryScreen
import com.example.ui.screens.CampusSafeRideScreen
import com.example.ui.screens.DriverModeScreen
import com.example.ui.screens.RiderHomeScreen
import com.example.ui.screens.WalletProfileScreen
import com.example.ui.theme.CampusCyan
import com.example.ui.theme.CampusEmerald
import com.example.ui.theme.CampusNavyDark
import com.example.ui.theme.CampusPurple
import com.example.ui.theme.CampusRideTheme
import com.example.ui.viewmodel.CampusViewModel

enum class MainNavTab(
    val title: String,
    val icon: ImageVector,
    val testTag: String
) {
    RIDE("Ride", Icons.Default.DirectionsCar, "tab_ride"),
    ACTIVITY("Trips", Icons.Default.History, "tab_activity"),
    DRIVER("Drive", Icons.Default.Speed, "tab_driver"),
    SAFERIDE("SafeRide", Icons.Default.Security, "tab_saferide"),
    WALLET("Student ID", Icons.Default.AccountBalance, "tab_wallet")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusRideTheme {
                CampusRideApp()
            }
        }
    }
}

@Composable
fun CampusRideApp(
    campusViewModel: CampusViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(MainNavTab.RIDE) }

    // If on a secondary tab, pressing Back returns to RIDE tab
    if (selectedTab != MainNavTab.RIDE) {
        BackHandler {
            selectedTab = MainNavTab.RIDE
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Surface(
                color = Color(0xFF0C1320),
                tonalElevation = 10.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav")
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    MainNavTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 10.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF00240E),
                                selectedTextColor = CampusEmerald,
                                indicatorColor = if (tab == MainNavTab.SAFERIDE) CampusPurple else CampusEmerald,
                                unselectedIconColor = Color(0xFF64748B),
                                unselectedTextColor = Color(0xFF64748B)
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (selectedTab) {
                MainNavTab.RIDE -> {
                    RiderHomeScreen(
                        viewModel = campusViewModel
                    )
                }

                MainNavTab.ACTIVITY -> {
                    ActivityHistoryScreen(
                        viewModel = campusViewModel,
                        onRebookTrip = { item ->
                            val dropoffLoc = CampusData.CAMPUS_LOCATIONS.firstOrNull { it.name == item.dropoffName }
                                ?: CampusData.CAMPUS_LOCATIONS[1]
                            campusViewModel.setDropoff(dropoffLoc)
                            selectedTab = MainNavTab.RIDE
                        }
                    )
                }

                MainNavTab.DRIVER -> {
                    DriverModeScreen(
                        viewModel = campusViewModel
                    )
                }

                MainNavTab.SAFERIDE -> {
                    CampusSafeRideScreen(
                        viewModel = campusViewModel,
                        onRequestSafeRide = {
                            selectedTab = MainNavTab.RIDE
                            campusViewModel.requestRide()
                        }
                    )
                }

                MainNavTab.WALLET -> {
                    WalletProfileScreen(
                        viewModel = campusViewModel
                    )
                }
            }
        }
    }
}
