package com.tuapp.clinicasalud.navigation

sealed class Screen(val route: String) {
    object Home : Screen(route = "home")
    object MyAppointments : Screen(route = "my_appointments")
    object History : Screen(route = "history")

    object DoctorProfile : Screen(route = "doctor_profile/{doctorId}") {
        fun createRoute(doctorId: Int): String = "doctor_profile/$doctorId"
    }

    object BookAppointment : Screen(route = "book_appointment/{doctorId}") {
        fun createRoute(doctorId: Int): String = "book_appointment/$doctorId"
    }

    object Confirmation : Screen(route = "confirmation/{doctorId}/{date}/{time}") {
        fun createRoute(doctorId: Int, date: String, time: String): String =
            "confirmation/$doctorId/$date/$time"
    }
}