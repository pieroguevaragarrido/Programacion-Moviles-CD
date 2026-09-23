package com.tuapp.clinicasalud.model

data class Doctor(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double
)

val especialidades = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")

val doctoresDePrueba = listOf(
    Doctor(1, "Dra. Ana Torres", "Cardiología", 4.8),
    Doctor(2, "Dr. Luis Ramos", "Pediatría", 4.5),
    Doctor(3, "Dra. Marta Ríos", "Dermatología", 4.9),
    Doctor(4, "Dr. Jorge Paredes", "Cardiología", 4.6),
    Doctor(5, "Dra. Carla Vidal", "Pediatría", 4.7)
)