# Registro de consultas a IA — Fase 2

Este registro documenta las consultas realizadas en esta conversación. Cada integrante debe añadir las consultas de sus propias sesiones antes de entregar.

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

- Angie debe añadir las consultas realizadas desde sus propias sesiones.
- Completar las capturas finales cuando se terminen las mejoras de la Fase 2.
- Confirmar nombres completos y reparto de tareas con ambas integrantes.
