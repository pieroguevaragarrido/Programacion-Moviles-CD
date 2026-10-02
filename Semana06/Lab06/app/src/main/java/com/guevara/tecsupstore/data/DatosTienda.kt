package com.guevara.tecsupstore.data

import com.guevara.tecsupstore.model.Producto
import com.guevara.tecsupstore.model.Seccion
import com.guevara.tecsupstore.model.Usuario

val usuarioActual = Usuario(
    nombre = "Juan José León",
    correo = "estudiante@tecsup.edu.pe"
)

val secciones = listOf(
    Seccion(
        titulo = "Tecnología",
        productos = listOf(
            Producto(1, "Audífonos Bluetooth", 89.90, "🎧", "Audífonos inalámbricos con micrófono."),
            Producto(2, "Mouse inalámbrico", 39.50, "🖱️", "Mouse óptico de 1600 DPI."),
            Producto(3, "Memoria USB 64 GB", 29.00, "💾", "Memoria USB 3.0 de alta velocidad.")
        )
    ),
    Seccion(
        titulo = "Útiles",
        productos = listOf(
            Producto(4, "Cuaderno A4", 12.00, "📓", "Cuaderno cuadriculado de 100 hojas."),
            Producto(5, "Pack de lapiceros", 8.50, "🖊️", "Pack de 6 lapiceros de tinta azul y negra."),
            Producto(6, "Calculadora científica", 65.00, "🧮", "Calculadora con 240 funciones.")
        )
    ),
    Seccion(
        titulo = "Ropa",
        productos = listOf(
            Producto(7, "Polo TECSUP", 35.00, "👕", "Polo de algodón con logo institucional."),
            Producto(8, "Casaca TECSUP", 120.00, "🧥", "Casaca ligera con capucha."),
            Producto(9, "Gorra TECSUP", 25.00, "🧢", "Gorra ajustable con logo bordado.")
        )
    )
)

val categorias = listOf("Todo") + secciones.map { it.titulo }
