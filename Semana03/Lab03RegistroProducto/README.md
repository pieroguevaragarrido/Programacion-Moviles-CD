Lab03 - Registro de Producto (Jetpack Compose)

**Autor:** Piero Guevara
**Curso:** Programación en Móviles 
**Sección:** C24-C

Pantalla de registro de producto con Jetpack Compose: campos de nombre,
precio y cantidad con estado (OutlinedTextField), botón para agregar y
Card de resumen con el importe calculado.

Capturas:

<img width="197" height="187" alt="image" src="https://github.com/user-attachments/assets/c8505c90-dc06-40d7-b86d-974408d19877" />

<img width="200" height="257" alt="image" src="https://github.com/user-attachments/assets/1b40733d-13f7-4291-a7c0-04c2489b5d9c" />

¿Qué pasa si declaras las variables SIN remember?

Al quitar remember, probé escribir en el campo de nombre y no me dejaba, tecleaba algo
y simplemente no aparecia. Entendí que sin remember, Compose no guarda el valor entre 
recomposiciones cada vez que la pantalla se redibuja, la variable vuelve a su estado inicial 
vacío. Con remember sí "recuerda" lo que había antes de redibujar, por eso el campo funciona.

Mejora con IA:

| Prompt que usé | Qué generó Gemini | Qué acepté o corregí                                                                                                             |
|---|---|----------------------------------------------------------------------------------------------------------------------------------|
| Agregar validación de campos vacíos y botón Limpiar en PantallaRegistro | Validación con isEmpty() y botón Limpiar igual de estilo que Agregar | Cambié isEmpty por isBlank (detectaba mal los espacios) y usé OutlinedButton en Limpiar para no romper la regla de un solo color |