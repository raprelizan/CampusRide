package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActiveRide
import com.example.model.RideStatus
import com.example.ui.theme.CampusAmber
import com.example.ui.theme.CampusCrimson
import com.example.ui.theme.CampusCyan
import com.example.ui.theme.CampusEmerald
import com.example.ui.theme.CampusPurple
import java.util.Locale

@Composable
fun ActiveRideCard(
    activeRide: ActiveRide,
    onOpenChat: () -> Unit,
    onCancelRide: () -> Unit,
    onOpenSafety: () -> Unit,
    onStartTrip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("active_ride_card"),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111927)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Drag handle
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF334155))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (activeRide.status) {
                RideStatus.SEARCHING_DRIVER -> {
                    SearchingDriverView(onCancelRide = onCancelRide)
                }

                RideStatus.DRIVER_ON_WAY, RideStatus.DRIVER_ARRIVED -> {
                    DriverAssignedView(
                        activeRide = activeRide,
                        onOpenChat = onOpenChat,
                        onOpenSafety = onOpenSafety,
                        onCancelRide = onCancelRide,
                        onStartTrip = onStartTrip
                    )
                }

                RideStatus.IN_PROGRESS -> {
                    TripInProgressView(
                        activeRide = activeRide,
                        onOpenSafety = onOpenSafety
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun SearchingDriverView(onCancelRide: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val radarScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radarScale"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(110.dp)
                .scale(radarScale)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(CampusEmerald.copy(alpha = 0.15f))
            )
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(CampusEmerald.copy(alpha = 0.3f))
            )
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(CampusEmerald)
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Color(0xFF00240E),
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Searching Nearby Student Drivers...",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Matching with verified college drivers in your campus sector",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        OutlinedButton(
            onClick = onCancelRide,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = CampusCrimson
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, CampusCrimson.copy(alpha = 0.5f)),
            modifier = Modifier.testTag("cancel_search_button")
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Cancel Request", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun DriverAssignedView(
    activeRide: ActiveRide,
    onOpenChat: () -> Unit,
    onOpenSafety: () -> Unit,
    onCancelRide: () -> Unit,
    onStartTrip: () -> Unit
) {
    val driver = activeRide.driver ?: return
    val isArrived = activeRide.status == RideStatus.DRIVER_ARRIVED

    Column(modifier = Modifier.fillMaxWidth()) {
        // Status & ETA Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isArrived) "DRIVER HAS ARRIVED! 🚗" else "DRIVER ON THE WAY",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isArrived) CampusEmerald else CampusCyan,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = if (isArrived) "Waiting outside ${activeRide.pickup.buildingCode}" else "Arriving in approx ${activeRide.etaMinutes} mins",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )
            }

            // PIN CODE Verification Box
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF1E2D22),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CampusEmerald)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SAFETY PIN",
                        style = MaterialTheme.typography.labelSmall,
                        color = CampusEmerald,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = activeRide.pinCode,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Driver Profile Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1A2333),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF28364F))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Driver Avatar
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF003816))
                        .border(2.dp, CampusEmerald, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = driver.initials,
                        style = MaterialTheme.typography.titleMedium,
                        color = CampusEmerald,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Driver Info
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = driver.name,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Student",
                            tint = CampusAmber,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "${driver.major} • ${driver.totalCampusTrips} campus trips",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = CampusAmber,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${driver.rating}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Car Plate & Model
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF0F172A),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569))
                    ) {
                        Text(
                            text = driver.plateNumber,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = driver.carModel,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row: Chat, Call, Safety, Cancel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onOpenChat,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF223045)
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("driver_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = CampusCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Chat", color = Color.White, fontSize = 13.sp)
            }

            Button(
                onClick = onOpenSafety,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B1D28)
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("safety_sos_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = CampusCrimson,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Safety", color = Color.White, fontSize = 13.sp)
            }

            OutlinedButton(
                onClick = onCancelRide,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CampusCrimson
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, CampusCrimson.copy(alpha = 0.4f)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("cancel_ride_button")
            ) {
                Text(text = "Cancel", fontSize = 13.sp)
            }
        }

        if (isArrived) {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onStartTrip,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("start_trip_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CampusEmerald,
                    contentColor = Color(0xFF00240E)
                )
            ) {
                Text(
                    text = "I'm in the Car • Start Trip",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun TripInProgressView(
    activeRide: ActiveRide,
    onOpenSafety: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "TRIP IN PROGRESS 🎓",
                    style = MaterialTheme.typography.labelSmall,
                    color = CampusEmerald,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Heading to ${activeRide.dropoff.name}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF1E293B)
            ) {
                Text(
                    text = "${activeRide.etaMinutes} min left",
                    style = MaterialTheme.typography.labelMedium,
                    color = CampusCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = { activeRide.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = CampusEmerald,
            trackColor = Color(0xFF28364F)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total: $${String.format(Locale.US, "%.2f", activeRide.fare / activeRide.splitCount.coerceAtLeast(1))}" + (if (activeRide.splitCount > 1) " (Split ${activeRide.splitCount} ways)" else ""),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF94A3B8),
                fontSize = 12.sp
            )

            Button(
                onClick = onOpenSafety,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B1D28)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = CampusCrimson,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Live Safety SOS", color = Color.White, fontSize = 11.sp)
            }
        }
    }
}
