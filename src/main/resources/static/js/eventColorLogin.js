
document.addEventListener('DOMContentLoaded', function () {
    var usernameInput = document.getElementById('username');
    var passwordInput = document.getElementById('password');

    usernameInput.addEventListener('input', function() {
        this.style.color = '#739FEA';
    });
    passwordInput.addEventListener('input', function() {
        this.style.color = '#739FEA'; 
    });
    usernameInput.addEventListener('blur', function() {
        this.style.color = '#FFFFFF';
    });
    passwordInput.addEventListener('blur', function() {
        this.style.color = '#FFFFFF'; 
    });
});
