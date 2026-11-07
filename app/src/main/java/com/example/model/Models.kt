package com.example.model

data class CampusPoint(
    val x: Float, // Normalized 0f..1f across canvas
    val y: Float
)

data class CampusLocation(
    val id: String,
    val name: String,
    val buildingCode: String,
    val category: CampusCategory,
    val address: String,
    val point: CampusPoint,
    val isPopular: Boolean = false
)

enum class CampusCategory(val label: String) {
    ACADEMIC("Academic"),
    RESIDENCE("Dorms & Housing"),
    TRANSIT("Transit Hubs"),
    STUDY_LEISURE("Campus Center & Food"),
    SPORTS("Athletics & Gym")
}

enum class VehicleType(
    val tierId: String,
    val title: String,
    val subtitle: String,
    val baseFare: Double,
    val perKmRate: Double,
    val capacity: Int,
    val badge: String?,
    val isNightFreeEligible: Boolean = false
) {
    CAMPUS_POOL(
        tierId = "pool",
        title = "Campus Carpool",
        subtitle = "Shared with verified students • Eco & low fare",
        baseFare = 2.00,
        perKmRate = 0.65,
        capacity = 3,
        badge = "Cheapest"
    ),
    CAMPUS_ECO(
        tierId = "eco",
        title = "Campus Express",
        subtitle = "Direct point-to-point across university quads",
        baseFare = 3.00,
        perKmRate = 0.95,
        capacity = 4,
        badge = "Fastest"
    ),
    STUDY_COMFORT(
        tierId = "comfort",
        title = "Study Comfort",
        subtitle = "Quiet ride, laptop desk space & phone chargers",
        baseFare = 4.50,
        perKmRate = 1.30,
        capacity = 4,
        badge = "Quiet Space"
    ),
    CAMPUS_SAFE_RIDE(
        tierId = "safe_ride",
        title = "SafeRide Night Escort",
        subtitle = "Vetted campus patrol • Free with student ID after 8 PM",
        baseFare = 0.00,
        perKmRate = 0.00,
        capacity = 2,
        badge = "FREE / 8PM-4AM",
        isNightFreeEligible = true
    ),
    CAMPUS_VAN_XL(
        tierId = "van_xl",
        title = "Study Squad XL",
        subtitle = "Move-in day, group study gear, sports teams",
        baseFare = 5.50,
        perKmRate = 1.60,
        capacity = 6,
        badge = "6 Seats"
    )
}

enum class RideStatus {
    IDLE,
    SELECTING_ROUTE,
    CONFIRM_BOOKING,
    SEARCHING_DRIVER,
    DRIVER_ON_WAY,
    DRIVER_ARRIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

data class StudentDriver(
    val id: String,
    val name: String,
    val initials: String,
    val major: String,
    val graduationYear: String,
    val rating: Float,
    val totalCampusTrips: Int,
    val carModel: String,
    val carColor: String,
    val plateNumber: String,
    val position: CampusPoint,
    val phone: String
)

data class ActiveRide(
    val id: String,
    val pickup: CampusLocation,
    val dropoff: CampusLocation,
    val vehicleType: VehicleType,
    val status: RideStatus,
    val driver: StudentDriver?,
    val pinCode: String,
    val distanceKm: Double,
    val etaMinutes: Int,
    val fare: Double,
    val studentDiscountApplied: Boolean = true,
    val splitCount: Int = 1,
    val splitFriends: List<String> = emptyList(),
    val progress: Float = 0f // 0f to 1f along route
)

data class ChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val text: String,
    val timestamp: String
)

data class RideHistoryItem(
    val id: String,
    val date: String,
    val pickupName: String,
    val dropoffName: String,
    val vehicleTitle: String,
    val totalCost: Double,
    val driverName: String,
    val driverMajor: String,
    val rating: Int,
    val isSplit: Boolean
)

data class DriverOffer(
    val id: String,
    val riderName: String,
    val riderMajor: String,
    val pickup: CampusLocation,
    val dropoff: CampusLocation,
    val estimatedEarnings: Double,
    val distanceKm: Double,
    val etaMinutes: Int,
    val expiresInSeconds: Int = 15
)
