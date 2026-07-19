package com.example.data.repository

import com.example.data.dao.PaymentDao
import com.example.data.dao.NotificationDao
import com.example.data.dao.UserDao
import com.example.data.entity.PaymentEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.UserEntity
import com.example.data.model.Bus
import com.example.data.model.BusRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class BusRepository(
    private val userDao: UserDao,
    private val paymentDao: PaymentDao,
    private val notificationDao: NotificationDao
) {
    // 1. Database Flows
    val activeUser: Flow<UserEntity?> = userDao.getActiveUser()
    val allPayments: Flow<List<PaymentEntity>> = paymentDao.getAllPayments()
    val totalPaidAmount: Flow<Double?> = paymentDao.getTotalPaidAmount()
    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()

    // 2. Predefined Campus Routes
    val campusRoutes = listOf(
        BusRoute(
            id = "ROUTE_NORTH",
            name = "North Campus Shuttle (Route A)",
            description = "Connects North Gate Terminal to central campus hubs and student hostel blocks.",
            stops = listOf("North Gate Terminal", "Science Block", "Central Library", "Main Cafeteria", "Hostel Block A"),
            schedule = listOf("08:00 AM", "09:30 AM", "11:00 AM", "01:30 PM", "03:30 PM", "05:30 PM"),
            pathPoints = listOf(
                0.15f to 0.20f,
                0.35f to 0.18f,
                0.50f to 0.25f,
                0.60f to 0.35f,
                0.75f to 0.20f,
                0.85f to 0.35f,
                0.70f to 0.50f,
                0.50f to 0.45f,
                0.30f to 0.35f,
                0.15f to 0.20f // Close loop
            )
        ),
        BusRoute(
            id = "ROUTE_SOUTH",
            name = "South Gate Express (Route B)",
            description = "Direct high-speed shuttle connecting South Gate Residential Entrance to academic blocks.",
            stops = listOf("South Gate Entrance", "Engineering Block", "Administration Wing", "Sports Complex", "Main Cafeteria"),
            schedule = listOf("08:15 AM", "09:45 AM", "11:15 AM", "02:00 PM", "04:00 PM", "06:00 PM"),
            pathPoints = listOf(
                0.20f to 0.85f,
                0.35f to 0.75f,
                0.50f to 0.65f,
                0.65f to 0.70f,
                0.80f to 0.85f,
                0.88f to 0.65f,
                0.70f to 0.50f,
                0.50f to 0.45f,
                0.35f to 0.60f,
                0.20f to 0.85f // Close loop
            )
        ),
        BusRoute(
            id = "ROUTE_METRO",
            name = "Metro Station Connector (Route C)",
            description = "Express link between city metro hub station and our campus center. Fast and frequent.",
            stops = listOf("Metro Station Exit", "Outer Ring Junction", "West Gate Canopy", "Central Library", "Administration Wing"),
            schedule = listOf("07:30 AM", "08:00 AM", "08:30 AM", "09:00 AM", "10:30 AM", "12:00 PM", "01:30 PM", "03:00 PM", "04:30 PM", "05:00 PM", "05:30 PM", "06:00 PM"),
            pathPoints = listOf(
                0.05f to 0.55f,
                0.20f to 0.53f,
                0.40f to 0.50f,
                0.50f to 0.45f,
                0.60f to 0.55f,
                0.75f to 0.58f,
                0.60f to 0.55f,
                0.50f to 0.45f,
                0.40f to 0.50f,
                0.20f to 0.53f,
                0.05f to 0.55f // Back and forth
            )
        ),
        BusRoute(
            id = "ROUTE_RING",
            name = "Hostels Ring Line (Route D)",
            description = "Continuous loop connecting residential sectors, dining halls, and auditoriums.",
            stops = listOf("PG Hostels", "Staff Quarters", "Girls Hostel Hub", "Auditorium", "Science Block", "Sports Complex"),
            schedule = listOf("07:45 AM", "08:45 AM", "09:45 AM", "10:45 AM", "12:45 PM", "02:45 PM", "04:45 PM", "05:45 PM", "06:45 PM"),
            pathPoints = listOf(
                0.85f to 0.15f,
                0.88f to 0.40f,
                0.82f to 0.60f,
                0.85f to 0.80f,
                0.65f to 0.85f,
                0.40f to 0.80f,
                0.35f to 0.60f,
                0.50f to 0.45f,
                0.50f to 0.25f,
                0.85f to 0.15f // Close loop
            )
        )
    )

    // 3. Simulated Live Bus Data
    private val _liveBuses = MutableStateFlow<List<Bus>>(emptyList())
    val liveBuses = _liveBuses.asStateFlow()

    // Animation progress tracker (0.0 to 1.0)
    private var animationProgress = 0.0f

    init {
        // Pre-populate with live buses
        _liveBuses.value = listOf(
            Bus(
                id = "BUS-N1",
                driverName = "Robert Chen",
                driverPhone = "+1 (555) 019-2834",
                routeId = "ROUTE_NORTH",
                routeName = "North Campus Shuttle (Route A)",
                status = "On Time",
                delayMinutes = 0,
                capacity = 50,
                currentPassengers = 18,
                latitudePercent = 0.15f,
                longitudePercent = 0.20f,
                nextStop = "Science Block",
                etaMinutes = 3
            ),
            Bus(
                id = "BUS-S2",
                driverName = "Sarah Jenkins",
                driverPhone = "+1 (555) 014-9982",
                routeId = "ROUTE_SOUTH",
                routeName = "South Gate Express (Route B)",
                status = "Delayed",
                delayMinutes = 6,
                capacity = 45,
                currentPassengers = 32,
                latitudePercent = 0.20f,
                longitudePercent = 0.85f,
                nextStop = "Administration Wing",
                etaMinutes = 8
            ),
            Bus(
                id = "BUS-M3",
                driverName = "Marcus Brody",
                driverPhone = "+1 (555) 012-3841",
                routeId = "ROUTE_METRO",
                routeName = "Metro Station Connector (Route C)",
                status = "Arriving",
                delayMinutes = 0,
                capacity = 60,
                currentPassengers = 48,
                latitudePercent = 0.40f,
                longitudePercent = 0.50f,
                nextStop = "Central Library",
                etaMinutes = 1
            ),
            Bus(
                id = "BUS-R4",
                driverName = "Elena Rostova",
                driverPhone = "+1 (555) 017-8822",
                routeId = "ROUTE_RING",
                routeName = "Hostels Ring Line (Route D)",
                status = "On Time",
                delayMinutes = 0,
                capacity = 50,
                currentPassengers = 12,
                latitudePercent = 0.85f,
                longitudePercent = 0.15f,
                nextStop = "Staff Quarters",
                etaMinutes = 4
            )
        )
    }

    // Function called periodically to simulate bus movement on the campus routes
    fun tickSimulation() {
        animationProgress += 0.015f
        if (animationProgress > 1.0f) animationProgress = 0.0f

        _liveBuses.update { currentList ->
            currentList.map { bus ->
                val route = campusRoutes.find { it.id == bus.routeId }
                if (route != null && route.pathPoints.isNotEmpty()) {
                    // Calculate index on path based on animationProgress and specific bus offsets to separate them
                    val offset = when (bus.id) {
                        "BUS-N1" -> 0.0f
                        "BUS-S2" -> 0.25f
                        "BUS-M3" -> 0.5f
                        "BUS-R4" -> 0.75f
                        else -> 0.0f
                    }
                    val progress = (animationProgress + offset) % 1.0f
                    val pathSize = route.pathPoints.size
                    val exactIndex = progress * (pathSize - 1)
                    val index1 = exactIndex.toInt()
                    val index2 = (index1 + 1) % pathSize
                    val t = exactIndex - index1

                    val (x1, y1) = route.pathPoints[index1]
                    val (x2, y2) = route.pathPoints[index2]

                    // Interpolated point
                    val x = x1 + (x2 - x1) * t
                    val y = y1 + (y2 - y1) * t

                    // Dynamically calculate the next stop and updating ETAs
                    val stopIndex = ((progress * route.stops.size).toInt()) % route.stops.size
                    val nextStop = route.stops[stopIndex]
                    val baseEta = (5 - (t * 5).toInt()).coerceAtLeast(1)
                    val eta = if (bus.status == "Delayed") baseEta + bus.delayMinutes else baseEta

                    // Simulate passenger variation slightly
                    val changeChance = (0..9).random()
                    val passengerVariation = when {
                        changeChance > 7 && bus.currentPassengers < bus.capacity -> 1
                        changeChance < 2 && bus.currentPassengers > 5 -> -1
                        else -> 0
                    }

                    bus.copy(
                        latitudePercent = x,
                        longitudePercent = y,
                        nextStop = nextStop,
                        etaMinutes = eta,
                        currentPassengers = bus.currentPassengers + passengerVariation
                    )
                } else {
                    bus
                }
            }
        }
    }

    // 4. User Profile Actions
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    suspend fun verifyUser(studentId: String) {
        val user = userDao.getActiveUser()
        userDao.insertUser(
            UserEntity(
                studentId = studentId,
                fullName = "Prachi Gadge",
                email = "prachiagadge243@gmail.com",
                phoneNumber = "+1 (555) 019-3388",
                isVerified = true,
                isTwoFactorEnabled = true
            )
        )
    }
    suspend fun clearSession() = userDao.clearUser()

    // 5. Payment Actions
    suspend fun processPayment(
        studentId: String,
        studentName: String,
        amount: Double,
        method: String,
        routeName: String
    ): PaymentEntity {
        val payment = PaymentEntity(
            studentId = studentId,
            studentName = studentName,
            amount = amount,
            paymentMethod = method,
            status = "SUCCESS",
            timestamp = System.currentTimeMillis(),
            routeName = routeName,
            referenceId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).uppercase()
        )
        paymentDao.insertPayment(payment)

        // Add a automatic payment notification
        val notification = NotificationEntity(
            title = "Payment Successful",
            message = "Your bus fee payment of $${String.format("%.2f", amount)} for ${routeName} was successfully processed. Ref: ${payment.referenceId}",
            timestamp = System.currentTimeMillis(),
            type = "PAYMENT"
        )
        notificationDao.insertNotification(notification)

        return payment
    }

    // 6. Notification Actions
    suspend fun insertNotification(title: String, message: String, type: String) {
        notificationDao.insertNotification(
            NotificationEntity(
                title = title,
                message = message,
                timestamp = System.currentTimeMillis(),
                type = type
            )
        )
    }

    suspend fun markNotificationAsRead(id: Int) = notificationDao.markAsRead(id)
    suspend fun markAllNotificationsAsRead() = notificationDao.markAllAsRead()
    suspend fun deleteNotification(id: Int) = notificationDao.deleteNotification(id)

    // Trigger pre-loaded sample delay notifications to populate initial user screens
    suspend fun triggerSampleNotifications() {
        val samples = listOf(
            NotificationEntity(
                title = "South Gate Express Delayed",
                message = "Route B - South Gate Express is delayed by 6 minutes near the Engineering Block roundabout due to service road maintenance.",
                timestamp = System.currentTimeMillis() - 600000, // 10 mins ago
                type = "DELAY"
            ),
            NotificationEntity(
                title = "Metro Shuttle Frequency Boost",
                message = "Starting Monday, Route C - Metro Station Connector will run every 15 minutes during morning peak hours (07:30 AM - 09:30 AM).",
                timestamp = System.currentTimeMillis() - 1200000, // 20 mins ago
                type = "GENERAL"
            ),
            NotificationEntity(
                title = "North Shuttle Arriving Hub",
                message = "Route A - North Campus Shuttle BUS-N1 is approaching the Central Library stop in 2 minutes.",
                timestamp = System.currentTimeMillis() - 1800000, // 30 mins ago
                type = "ARRIVAL"
            )
        )
        samples.forEach { notificationDao.insertNotification(it) }
    }
}
