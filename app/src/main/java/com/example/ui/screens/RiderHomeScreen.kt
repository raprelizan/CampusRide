package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CampusData
import com.example.model.ActiveRide
import com.example.model.CampusLocation
import com.example.model.RideStatus
import com.example.model.VehicleType
import com.example.ui.components.ActiveRideCard
import com.example.ui.components.CampusLocationPickerModal
import com.example.ui.components.CampusMapView
import com.example.ui.components.ChatSheet
import com.example.ui.components.RatingTipDialog
import com.example.ui.components.RideBookingSheet
import com.example.ui.components.SplitFareDialog
import com.example.ui.theme.CampusAmber
import com.example.ui.theme.CampusCyan
import com.example.ui.theme.CampusEmerald
import com.example.ui.theme.CampusPurple
import com.example.ui.viewmodel.CampusViewModel
import java.util.Locale

@Composable
fun RiderHomeScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    val riderState by viewModel.riderState.collectAsState()
    val profileState by viewModel.profileState.collectAsState()

    var showPickupPicker by remember { mutableStateOf(false) }
    var showDropoffPicker by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(riderState.systemMessage) {
        val msg = riderState.systemMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSystemMessage()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090E17))
            .testTag("rider_home_screen")
    ) {
        // 1. Campus Live Vector Map
        CampusMapView(
            pickup = riderState.pickup,
            dropoff = riderState.dropoff,
            activeRide = riderState.activeRide,
            nearbyDrivers = CampusData.STUDENT_DRIVERS,
            onLocationTapped = { loc ->
                if (riderState.activeRide == null) {
                    if (loc.id != riderState.pickup.id) {
                        viewModel.setDropoff(loc)
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Top Header Bar (Edge-to-Edge Safe Area)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xD90F172A),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF28364F)),
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // University Brand + Student Verified Tag
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF003816)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = CampusEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CampusRide",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = CampusEmerald.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = ".EDU",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CampusEmerald,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = profileState.fullName + " • " + profileState.studentId,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }

                // Student Wallet Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E2838),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalAtm,
                            contentDescription = null,
                            tint = CampusEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$" + String.format(Locale.US, "%.2f", profileState.walletBalance),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 3. Bottom Card / Sheet (Booking Form OR Active Ride Card)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            if (riderState.activeRide == null) {
                RideBookingSheet(
                    pickup = riderState.pickup,
                    dropoff = riderState.dropoff,
                    selectedVehicle = riderState.selectedVehicle,
                    fareForVehicle = { type -> viewModel.calculateCurrentFare(type) },
                    studentDiscountApplied = riderState.studentDiscountApplied,
                    splitCount = riderState.splitCount,
                    onPickupClick = { showPickupPicker = true },
                    onDropoffClick = { showDropoffPicker = true },
                    onSwapClick = { viewModel.swapLocations() },
                    onSelectShortcut = { loc -> viewModel.setDropoff(loc) },
                    onVehicleSelect = { type -> viewModel.selectVehicle(type) },
                    onSplitFareClick = { viewModel.openSplitDialog(true) },
                    onRequestRideClick = { viewModel.requestRide() }
                )
            } else {
                ActiveRideCard(
                    activeRide = riderState.activeRide!!,
                    onOpenChat = { viewModel.openChat(true) },
                    onCancelRide = { viewModel.cancelRide() },
                    onOpenSafety = { viewModel.openSafetyDialog(true) },
                    onStartTrip = { viewModel.startTripFromRiderOrDriver() }
                )
            }
        }

        // Snackbar host for alerts
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 70.dp)
        )

        // 4. Modals and Dialogs
        if (showPickupPicker) {
            CampusLocationPickerModal(
                title = "Select Campus Pickup",
                selectedLocationId = riderState.pickup.id,
                onSelectLocation = { loc ->
                    viewModel.setPickup(loc)
                    showPickupPicker = false
                },
                onDismiss = { showPickupPicker = false }
            )
        }

        if (showDropoffPicker) {
            CampusLocationPickerModal(
                title = "Select Campus Destination",
                selectedLocationId = riderState.dropoff?.id,
                onSelectLocation = { loc ->
                    viewModel.setDropoff(loc)
                    showDropoffPicker = false
                },
                onDismiss = { showDropoffPicker = false }
            )
        }

        if (riderState.isChatOpen) {
            ChatSheet(
                driver = riderState.activeRide?.driver ?: CampusData.STUDENT_DRIVERS[0],
                messages = riderState.chatMessages,
                onSendMessage = { msg -> viewModel.sendRiderChatMessage(msg) },
                onDismiss = { viewModel.openChat(false) }
            )
        }

        if (riderState.isSplitDialogOpen) {
            SplitFareDialog(
                totalFare = viewModel.calculateCurrentFare(riderState.selectedVehicle),
                initialFriends = riderState.splitFriends,
                onApply = { friends -> viewModel.applySplitFare(friends) },
                onDismiss = { viewModel.openSplitDialog(false) }
            )
        }

        if (riderState.isRatingDialogOpen && riderState.lastCompletedRide != null) {
            RatingTipDialog(
                ride = riderState.lastCompletedRide!!,
                onSubmit = { stars, tip -> viewModel.submitRating(stars, tip) },
                onDismiss = { viewModel.dismissRatingDialog() }
            )
        }
    }
}
