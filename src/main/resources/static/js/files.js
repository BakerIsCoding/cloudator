document.querySelectorAll('.visualizacionPoP').forEach(item => {
    item.addEventListener('change', function () {
        var fileID = this.getAttribute('data-fileid');
        var newValue = this.value === 'public'; // Convertir a booleano: 'public' es true, cualquier otra cosa es false

        fetch(`/users/files/${fileID}/visibility?isPublic=${newValue}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                console.log('Success:', data);
                alert('Visibilidad del archivo actualizada con éxito!');
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al actualizar la visibilidad del archivo: ' + error.message);
            });
    });
});
function addIdToModal(fileId) {
    document.getElementById('deleteID').innerHTML = "";
    document.getElementById('deleteID').innerHTML = fileId;
    console.log(fileId + "id del archivo a eliminar")
}
document.getElementById('eliminarArchivoBtn').addEventListener('click', function () {
    var fileId = document.getElementById("deleteID").textContent;
    console.log('Eliminar el archivo con id:', fileId);

    if (confirm('¿Estás seguro de que deseas eliminar este archivo?')) {
        deleteFile(fileId);
    }
});

function deleteFile(fileId) {
    fetch('/users/files/delete/' + fileId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al eliminar el archivo');
            }
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al eliminar el archivo');
        });
}