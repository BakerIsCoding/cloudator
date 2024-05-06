document.addEventListener('DOMContentLoaded', function() {
    var roles = document.body.dataset.userRoles.split(',');

    var basicButton = document.getElementById('basicButton');
    var premiumButton = document.getElementById('premiumButton');

    // Deshabilitar el botón del plan actual basado en roles
    if(roles.includes('ROLE_USER')) {
        basicButton.disabled = true;
    } else if(roles.includes('ROLE_PREMIUM')) {
        premiumButton.disabled = true;
    }

    basicButton.addEventListener('click', function() {
        updateRoleBasic(user.id);
    });

    premiumButton.addEventListener('click', function() {
        updateRolePremium(user.id);
    });

    function updateRoleBasic(userId) {
        basicButton.disabled = true; 
        premiumButton.disabled = false;

        fetch('/users/plan/basic/' + userId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al degradar al usuario');
            }
            
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al degradar al usuario');
            // Revertir los cambios si hay un error
            basicButton.disabled = false;
            premiumButton.disabled = true;
        });
    }

    function updateRolePremium(userId) {
        premiumButton.disabled = true; 
        basicButton.disabled = false;

        fetch('/users/plan/premium/' + userId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al ascender al usuario');
            }
            // Opcional: Manejar la respuesta
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al ascender al usuario');
            // Revertir los cambios si hay un error
            premiumButton.disabled = false;
            basicButton.disabled = true;
        });
    }
});
