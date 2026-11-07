package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CampusData
import com.example.model.CampusLocation
import com.example.model.VehicleType
import com.example.ui.theme.CampusAmber
import com.example.ui.theme.CampusCyan
import com.example.ui.theme.CampusEmerald
import com.example.ui.theme.CampusNavyDark
import com.example.ui.theme.CampusPurple
import java.util.Locale

@Composable
fun RideBookingSheet(
    pickup: CampusLocation,
    dropoff: CampusLocation?,
    selectedVehicle: VehicleType,
    fareForVehicle: (VehicleType) -> Double,
    studentDiscountApplied: Boolean,
    splitCount: Int,
    onPickupClick: () -> Unit,
    onDropoffClick: () -> Unit,
    onSwapClick: () -> Unit,
    onSelectShortcut: (CampusLocation) -> Unit,
    onVehicleSelect: (VehicleType) -> Unit,
    onSplitFareClick: () -> Unit,
    onRequestRideClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ride_booking_sheet"),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111927)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
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

            // 1. Pickup & Dropoff Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF1A2333),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2B384E))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Route Indicator Icons
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(CampusEmerald)
                        )
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(26.dp)
                                .background(Color(0xFF475569))
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(CampusCyan)
                        )
                    }

                    // Location Texts
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pickup
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPickupClick() }
                                .testTag("pickup_selector")
                        ) {
                            Text(
                                text = "PICKUP",
                                style = MaterialTheme.typography.labelSmall,
                                color = CampusEmerald,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                            Text(
                                text = pickup.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFF263345))
                        )

                        // Dropoff
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDropoffClick() }
                                .testTag("dropoff_selector")
                        ) {
                            Text(
                                text = "CAMPUS DESTINATION",
                                style = MaterialTheme.typography.labelSmall,
                                color = CampusCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                            Text(
                                text = dropoff?.name ?: "Where to on campus?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (dropoff != null) Color.White else Color(0xFF94A3B8),
                                fontWeight = if (dropoff != null) FontWeight.SemiBold else FontWeight.Normal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Swap Button
                    IconButton(
                        onClick = onSwapClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF243044))
                            .testTag("swap_locations_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap Locations",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Quick Campus Shortcuts Horizontal Row
            Text(
                text = "POPULAR CAMPUS STOPS",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(CampusData.CAMPUS_LOCATIONS.filter { it.isPopular }) { loc ->
                    val isCurrent = loc.id == dropoff?.id
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isCurrent) CampusCyan.copy(alpha = 0.2f) else Color(0xFF1E293B),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCurrent) CampusCyan else Color(0xFF334155)
                        ),
                        modifier = Modifier.clickable { onSelectShortcut(loc) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = if (isCurrent) CampusCyan else Color(0xFF94A3B8),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = loc.buildingCode,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isCurrent) CampusCyan else Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Vehicle Tiers Carousel
            Text(
                text = "CHOOSE CAMPUS RIDE TIER",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(VehicleType.values()) { type ->
                    val isSelected = type == selectedVehicle
                    val fare = fareForVehicle(type)

                    Card(
                        modifier = Modifier
                            .width(165.dp)
                            .clickable { onVehicleSelect(type) }
                            .testTag("tier_${type.tierId}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF1D3228) else Color(0xFF172033)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (isSelected) CampusEmerald else Color(0xFF28364F)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = when (type) {
                                        VehicleType.CAMPUS_POOL -> Icons.Default.Group
                                        VehicleType.CAMPUS_ECO -> Icons.Default.DirectionsCar
                                        VehicleType.STUDY_COMFORT -> Icons.Default.School
                                        VehicleType.CAMPUS_SAFE_RIDE -> Icons.Default.Security
                                        VehicleType.CAMPUS_VAN_XL -> Icons.Default.CarRental
                                    },
                                    contentDescription = null,
                                    tint = if (type == VehicleType.CAMPUS_SAFE_RIDE) CampusPurple else CampusEmerald,
                                    modifier = Modifier.size(24.dp)
                                )

                                if (type.badge != null) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (type.isNightFreeEligible) CampusPurple.copy(alpha = 0.25f) else CampusEmerald.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = type.badge,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (type.isNightFreeEligible) CampusPurple else CampusEmerald,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = type.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = type.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.height(30.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (fare == 0.0) "FREE" else "$" + String.format(Locale.US, "%.2f", fare),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (fare == 0.0) CampusPurple else CampusEmerald,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Text(
                                    text = "${type.capacity} seats",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF64748B),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Student Discount & Split Fare Banner
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF1E2838),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = CampusAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "15% Student Discount Active",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Verified .edu University Account",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF94A3B8),
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Split fare chip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF28364F),
                        modifier = Modifier.clickable { onSplitFareClick() }
                    ) {
                        Text(
                            text = if (splitCount > 1) "Split ($splitCount ways)" else "Split Fare",
                            style = MaterialTheme.typography.labelSmall,
                            color = CampusCyan,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Primary CTA: Request Campus Ride
            val currentFare = fareForVehicle(selectedVehicle)
            val displayFare = if (splitCount > 1) currentFare / splitCount else currentFare

            Button(
                onClick = onRequestRideClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("request_ride_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedVehicle == VehicleType.CAMPUS_SAFE_RIDE) CampusPurple else CampusEmerald,
                    contentColor = Color(0xFF00240E)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Request ${selectedVehicle.title}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedVehicle == VehicleType.CAMPUS_SAFE_RIDE) Color.White else Color(0xFF00240E)
                    )
                    Text(
                        text = if (displayFare == 0.0) "FREE (Night Patrol)" else "$" + String.format(Locale.US, "%.2f", displayFare) + (if (splitCount > 1) " each" else ""),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (selectedVehicle == VehicleType.CAMPUS_SAFE_RIDE) Color.White else Color(0xFF00240E)
                    )
                }
            }
        }
    }
}
