function verificarContrasena() {
    var contraseña = document.getElementById("password").value;
    var confirmarContraseña = document.getElementById("confirmar").value;
    var errorDiv = document.getElementById("errorpassworddif");

    // Verificar si las contraseñas coinciden
    if (contraseña !== confirmarContraseña) {
        
        href = "https://cloudator.live/register?errorpassworddif=true"
        window.location.href = href;
        return; // Detiene la ejecución de la función si las contraseñas no coinciden
    } else {

        document.querySelector("form").submit(); // Envía el formulario manualmente
    }
  }
function mostrarContrasena() {
    var x = document.getElementById("password");
    var y = document.getElementById("confirmar");
    if (x.type === "password") {
        x.type = "text";
        y.type = "text";
    } else {
        x.type = "password";
        y.type = "password";
    }
}
