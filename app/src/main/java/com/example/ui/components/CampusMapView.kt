package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CampusData
import com.example.model.ActiveRide
import com.example.model.CampusLocation
import com.example.model.CampusPoint
import com.example.model.RideStatus
import com.example.model.StudentDriver
import com.example.ui.theme.CampusCyan
import com.example.ui.theme.CampusEmerald
import com.example.ui.theme.CampusNavyDark
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun CampusMapView(
    pickup: CampusLocation,
    dropoff: CampusLocation?,
    activeRide: ActiveRide?,
    nearbyDrivers: List<StudentDriver>,
    modifier: Modifier = Modifier,
    onLocationTapped: (CampusLocation) -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 32f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    val carBob by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "carBob"
    )

    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier.testTag("campus_map_view")) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val tapNormX = tapOffset.x / size.width
                        val tapNormY = tapOffset.y / size.height

                        // Find closest campus location
                        val closest = CampusData.CAMPUS_LOCATIONS.minByOrNull { loc ->
                            hypot(loc.point.x - tapNormX, loc.point.y - tapNormY)
                        }
                        if (closest != null) {
                            val dist = hypot(closest.point.x - tapNormX, closest.point.y - tapNormY)
                            if (dist < 0.12f) {
                                onLocationTapped(closest)
                            }
                        }
                    }
                }
        ) {
            val canvasW = size.width
            val canvasH = size.height

            // 1. Campus Background (Deep Midnight Tech Slate)
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF131D2E), Color(0xFF090E17)),
                    center = Offset(canvasW * 0.5f, canvasH * 0.5f),
                    radius = canvasW.coerceAtLeast(canvasH)
                )
            )

            // 2. Campus Lawns, Botanical Quads, and Lake
            drawCampusFeatures(canvasW, canvasH)

            // 3. Campus Roads & Pedestrian Walkways
            drawCampusRoads(canvasW, canvasH)

            // 4. University Building Footprints
            drawCampusBuildings(canvasW, canvasH)

            // 5. Active Route Polyline
            val targetDropoff = dropoff ?: (if (activeRide != null) activeRide.dropoff else null)
            if (targetDropoff != null) {
                drawRoutePolyline(
                    p1 = pickup.point,
                    p2 = targetDropoff.point,
                    canvasW = canvasW,
                    canvasH = canvasH,
                    progress = activeRide?.progress ?: 0f
                )
            }

            // 6. Nearby Available Student Drivers
            nearbyDrivers.forEach { driver ->
                val drvX = driver.position.x * canvasW
                val drvY = driver.position.y * canvasH

                // Car marker circle with mini vehicle glyph
                drawCircle(
                    color = Color(0xFF1E293B),
                    radius = 16f,
                    center = Offset(drvX, drvY)
                )
                drawCircle(
                    color = CampusEmerald,
                    radius = 14f,
                    center = Offset(drvX, drvY),
                    style = Stroke(width = 3f)
                )
                drawCircle(
                    color = Color.White,
                    radius = 6f,
                    center = Offset(drvX, drvY)
                )
            }

            // 7. Active Live Car along the Route (if Ride is active)
            if (activeRide != null && (activeRide.status == RideStatus.DRIVER_ON_WAY || activeRide.status == RideStatus.IN_PROGRESS)) {
                val pStart = if (activeRide.status == RideStatus.DRIVER_ON_WAY) {
                    activeRide.driver?.position ?: CampusPoint(pickup.point.x - 0.1f, pickup.point.y + 0.1f)
                } else {
                    pickup.point
                }
                val pEnd = if (activeRide.status == RideStatus.DRIVER_ON_WAY) pickup.point else activeRide.dropoff.point

                val currentProg = if (activeRide.status == RideStatus.DRIVER_ON_WAY) 0.6f else activeRide.progress
                val carX = (pStart.x + (pEnd.x - pStart.x) * currentProg) * canvasW
                val carY = (pStart.y + (pEnd.y - pStart.y) * currentProg) * canvasH + carBob

                // Glowing Car Badge
                drawCircle(
                    color = CampusEmerald.copy(alpha = 0.35f),
                    radius = 26f,
                    center = Offset(carX, carY)
                )
                drawCircle(
                    color = Color(0xFF0F172A),
                    radius = 18f,
                    center = Offset(carX, carY)
                )
                drawCircle(
                    color = CampusEmerald,
                    radius = 18f,
                    center = Offset(carX, carY),
                    style = Stroke(width = 4f)
                )
                drawCircle(
                    color = Color.White,
                    radius = 7f,
                    center = Offset(carX, carY)
                )
            }

            // 8. Pickup Marker (Emerald Pin with Pulse)
            val pickX = pickup.point.x * canvasW
            val pickY = pickup.point.y * canvasH

            // Pulse Ring
            drawCircle(
                color = CampusEmerald.copy(alpha = pulseAlpha),
                radius = pulseRadius,
                center = Offset(pickX, pickY)
            )
            // Outer Halo
            drawCircle(
                color = Color(0xFF0F172A),
                radius = 16f,
                center = Offset(pickX, pickY)
            )
            // Core Emerald Pin
            drawCircle(
                color = CampusEmerald,
                radius = 12f,
                center = Offset(pickX, pickY)
            )
            drawCircle(
                color = Color.White,
                radius = 5f,
                center = Offset(pickX, pickY)
            )

            // 9. Dropoff Marker (Cyan Pin with Target)
            if (targetDropoff != null) {
                val dropX = targetDropoff.point.x * canvasW
                val dropY = targetDropoff.point.y * canvasH

                drawCircle(
                    color = CampusCyan.copy(alpha = 0.25f),
                    radius = 20f,
                    center = Offset(dropX, dropY)
                )
                drawCircle(
                    color = Color(0xFF0F172A),
                    radius = 16f,
                    center = Offset(dropX, dropY)
                )
                drawCircle(
                    color = CampusCyan,
                    radius = 12f,
                    center = Offset(dropX, dropY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = Offset(dropX, dropY)
                )
            }

            // 10. Draw Campus Building Names / Codes
            CampusData.CAMPUS_LOCATIONS.forEach { loc ->
                val lx = loc.point.x * canvasW
                val ly = loc.point.y * canvasH
                val isSelected = loc.id == pickup.id || loc.id == targetDropoff?.id

                val label = loc.buildingCode
                val measured = textMeasurer.measure(
                    text = label,
                    style = TextStyle(
                        color = if (isSelected) Color.White else Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                )

                val labelX = lx - (measured.size.width / 2f)
                val labelY = ly + 14f

                // Small pill background for readability
                drawRoundRect(
                    color = if (isSelected) Color(0xFF0F172A) else Color(0x991E293B),
                    topLeft = Offset(labelX - 8f, labelY - 3f),
                    size = Size(measured.size.width + 16f, measured.size.height + 6f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                if (isSelected) {
                    drawRoundRect(
                        color = if (loc.id == pickup.id) CampusEmerald else CampusCyan,
                        topLeft = Offset(labelX - 8f, labelY - 3f),
                        size = Size(measured.size.width + 16f, measured.size.height + 6f),
                        cornerRadius = CornerRadius(8f, 8f),
                        style = Stroke(width = 2f)
                    )
                }

                drawText(
                    textMeasurer = textMeasurer,
                    text = label,
                    topLeft = Offset(labelX, labelY),
                    style = TextStyle(
                        color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                )
            }
        }

        // Campus Map Floating Overlays (Campus Legend Badge)
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xCC090E17),
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                Text(
                    text = "CAMPUS LIVE MAP",
                    style = MaterialTheme.typography.labelSmall,
                    color = CampusEmerald,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Tap any hall or pin to set route",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                )
            }
        }
    }
}

private fun DrawScope.drawCampusFeatures(w: Float, h: Float) {
    // University Botanical Lake / Pond
    val lakePath = Path().apply {
        moveTo(w * 0.70f, h * 0.15f)
        cubicTo(w * 0.85f, h * 0.12f, w * 0.92f, h * 0.22f, w * 0.85f, h * 0.28f)
        cubicTo(w * 0.78f, h * 0.32f, w * 0.68f, h * 0.22f, w * 0.70f, h * 0.15f)
        close()
    }
    drawPath(
        path = lakePath,
        color = Color(0xFF0C2744) // Deep navy-blue water
    )
    drawPath(
        path = lakePath,
        color = Color(0xFF0077B6).copy(alpha = 0.3f),
        style = Stroke(width = 2f)
    )

    // Central Quad Greenery (Lawn 1)
    drawRoundRect(
        color = Color(0xFF0A2E20), // Rich forest dark green
        topLeft = Offset(w * 0.40f, h * 0.38f),
        size = Size(w * 0.20f, h * 0.16f),
        cornerRadius = CornerRadius(20f, 20f)
    )

    // North Residential Green Park (Lawn 2)
    drawRoundRect(
        color = Color(0xFF08261A),
        topLeft = Offset(w * 0.16f, h * 0.12f),
        size = Size(w * 0.18f, h * 0.14f),
        cornerRadius = CornerRadius(16f, 16f)
    )

    // Athletics Turf Field (Lawn 3)
    drawRoundRect(
        color = Color(0xFF093021),
        topLeft = Offset(w * 0.12f, h * 0.60f),
        size = Size(w * 0.16f, h * 0.14f),
        cornerRadius = CornerRadius(14f, 14f)
    )
}

private fun DrawScope.drawCampusRoads(w: Float, h: Float) {
    val roadColor = Color(0xFF1E293B)
    val pathwayColor = Color(0xFF172033)

    // Main University Loop Road (Around Campus)
    val mainLoop = Path().apply {
        moveTo(w * 0.50f, h * 0.08f)
        lineTo(w * 0.90f, h * 0.25f)
        lineTo(w * 0.88f, h * 0.80f)
        lineTo(w * 0.50f, h * 0.92f)
        lineTo(w * 0.10f, h * 0.80f)
        lineTo(w * 0.12f, h * 0.25f)
        close()
    }
    drawPath(path = mainLoop, color = roadColor, style = Stroke(width = 16f, cap = StrokeCap.Round, join = StrokeJoin.Round))

    // Central University Boulevard (North to South spine)
    drawLine(
        color = roadColor,
        start = Offset(w * 0.50f, h * 0.08f),
        end = Offset(w * 0.50f, h * 0.92f),
        strokeWidth = 20f,
        cap = StrokeCap.Round
    )

    // Tech Quad Cross Avenue (West to East)
    drawLine(
        color = roadColor,
        start = Offset(w * 0.12f, h * 0.35f),
        end = Offset(w * 0.88f, h * 0.35f),
        strokeWidth = 14f,
        cap = StrokeCap.Round
    )

    // South Dining & Transit Avenue
    drawLine(
        color = roadColor,
        start = Offset(w * 0.10f, h * 0.68f),
        end = Offset(w * 0.88f, h * 0.68f),
        strokeWidth = 14f,
        cap = StrokeCap.Round
    )

    // Diagonal Campus Promenade (Pedestrian walkway)
    drawLine(
        color = pathwayColor,
        start = Offset(w * 0.25f, h * 0.22f),
        end = Offset(w * 0.75f, h * 0.75f),
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = pathwayColor,
        start = Offset(w * 0.75f, h * 0.22f),
        end = Offset(w * 0.25f, h * 0.75f),
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawCampusBuildings(w: Float, h: Float) {
    val bldgColor = Color(0xFF192233)
    val bldgBorder = Color(0xFF2C3B54)

    val buildings = listOf(
        // Library building footprint
        Offset(w * 0.58f, h * 0.28f) to Size(w * 0.14f, h * 0.08f),
        // Student Union footprint
        Offset(w * 0.44f, h * 0.45f) to Size(w * 0.15f, h * 0.07f),
        // Engineering building
        Offset(w * 0.28f, h * 0.26f) to Size(w * 0.13f, h * 0.08f),
        // North Dorms complex
        Offset(w * 0.18f, h * 0.14f) to Size(w * 0.12f, h * 0.07f),
        // South Apartments
        Offset(w * 0.72f, h * 0.68f) to Size(w * 0.14f, h * 0.09f),
        // Metro Terminal
        Offset(w * 0.43f, h * 0.83f) to Size(w * 0.16f, h * 0.06f),
        // Recreation Gym & Arena
        Offset(w * 0.15f, h * 0.62f) to Size(w * 0.12f, h * 0.08f),
        // Bio-Science Hall
        Offset(w * 0.76f, h * 0.35f) to Size(w * 0.13f, h * 0.07f)
    )

    buildings.forEach { (offset, size) ->
        drawRoundRect(
            color = bldgColor,
            topLeft = offset,
            size = size,
            cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
            color = bldgBorder,
            topLeft = offset,
            size = size,
            cornerRadius = CornerRadius(10f, 10f),
            style = Stroke(width = 1.5f)
        )
    }
}

private fun DrawScope.drawRoutePolyline(
    p1: CampusPoint,
    p2: CampusPoint,
    canvasW: Float,
    canvasH: Float,
    progress: Float
) {
    val x1 = p1.x * canvasW
    val y1 = p1.y * canvasH
    val x2 = p2.x * canvasW
    val y2 = p2.y * canvasH

    // Intermediate waypoint for organic campus street turn
    val midX = (x1 + x2) / 2f
    val midY = if (x1 != x2) (y1 + y2) / 2f + 25f else (y1 + y2) / 2f

    val routePath = Path().apply {
        moveTo(x1, y1)
        quadraticTo(midX, midY, x2, y2)
    }

    // Outer glow path
    drawPath(
        path = routePath,
        color = CampusEmerald.copy(alpha = 0.25f),
        style = Stroke(width = 14f, cap = StrokeCap.Round)
    )

    // Main line path (Dashed animated or solid)
    drawPath(
        path = routePath,
        color = CampusEmerald,
        style = Stroke(
            width = 6f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 12f), 0f)
        )
    )
}
