let selectedAlimentoId = null;

function getAuthToken() {
    const authData = localStorage.getItem("authData");
    return authData ? JSON.parse(authData).token : null;
}

function getAuthUser() {
    const authData = localStorage.getItem("authData");
    return authData ? JSON.parse(authData).usuario : null;
}

function showAlert(message, type = "warning") {
    alert(`${type.toUpperCase()}: ${message}`);
}

function validateToken(callback) {
    const authData = localStorage.getItem("authData");
    if (!authData) {
        showAlert("No tienes autorización para realizar esta acción. Inicia sesión.", "danger");
        window.location.href = "../../index.html";
        return;
    }

    const { usuario, token } = JSON.parse(authData);

    fetch(`http://localhost:8080/GestionElZarape/api/Login/validarToken?nombreU=${encodeURIComponent(usuario)}&token=${encodeURIComponent(token)}`)
    .then(response => response.json())
    .then(data => {
        if (!data.valido) {
            showAlert("Sesión expirada o token inválido. Inicia sesión nuevamente.", "danger");
            localStorage.removeItem("authData");
            return;
        }
        callback();
    })
    .catch(error => {
        console.error("Error al validar el token:", error);
    });
}

function loadAlimentosList() {
    validateToken(() => {
        const token = getAuthToken();
        const usuario = getAuthUser();
        fetch('http://localhost:8080/GestionElZarape/api/Alimento/getAllAlimentos', {
            headers: { 
                "Authorization": `Bearer ${token}`,
                "usuario": usuario,
                "token": token
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Error en la respuesta del servidor');
            return response.json();
        })
        .then(data => {
            const alimentosList = document.getElementById('food-list');
            alimentosList.innerHTML = '';

            if (data.data.length === 0) {
                const emptyRow = document.createElement('tr');
                emptyRow.innerHTML = '<td colspan="5">No hay alimentos registrados.</td>';
                alimentosList.appendChild(emptyRow);
                return;
            }

            data.data.forEach(alimento => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${alimento.nombre}</td>
                    <td>${alimento.precio.toFixed(2)}</td>
                    <td>${alimento.descripcion}</td>
                    <td><img src="${alimento.foto}" alt="${alimento.nombre}" style="width: 50px; height: 50px;"></td>
                    <td>${alimento.categoria.descripcion}</td>
                `;
                row.addEventListener('click', () => selectAlimento(alimento));
                alimentosList.appendChild(row);
            });
        })
        .catch(error => console.error('Error al cargar los alimentos:', error));
    });
}

function selectAlimento(alimento) {
    selectedAlimentoId = alimento.idProducto;
    document.getElementById('idAlimento').value = alimento.idProducto || '';
    document.getElementById('food-name').value = alimento.nombre || '';
    document.getElementById('food-price').value = alimento.precio || '';
    document.getElementById('food-description').value = alimento.descripcion || '';
    document.getElementById('food-category').value = alimento.categoria.descripcion || '';
    document.getElementById('food-photo').value = ''; // No mostrar la URL en el campo de archivo
    
    // Mostrar la imagen actual del alimento
    const currentPhotoElement = document.getElementById('current-food-photo');
    currentPhotoElement.src = alimento.foto;
    currentPhotoElement.style.display = 'block';
    currentPhotoElement.alt = `Imagen actual de ${alimento.nombre}`;

    document.getElementById('delete-btn').style.display = 'inline-block';
    document.getElementById('edit-btn').style.display = 'inline-block';
    document.getElementById('cancel-edit').style.display = 'inline-block';
    document.querySelector('.edit').style.display = 'inline-block';
}

function clearForm() {
    selectedAlimentoId = null;
    document.getElementById('food-form').reset();
    document.getElementById('edit-btn').style.display = 'none';
    document.getElementById('delete-btn').style.display = 'none';
    document.getElementById('cancel-edit').style.display = 'none';
    document.querySelector('.edit').style.display = 'none';
    
    // Ocultar la imagen actual al limpiar el formulario
    document.getElementById('current-food-photo').style.display = 'none';
}

function convertImageToBase64(file, callback) {
    const reader = new FileReader();
    reader.onload = function(event) {
        callback(event.target.result);
    };
    reader.readAsDataURL(file);
}

function saveAlimento(event) {
    event.preventDefault();

    validateToken(() => {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const photoFile = document.getElementById('food-photo').files[0];
        const currentPhotoSrc = document.getElementById('current-food-photo').src;

        if (!photoFile && !selectedAlimentoId) {
            alert('Por favor, selecciona una imagen para nuevos alimentos.');
            return;
        }

        const saveFunction = (imageData) => {
            const alimentoData = {
                idProducto: selectedAlimentoId,
                nombre: document.getElementById('food-name').value.trim(),
                descripcion: document.getElementById('food-description').value.trim(),
                foto: imageData || currentPhotoSrc,
                precio: parseFloat(document.getElementById('food-price').value),
                categoria: document.getElementById('food-category').value.trim(),
            };

            if (!alimentoData.nombre || isNaN(alimentoData.precio) || !alimentoData.foto) {
                alert('Por favor, completa los campos requeridos correctamente.');
                return;
            }

            const url = selectedAlimentoId
                ? 'http://localhost:8080/GestionElZarape/api/Alimento/updateAlimento'
                : 'http://localhost:8080/GestionElZarape/api/Alimento/insertAlimento';

            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    "Authorization": `Bearer ${token}`,
                    "usuario": usuario,
                    "token": token
                },
                body: new URLSearchParams(alimentoData).toString(),
            })
            .then(response => {
                if (!response.ok) throw new Error('Error al guardar el alimento');
                return response.json();
            })
            .then(data => {
                alert(data.result || 'Operación exitosa');
                clearForm();
                loadAlimentosList();
            })
            .catch(error => console.error('Error al guardar el alimento:', error));
        };

        if (photoFile) {
            convertImageToBase64(photoFile, function(base64Image) {
                saveFunction(base64Image);
            });
        } else {
            // Si estamos editando y no se subió nueva imagen, usar la existente
            saveFunction(null);
        }
    });
}

function deleteAlimento() {
    if (!selectedAlimentoId) {
        alert('No hay ningún alimento seleccionado');
        return;
    }

    validateToken(() => {
        const token = getAuthToken();
        const usuario = getAuthUser();
        if (confirm('¿Estás seguro de eliminar este alimento?')) {
            fetch('http://localhost:8080/GestionElZarape/api/Alimento/deleteAlimento', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    "Authorization": `Bearer ${token}`,
                    "usuario": usuario,
                    "token": token
                },
                body: new URLSearchParams({ idProducto: selectedAlimentoId }).toString(),
            })
            .then(response => {
                if (!response.ok) throw new Error('Error al eliminar el alimento');
                return response.json();
            })
            .then(data => {
                alert(data.result || 'Eliminación exitosa');
                clearForm();
                loadAlimentosList();
            })
            .catch(error => console.error('Error al eliminar el alimento:', error));
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    loadAlimentosList();
    document.getElementById('food-form').addEventListener('submit', saveAlimento);
    document.getElementById('edit-btn').addEventListener('click', saveAlimento);
    document.getElementById('delete-btn').addEventListener('click', deleteAlimento);
    document.getElementById('cancel-edit').addEventListener('click', clearForm);
});

document.getElementById('search-input').addEventListener('input', function () {
    const searchTerm = this.value.toLowerCase();
    filterAlimentosList(searchTerm);
});

function filterAlimentosList(searchTerm) {
    const alimentosList = document.getElementById('food-list');
    const rows = alimentosList.getElementsByTagName('tr');

    Array.from(rows).forEach(row => {
        const alimentoName = row.cells[0].textContent.toLowerCase();
        const alimentoCategory = row.cells[4].textContent.toLowerCase();

        if (alimentoName.includes(searchTerm) || alimentoCategory.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function borrarTokenA() {
    const authData = localStorage.getItem("authData");

    if (authData) {
        try {
            const parsedAuthData = JSON.parse(authData);
            const usuario = parsedAuthData.usuario;

            if (usuario) {
                fetch(`http://localhost:8080/GestionElZarape/api/Login/eliminartoken?nombreU=${encodeURIComponent(usuario)}`, {
                    method: "DELETE",
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Error al eliminar el token en el servidor");
                    }
                })
                .catch(error => console.error("Error:", error))
                .finally(() => {
                    localStorage.removeItem("authData");
                });
            } else {
                console.error("No se encontró el usuario en authData");
            }
        } catch (error) {
            console.error("Error al parsear authData:", error);
        }
    } else {
        console.log("No hay datos de autenticación en localStorage");
    }
    localStorage.removeItem("authData");
}

// Inicializar la lista al cargar
loadAlimentosList();