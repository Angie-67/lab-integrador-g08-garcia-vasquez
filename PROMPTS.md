# Registro de consultas a IA — Fase 2

Este registro documenta las consultas realizadas en esta conversación. Cada integrante debe añadir las consultas de sus propias sesiones antes de entregar.

## Persona A

## Consulta 1

- **Fecha:** 2026-11-08
- **Herramienta:** Android Studio AI (Gemini)
- **Prompt:** "Verifica que nos encontremos en la rama `mejora-ia` y que el working tree esté limpio antes de realizar cualquier cambio correspondiente a las tareas de la Persona A en la Fase 2."
- **Qué respondió (resumen):** Comprobó mediante comandos de Git la rama activa y el estado del repositorio, confirmando que estábamos en `mejora-ia` con los cambios previos sincronizados y listos para proceder.
- **Qué usamos o cambiamos y por qué:** Se aseguró el cumplimiento de la condición obligatoria de trabajar exclusivamente en la rama asignada sin afectar `main`.
- **Qué aprendimos:** Verificar el entorno de Git antes de iniciar modificaciones previene conflictos de ramas y asegura que el trabajo se mantenga en el flujo correcto.

## Consulta 2

- **Fecha:** 2026-11-08
- **Herramienta:** Android Studio AI (Gemini)
- **Prompt:** "Actúa como desarrollador Android experto en Kotlin, MVVM y pruebas unitarias. Como primera tarea de Persona A, amplía las pruebas de `ListaViewModel` con casos de catálogo vacío y reintento exitoso, sin repetir lo existente."
- **Qué respondió (resumen):** Analizó el archivo `ListaViewModelTest.kt` existente, diseñó los nuevos casos de prueba para vacíos y reintentos, y adaptó los fakes necesarios para simular las respuestas del repositorio.
- **Qué usamos o cambiamos y por qué:** Se añadió cobertura en `ListaViewModelTest.kt` para evaluar la correcta emisión de estados cuando la red falla y luego se recupera, o cuando el catálogo devuelve cero elementos.
- **Qué aprendimos:** Los ViewModels con flujos reactivos necesitan pruebas específicas para manejar escenarios de reintento de red y listas vacías sin perder la estabilidad de la UI.

## Consulta 3

- **Fecha:** 2026-11-08
- **Herramienta:** Android Studio AI (Gemini)
- **Prompt:** "Ejecuta las pruebas unitarias afectadas con Gradle y, si pasan correctamente, prepara el primer commit con el mensaje `test: ampliar cobertura de ListaViewModel con catálogo vacío y reintento`."
- **Qué respondió (resumen):** Ejecutó la tarea de pruebas en Gradle, verificó que las 6 pruebas unitarias pasaran sin errores y registró el primer commit en la rama `mejora-ia`.
- **Qué usamos o cambiamos y por qué:** Se aplicó el primer commit oficial (`e76a896`) asegurando que el código de la capa de presentación estuviera respaldado por pruebas verificadas.
- **Qué aprendimos:** Correr Gradle localmente antes de confirmar el commit garantiza que no existan errores de compilación ni fallos en las reglas de despacho de corrutinas (`MainDispatcherRule`).

## Consulta 4

- **Fecha:** 2026-11-08
- **Herramienta:** Android Studio AI (Gemini)
- **Prompt:** "Ahora pasa a la segunda tarea. Agrega pruebas con fakes para `ItemRepositoryImpl` que comprueben que se cuentan solo los Pokémon nuevos insertados, que una sincronización repetida no los vuelve a contar y que los favoritos se conservan."
- **Qué respondió (resumen):** Diseñó una nueva suite de pruebas (`ItemRepositoryImplTest.kt`) implementando fakes de `ItemDao` y `ApiService` para simular el comportamiento de la base de datos Room.
- **Qué usamos o cambiamos y por qué:** Se cubrieron los tres escenarios clave del repositorio: conteo preciso con `OnConflictStrategy.IGNORE`, idempotencia en sincronizaciones repetidas y persistencia de favoritos.
- **Qué aprendimos:** El uso de dobles de prueba (fakes) que implementan las interfaces DAO de Room permite probar la lógica de sincronización y mapeo de datos de forma rápida y aislada.

## Consulta 5

- **Fecha:** 2026-11-08
- **Herramienta:** Android Studio AI (Gemini)
- **Prompt:** "Ejecuta todas las pruebas unitarias del proyecto para validar Room y ViewModel juntos, y realiza el segundo commit con el mensaje `test: verificar inserciones nuevas y favoritos en Room`."
- **Qué respondió (resumen):** Ejecutó la compilación y validación con Gradle, confirmando que las 9 pruebas unitarias terminaron con éxito (`9 passed, 0 failed`), y registró el segundo commit (`34d1d9b`).
- **Qué usamos o cambiamos y por qué:** Se concluyó la implementación de la Fase 2 para la Persona A con ambos commits verificados y documentados.
- **Qué aprendimos:** Las pruebas unitarias de repositorios y ViewModels desacoplados permiten validar la arquitectura MVVM completa sin depender de emuladores ni instrumentación pesada.

## Persona B 

## Consulta 1

- **Fecha:** 2026-11-08
- **Herramienta:** ChatGPT / Codex
- **Prompt:** Revisar el texto pegado con propuestas para corregir el conteo de inserciones de Room, persistencia de navegación, contratos de autenticación e ilustración del detalle.
- **Qué respondió (resumen):** Contrastó las propuestas con el proyecto. Encontró que la navegación con `rememberSaveable`, el contrato de autenticación y la ilustración ya estaban implementados; el conteo real de inserciones seguía pendiente.
- **Qué usamos o cambiamos y por qué:** Se mantuvo como pendiente el conteo de Room. Más tarde se integró el cambio que hace que el DAO devuelva los IDs insertados y cuente solo las filas nuevas.
- **Qué aprendimos:** Con `OnConflictStrategy.IGNORE`, Room permite distinguir inserciones nuevas de filas ignoradas por conflicto.

## Consulta 2

- **Fecha:** 2026-11-08
- **Herramienta:** ChatGPT / Codex
- **Prompt:** "creamos una pantalla de bienvenida"
- **Qué respondió (resumen):** Propuso integrar una pantalla inicial en Compose que conduce al inicio de sesión y se muestra mientras se verifica la sesión guardada.
- **Qué usamos o cambiamos y por qué:** Se creó `WelcomeScreen` con una ilustración de Poké Ball y un botón de inicio de sesión para dar contexto a la app antes de autenticar.
- **Qué aprendimos:** La bienvenida debe convivir con la restauración de sesión para no interrumpir el acceso automático de una cuenta ya autenticada.

## Consulta 3

- **Fecha:** 2026-11-08
- **Herramienta:** ChatGPT / Codex
- **Prompt:** Quitar de la bienvenida las etiquetas “Catálogo”, “Favoritos” y “Detalles”, señaladas en una captura.
- **Qué respondió (resumen):** Eliminó las etiquetas solicitadas y volvió a compilar e instalar la aplicación en el emulador.
- **Qué usamos o cambiamos y por qué:** Se retiraron las tres etiquetas y se conservó el resto del diseño.
- **Qué aprendimos:** Una captura de referencia ayuda a precisar qué elemento visual debe cambiar sin rediseñar toda la pantalla.

## Consulta 4

- **Fecha:** 2026-11-08
- **Herramienta:** ChatGPT / Codex
- **Prompt:** Crear un README con instrucciones y capturas.
- **Qué respondió (resumen):** Revisó la configuración del proyecto, añadió un README con funcionalidades, requisitos, Firebase, ejecución y capturas de bienvenida e inicio de sesión.
- **Qué usamos o cambiamos y por qué:** Se conservó el README como guía de instalación del repositorio; se amplió después con el grupo, variante, creación de perfiles de prueba y reparto provisional.
- **Qué aprendimos:** La configuración de `google-services.json` debe documentarse sin subir el archivo ni incluir contraseñas.

## Consulta 5

- **Fecha:** 2026-11-08
- **Herramienta:** ChatGPT / Codex
- **Prompt:** Revisar qué requisitos del laboratorio faltaban según el tema Pokémon tipo fuego.
- **Qué respondió (resumen):** Comparó la aplicación con la rúbrica y detectó que el admin no podía marcar Pokémon como favoritos ni abrir la pantalla Favoritos.
- **Qué usamos o cambiamos y por qué:** Se añadió Favoritos a la navegación del admin y se habilitó el control de like para los Pokémon del catálogo en ambos roles, alineando el comportamiento con la guía.
- **Qué aprendimos:** Las tablas de permisos por rol de la guía deben contrastarse con las condiciones reales de navegación y de cada componente.

## Consulta 6

- **Fecha:** 2026-11-08
- **Herramienta:** ChatGPT / Codex
- **Prompt:** Usar la guía de roles de la Pokédex para preparar la documentación y el reparto del trabajo.
- **Qué respondió (resumen):** Alineó la estructura propuesta con el flujo de dos personas: Persona A se enfoca en API, Room y catálogo; Persona B en login, roles, navegación y pantallas complementarias.
- **Qué usamos o cambiamos y por qué:** Se preparó texto para Google Docs con el reparto provisional basado en autores y áreas de los commits. Los nombres completos y el reparto real quedan para confirmación del equipo.
- **Qué aprendimos:** Acordar el modelo `Item` y las firmas de `ItemRepository` antes de dividir archivos reduce conflictos durante la integración.

## Consulta 7

- **Fecha:** 2026-11-08
- **Herramienta:** ChatGPT / Codex
- **Prompt:** Revisar el estado de la Fase 2 y contrastar los commits visibles en la rama `mejora-ia`.
- **Qué respondió (resumen):** Actualizó la rama local y encontró los commits de pruebas para lista vacía, reintento, conteo de nuevos registros y conservación de favoritos. Ejecutó `./gradlew.bat test`, que terminó correctamente.
- **Qué usamos o cambiamos y por qué:** Se incorporaron los commits remotos de pruebas a la rama local para que el estado de trabajo coincida con GitHub.
- **Qué aprendimos:** Conviene actualizar las referencias remotas antes de contar los commits propios de una rama.

## Pendientes para cerrar este registro

- Completar las capturas finales cuando se terminen las mejoras de la Fase 2.
