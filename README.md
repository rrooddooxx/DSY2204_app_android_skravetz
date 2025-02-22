# Proyecto Android: App "Lazarillo"

![lazarillo_con_texto_header.svg](lazarillo_con_texto_header.svg)![laz.svg](LOGO_NO_TEXT.svg)

> Desarrollada por Sebastián Kravetz
> Asignatura de Desarrollo Apps Móviles con Kotlin y Jetpack Compose

### Descripción
Este proyecto es una aplicación móvil desarrollada con **Kotlin** y **Jetpack Compose** en **Android Studio**, orientada a la ayuda de personas con necesidades especiales de comunicación, por lo que actúa como ayuda a la hora de interactuar con otros, proveyendo los servicios de Voz-a-Texto y Texto-a-Voz.

### Funcionalidades principales
- Registro de usuarios.
- Reestablecimiento de contraseña.
- Texto-a-Voz: Permite al usuario convertir texto en voz, escribiendo el texto deseado en un input que luego es leído por el motor generativo. 
- Voz-a-Text: Permite al usuario convertir voz en texto, dando la posibilidad al usuario de abrir su micrófono y recibir una transcripción del audio inmediata.
- Persistencia de cada una de las solicitudes realizadas por un usuario.
- Navegación fácil y dinámica.

---

## Requisitos previos
Asegúrate de tener lo siguiente instalado antes de comenzar:

1. **Android Studio**
2. **JDK 21**.
3. **Gradle** configurado en tu sistema (el proyecto utiliza la configuración de Gradle del wrapper).
4. Emulador o dispositivo Android con API nivel 24 o superior.

---

## Instalación y Ejecución para Desarrolladores

### Clonar el repositorio
```bash
git clone https://github.com/rrooddooxx/DSY2204_app_android_skravetz.git
```

### Configuración del entorno
1. Abre el proyecto en **Android Studio**.
2. Permite que Gradle sincronice automáticamente las dependencias.

### Compilación y Ejecución
1. Asegúrate de tener configurado un emulador o dispositivo físico con API 24 o superior.
2. Ejecuta el proyecto seleccionando el botón de **Run** o usa el comando:
   ```bash
   ./gradlew assembleDebug
   ```
3. El APK generado estará disponible en la carpeta `build/outputs/apk/debug`.

---

## Normas de contribución

Este proyecto utiliza **Conventional Commits** para mantener un historial limpio y fácil de entender.

#### Tipos de commits
- **feat**: Incorporación de nuevas funcionalidades.
- **fix**: Corrección de errores.
- **docs**: Cambios en la documentación.
- **style**: Cambios que no afectan la lógica (formato, estilo, etc.).
- **refactor**: Refactorización de código sin cambios funcionales.
- **test**: Agregado o modificación de pruebas.
- **chore**: Cambios menores o de mantenimiento.

### Ejemplo de commit
```
feat(authentication): agregar validación en el registro de usuarios
```

---

## Licencia
Este proyecto está bajo la licencia [MIT](LICENSE).

---

## Contacto
Si tienes alguna duda o sugerencia, por favor, contacta a: 
- **Autor**: [Sebastián Kravetz](mailto:root@sebastiankravetz.dev)
