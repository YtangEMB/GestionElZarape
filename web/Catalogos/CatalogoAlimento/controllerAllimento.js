let selectedFoodId = null;

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

function loadFoodList() {
    validateToken(() => {
        const token = getAuthToken();
        const usuario = getAuthUser();
        fetch('http://localhost:8080/GestionElZarape/api/Alimento/getAllAlimentos', {
            headers: { 
                "Authorization": `Bearer ${token}`,
                "usuario":usuario,
                "token":token
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Error en la respuesta del servidor');
            return response.json();
        })
        .then(data => {
            const foodList = document.getElementById('food-list');
            foodList.innerHTML = '';

            if (data.data.length === 0) {
                foodList.innerHTML = '<tr><td colspan="5">No hay alimentos registrados.</td></tr>';
                return;
            }

            data.data.forEach(food => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${food.nombre}</td>
                    <td>${food.precio.toFixed(2)}</td>
                    <td>${food.descripcion}</td>
                    <td><img src="${food.foto}" alt="${food.nombre}" style="width: 50px; height: 50px;"></td>
                    <td>${food.categoria.descripcion}</td>
                `;
                row.addEventListener('click', () => selectFood(food));
                foodList.appendChild(row);
            });
        })
        .catch(error => console.error('Error al cargar los alimentos:', error));
    });
}

function selectFood(food) {
    selectedFoodId = food.idProducto;
    document.getElementById('idAlimento').value = food.idProducto || '';
    document.getElementById('food-name').value = food.nombre || '';
    document.getElementById('food-price').value = food.precio || '';
    document.getElementById('food-description').value = food.descripcion || '';
    document.getElementById('food-category').value = food.categoria.descripcion || '';
    document.getElementById('food-photo').value = food.foto || ''; 

    document.getElementById('delete-btn').style.display = 'inline-block';
    document.getElementById('edit-btn').style.display = 'inline-block';
    document.getElementById('cancel-edit').style.display = 'inline-block';
    document.querySelector('.edit').style.display = 'inline-block'; 
}

function clearForm() {
    selectedFoodId = null;
    document.getElementById('food-form').reset();
    document.getElementById('edit-btn').style.display = 'none';
    document.getElementById('delete-btn').style.display = 'none';
    document.getElementById('cancel-edit').style.display = 'none';
    document.querySelector('.edit').style.display = 'none';
}

function saveFood(event) {
    event.preventDefault();

    validateToken(() => {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const foodData = {
            idProducto: selectedFoodId,
            nombre: document.getElementById('food-name').value.trim(),
            descripcion: document.getElementById('food-description').value.trim(),
            foto: document.getElementById('food-photo').value.trim(),
            precio: parseFloat(document.getElementById('food-price').value),
            categoria: document.getElementById('food-category').value.trim(),
        };

        if (!foodData.nombre || isNaN(foodData.precio) || !foodData.foto) {
            alert('Por favor, completa los campos requeridos correctamente.');
            return;
        }

        const url = selectedFoodId
            ? 'http://localhost:8080/GestionElZarape/api/Alimento/updateAlimento'
            : 'http://localhost:8080/GestionElZarape/api/Alimento/insertAlimento';

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                "Authorization": `Bearer ${token}`,
                "usuario":usuario,
                "token":token
            },
            body: new URLSearchParams(foodData).toString(),
        })
        .then(response => {
            if (!response.ok) throw new Error('Error al guardar el alimento');
            return response.json();
        })
        .then(data => {
            alert(data.result || 'Operación exitosa');
            clearForm();
            loadFoodList();
        })
        .catch(error => console.error('Error al guardar el alimento:', error));
    });
}

function deleteFood() {
    if (!selectedFoodId) {
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
                    "usuario":usuario,
                    "token":token
                },
                body: new URLSearchParams({ idProducto: selectedFoodId }).toString(),
            })
            .then(response => {
                if (!response.ok) throw new Error('Error al eliminar el alimento');
                return response.json();
            })
            .then(data => {
                alert(data.result || 'Eliminación exitosa');
                clearForm();
                loadFoodList();
            })
            .catch(error => console.error('Error al eliminar el alimento:', error));
        }
    });
}

// Evento al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    loadFoodList();
    document.getElementById('food-form').addEventListener('submit', saveFood);
    document.getElementById('edit-btn').addEventListener('click', saveFood);
    document.getElementById('delete-btn').addEventListener('click', deleteFood);
    document.getElementById('cancel-edit').addEventListener('click', clearForm);
});

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
