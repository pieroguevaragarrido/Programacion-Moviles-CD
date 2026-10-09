package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.*

object Repositorio {
    private val usuarios = mutableListOf<Usuario>()
    private val especialidades = mutableListOf(
        Especialidad(1, "Medicina General", "Consulta integral y chequeos preventivos"),
        Especialidad(2, "Pediatría", "Atención médica especializada para niños"),
        Especialidad(3, "Cardiología", "Diagnóstico y tratamiento del corazón"),
        Especialidad(4, "Odontología", "Salud bucal y tratamientos dentales"),
        Especialidad(5, "Ginecología", "Salud integral de la mujer"),
        Especialidad(6, "Traumatología", "Lesiones de huesos y articulaciones"),
        Especialidad(7, "Oftalmología", "Cuidado de la visión y ojos")
    )
    private val medicos = mutableListOf(
        Medico(1, "Dra. Ana Torres", 1, "Medicina General", "S/ 80.00"),
        Medico(2, "Dr. Luis Ramos", 1, "Medicina General", "S/ 80.00"),
        Medico(3, "Dra. Gisela Rojas", 2, "Pediatría", "S/ 90.00"),
        Medico(4, "Dr. Carlos Mendoza", 3, "Cardiología", "S/ 120.00"),
        Medico(5, "Dra. Elena Silva", 4, "Odontología", "S/ 100.00")
    )
    private val citas = mutableListOf<Cita>()
    private val horariosBase = listOf("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00")

    var usuarioActual: Usuario? = Usuario(1, "Juan Perez", "juan@correo.com", "12345678", "123456")

    fun registrarUsuario(usuario: Usuario): Boolean {
        if (usuarios.any { it.email == usuario.email }) return false
        usuarios.add(usuario)
        usuarioActual = usuario
        return true
    }

    fun iniciarSesion(email: String, clave: String): Usuario? {
        val user = usuarios.find { it.email == email && it.clave == clave }
        if (user != null) {
            usuarioActual = user
        }
        return user
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun buscarEspecialidades(query: String): List<Especialidad> {
        if (query.isBlank()) return especialidades
        return especialidades.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun especialidadesDestacadas(): List<Especialidad> = especialidades.take(4)

    fun obtenerEspecialidad(id: Int): Especialidad? = especialidades.find { it.id == id }
    fun obtenerMedico(id: Int): Medico? = medicos.find { it.id == id }
    fun obtenerCita(id: Int): Cita? = citas.find { it.id == id }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos.filter { it.especialidadId == especialidadId }
    }

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupados = citas.filter { it.medicoId == medicoId && it.fecha == fecha }.map { it.hora }
        return horariosBase.filter { it !in ocupados }
    }

    fun agendarCita(medicoId: Int, fecha: String, hora: String): Cita? {
        val user = usuarioActual ?: return null
        val medico = obtenerMedico(medicoId) ?: return null
        val nuevaCita = Cita(
            id = citas.size + 1,
            usuarioId = user.id,
            medicoId = medico.id,
            medicoNombre = medico.nombre,
            especialidadNombre = medico.especialidadNombre,
            fecha = fecha,
            hora = hora,
            precio = medico.precio
        )
        citas.add(nuevaCita)
        return nuevaCita
    }

    fun citasDelUsuario(): List<Cita> {
        val userId = usuarioActual?.id ?: return emptyList()
        return citas.filter { it.usuarioId == userId }
    }

    fun cancelarCita(id: Int): Boolean {
        return citas.removeIf { it.id == id }
    }
}
