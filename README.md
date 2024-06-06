## Cloudator

Cloudator es una aplicación de Java Spring Boot diseñada para proporcionar una plataforma de almacenamiento en la nube similar a Mega.io. Permite a los usuarios subir, descargar y gestionar archivos tras iniciar sesión.

### Características

- **Autenticación de Usuarios**: Sistema de inicio de sesión seguro para los usuarios.
- **Gestión de Archivos**: Subida, descarga y gestión de archivos.
- **Interfaz de Usuario**: Interfaces interactivas y fáciles de usar para la gestión de archivos.

### Estructura del Proyecto

- **Backend**: Desarrollado con Java Spring Boot, gestionando la autenticación de usuarios, almacenamiento de archivos y endpoints de la API.
- **Frontend**: Desarrollado con JavaScript, HTML y CSS, proporcionando una interfaz de usuario intuitiva.

### Configuración 
En el archivo de `src/main/resources/application.properties` se encuentra la configuración del proyecto, estos parámetros deben ser editados:

- `spring.datasource.url` Url de la base de datos
- `spring.datasource.username` Usuario de la base de datos
- `spring.datasource.password` Contraseña de la base de datos
- `spring.servlet.multipart.max-file-size` Se debe poner el tamaño maximo que puede pasar por el servidor (EN GB)
- `spring.servlet.multipart.max-request-size` Se debe poner el tamaño maximo que puede pasar por el servidor (EN GB)
- `secretkey` Se utiliza para la encriptación y desencriptación, debe ser diferente a `secretencryptor`
- `secretencryptor` Se utiliza para la encriptación y desencriptación, debe ser diferente a `secretkey`
- `domain` El dominio de la aplicación de archivos

### Instalación

1. **Clonar el Repositorio**:
   ```bash
   git clone https://github.com/BakerIsCoding/cloudator.git
   cd cloudator
   ```

2. **Construir el Proyecto**:
    Usar Maven para construir el proyecto.
    
    ```bash
    ./mvnw clean install
    ```
3. **Ejecutar la Aplicación**:
    Iniciar la aplicación de Spring Boot.
    ```bash
    ./mvnw spring-boot:run
    ```
4. **Acceder a la Aplicación**:
    Visitar http://localhost:8080 en tu navegador web.

### Licencia

Este proyecto está licenciado bajo la Licencia MIT.

# Otros Repositorios del Proyecto Cloudator

Puedes revisar los demás repositorios aquí:

- **Cloudator - API:** Aplicación que se encarga de mostrar las vistas y comunicarse con el servidor de archivos.  
  [https://github.com/BakerIsCoding/cloudator](https://github.com/BakerIsCoding/cloudator)

- **Cloudator - FILES:** Aplicación que se encarga de escuchar al servidor de API, y de iniciar una descarga cuando el usuario la necesita.  
  [https://github.com/BakerIsCoding/cloudator-files](https://github.com/BakerIsCoding/cloudator-files)

- **Cloudator-Android:** Aplicación móvil.  
  [https://github.com/BakerIsCoding/Android-Cloudator-App/](https://github.com/BakerIsCoding/Android-Cloudator-App/)

# Autores del Proyecto Cloudator

Este proyecto ha sido desarrollado por los siguientes autores:

- **[Cramcat639](https://github.com/Cramcat639)**
- **[EdNuGa](https://github.com/EdNuGa)**
- **[BakerIsCoding](https://github.com/BakerIsCoding)**
