const API_BASE_URL = "http://localhost:8080/GestionElZarape/api/Empleado";
let selectedEmployeeId = null;


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

function renderEmployeeList(empleados) {
    const userList = document.getElementById("user-list");
    userList.innerHTML = empleados.map(empleado => `
        <tr data-id="${empleado.idEmpleado}" onclick="selectEmployee(${empleado.idEmpleado})">
            <td>${empleado.usuario.nombre}</td>
            <td>${empleado.persona.nombre}</td>
            <td>${empleado.persona.apellidos}</td>
            <td>${empleado.persona.telefono}</td>
            <td>${empleado.ciudad.nombre}</td>
            <td>${empleado.sucursal.nombre}</td>
            <td>${empleado.estado.nombre}</td>
            <td style="visibility: hidden; display: none;">${empleado.usuario.contrasenia}</td>
        </tr>
    `).join("");
}

function loadEmployee(idEmpleado) {
    selectedEmployeeId = idEmpleado;
    const row = document.querySelector(`#user-list tr[data-id="${idEmpleado}"]`);
    
    document.getElementById("idPersona").value = idEmpleado;
    document.getElementById("user-user").value = row.cells[0].textContent;
    document.getElementById("user-name").value = row.cells[1].textContent;
    document.getElementById("user-lastname").value = row.cells[2].textContent;
    document.getElementById("user-phone").value = row.cells[3].textContent;
    document.getElementById("user-password").value = row.cells[7].textContent;
    
    // Establecer el valor de la ciudad (ahora es un input con datalist)
    document.getElementById("user-city").value = row.cells[4].textContent;
    
    // Seleccionar la sucursal en el select
    const branchSelect = document.getElementById('user-branch');
    const branchName = row.cells[5].textContent;
    
    for (let i = 0; i < branchSelect.options.length; i++) {
        if (branchSelect.options[i].value === branchName) {
            branchSelect.selectedIndex = i;
            break;
        }
    }

    showActionButtons(idEmpleado);
}

function selectEmployee(idEmpleado) {
    loadEmployee(idEmpleado);
}

function showActionButtons(idEmpleado) {
    document.getElementById("edit-btn").style.display = "inline-block";
    document.getElementById("delete-btn").style.display = "inline-block";
    document.getElementById("cancel-edit").style.display = "inline-block";

    document.getElementById("edit-btn").onclick = () => updateEmployee({
        idEmpleado: selectedEmployeeId,
        nombre: document.getElementById("user-name").value,
        apellidos: document.getElementById("user-lastname").value,
        telefono: document.getElementById("user-phone").value,
        nombreCiudad: document.getElementById("user-city").value,
        nombreUsuario: document.getElementById("user-user").value,
        contrasenia: document.getElementById("user-password").value,
        nombreSucursal: document.getElementById("user-branch").value,
    });

    document.getElementById("delete-btn").onclick = () => deleteEmployee(selectedEmployeeId);
    document.getElementById("cancel-edit").onclick = () => cancelEdit();
}

function cancelEdit() {
    selectedEmployeeId = null;
    document.getElementById("user-form").reset();
    document.getElementById("edit-btn").style.display = "none";
    document.getElementById("delete-btn").style.display = "none";
    document.getElementById("cancel-edit").style.display = "none";
}

async function getAllEmployees() {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch('http://localhost:8080/GestionElZarape/api/Empleado/getAllEmpleado', {
            headers: { 
                "Authorization": `Bearer ${token}`,
                "usuario":usuario,
                "token":token
            }
        });
        const empleados = await response.json();
        renderEmployeeList(empleados);
    } catch (error) {
        console.error("Error al cargar empleados:", error);
    }
}

async function insertEmployee(empleado) {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch(`${API_BASE_URL}/insertEmpleado`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "usuario":usuario,
                "token":token
            },
            body: new URLSearchParams(empleado)
        });
        const data = await response.json();
        alert(data.message || "Empleado guardado correctamente");
        getAllEmployees();
    } catch (error) {
        console.error("Error al insertar empleado:", error);
    }
}

document.getElementById("user-form").addEventListener("submit", function (event) {
    event.preventDefault();
    
    const empleado = {
        nombreUsuario: document.getElementById("user-user").value,
        nombre: document.getElementById("user-name").value,
        apellidos: document.getElementById("user-lastname").value,
        telefono: document.getElementById("user-phone").value,
        nombreCiudad: document.getElementById("user-city").value,
        contrasenia: document.getElementById("user-password").value,
        nombreSucursal: document.getElementById("user-branch").value,
    };

    insertEmployee(empleado);
});


async function updateEmployee(empleado) {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch(`${API_BASE_URL}/updateEmpleado`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "usuario":usuario,
                "token":token
            },
            body: new URLSearchParams(empleado)
        });
        const data = await response.json();
        alert(data.message || "Empleado actualizado correctamente");
        getAllEmployees();
        cancelEdit();
    } catch (error) {
        console.error("Error al actualizar empleado:", error);
    }
}

async function deleteEmployee(idEmpleado) {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch(`${API_BASE_URL}/deleteEmpleado`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded",
                "usuario":usuario,
                "token":token },
            body: new URLSearchParams({ idEmpleado }),
        });
        const data = await response.json();
        alert(data.message || "Empleado eliminado correctamente");
        getAllEmployees();
        cancelEdit();
    } catch (error) {
        console.error("Error al eliminar empleado:", error);
    }
}

document.getElementById('search-input').addEventListener('input', function () {
    const searchTerm = this.value.toLowerCase();
    filterEmpleadosList(searchTerm);
});

function filterEmpleadosList(searchTerm) {
    const EmpleadosList = document.getElementById('user-list');
    const rows = EmpleadosList.getElementsByTagName('tr');

    Array.from(rows).forEach(row => {
        const EmpleadoName = row.cells[1].textContent.toLowerCase();
        const EmpleadoUser = row.cells[0].textContent.toLowerCase();

        if (EmpleadoName.includes(searchTerm) || EmpleadoUser.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function borrarTokenE() {
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

async function loadCities() {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch('http://localhost:8080/GestionElZarape/api/Empleado/getAllCiudad', {
            headers: { 
                "Authorization": `Bearer ${token}`,
                "usuario": usuario,
                "token": token
            }
        });
        const ciudades = await response.json();
        const citiesDatalist = document.getElementById('cities-list');
        
        // Limpiar el datalist
        citiesDatalist.innerHTML = '';
        
        // Ordenar ciudades alfabéticamente
        ciudades.sort((a, b) => a.nombre.localeCompare(b.nombre));
        
        // Agregar opciones al datalist
        ciudades.forEach(ciudad => {
            const option = document.createElement('option');
            option.value = ciudad.nombre;
            citiesDatalist.appendChild(option);
        });
    } catch (error) {
        console.error("Error al cargar ciudades:", error);
    }
}

async function loadBranches() {
    try {
        const token = getAuthToken();
        const usuario = getAuthUser();
        const response = await fetch('http://localhost:8080/GestionElZarape/api/Sucursal/getAllSucursales', {
            headers: { 
                "Authorization": `Bearer ${token}`,
                "usuario": usuario,
                "token": token
            }
        });
        const data = await response.json();
        const branchSelect = document.getElementById('user-branch');
        
        // Limpiar select excepto la primera opción
        while (branchSelect.options.length > 1) {
            branchSelect.remove(1);
        }
        
        if (data.result === "success" && data.data) {
            data.data.forEach(sucursal => {
                const option = document.createElement('option');
                option.value = sucursal.nombre;
                option.textContent = sucursal.nombre;
                branchSelect.appendChild(option);
            });
        }
    } catch (error) {
        console.error("Error al cargar sucursales:", error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    validateToken(() => {
        getAllEmployees();
        loadCities();
        loadBranches();
    });
});
