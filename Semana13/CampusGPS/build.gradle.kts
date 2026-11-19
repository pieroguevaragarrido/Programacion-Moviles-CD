// AGP 9 trae Kotlin integrado, pero algunas librerías ya vienen compiladas con Kotlin 2.4.
// Se fuerza esa versión del compilador de Kotlin aquí:
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.0")
    }
}

plugins {
    id("com.android.application") version "9.1.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.0" apply false
}
