let selectedBebidaId = null;

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

function loadBebidasList() {
    validateToken(() => {
        const token = getAuthToken();
        const usuario = getAuthUser();
        fetch('http://localhost:8080/GestionElZarape/api/Bebida/getAllBebidas', {
            headers: { 
                "Authorization": `Bearer ${token}`,
                "usuario":usuario,
                "token":token
            }
        })
    .then(response => {
        if (!response.ok)
            throw new Error('Error en la respuesta del servidor');
        return response.json();
    })
    .then(data => {
        const BebidasList = document.getElementById('beverage-list');
        BebidasList.innerHTML = '';

        if (data.length === 0) {
            const emptyRow = document.createElement('tr');
            emptyRow.innerHTML = '<td colspan="5">No hay bebidas registradas.</td>';
            BebidasList.appendChild(emptyRow);
            return;
        }

        data.forEach(bebida => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td>${bebida.nombre}</td>
            <td>${bebida.precio.toFixed(2)}</td>
            <td>${bebida.descripcion}</td>
            <td><img src="${bebida.foto}" alt="${bebida.nombre}" style="width: 50px; height: 50px;"></td>
            <td>${bebida.categoria.descripcion}</td>
            `;
            row.addEventListener('click', () => selectBebida(bebida));
            BebidasList.appendChild(row);
        });
    })
    .catch(error => console.error('Error al cargar los alimentos:', error));
    });
}

function selectBebida(bebidas) {
    selectedBebidaId = bebidas.idProducto;
    document.getElementById('idBebida').value = bebidas.idProducto || '';
    document.getElementById('beverage-name').value = bebidas.nombre || '';
    document.getElementById('beverage-price').value = bebidas.precio || '';
    document.getElementById('beverage-description').value = bebidas.descripcion || '';
    document.getElementById('beverage-category').value = bebidas.categoria.descripcion || '';
    document.getElementById('beverage-photo').value = bebidas.foto || ''; 

    document.getElementById('delete-btn').style.display = 'inline-block';
    document.getElementById('edit-btn').style.display = 'inline-block';
    document.getElementById('cancel-edit').style.display = 'inline-block';
    document.querySelector('.edit').style.display = 'inline-block'; 
}

function clearForm() {
    selectedBebidaId = null;
    document.getElementById('beverage-form').reset();
    document.getElementById('edit-btn').style.display = 'none';
    document.getElementById('delete-btn').style.display = 'none';
    document.getElementById('cancel-edit').style.display = 'none';
    document.querySelector('.edit').style.display = 'none';
}

function saveBebida(event) {
    event.preventDefault();
    
    validateToken(() => {
        const token = getAuthToken();
        const usuario = getAuthUser();
    const BebidasData = {
        idProducto: selectedBebidaId,
        nombre: document.getElementById('beverage-name').value.trim(),
        descripcion: document.getElementById('beverage-description').value.trim(),
        foto: document.getElementById('beverage-photo').value.trim(),
        precio: parseFloat(document.getElementById('beverage-price').value),
        categoria: document.getElementById('beverage-category').value.trim(),
    };

    if (!BebidasData.nombre || isNaN(BebidasData.precio) || !BebidasData.foto) {
        alert('Por favor, completa los campos requeridos correctamente.');
        return;
    }

    const url = selectedBebidaId
            ? 'http://localhost:8080/GestionElZarape/api/Bebida/updateBebida'
            : 'http://localhost:8080/GestionElZarape/api/Bebida/insertBebida';

    const formData = new URLSearchParams(BebidasData).toString();

    fetch(url, {
        method: 'POST',
        headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                "Authorization": `Bearer ${token}`,
                "usuario":usuario,
                "token":token
            },
        body: formData,
    })
    .then(response => {
        if (!response.ok)
            throw new Error('Error al guardar la bebida');
        return response.json();
    })
    .then(data => {
        alert(data.result || 'Operación exitosa');
        clearForm();
        loadBebidasList();
    })
    .catch(error => console.error('Error al guardar la bebida:', error));
    });
}

function deleteBebida() {
    if (!selectedBebidaId) {
        alert('No hay ningúna bebida seleccionada');
        return;
    }
    
    validateToken(() => {
        const token = getAuthToken();
        const usuario = getAuthUser();
    if (confirm('¿Estás seguro de eliminar esta bebida?')) {
        const formData = new URLSearchParams({idProducto: selectedBebidaId}).toString();

        fetch('http://localhost:8080/GestionElZarape/api/Bebida/deleteBebida', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                "Authorization": `Bearer ${token}`,
                "usuario":usuario,
                "token":token
            },
            body: formData,
        })
                .then(response => {
                    if (!response.ok)
                        throw new Error('Error al eliminar la bebida');
                    return response.json();
                })
                .then(data => {
                    alert(data.result || 'Eliminación exitosa');
                    clearForm();
                    loadBebidasList();
                })
                .catch(error => console.error('Error al eliminar la bebida:', error));
    }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    loadBebidasList();
    document.getElementById('beverage-form').addEventListener('submit', saveBebida);
    document.getElementById('edit-btn').addEventListener('click', saveBebida);
    document.getElementById('delete-btn').addEventListener('click', deleteBebida);
    document.getElementById('cancel-edit').addEventListener('click', clearForm);
});

document.getElementById('search-input').addEventListener('input', function () {
    const searchTerm = this.value.toLowerCase();
    filterBebidasList(searchTerm);
});

function filterBebidasList(searchTerm) {
    const BebidasList = document.getElementById('beverage-list');
    const rows = BebidasList.getElementsByTagName('tr');

    Array.from(rows).forEach(row => {
        const BabidaName = row.cells[0].textContent.toLowerCase();
        const BebidaCategory = row.cells[4].textContent.toLowerCase();

        if (BabidaName.includes(searchTerm) || BebidaCategory.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function borrarTokenB() {
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


loadBebidasList();