
document.addEventListener('DOMContentLoaded', function () {
    var usernameInput = document.getElementById('username');
    var passwordInput = document.getElementById('password');
    var emailInput = document.getElementById('email');

    usernameInput.addEventListener('input', function() {
        this.style.color = '#87CEEB';
    });
    passwordInput.addEventListener('input', function() {
        this.style.color = '#87CEEB'; 
    });
    emailInput.addEventListener('input', function() {
        this.style.color = '#87CEEB'; 
    });
    usernameInput.addEventListener('blur', function() {
        this.style.color = '#FFFFFF';
    });
    passwordInput.addEventListener('blur', function() {
        this.style.color = '#FFFFFF'; 
    });
    emailInput.addEventListener('blur', function() {
        this.style.color = '#FFFFFF'; 
    });
});
