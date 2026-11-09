# Pokédex Pokémon

Aplicación Android para explorar Pokémon de tipo fuego, consultar sus detalles y guardar favoritos. El proyecto usa Jetpack Compose para la interfaz, Firebase para autenticación y roles, Room para conservar el catálogo local y PokéAPI como fuente de datos.

**Grupo:** G08 · **Tema:** Pokémon · **Variante:** tipo fuego (`fire`).

## Capturas

Agrega las capturas al completar la Fase 2.

| Pantalla | Espacio para la captura |
|---|---|
| Inicio de sesión | _Pegar captura aquí_ |
| Catálogo de Pokémon de tipo fuego | _Pegar captura aquí_ |
| Detalle de un Pokémon | _Pegar captura aquí_ |
| Favoritos | _Pegar captura aquí_ |
| Panel de administración | _Pegar captura aquí_ |
| Notificación de sincronización | _Pegar captura aquí_ |

## Funcionalidades

- Inicio de sesión con correo y contraseña mediante Firebase Authentication.
- Roles `admin` y `usuario` consultados en Firestore.
- Catálogo de Pokémon de tipo fuego desde [PokéAPI](https://pokeapi.co/).
- Detalles con ilustración, tipos, características, estadísticas y habilidades.
- Catálogo y favoritos persistidos en Room; el catálogo guardado puede consultarse sin conexión.
- Panel de administración para sincronizar el catálogo, ver totales y limpiar la caché local.
- Notificación al terminar la sincronización, sujeta al permiso de notificaciones de Android.
- Interfaz desarrollada con Jetpack Compose y Material 3.

## Requisitos

- Android Studio compatible con Android Gradle Plugin `9.3.3`.
- Android SDK Platform `37`.
- JDK `25` (el proyecto configura el toolchain de Gradle para esta versión).
- Una configuración de Firebase para Android con el identificador `com.tecsup.catalogo_pokemon`.

## Configuración de Firebase

1. Crea o selecciona un proyecto en Firebase y registra una aplicación Android con el identificador `com.tecsup.catalogo_pokemon`.
2. Descarga el archivo `google-services.json` y colócalo en `app/google-services.json`. Este archivo está excluido de Git porque contiene configuración propia del proyecto Firebase.
3. Habilita el proveedor **Correo electrónico/contraseña** en Firebase Authentication.
4. En **Authentication → Usuarios**, crea dos cuentas de prueba: una para el rol `admin` y otra para `usuario`. Define una contraseña para cada una y guarda sus correos y contraseñas para probar el inicio de sesión; no las agregues al repositorio.
5. Copia el UID de cada cuenta. En Cloud Firestore, crea la colección `usuarios` y un documento cuyo ID sea exactamente ese UID. En cada documento agrega `nombre` (string) y `rol` (string): `admin` para la cuenta administradora y `usuario` para la cuenta de usuario.
6. Configura las reglas de Firestore para que cada persona pueda leer solo su perfil. Por ejemplo, si la app solo necesita leer perfiles, puedes denegar las escrituras desde el cliente:

   ```text
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /usuarios/{uid} {
         allow read: if request.auth != null && request.auth.uid == uid;
         allow write: if false;
       }
     }
   }
   ```

   Crea y cambia los roles desde Firebase Console o desde un entorno confiable. No permitas que una persona cambie su propio campo `rol` desde la app.

La aplicación espera el documento `usuarios/{uid}` para recuperar el rol. Si falta el documento o el rol tiene otro valor, no se completará el inicio de sesión.

## Compilar y ejecutar

Abre el proyecto en Android Studio, espera a que termine la sincronización de Gradle y ejecuta la configuración `app` en un emulador o dispositivo con Android 7.0 (API 24) o superior.

También puedes usar el wrapper desde la raíz del repositorio:

### Windows (PowerShell)

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:installDebug
```

### macOS o Linux

```bash
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

El APK de depuración se genera en `app/build/outputs/apk/debug/app-debug.apk`.

## Estructura del proyecto

```text
app/src/main/java/com/tecsup/catalogo_pokemon/
├── data/
│   ├── auth/          # Firebase Authentication y perfiles Firestore
│   ├── local/         # Entidades, DAO y base de datos Room
│   ├── mapper/        # Conversión entre DTO, entidades y dominio
│   ├── remote/        # Cliente Retrofit y DTO de PokéAPI
│   └── repository/    # Repositorio del catálogo
├── domain/model/      # Modelos Item y Sesion/Rol
├── notification/      # Notificaciones de sincronización
└── ui/                # Pantallas Compose, ViewModels, navegación y tema
```

## Reparto de tareas

El siguiente reparto se resume a partir de los autores y archivos de sus commits. Confirmen los nombres completos y ajusten la lista para que refleje también el trabajo realizado fuera de Git.

| Integrante (autor en Git) | Áreas principales |
|---|---|
| Mayra (`mayragarcia-dev`) | Interfaz Compose y tema; autenticación, sesión y roles; navegación y pantallas de catálogo, detalle, favoritos y administración; notificaciones y pruebas del ViewModel de lista. |
| Angie (`angieio`) | Estructura inicial y contratos; integración de PokéAPI con Retrofit y DTO; persistencia con Room; repositorio, mappers e inyección de dependencias. |

## Tecnologías

- Kotlin y Android Gradle Plugin.
- Jetpack Compose y Material 3.
- Firebase Authentication y Cloud Firestore.
- Room con KSP.
- Retrofit, OkHttp y Kotlin Serialization.
- Coroutines y StateFlow.
