Ejercicio Práctico - Registro de Notas (Jetpack Compose)

**Autor:** Piero Guevara
**Curso:** Programación en Móviles — 4to ciclo
**Sección:** C24-C

Pantalla que calcula el promedio ponderado de 4 cursos usando controles
nuevos de Jetpack Compose: Slider (nota 0-20), Switch (redondear promedio
final), Checkbox (confirmación) y Card de resultado con observación
según rango de promedio.

Cursos y pesos:
- Fundamentos de Programación: 20%
- Programación Orientada a Objetos: 25%
- Programación en Móviles: 30%
- Base de Datos: 25%

## Captura
<img width="221" height="454" alt="image" src="https://github.com/user-attachments/assets/6e3f5da2-0b69-4160-a160-41d7c94cf21e" />
<img width="216" height="451" alt="image" src="https://github.com/user-attachments/assets/0ee9b961-a647-473f-a369-6b5669a46fcc" />
<img width="215" height="453" alt="image" src="https://github.com/user-attachments/assets/55f3cb70-83b0-446e-b27a-04bac1917340" />
<img width="215" height="449" alt="image" src="https://github.com/user-attachments/assets/7c477cd1-ac3d-4133-96af-091e30d69417" />


Casos de prueba verificados:

| Notas (F, POO, M, BD) | Redondear | Ponderado | Final | Observación |
|---|---|---|---|---|
| 15, 13, 16, 14 | Sí | 14.55 | 15 | APROBADO |
| 12, 10, 11, 9 | No | 10.45 | 10.45 | EN RECUPERACIÓN |
| 18, 17, 19, 18 | Sí | 18.05 | 18 | EXCELENTE |
| 8, 9, 7, 10 | No | 8.45 | 8.45 | DESAPROBADO |

Los 4 casos coincidieron con lo esperado al probar en el emulador.

Reto opcional implementado:
Semáforo de color en el badge de cada Slider: rojo si la nota es menor
a 13, verde si es 13 o más.

Commits:
1. `Agrega filas de curso con Slider y badge de nota`
2. `Agrega switch, checkbox y boton condicionado a confirmacion`
3. `Implementa calculo de promedio, tarjeta de resultado y diseno final`
