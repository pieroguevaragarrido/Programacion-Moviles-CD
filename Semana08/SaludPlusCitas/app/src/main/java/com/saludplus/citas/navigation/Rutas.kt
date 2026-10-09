package com.saludplus.citas.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Registro : Rutas("registro")
    object Login : Rutas("login")
    object Home : Rutas("home")
    object Especialidades : Rutas("especialidades")
    object Medicos : Rutas("medicos/{especialidadId}") {
        fun crearRuta(especialidadId: Int) = "medicos/$especialidadId"
    }
    object FechaHora : Rutas("fechahora/{medicoId}") {
        fun crearRuta(medicoId: Int) = "fechahora/$medicoId"
    }
    object ConfirmarCita : Rutas("confirmar/{medicoId}/{fecha}/{hora}") {
        fun crearRuta(medicoId: Int, fecha: String, hora: String) = "confirmar/$medicoId/$fecha/$hora"
    }
    object CitaExitosa : Rutas("cita_exitosa/{citaId}") {
        fun crearRuta(citaId: Int) = "cita_exitosa/$citaId"
    }
    object MisCitas : Rutas("mis_citas")
    object Perfil : Rutas("perfil")
    object DetalleCita : Rutas("detalle_cita/{citaId}") {
        fun crearRuta(citaId: Int) = "detalle_cita/$citaId"
    }
    object Resultados : Rutas("resultados")
    object Notificaciones : Rutas("notificaciones")
    object Terminos : Rutas("terminos")
}
