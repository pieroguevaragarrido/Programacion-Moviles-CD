package com.tuapp.navlab.data

data class Student(
    val id: Int,
    val name: String,
    val career: String,
    val email: String,
    val phone: String,
    val cycle: String,
    val bio: String,
    val initials: String
)

object StudentRepository {
    val students = listOf(
        Student(
            id = 1,
            name = "Piero Guevara",
            career = "Diseño y Desarrollo de Software",
            email = "piero.guevara@tecsup.edu.pe",
            phone = "+51 987 654 321",
            cycle = "V Ciclo",
            bio = "Estudiante apasionado por la programación móvil, desarrollo Android con Jetpack Compose y la arquitectura de software moderna.",
            initials = "PG"
        ),
        Student(
            id = 2,
            name = "María López",
            career = "Redes y Comun. de Datos",
            email = "maria.lopez@tecsup.edu.pe",
            phone = "+51 912 345 678",
            cycle = "IV Ciclo",
            bio = "Especialista en seguridad de redes, configuración de routers e infraestructura en la nube.",
            initials = "ML"
        ),
        Student(
            id = 3,
            name = "Carlos Mendoza",
            career = "Big Data y Analítica",
            email = "carlos.mendoza@tecsup.edu.pe",
            phone = "+51 923 456 789",
            cycle = "VI Ciclo",
            bio = "Interesado en Machine Learning, procesamiento de datos a gran escala y visualización de métricas.",
            initials = "CM"
        ),
        Student(
            id = 4,
            name = "Ana Flores",
            career = "Diseño y Desarrollo de Software",
            email = "ana.flores@tecsup.edu.pe",
            phone = "+51 934 567 890",
            cycle = "V Ciclo",
            bio = "Desarrolladora Frontend & UI/UX Designer enfocada en crear experiencias accesibles e intuitivas.",
            initials = "AF"
        ),
        Student(
            id = 5,
            name = "Jorge Ramírez",
            career = "Mantenimiento de Maquinaria",
            email = "jorge.ramirez@tecsup.edu.pe",
            phone = "+51 945 678 901",
            cycle = "III Ciclo",
            bio = "Técnico especialista en automatización industrial y mantenimiento preventivo de equipos pesados.",
            initials = "JR"
        ),
        Student(
            id = 6,
            name = "Lucía Torres",
            career = "Ciberseguridad",
            email = "lucia.torres@tecsup.edu.pe",
            phone = "+51 956 789 012",
            cycle = "V Ciclo",
            bio = "Investigadora en pruebas de penetración, análisis de vulnerabilidades y seguridad defensiva.",
            initials = "LT"
        ),
        Student(
            id = 7,
            name = "Diego Salazar",
            career = "Diseño y Desarrollo de Software",
            email = "diego.salazar@tecsup.edu.pe",
            phone = "+51 967 890 123",
            cycle = "VI Ciclo",
            bio = "Desarrollador Backend fascinado por arquitecturas de microservicios, Kotlin Ktor y bases de datos NoSQL.",
            initials = "DS"
        ),
        Student(
            id = 8,
            name = "Sofia Vargas",
            career = "Electrónica y Automatización",
            email = "sofia.vargas@tecsup.edu.pe",
            phone = "+51 978 901 234",
            cycle = "IV Ciclo",
            bio = "Entusiasta de la robótica, sistemas embebidos, Internet de las Cosas (IoT) y microcontroladores.",
            initials = "SV"
        )
    )

    fun getStudentById(id: Int): Student {
        return students.find { it.id == id } ?: students.first()
    }
}
