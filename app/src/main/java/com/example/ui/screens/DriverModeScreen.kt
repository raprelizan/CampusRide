package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DriverOffer
import com.example.model.RideStatus
import com.example.ui.theme.CampusAmber
import com.example.ui.theme.CampusCrimson
import com.example.ui.theme.CampusCyan
import com.example.ui.theme.CampusEmerald
import com.example.ui.viewmodel.CampusViewModel
import java.util.Locale

@Composable
fun DriverModeScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    val driverState by viewModel.driverState.collectAsState()
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulseDriver")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driverPulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090E17))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(scrollState)
            .testTag("driver_mode_screen")
    ) {
        // 1. Header with Title & Online Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Student Driver Hub",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = CampusEmerald.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "EARN ON CAMPUS",
                            style = MaterialTheme.typography.labelSmall,
                            color = CampusEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "Give campus rides between your classes",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
            }

            // Online / Offline Switch
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E2838))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            if (driverState.isOnline) CampusEmerald.copy(alpha = pulseAlpha)
                            else Color(0xFF64748B)
                        )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (driverState.isOnline) "ONLINE" else "OFFLINE",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (driverState.isOnline) CampusEmerald else Color(0xFF94A3B8),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = driverState.isOnline,
                    onCheckedChange = { viewModel.toggleDriverOnline() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF00240E),
                        checkedTrackColor = CampusEmerald,
                        uncheckedThumbColor = Color(0xFF94A3B8),
                        uncheckedTrackColor = Color(0xFF334155)
                    ),
                    modifier = Modifier.testTag("driver_online_toggle")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Today's Earnings Card
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111927)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF28364F)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TODAY'S STUDENT EARNINGS",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1E2D22),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CampusEmerald)
                    ) {
                        Text(
                            text = "+18% vs Yesterday",
                            style = MaterialTheme.typography.labelSmall,
                            color = CampusEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$" + String.format(Locale.US, "%.2f", driverState.todayEarnings),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Trips completed
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF172033),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "TRIPS",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${driverState.completedTripsCount}",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Hours online
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF172033),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "ONLINE",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${driverState.hoursOnline}h",
                                style = MaterialTheme.typography.titleMedium,
                                color = CampusCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Rating
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF172033),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "RATING",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = CampusAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${driverState.rating}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Status Section: Either Offline, Waiting for Requests, Incoming Offer, or Active Trip
        if (!driverState.isOnline) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF141D2C),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "You are currently Offline",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Flip the switch above to go online and receive student ride requests on campus.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                    )
                    Button(
                        onClick = { viewModel.toggleDriverOnline() },
                        colors = ButtonDefaults.buttonColors(containerColor = CampusEmerald),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Go Online Now", color = Color(0xFF00240E), fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else if (driverState.activeTrip != null) {
            // Driver is in active trip
            val trip = driverState.activeTrip!!
            val isArrived = trip.status == RideStatus.DRIVER_ARRIVED
            val isInProgress = trip.status == RideStatus.IN_PROGRESS

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF132338)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CampusCyan),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isInProgress) "IN TRANSIT TO DROPOFF" else if (isArrived) "ARRIVED AT PICKUP" else "NAVIGATE TO PICKUP",
                            style = MaterialTheme.typography.labelSmall,
                            color = CampusCyan,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "+$" + String.format(Locale.US, "%.2f", trip.fare),
                            style = MaterialTheme.typography.titleMedium,
                            color = CampusEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isInProgress) trip.dropoff.name else trip.pickup.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isInProgress) trip.dropoff.address else trip.pickup.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (!isArrived && !isInProgress) {
                        Button(
                            onClick = { viewModel.driverMarkArrived() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("driver_arrived_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CampusCyan),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null, tint = Color(0xFF002836))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Tap when Arrived at Pickup", color = Color(0xFF002836), fontWeight = FontWeight.Bold)
                        }
                    } else if (isArrived && !isInProgress) {
                        // PIN verification field
                        Text(
                            text = "Ask rider for their 4-digit Safety PIN:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = driverState.enteredPin,
                                onValueChange = { viewModel.updateDriverPin(it) },
                                placeholder = { Text("4-digit PIN (e.g. 3814)", color = Color(0xFF64748B)) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("driver_pin_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = CampusEmerald,
                                    unfocusedBorderColor = Color(0xFF334155),
                                    focusedContainerColor = Color(0xFF1E2838),
                                    unfocusedContainerColor = Color(0xFF1E2838)
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.driverVerifyPinAndStart() },
                                colors = ButtonDefaults.buttonColors(containerColor = CampusEmerald),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .height(52.dp)
                                    .testTag("verify_pin_button")
                            ) {
                                Text(text = "Start Trip", color = Color(0xFF00240E), fontWeight = FontWeight.Bold)
                            }
                        }

                        if (driverState.pinError != null) {
                            Text(
                                text = driverState.pinError!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = CampusCrimson,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    } else if (isInProgress) {
                        Button(
                            onClick = { viewModel.driverCompleteActiveTrip() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("driver_complete_trip_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CampusEmerald),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00240E))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Complete Trip & Earn $${String.format(Locale.US, "%.2f", trip.fare)}",
                                color = Color(0xFF00240E),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        } else if (driverState.currentOffer != null) {
            // Live Incoming Ride Offer
            val offer = driverState.currentOffer!!
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3022)),
                border = androidx.compose.foundation.BorderStroke(2.dp, CampusEmerald),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("incoming_offer_card")
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CampusEmerald
                        ) {
                            Text(
                                text = "NEW RIDE REQUEST",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF00240E),
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = "$${String.format(Locale.US, "%.2f", offer.estimatedEarnings)}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = offer.riderName,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = offer.riderMajor,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF14241B),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "FROM: ${offer.pickup.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CampusEmerald,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "TO: ${offer.dropoff.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CampusCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${offer.distanceKm} km • approx ${offer.etaMinutes} mins",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF94A3B8),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.driverDeclineOffer() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("decline_offer_button"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8)),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569))
                        ) {
                            Text(text = "Decline", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.driverAcceptOffer() },
                            modifier = Modifier
                                .weight(1.4f)
                                .height(48.dp)
                                .testTag("accept_offer_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CampusEmerald),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Accept Ride",
                                color = Color(0xFF00240E),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        } else {
            // Online and waiting for requests
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF131D2E),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(CampusEmerald.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = CampusEmerald,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Searching Campus Sectors...",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "You're prioritized for rides originating near campus dorms and libraries. New requests will chime automatically.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
