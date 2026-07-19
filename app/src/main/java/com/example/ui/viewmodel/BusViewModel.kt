package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.CampusBusDatabase
import com.example.data.entity.NotificationEntity
import com.example.data.entity.PaymentEntity
import com.example.data.entity.UserEntity
import com.example.data.model.Bus
import com.example.data.model.BusRoute
import com.example.data.repository.BusRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    TRACKING,
    PAYMENT,
    NOTIFICATIONS,
    ACCOUNT
}

data class MissedBusRecommendation(
    val routeName: String,
    val alternativeType: String, // "Next Scheduled", "Overlapping Route", "Nearby Live Bus", "Walk Advice"
    val details: String,
    val etaDescription: String
)

class BusViewModel(application: Application) : AndroidViewModel(application) {
    private val db = CampusBusDatabase.getDatabase(application)
    private val repository = BusRepository(db.userDao(), db.paymentDao(), db.notificationDao())

    // 1. Reactive Database & Simulated Flows
    val activeUser: StateFlow<UserEntity?> = repository.activeUser.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val allPayments: StateFlow<List<PaymentEntity>> = repository.allPayments.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalPaidAmount: StateFlow<Double?> = repository.totalPaidAmount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val liveBuses: StateFlow<List<Bus>> = repository.liveBuses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val campusRoutes = repository.campusRoutes

    // 2. Navigation State
    var currentTab by mutableStateOf(AppTab.TRACKING)
        private set

    fun selectTab(tab: AppTab) {
        currentTab = tab
    }

    // 3. Login & Authentication Flow States
    var studentIdInput by mutableStateOf("")
    var passwordInput by mutableStateOf("")
    var loginError by mutableStateOf<String?>(null)
    var isVerifyingTwoFactor by mutableStateOf(false)
    var twoFactorCodeInput by mutableStateOf("")
    var twoFactorError by mutableStateOf<String?>(null)
    var generatedTwoFactorCode by mutableStateOf("")

    // 4. Tracking Screen UI States
    var selectedRouteId by mutableStateOf<String?>("ROUTE_NORTH")
    var selectedBusId by mutableStateOf<String?>(null)

    // "Missed Bus Assistant" Input & Output States
    var missedSelectedRouteId by mutableStateOf("ROUTE_NORTH")
    var missedTimeHour by mutableStateOf("08")
    var missedTimeMinute by mutableStateOf("00")
    var missedTimeAmPm by mutableStateOf("AM")
    var recommendations by mutableStateOf<List<MissedBusRecommendation>>(emptyList())

    // 5. Payment Screen UI States
    val outstandingFee = 150.0 // Semester transportation fee
    var paymentAmountInput by mutableStateOf("150.00")
    var paymentMethodSelection by mutableStateOf("UPI") // "UPI", "Credit Card", "Debit Card"
    var paymentCardNumber by mutableStateOf("")
    var paymentCardExpiry by mutableStateOf("")
    var paymentCardCvv by mutableStateOf("")
    var paymentSuccessMessage by mutableStateOf<String?>(null)
    var isProcessingPayment by mutableStateOf(false)

    // 6. Alert Preferences
    var isArrivalAlertsEnabled by mutableStateOf(true)
    var isDelayAlertsEnabled by mutableStateOf(true)
    var isGeneralAlertsEnabled by mutableStateOf(true)

    // Simulation simulation Ticker Job
    private var simulationJob: Job? = null

    init {
        startSimulation()
        // Initialize with default sample notifications if empty
        viewModelScope.launch {
            repository.allNotifications.collect { list ->
                if (list.isEmpty()) {
                    repository.triggerSampleNotifications()
                }
            }
        }
    }

    private fun startSimulation() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            while (true) {
                delay(1500)
                repository.tickSimulation()
            }
        }
    }

    // 7. Actions & Event Handlers

    // Login Initiated
    fun onLoginSubmit() {
        if (studentIdInput.isBlank() || passwordInput.isBlank()) {
            loginError = "Please fill in all fields"
            return
        }
        if (passwordInput.length < 4) {
            loginError = "Password must be at least 4 characters"
            return
        }

        // Simulating credential checking
        loginError = null
        // Generate a 4-digit code for 2-step verification
        generatedTwoFactorCode = (1000..9999).random().toString()
        isVerifyingTwoFactor = true
        twoFactorCodeInput = ""
        twoFactorError = null

        // Trigger a local notification representing the 2FA code delivery!
        viewModelScope.launch {
            repository.insertNotification(
                title = "Your Secure OTP Code",
                message = "USE CODE: $generatedTwoFactorCode for 2-Step verification. This code is valid for 10 minutes.",
                type = "GENERAL"
            )
        }
    }

    // Verify 2FA code
    fun onTwoFactorVerify() {
        if (twoFactorCodeInput != generatedTwoFactorCode && twoFactorCodeInput != "1234") { // Allow 1234 as fallback
            twoFactorError = "Incorrect code. Please check notifications or try again."
            return
        }

        twoFactorError = null
        viewModelScope.launch {
            repository.verifyUser(studentIdInput)
            isVerifyingTwoFactor = false
            currentTab = AppTab.TRACKING
            // Log successful sign in
            repository.insertNotification(
                title = "Login Successful",
                message = "Welcome, Prachi Gadge. You logged in securely with Two-Step Verification.",
                type = "GENERAL"
            )
        }
    }

    // Cancel 2-Factor state
    fun cancelTwoFactor() {
        isVerifyingTwoFactor = false
        twoFactorCodeInput = ""
        twoFactorError = null
    }

    // Log out
    fun onLogout() {
        viewModelScope.launch {
            repository.clearSession()
            studentIdInput = ""
            passwordInput = ""
            isVerifyingTwoFactor = false
            currentTab = AppTab.ACCOUNT
        }
    }

    // Calculate recommendations for missed scheduled bus
    fun calculateMissedBusAlternatives() {
        val selectedRoute = campusRoutes.find { it.id == missedSelectedRouteId } ?: return
        val amPm = missedTimeAmPm
        val inputHour = missedTimeHour.toIntOrNull() ?: 8
        val inputMin = missedTimeMinute.toIntOrNull() ?: 0

        // Parse time into minutes from midnight
        val inputTimeInMinutes = (if (amPm == "PM" && inputHour != 12) inputHour + 12 else if (amPm == "AM" && inputHour == 12) 0 else inputHour) * 60 + inputMin

        val list = mutableListOf<MissedBusRecommendation>()

        // 1. Next scheduled bus on the same route
        var nextScheduledTimeStr = ""
        var nextScheduledMinutes = Int.MAX_VALUE
        for (sched in selectedRoute.schedule) {
            val isPm = sched.endsWith("PM")
            val cleanSched = sched.replace(" AM", "").replace(" PM", "")
            val parts = cleanSched.split(":")
            val h = parts[0].toIntOrNull() ?: 12
            val m = parts[1].toIntOrNull() ?: 0
            val schedInMinutes = (if (isPm && h != 12) h + 12 else if (!isPm && h == 12) 0 else h) * 60 + m
            if (schedInMinutes > inputTimeInMinutes && schedInMinutes < nextScheduledMinutes) {
                nextScheduledMinutes = schedInMinutes
                nextScheduledTimeStr = sched
            }
        }

        if (nextScheduledTimeStr.isNotEmpty()) {
            val waitTime = nextScheduledMinutes - inputTimeInMinutes
            list.add(
                MissedBusRecommendation(
                    routeName = selectedRoute.name,
                    alternativeType = "Next Scheduled Bus",
                    details = "The next scheduled bus on this route departs at $nextScheduledTimeStr.",
                    etaDescription = "Departs in $waitTime minutes"
                )
            )
        } else {
            list.add(
                MissedBusRecommendation(
                    routeName = selectedRoute.name,
                    alternativeType = "Next Scheduled Bus",
                    details = "There are no more scheduled departures for this route today.",
                    etaDescription = "No more runs today"
                )
            )
        }

        // 2. Overlapping alternative routes
        val otherRoutes = campusRoutes.filter { it.id != missedSelectedRouteId }
        for (or in otherRoutes) {
            // Find common stops
            val commonStops = or.stops.intersect(selectedRoute.stops.toSet())
            if (commonStops.isNotEmpty()) {
                list.add(
                    MissedBusRecommendation(
                        routeName = or.name,
                        alternativeType = "Overlapping Route",
                        details = "Take this line which shares stops: ${commonStops.joinToString(", ")}. You can board from any of these central hubs.",
                        etaDescription = "Frequent campus loops"
                    )
                )
            }
        }

        // 3. Live nearby active buses
        val liveBusesOnRoute = liveBuses.value.filter { it.routeId == missedSelectedRouteId }
        if (liveBusesOnRoute.isNotEmpty()) {
            for (lb in liveBusesOnRoute) {
                list.add(
                    MissedBusRecommendation(
                        routeName = selectedRoute.name,
                        alternativeType = "Nearby Live Bus",
                        details = "Live Bus ${lb.id} is currently operating on this route. Next stop: ${lb.nextStop}. Driver: ${lb.driverName}.",
                        etaDescription = "Approaching next stop in ${lb.etaMinutes} mins"
                    )
                )
            }
        } else {
            // Suggest the closest live bus regardless of route
            val closestLive = liveBuses.value.minByOrNull { it.etaMinutes }
            if (closestLive != null) {
                list.add(
                    MissedBusRecommendation(
                        routeName = closestLive.routeName,
                        alternativeType = "Nearby Live Bus (Alt Route)",
                        details = "Bus ${closestLive.id} is 1.5 miles away. Next stop is ${closestLive.nextStop}. Can be used to reach central campus hubs.",
                        etaDescription = "Arriving in ${closestLive.etaMinutes} mins"
                    )
                )
            }
        }

        // 4. Pedestrian health advice (walk)
        list.add(
            MissedBusRecommendation(
                routeName = "Campus Walk Paths",
                alternativeType = "Walk Advice",
                details = "Weather is pleasant (72°F). Walking between academic buildings and hostels takes approximately 12-15 minutes via safe pedestrian zones.",
                etaDescription = "Approx. 12 mins walk"
            )
        )

        recommendations = list
    }

    // Payment Processing
    fun payBusFees() {
        val amt = paymentAmountInput.toDoubleOrNull() ?: 0.0
        if (amt <= 0.0) {
            paymentSuccessMessage = null
            return
        }

        if (paymentMethodSelection != "UPI" && paymentCardNumber.length < 12) {
            paymentSuccessMessage = "Please enter a valid card number"
            return
        }

        isProcessingPayment = true
        paymentSuccessMessage = null

        viewModelScope.launch {
            delay(1800) // Simulate processing time
            val studentId = activeUser.value?.studentId ?: "ST-99221"
            val studentName = activeUser.value?.fullName ?: "Guest Student"
            val route = campusRoutes.find { it.id == selectedRouteId }?.name ?: "All Campus Routes"

            val paymentEntity = repository.processPayment(
                studentId = studentId,
                studentName = studentName,
                amount = amt,
                method = paymentMethodSelection,
                routeName = route
            )

            isProcessingPayment = false
            paymentSuccessMessage = "Payment of $${String.format("%.2f", amt)} was successful! Ref: ${paymentEntity.referenceId}"
            paymentCardNumber = ""
            paymentCardExpiry = ""
            paymentCardCvv = ""
        }
    }

    // Clear Payment state
    fun dismissPaymentSuccess() {
        paymentSuccessMessage = null
    }

    // Trigger dummy alert (simulates real system push alert)
    fun simulateTrafficDelayAlert() {
        viewModelScope.launch {
            val randomRoute = campusRoutes.random()
            val delays = listOf(5, 8, 12, 15)
            val delay = delays.random()
            val reasons = listOf("heavy traffic", "road repairs near admin", "vehicle inspection", "sports day parade blockage")
            val reason = reasons.random()

            repository.insertNotification(
                title = "Alert: ${randomRoute.stops.first()} Delay",
                message = "${randomRoute.name} is experiencing a delay of $delay minutes due to $reason. Please plan accordingly.",
                type = "DELAY"
            )
        }
    }

    // Notification mark as read
    fun markNotificationAsRead(id: Int) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun deleteNotification(id: Int) {
        viewModelScope.launch {
            repository.deleteNotification(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        simulationJob?.cancel()
    }
}
