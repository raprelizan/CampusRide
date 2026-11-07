package com.example.data

import com.example.model.CampusCategory
import com.example.model.CampusLocation
import com.example.model.CampusPoint
import com.example.model.RideHistoryItem
import com.example.model.StudentDriver

object CampusData {

    val CAMPUS_LOCATIONS = listOf(
        CampusLocation(
            id = "loc_library",
            name = "W.E.B. Du Bois Central Library",
            buildingCode = "LIB-A",
            category = CampusCategory.ACADEMIC,
            address = "Central Plaza, Level 1",
            point = CampusPoint(0.62f, 0.32f),
            isPopular = true
        ),
        CampusLocation(
            id = "loc_student_union",
            name = "Student Union & Dining Commons",
            buildingCode = "SU-101",
            category = CampusCategory.STUDY_LEISURE,
            address = "Campus Center Way",
            point = CampusPoint(0.48f, 0.48f),
            isPopular = true
        ),
        CampusLocation(
            id = "loc_engineering",
            name = "Turing Engineering & Robotics Complex",
            buildingCode = "ENG-4",
            category = CampusCategory.ACADEMIC,
            address = "North Tech Quad",
            point = CampusPoint(0.32f, 0.30f),
            isPopular = true
        ),
        CampusLocation(
            id = "loc_north_dorms",
            name = "Orchard Hill Student Housing",
            buildingCode = "DORM-N",
            category = CampusCategory.RESIDENCE,
            address = "North Campus Residential Loop",
            point = CampusPoint(0.22f, 0.18f),
            isPopular = true
        ),
        CampusLocation(
            id = "loc_south_apartments",
            name = "University Village Grad Apartments",
            buildingCode = "UV-3",
            category = CampusCategory.RESIDENCE,
            address = "South Elm Avenue",
            point = CampusPoint(0.78f, 0.72f),
            isPopular = true
        ),
        CampusLocation(
            id = "loc_metro",
            name = "University Central Metro & Transit Hub",
            buildingCode = "TRN-M",
            category = CampusCategory.TRANSIT,
            address = "Subway Entrance & Bus Loop",
            point = CampusPoint(0.50f, 0.86f),
            isPopular = true
        ),
        CampusLocation(
            id = "loc_gym",
            name = "Recreation Center & Olympic Pool",
            buildingCode = "GYM-C",
            category = CampusCategory.SPORTS,
            address = "West Stadium Road",
            point = CampusPoint(0.20f, 0.65f),
            isPopular = false
        ),
        CampusLocation(
            id = "loc_science",
            name = "Bio-Medical Research Hall",
            buildingCode = "SCI-2",
            category = CampusCategory.ACADEMIC,
            address = "East Science Walk",
            point = CampusPoint(0.82f, 0.38f),
            isPopular = false
        ),
        CampusLocation(
            id = "loc_main_gate",
            name = "Historic Main Gate & Clocktower",
            buildingCode = "GATE-1",
            category = CampusCategory.TRANSIT,
            address = "1870 University Boulevard",
            point = CampusPoint(0.50f, 0.12f),
            isPopular = false
        ),
        CampusLocation(
            id = "loc_downtown",
            name = "College Town & Coffee Roasters",
            buildingCode = "TOWN",
            category = CampusCategory.STUDY_LEISURE,
            address = "Main Street Arts District",
            point = CampusPoint(0.15f, 0.88f),
            isPopular = true
        )
    )

    val STUDENT_DRIVERS = listOf(
        StudentDriver(
            id = "drv_1",
            name = "Marcus Vance",
            initials = "MV",
            major = "Computer Science '25",
            graduationYear = "Class of 2025",
            rating = 4.96f,
            totalCampusTrips = 342,
            carModel = "Tesla Model 3 • White",
            carColor = "White",
            plateNumber = "UNI-7729",
            position = CampusPoint(0.56f, 0.38f),
            phone = "+1 (555) 234-5678"
        ),
        StudentDriver(
            id = "drv_2",
            name = "Elena Rostova",
            initials = "ER",
            major = "Mechanical Eng '24",
            graduationYear = "Class of 2024",
            rating = 4.98f,
            totalCampusTrips = 512,
            carModel = "Toyota Prius Hybrid • Sky Blue",
            carColor = "Sky Blue",
            plateNumber = "CAM-4410",
            position = CampusPoint(0.40f, 0.55f),
            phone = "+1 (555) 876-5432"
        ),
        StudentDriver(
            id = "drv_3",
            name = "Jordan Blake",
            initials = "JB",
            major = "Business Administration '26",
            graduationYear = "Class of 2026",
            rating = 4.91f,
            totalCampusTrips = 189,
            carModel = "Honda Civic • Emerald Grey",
            carColor = "Grey",
            plateNumber = "STU-9921",
            position = CampusPoint(0.25f, 0.28f),
            phone = "+1 (555) 432-1098"
        ),
        StudentDriver(
            id = "drv_4",
            name = "Officer Davies & Cadet Chen",
            initials = "CC",
            major = "Campus Safety Escort Corps",
            graduationYear = "SafeRide Patrol Unit 4",
            rating = 5.0f,
            totalCampusTrips = 890,
            carModel = "Campus SafeRide EV Shuttle",
            carColor = "Official University Patrol",
            plateNumber = "SAFE-04",
            position = CampusPoint(0.52f, 0.52f),
            phone = "+1 (555) 800-SAFE"
        )
    )

    val CANNED_STUDENT_MESSAGES = listOf(
        "I'm waiting outside the main entrance 🎓",
        "By the bicycle racks near Hall B 🚲",
        "Wearing a navy college hoodie 🧥",
        "Coming down the elevator, 1 minute! ⏳",
        "I have a project presentation board with me 📐",
        "Thanks! I see your car flashing hazard lights 🚗"
    )

    val INITIAL_HISTORY = listOf(
        RideHistoryItem(
            id = "trip_101",
            date = "Yesterday, 9:45 PM",
            pickupName = "W.E.B. Du Bois Central Library",
            dropoffName = "Orchard Hill Student Housing",
            vehicleTitle = "SafeRide Night Escort",
            totalCost = 0.00,
            driverName = "SafeRide Patrol Unit 4",
            driverMajor = "Campus Safety",
            rating = 5,
            isSplit = false
        ),
        RideHistoryItem(
            id = "trip_100",
            date = "Nov 4, 3:15 PM",
            pickupName = "Student Union",
            dropoffName = "University Central Metro Hub",
            vehicleTitle = "Campus Carpool (Shared)",
            totalCost = 3.25,
            driverName = "Elena Rostova",
            driverMajor = "Mechanical Eng '24",
            rating = 5,
            isSplit = true
        ),
        RideHistoryItem(
            id = "trip_099",
            date = "Nov 2, 8:20 AM",
            pickupName = "Orchard Hill Student Housing",
            dropoffName = "Turing Engineering Complex",
            vehicleTitle = "Campus Express",
            totalCost = 4.10,
            driverName = "Marcus Vance",
            driverMajor = "Computer Science '25",
            rating = 5,
            isSplit = false
        )
    )
}
