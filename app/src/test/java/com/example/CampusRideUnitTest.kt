package com.example

import com.example.data.CampusData
import com.example.model.VehicleType
import com.example.ui.viewmodel.CampusViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CampusRideUnitTest {

    private lateinit var viewModel: CampusViewModel

    @Before
    fun setUp() {
        viewModel = CampusViewModel()
    }

    @Test
    fun testInitialRiderState() {
        val state = viewModel.riderState.value
        assertNotNull(state.pickup)
        assertNotNull(state.dropoff)
        assertEquals(VehicleType.CAMPUS_POOL, state.selectedVehicle)
        assertTrue(state.studentDiscountApplied)
    }

    @Test
    fun testFareCalculationWithStudentDiscount() {
        val poolFare = viewModel.calculateCurrentFare(VehicleType.CAMPUS_POOL)
        assertTrue("Fare should be greater than 0", poolFare > 0.0)

        // SafeRide should be FREE for students
        val safeRideFare = viewModel.calculateCurrentFare(VehicleType.CAMPUS_SAFE_RIDE)
        assertEquals(0.0, safeRideFare, 0.001)
    }

    @Test
    fun testSplitFareCalculation() {
        val friends = listOf("Taylor", "Jordan", "Sam")
        viewModel.applySplitFare(friends)

        val state = viewModel.riderState.value
        assertEquals(4, state.splitCount)
        assertEquals(3, state.splitFriends.size)
    }

    @Test
    fun testDriverOnlineToggle() {
        assertEquals(false, viewModel.driverState.value.isOnline)
        viewModel.toggleDriverOnline()
        assertEquals(true, viewModel.driverState.value.isOnline)
    }

    @Test
    fun testWalletTopUp() {
        val initialBalance = viewModel.profileState.value.walletBalance
        viewModel.topUpWallet(20.0)
        assertEquals(initialBalance + 20.0, viewModel.profileState.value.walletBalance, 0.01)
    }
}
