
document.addEventListener('DOMContentLoaded', function () {
    var usernameInput = document.getElementById('username');
    var passwordInput = document.getElementById('password');
    

    usernameInput.addEventListener('input', function() {
        this.style.color = '#your-input-color';
    });

    passwordInput.addEventListener('input', function() {
        this.style.color = '#your-input-color';
    });

    usernameInput.addEventListener('blur', function() {
        this.style.color = '#your-blur-color';
    });

    passwordInput.addEventListener('blur', function() {
        this.style.color = '#your-blur-color';
    });
});
