function verificarContrasena() {
    var contraseña = document.getElementById("password").value;
    var confirmarContraseña = document.getElementById("confirmar").value;
    var errorDiv = document.getElementById("errorpassworddif");

    // Verificar si las contraseñas coinciden
    if (contraseña !== confirmarContraseña) {
        console.log("Las contraseñas no coinciden");
        href = "http://localhost:8080/register?errorpassworddif=true"
        window.location.href = href;
        return; // Detiene la ejecución de la función si las contraseñas no coinciden
    } else {
        console.log("Las contraseñas coinciden");

        document.querySelector("form").submit(); // Envía el formulario manualmente
    }
  }