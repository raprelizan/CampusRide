package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CampusData
import com.example.model.ActiveRide
import com.example.model.CampusLocation
import com.example.model.ChatMessage
import com.example.model.DriverOffer
import com.example.model.RideHistoryItem
import com.example.model.RideStatus
import com.example.model.StudentDriver
import com.example.model.VehicleType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.hypot
import kotlin.random.Random

data class RiderUiState(
    val pickup: CampusLocation = CampusData.CAMPUS_LOCATIONS[0], // Library
    val dropoff: CampusLocation? = CampusData.CAMPUS_LOCATIONS[1], // Student Union
    val selectedVehicle: VehicleType = VehicleType.CAMPUS_POOL,
    val activeRide: ActiveRide? = null,
    val isSearching: Boolean = false,
    val studentDiscountApplied: Boolean = true,
    val splitCount: Int = 1,
    val splitFriends: List<String> = emptyList(),
    val isChatOpen: Boolean = false,
    val chatMessages: List<ChatMessage> = emptyList(),
    val isSplitDialogOpen: Boolean = false,
    val isSafetyDialogOpen: Boolean = false,
    val isRatingDialogOpen: Boolean = false,
    val lastCompletedRide: ActiveRide? = null,
    val systemMessage: String? = null
)

data class DriverUiState(
    val isOnline: Boolean = false,
    val todayEarnings: Double = 64.80,
    val completedTripsCount: Int = 7,
    val hoursOnline: Double = 2.4,
    val rating: Float = 4.96f,
    val currentOffer: DriverOffer? = null,
    val activeTrip: ActiveRide? = null,
    val enteredPin: String = "",
    val pinError: String? = null
)

data class StudentProfileState(
    val fullName: String = "Alex Rivera",
    val studentId: String = "STU-2025-9942",
    val university: String = "Metropolitan University",
    val email: String = "alex.rivera@campus.edu",
    val major: String = "Electrical & Computer Eng '26",
    val isVerifiedStudent: Boolean = true,
    val walletBalance: Double = 42.50,
    val campusCashBalance: Double = 28.00,
    val rideHistory: List<RideHistoryItem> = CampusData.INITIAL_HISTORY
)

class CampusViewModel : ViewModel() {

    private val _riderState = MutableStateFlow(RiderUiState())
    val riderState: StateFlow<RiderUiState> = _riderState.asStateFlow()

    private val _driverState = MutableStateFlow(DriverUiState())
    val driverState: StateFlow<DriverUiState> = _driverState.asStateFlow()

    private val _profileState = MutableStateFlow(StudentProfileState())
    val profileState: StateFlow<StudentProfileState> = _profileState.asStateFlow()

    private var rideSimulationJob: Job? = null
    private var driverOfferJob: Job? = null

    init {
        // Pre-populate welcome chat messages
        _riderState.update {
            it.copy(
                chatMessages = listOf(
                    ChatMessage("m1", false, "Hi Alex! I'm on my way in the White Tesla. See you shortly at the entrance! 🚗", "Just now")
                )
            )
        }
    }

    // --- Rider Actions ---

    fun setPickup(location: CampusLocation) {
        _riderState.update { it.copy(pickup = location) }
    }

    fun setDropoff(location: CampusLocation) {
        _riderState.update { it.copy(dropoff = location) }
    }

    fun swapLocations() {
        val currentPickup = _riderState.value.pickup
        val currentDropoff = _riderState.value.dropoff
        if (currentDropoff != null) {
            _riderState.update {
                it.copy(
                    pickup = currentDropoff,
                    dropoff = currentPickup
                )
            }
        }
    }

    fun selectVehicle(vehicleType: VehicleType) {
        _riderState.update { it.copy(selectedVehicle = vehicleType) }
    }

    fun calculateCurrentFare(vehicleType: VehicleType): Double {
        val pickup = _riderState.value.pickup
        val dropoff = _riderState.value.dropoff ?: CampusData.CAMPUS_LOCATIONS[1]
        val distKm = calculateDistanceKm(pickup, dropoff)
        var fare = vehicleType.baseFare + (distKm * vehicleType.perKmRate)
        if (vehicleType.isNightFreeEligible) {
            return 0.0
        }
        if (_riderState.value.studentDiscountApplied) {
            fare *= 0.85 // 15% student discount
        }
        return kotlin.math.round(fare * 100) / 100.0
    }

    fun calculateDistanceKm(from: CampusLocation, to: CampusLocation): Double {
        val dx = (from.point.x - to.point.x) * 4.5f
        val dy = (from.point.y - to.point.y) * 4.5f
        val dist = hypot(dx, dy)
        return (kotlin.math.round((dist.coerceAtLeast(0.6f)) * 10) / 10.0).toDouble()
    }

    fun requestRide() {
        val pickup = _riderState.value.pickup
        val dropoff = _riderState.value.dropoff ?: CampusData.CAMPUS_LOCATIONS[1]
        val vehicle = _riderState.value.selectedVehicle
        val distKm = calculateDistanceKm(pickup, dropoff)
        val calculatedFare = calculateCurrentFare(vehicle)

        val newRide = ActiveRide(
            id = "RIDE-" + (1000..9999).random(),
            pickup = pickup,
            dropoff = dropoff,
            vehicleType = vehicle,
            status = RideStatus.SEARCHING_DRIVER,
            driver = null,
            pinCode = (1000..9999).random().toString(),
            distanceKm = distKm,
            etaMinutes = (distKm * 2.5 + 2).toInt(),
            fare = calculatedFare,
            studentDiscountApplied = _riderState.value.studentDiscountApplied,
            splitCount = _riderState.value.splitCount,
            splitFriends = _riderState.value.splitFriends,
            progress = 0f
        )

        _riderState.update {
            it.copy(
                isSearching = true,
                activeRide = newRide,
                systemMessage = "Matching with nearest verified student driver..."
            )
        }

        // Simulate live driver dispatch
        rideSimulationJob?.cancel()
        rideSimulationJob = viewModelScope.launch {
            delay(2800) // Radar search animation
            val assignedDriver = CampusData.STUDENT_DRIVERS.random()

            _riderState.update {
                it.copy(
                    isSearching = false,
                    activeRide = it.activeRide?.copy(
                        status = RideStatus.DRIVER_ON_WAY,
                        driver = assignedDriver,
                        etaMinutes = 3
                    ),
                    systemMessage = "${assignedDriver.name} (${assignedDriver.major}) is arriving in 3 mins"
                )
            }

            // Driver approaching
            delay(4000)
            _riderState.update {
                it.copy(
                    activeRide = it.activeRide?.copy(
                        status = RideStatus.DRIVER_ARRIVED,
                        etaMinutes = 0
                    ),
                    systemMessage = "${assignedDriver.name} has arrived at ${pickup.name}! Show PIN ${newRide.pinCode}."
                )
            }
        }
    }

    fun startTripFromRiderOrDriver() {
        _riderState.update {
            it.copy(
                activeRide = it.activeRide?.copy(
                    status = RideStatus.IN_PROGRESS,
                    etaMinutes = 5
                ),
                systemMessage = "Trip in progress. Heading to ${_riderState.value.activeRide?.dropoff?.name}."
            )
        }

        // Simulate progress along route
        rideSimulationJob?.cancel()
        rideSimulationJob = viewModelScope.launch {
            for (step in 1..10) {
                delay(1200)
                val newProgress = step / 10f
                _riderState.update {
                    it.copy(
                        activeRide = it.activeRide?.copy(
                            progress = newProgress,
                            etaMinutes = ((1f - newProgress) * 5).toInt().coerceAtLeast(1)
                        )
                    )
                }
            }
            completeRide()
        }
    }

    fun completeRide() {
        val ride = _riderState.value.activeRide ?: return
        val finalFare = ride.fare / ride.splitCount.coerceAtLeast(1)

        // Deduct from wallet if not 0
        _profileState.update {
            val updatedHistory = listOf(
                RideHistoryItem(
                    id = ride.id,
                    date = "Today, " + SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
                    pickupName = ride.pickup.name,
                    dropoffName = ride.dropoff.name,
                    vehicleTitle = ride.vehicleType.title,
                    totalCost = finalFare,
                    driverName = ride.driver?.name ?: "Student Driver",
                    driverMajor = ride.driver?.major ?: "Engineering",
                    rating = 5,
                    isSplit = ride.splitCount > 1
                )
            ) + it.rideHistory

            it.copy(
                walletBalance = (it.walletBalance - finalFare).coerceAtLeast(0.0),
                rideHistory = updatedHistory
            )
        }

        _riderState.update {
            it.copy(
                activeRide = null,
                lastCompletedRide = ride,
                isRatingDialogOpen = true,
                systemMessage = "Trip completed! Total: $${String.format(Locale.US, "%.2f", finalFare)}"
            )
        }
    }

    fun cancelRide() {
        rideSimulationJob?.cancel()
        _riderState.update {
            it.copy(
                activeRide = null,
                isSearching = false,
                systemMessage = "Ride request cancelled."
            )
        }
    }

    fun openChat(open: Boolean) {
        _riderState.update { it.copy(isChatOpen = open) }
    }

    fun sendRiderChatMessage(text: String) {
        if (text.isBlank()) return
        val msg = ChatMessage(
            id = "msg_" + System.currentTimeMillis(),
            isFromUser = true,
            text = text,
            timestamp = "Just now"
        )
        _riderState.update {
            it.copy(chatMessages = it.chatMessages + msg)
        }

        // Automatic polite driver reply
        viewModelScope.launch {
            delay(1500)
            val driverReply = ChatMessage(
                id = "msg_rep_" + System.currentTimeMillis(),
                isFromUser = false,
                text = "Got it, thanks! Pulling up right now.",
                timestamp = "Just now"
            )
            _riderState.update {
                it.copy(chatMessages = it.chatMessages + driverReply)
            }
        }
    }

    fun openSplitDialog(open: Boolean) {
        _riderState.update { it.copy(isSplitDialogOpen = open) }
    }

    fun applySplitFare(friendEmailsOrNames: List<String>) {
        val count = friendEmailsOrNames.size + 1
        _riderState.update {
            it.copy(
                splitCount = count,
                splitFriends = friendEmailsOrNames,
                isSplitDialogOpen = false,
                systemMessage = "Fare split $count ways! ($${String.format(Locale.US, "%.2f", calculateCurrentFare(it.selectedVehicle) / count)} each)"
            )
        }
    }

    fun openSafetyDialog(open: Boolean) {
        _riderState.update { it.copy(isSafetyDialogOpen = open) }
    }

    fun submitRating(stars: Int, tip: Double) {
        _profileState.update {
            it.copy(walletBalance = (it.walletBalance - tip).coerceAtLeast(0.0))
        }
        _riderState.update {
            it.copy(
                isRatingDialogOpen = false,
                systemMessage = "Thanks for rating! $stars ⭐ rating submitted."
            )
        }
    }

    fun dismissRatingDialog() {
        _riderState.update { it.copy(isRatingDialogOpen = false) }
    }

    // --- Driver Mode Actions ---

    fun toggleDriverOnline() {
        val nextOnline = !_driverState.value.isOnline
        _driverState.update { it.copy(isOnline = nextOnline, currentOffer = null) }

        if (nextOnline) {
            // Trigger an incoming student ride request offer after 3 seconds
            driverOfferJob?.cancel()
            driverOfferJob = viewModelScope.launch {
                delay(3000)
                if (_driverState.value.isOnline && _driverState.value.activeTrip == null) {
                    val pickup = CampusData.CAMPUS_LOCATIONS[2] // Engineering
                    val dropoff = CampusData.CAMPUS_LOCATIONS[3] // North Dorms
                    val offer = DriverOffer(
                        id = "OFFER-" + (100..999).random(),
                        riderName = "Samantha Lee",
                        riderMajor = "Architecture '25 • 4.9⭐",
                        pickup = pickup,
                        dropoff = dropoff,
                        estimatedEarnings = 5.25,
                        distanceKm = 1.4,
                        etaMinutes = 4,
                        expiresInSeconds = 15
                    )
                    _driverState.update { it.copy(currentOffer = offer) }
                }
            }
        } else {
            driverOfferJob?.cancel()
        }
    }

    fun driverAcceptOffer() {
        val offer = _driverState.value.currentOffer ?: return
        val trip = ActiveRide(
            id = offer.id,
            pickup = offer.pickup,
            dropoff = offer.dropoff,
            vehicleType = VehicleType.CAMPUS_ECO,
            status = RideStatus.DRIVER_ON_WAY,
            driver = null,
            pinCode = "3814",
            distanceKm = offer.distanceKm,
            etaMinutes = offer.etaMinutes,
            fare = offer.estimatedEarnings,
            progress = 0f
        )
        _driverState.update {
            it.copy(
                currentOffer = null,
                activeTrip = trip,
                enteredPin = "",
                pinError = null
            )
        }
    }

    fun driverDeclineOffer() {
        _driverState.update { it.copy(currentOffer = null) }
    }

    fun driverMarkArrived() {
        _driverState.update {
            it.copy(
                activeTrip = it.activeTrip?.copy(status = RideStatus.DRIVER_ARRIVED)
            )
        }
    }

    fun updateDriverPin(pin: String) {
        _driverState.update { it.copy(enteredPin = pin, pinError = null) }
    }

    fun driverVerifyPinAndStart() {
        val currentTrip = _driverState.value.activeTrip ?: return
        if (_driverState.value.enteredPin == currentTrip.pinCode || _driverState.value.enteredPin == "3814") {
            _driverState.update {
                it.copy(
                    activeTrip = it.activeTrip?.copy(status = RideStatus.IN_PROGRESS),
                    pinError = null
                )
            }
        } else {
            _driverState.update {
                it.copy(pinError = "Incorrect student PIN code. Ask rider for their 4-digit PIN.")
            }
        }
    }

    fun driverCompleteActiveTrip() {
        val currentTrip = _driverState.value.activeTrip ?: return
        val earnings = currentTrip.fare

        _driverState.update {
            it.copy(
                activeTrip = null,
                todayEarnings = it.todayEarnings + earnings,
                completedTripsCount = it.completedTripsCount + 1,
                enteredPin = ""
            )
        }
    }

    // --- Profile & Wallet Actions ---

    fun topUpWallet(amount: Double) {
        _profileState.update {
            it.copy(walletBalance = it.walletBalance + amount)
        }
        _riderState.update {
            it.copy(systemMessage = "Added $${String.format(Locale.US, "%.2f", amount)} to Campus Wallet!")
        }
    }

    fun clearSystemMessage() {
        _riderState.update { it.copy(systemMessage = null) }
    }
}
