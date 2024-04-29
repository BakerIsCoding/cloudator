function addIdToModal(userId) {
  document.getElementById('deleteID').innerHTML = "";
  document.getElementById('deleteID').innerHTML = userId;
  console.log(userId + "added as deleted")
}

function addIdToBlockModal(userId) {
  document.getElementById('blockedID').innerHTML = "";
  document.getElementById('blockedID').innerHTML = userId;
  console.log(userId + "added as blocked")
}

function addIdToUnblockModal(userId) {
  document.getElementById('unblockedID').innerHTML = "";
  document.getElementById('unblockedID').innerHTML = userId;
  console.log(userId + "added as unblocked")
}

function addIdToUpgradeModal(userId) {
  document.getElementById('upgradeID').innerHTML = "";
  document.getElementById('upgradeID').innerHTML = userId;
  console.log(userId + "added as upgraded")
}

document.getElementById('unblockUsuarioBtn').addEventListener('click', function () {
  var unblockUserId = document.getElementById("unblockedID").textContent;
  console.log('Desbloquear usuario con id:', unblockUserId);

  if (confirm('¿Estás seguro de que deseas desbloquear este usuario?')) {
    desbloquearUsuario(unblockUserId);
  }
});

document.getElementById('blockUsuarioBtn').addEventListener('click', function () {
  var blockUserId = document.getElementById("blockedID").textContent;
  console.log('Bloquear usuario con id:', blockUserId);

  if (confirm('¿Estás seguro de que deseas bloquear este usuario?')) {
    bloquearUsuario(blockUserId);
  }
});

document.getElementById('eliminarUsuarioBtn').addEventListener('click', function () {
  var userId = document.getElementById("deleteID").textContent;
  console.log('Eliminar usuario con id:', userId);

  if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
    eliminarUsuario(userId);
  }
});

document.getElementById('upgradeUsuarioBtn').addEventListener('click', function () {
  var userId = document.getElementById("upgradeID").textContent;
  console.log('Ascender usuario con id:', userId);

  if (confirm('¿Estás seguro de que deseas ascender este usuario a Administrador?')) {
    upgradeUsuario(userId);
  }
});



function desbloquearUsuario(userId) {
  fetch('/admin/users/unblock/' + userId, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Error al desbloquear el usuario');
      }
      window.location.reload();
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error al desbloquear el usuario');
    });
}

function bloquearUsuario(userId) {
  fetch('/admin/users/block/' + userId, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Error al bloquear el usuario');
      }
      window.location.reload();
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error al bloquear el usuario');
    });
}

function eliminarUsuario(userId) {
  fetch('/admin/users/delete/' + userId, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Error al eliminar el usuario');
      }
      window.location.reload();
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error al eliminar el usuario');
    });
}

function upgradeUsuario(userId) {
  fetch('/admin/users/upgrade/' + userId, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Error al ascender al usuario');
      }
      window.location.reload();
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error al ascender al usuario');
    });
}