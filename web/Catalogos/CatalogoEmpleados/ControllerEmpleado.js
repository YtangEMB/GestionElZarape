const API_BASE_URL = "http://localhost:8080/GestionElZarape/api/Empleado";
let selectedEmployeeId = null;

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
    document.getElementById("user-city").value = row.cells[4].textContent;
    document.getElementById("user-branch").value = row.cells[5].textContent;
    document.getElementById("user-password").value = row.cells[7].textContent;

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
        const response = await fetch(`${API_BASE_URL}/getAllEmpleado`);
        const empleados = await response.json();
        renderEmployeeList(empleados);
    } catch (error) {
        console.error("Error al cargar empleados:", error);
    }
}

async function insertEmployee(empleado) {
    try {
        const response = await fetch(`${API_BASE_URL}/insertEmpleado`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
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
        const response = await fetch(`${API_BASE_URL}/updateEmpleado`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
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
        const response = await fetch(`${API_BASE_URL}/deleteEmpleado`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
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

getAllEmployees();
