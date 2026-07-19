package com.example.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.data.model.Bus
import com.example.data.model.BusRoute

@Composable
fun LiveMapCanvas(
    routes: List<BusRoute>,
    selectedRouteId: String?,
    buses: List<Bus>,
    selectedBusId: String?,
    onBusSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outline

    // Pulsing effect for bus circles
    val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseRadius"
    )

    // Predefined campus hubs for visual reference on the map
    val campusHubs = remember {
        listOf(
            CampusHub("North Gate", 0.15f, 0.20f, "Gate"),
            CampusHub("Science Block", 0.50f, 0.25f, "Academic"),
            CampusHub("Hostel Block A", 0.85f, 0.15f, "Residential"),
            CampusHub("Central Library", 0.50f, 0.45f, "Hub"),
            CampusHub("Main Cafeteria", 0.70f, 0.50f, "Dining"),
            CampusHub("South Gate", 0.20f, 0.85f, "Gate"),
            CampusHub("Engineering Block", 0.35f, 0.75f, "Academic"),
            CampusHub("Sports Complex", 0.80f, 0.85f, "Recreation"),
            CampusHub("Metro Station Link", 0.05f, 0.55f, "Transit")
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .pointerInput(buses) {
                    detectTapGestures { offset ->
                        // Detect click on live buses
                        val width = size.width
                        val height = size.height

                        var clickedBusId: String? = null
                        for (bus in buses) {
                            val busX = bus.latitudePercent * width
                            val busY = bus.longitudePercent * height
                            val distance = Math.hypot((offset.x - busX).toDouble(), (offset.y - busY).toDouble())
                            if (distance < 45.0) { // 45 pixels tap target tolerance
                                clickedBusId = bus.id
                                break
                            }
                        }
                        if (clickedBusId != null) {
                            onBusSelected(clickedBusId)
                        }
                    }
                }
        ) {
            val width = size.width
            val height = size.height

            // Draw Geometric Balance Radial Dot Grid Pattern
            val dotColor = primaryColor.copy(alpha = 0.08f)
            val spacing = 32f
            var xPos = 0f
            while (xPos < width) {
                var yPos = 0f
                while (yPos < height) {
                    drawCircle(
                        color = dotColor,
                        radius = 2.5f,
                        center = Offset(xPos, yPos)
                    )
                    yPos += spacing
                }
                xPos += spacing
            }

            // 1. Draw Campus Background roads / boundaries (Subtle Lines)
            val roadPaintColor = outlineColor.copy(alpha = 0.25f)
            // Major road grid connecting nodes
            drawLine(
                color = roadPaintColor,
                start = Offset(0.15f * width, 0.20f * height),
                end = Offset(0.85f * width, 0.15f * height),
                strokeWidth = 6f
            )
            drawLine(
                color = roadPaintColor,
                start = Offset(0.50f * width, 0.25f * height),
                end = Offset(0.50f * width, 0.45f * height),
                strokeWidth = 6f
            )
            drawLine(
                color = roadPaintColor,
                start = Offset(0.50f * width, 0.45f * height),
                end = Offset(0.20f * width, 0.85f * height),
                strokeWidth = 6f
            )
            drawLine(
                color = roadPaintColor,
                start = Offset(0.50f * width, 0.45f * height),
                end = Offset(0.80f * width, 0.85f * height),
                strokeWidth = 6f
            )
            drawLine(
                color = roadPaintColor,
                start = Offset(0.05f * width, 0.55f * height),
                end = Offset(0.50f * width, 0.45f * height),
                strokeWidth = 6f
            )

            // 2. Draw ALL routes, with the currently SELECTED route highlighted
            for (route in routes) {
                val isSelected = route.id == selectedRouteId
                val routeColor = when (route.id) {
                    "ROUTE_NORTH" -> Color(0xFF1E88E5) // Nice Blue
                    "ROUTE_SOUTH" -> Color(0xFF43A047) // Nice Green
                    "ROUTE_METRO" -> Color(0xFF8E24AA) // Nice Purple
                    "ROUTE_RING" -> Color(0xFFFB8C00) // Nice Orange
                    else -> primaryColor
                }

                if (route.pathPoints.isNotEmpty()) {
                    val path = Path().apply {
                        val first = route.pathPoints.first()
                        moveTo(first.first * width, first.second * height)
                        for (i in 1 until route.pathPoints.size) {
                            val pt = route.pathPoints[i]
                            lineTo(pt.first * width, pt.second * height)
                        }
                    }

                    drawPath(
                        path = path,
                        color = if (isSelected) routeColor else routeColor.copy(alpha = 0.2f),
                        style = Stroke(
                            width = if (isSelected) 10f else 4f,
                            miter = 2f
                        )
                    )
                }
            }

            // 3. Draw Campus Landmark Hubs (Circles + labels)
            campusHubs.forEach { hub ->
                val x = hub.xPercent * width
                val y = hub.yPercent * height

                // Background building card
                drawCircle(
                    color = surfaceVariantColor,
                    radius = 24f,
                    center = Offset(x, y)
                )

                // Colored inner ring based on hub type
                val indicatorColor = when (hub.type) {
                    "Gate" -> Color(0xFFE53935) // Red
                    "Academic" -> Color(0xFF3949AB) // Indigo
                    "Transit" -> Color(0xFF8E24AA) // Purple
                    "Residential" -> Color(0xFFFDD835) // Yellow
                    else -> Color(0xFF00ACC1) // Teal
                }

                drawCircle(
                    color = indicatorColor,
                    radius = 8f,
                    center = Offset(x, y)
                )

                // Label Native Text
                drawContext.canvas.nativeCanvas.apply {
                    val paint = Paint().apply {
                        color = onSurfaceColor.hashCode()
                        textSize = 28f
                        textAlign = Paint.Align.CENTER
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                    drawText(hub.name, x, y + 48f, paint)
                }
            }

            // 4. Draw LIVE Buses operating on campus
            buses.forEach { bus ->
                val busX = bus.latitudePercent * width
                val busY = bus.longitudePercent * height
                val isSelected = bus.id == selectedBusId

                val routeColor = when (bus.routeId) {
                    "ROUTE_NORTH" -> Color(0xFF1E88E5)
                    "ROUTE_SOUTH" -> Color(0xFF43A047)
                    "ROUTE_METRO" -> Color(0xFF8E24AA)
                    "ROUTE_RING" -> Color(0xFFFB8C00)
                    else -> secondaryColor
                }

                // Selection glow rings
                if (isSelected) {
                    drawCircle(
                        color = routeColor.copy(alpha = 0.3f),
                        radius = 32f,
                        center = Offset(busX, busY)
                    )
                    drawCircle(
                        color = routeColor.copy(alpha = 0.5f),
                        radius = 22f,
                        center = Offset(busX, busY),
                        style = Stroke(width = 3f)
                    )
                } else {
                    // Pulsing dynamic aura for active buses
                    drawCircle(
                        color = routeColor.copy(alpha = 0.15f),
                        radius = pulseRadius + 6f,
                        center = Offset(busX, busY)
                    )
                }

                // Core Bus circle
                drawCircle(
                    color = routeColor,
                    radius = 12f,
                    center = Offset(busX, busY)
                )

                // Bus Icon indicator (Little white steering center)
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = Offset(busX, busY)
                )

                // Draw Bus identifier label above the marker
                drawContext.canvas.nativeCanvas.apply {
                    val paint = Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 24f
                        textAlign = Paint.Align.CENTER
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }

                    // Draw a little background pill for readability
                    val backgroundPaint = Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        alpha = 220
                    }
                    val rectWidth = 100f
                    val rectHeight = 36f
                    drawRoundRect(
                        busX - rectWidth/2,
                        busY - 46f - rectHeight/2,
                        busX + rectWidth/2,
                        busY - 46f + rectHeight/2,
                        8f, 8f,
                        backgroundPaint
                    )

                    drawText(bus.id, busX, busY - 36f, paint)
                }
            }
        }
    }
}

data class CampusHub(
    val name: String,
    val xPercent: Float,
    val yPercent: Float,
    val type: String
)
