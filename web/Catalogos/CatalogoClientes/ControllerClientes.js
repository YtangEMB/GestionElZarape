validateToken();
const API_BASE_URL = "http://localhost:8080/GestionElZarape/api/Cliente";
let selectedCustomerId = null;

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

function renderCustomersList(clientes) {
    const userList = document.getElementById("user-list");
    userList.innerHTML = clientes.map(cliente => `
        <tr data-id="${cliente.idCliente}" onclick="selectCustomer(${cliente.idCliente})">
            <td>${cliente.usuario.nombre}</td>
            <td>${cliente.persona.nombre}</td>
            <td>${cliente.persona.apellidos}</td>
            <td>${cliente.persona.telefono}</td>
            <td>${cliente.ciudad.nombre}</td>
            <td>${cliente.estado.nombre}</td>
            <td style="visibility: hidden; display: none;">${cliente.usuario.contrasenia}</td>
        </tr>
    `).join("");
}

function loadCustomer(idCliente) {
    selectedCustomerId = idCliente;
    const row = document.querySelector(`#user-list tr[data-id="${idCliente}"]`);
    
    document.getElementById("idCliente").value = idCliente;
    document.getElementById("user-user").value = row.cells[0].textContent;
    document.getElementById("user-name").value = row.cells[1].textContent;
    document.getElementById("user-lastname").value = row.cells[2].textContent;
    document.getElementById("user-phone").value = row.cells[3].textContent;
    document.getElementById("user-city").value = row.cells[4].textContent;
    document.getElementById("user-password").value = row.cells[6].textContent;

    showActionButtons(idCliente);
}

function selectCustomer(idCliente) {
    loadCustomer(idCliente);
}

function showActionButtons(idCliente) {
    document.getElementById("edit-btn").style.display = "inline-block";
    document.getElementById("delete-btn").style.display = "inline-block";
    document.getElementById("cancel-edit").style.display = "inline-block";

    document.getElementById("edit-btn").onclick = () => updateCustomer({
        idCliente: selectedCustomerId,
        nombre: document.getElementById("user-name").value,
        apellidos: document.getElementById("user-lastname").value,
        telefono: document.getElementById("user-phone").value,
        nombreCiudad: document.getElementById("user-city").value,
        nombreUsuario: document.getElementById("user-user").value,
        contrasenia: document.getElementById("user-password").value,
    });

    document.getElementById("delete-btn").onclick = () => deleteCustomer(selectedCustomerId);
    document.getElementById("cancel-edit").onclick = () => cancelEdit();
}

function cancelEdit() {
    selectedCustomerId = null;
    document.getElementById("user-form").reset();
    document.getElementById("edit-btn").style.display = "none";
    document.getElementById("delete-btn").style.display = "none";
    document.getElementById("cancel-edit").style.display = "none";
}

async function getAllCustomer() {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch('http://localhost:8080/GestionElZarape/api/Cliente/getAllCliente', {
            headers: { 
                "Authorization": `Bearer ${token}`,
                "usuario":usuario,
                "token":token
            }
        });
        const clientes = await response.json();
        renderCustomersList(clientes);
    } catch (error) {
        console.error("Error al cargar clientes:", error);
    }
}


async function insertCustomer(cliente) {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch(`${API_BASE_URL}/insertCliente`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "usuario":usuario,
                "token":token
            },
            body: new URLSearchParams(cliente)
        });
        const data = await response.json();
        alert(data.message || "Empleado guardado correctamente");
        getAllCustomer();
    } catch (error) {
        console.error("Error al insertar cliente:", error);
    }
}

async function updateCustomer(cliente) {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch(`${API_BASE_URL}/updateCliente`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "usuario":usuario,
                "token":token
            },
            body: new URLSearchParams(cliente)
        });
        const data = await response.json();
        alert(data.message || "Cliente actualizado correctamente");
        getAllCustomer();
        cancelEdit(); // Resetea el formulario después de la actualización
    } catch (error) {
        console.error("Error al actualizar cliente:", error);
    }
}

async function deleteCustomer(idCliente) {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch(`${API_BASE_URL}/deleteCliente`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded",
                "usuario":usuario,
                "token":token },
            body: new URLSearchParams({ idCliente }),
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        alert(data.message || "Cliente eliminado correctamente");
        getAllCustomer();
    } catch (error) {
        console.error("Error al eliminar cliente:", error);
        alert("Error al eliminar el cliente. Por favor, verifica la consola para más detalles.");
    }
}

document.getElementById("user-form").addEventListener("submit", function (event) {
    event.preventDefault();
    
    const cliente = {
        nombreUsuario: document.getElementById("user-user").value,
        nombre: document.getElementById("user-name").value,
        apellidos: document.getElementById("user-lastname").value,
        telefono: document.getElementById("user-phone").value,
        nombreCiudad: document.getElementById("user-city").value,
        contrasenia: document.getElementById("user-password").value,
    };

    insertCustomer(cliente);
});


document.getElementById('search-input').addEventListener('input', function () {
    const searchTerm = this.value.toLowerCase();
    filterCustomerList(searchTerm);
});

function filterCustomerList(searchTerm) {
    const CustomerList = document.getElementById('user-list');
    const rows = CustomerList.getElementsByTagName('tr');

    Array.from(rows).forEach(row => {
        const CustomerName = row.cells[1].textContent.toLowerCase();
        const CustomerUser = row.cells[0].textContent.toLowerCase();

        if (CustomerName.includes(searchTerm) || CustomerUser.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function borrarTokenC() {
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


getAllCustomer();


