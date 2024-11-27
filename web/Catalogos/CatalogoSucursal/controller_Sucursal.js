const API_BASE_URL = "http://localhost:8080/GestionElZarape/api/Sucursal";
let selectedBranchId = null;

function renderBranchList(sucursales) {
    const branchList = document.getElementById("branch-list");
    branchList.innerHTML = sucursales.map(sucursal => `
        <tr data-id="${sucursal.idSucursal}" onclick="selectBranch(${sucursal.idSucursal})">
            <td>${sucursal.nombre}</td>
            <td>${sucursal.calle}, ${sucursal.numCalle}, ${sucursal.colonia}</td>
            <td><img src="${sucursal.foto}" alt="Foto" style="width: 50px; height: auto;"></td>
            <td><a href="${sucursal.urlWeb}" target="_blank">${sucursal.urlWeb}</a></td>
            <td>${sucursal.latitud}</td>
            <td>${sucursal.longitud}</td>
            <td>${sucursal.horarios}</td>
            <td>${sucursal.ciudadNombre}</td>
            <td>${sucursal.estadoNombre}</td>
        </tr>
    `).join("");
}

function loadSucursal(idSucursal) {
    selectedBranchId = idSucursal;
    const row = document.querySelector(`#branch-list tr[data-id="${idSucursal}"]`);
    
    document.getElementById('idSucursal').value = idSucursal;
    document.getElementById('branch-name').value = row.cells[0].textContent;
    
    const direccion = row.cells[1].textContent.split(",");  
    const calle = direccion[0]?.trim() || "";  
    const numCalle = direccion[1]?.trim() || "";  
    const colonia = direccion[2]?.trim() || "";  
    
    document.getElementById('branch-calle').value = calle;
    document.getElementById('branch-numcalle').value = numCalle;
    document.getElementById('branch-colonia').value = colonia;
    
    document.getElementById('branch-photo').value = row.cells[2].querySelector('img').src;
    document.getElementById('branch-url').value = row.cells[3].textContent;
    document.getElementById('branch-lat').value = row.cells[4].textContent;
    document.getElementById('branch-lon').value = row.cells[5].textContent;
    document.getElementById('branch-time').value = row.cells[6].textContent;
    document.getElementById('branch-city').value = row.cells[7].textContent;
}


function selectBranch(idSucursal) {
    loadSucursal(idSucursal);
    showActionButtons(idSucursal);
}

function showActionButtons(idSucursal) {
    document.getElementById('edit-btn').style.display = 'inline-block';
    document.getElementById('delete-btn').style.display = 'inline-block';
    document.getElementById('cancel-edit').style.display = 'inline-block';
    
    document.getElementById('edit-btn').onclick = () => updateSucursal({
        idSucursal: selectedBranchId,
        nombre: document.getElementById("branch-name").value,
        latitud: document.getElementById("branch-lat").value,
        longitud: document.getElementById("branch-lon").value,
        foto: document.getElementById("branch-photo").value,
        urlWeb: document.getElementById("branch-url").value,
        horarios: document.getElementById("branch-time").value,
        calle: document.getElementById("branch-calle").value,
        numCalle: document.getElementById("branch-numcalle").value,
        colonia: document.getElementById("branch-colonia").value,
        nombreCiudad: document.getElementById("branch-city").value
    });
    
    document.getElementById('delete-btn').onclick = () => deleteSucursal(selectedBranchId);
    
    document.getElementById('cancel-edit').onclick = () => cancelEdit();
}

function cancelEdit() {
    selectedBranchId = null;
    document.getElementById('branch-form').reset();
    document.getElementById('edit-btn').style.display = 'none';
    document.getElementById('delete-btn').style.display = 'none';
    document.getElementById('cancel-edit').style.display = 'none';
}

async function getAllSucursales() {
    try {
        const response = await fetch(`${API_BASE_URL}/getAllSucursales`);
        const data = await response.json();
        if (data.result === "success") {
            renderBranchList(data.data);
        } else {
            alert("Error al obtener las sucursales: " + data.message);
        }
    } catch (error) {
        console.error("Error al conectar con el servidor:", error);
    }
}

async function insertSucursal(sucursal) {
    try {
        const response = await fetch(`${API_BASE_URL}/insertSucursal`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams(sucursal)
        });
        const data = await response.json();
        if (data.result === "success") {
            alert(data.message);
            getAllSucursales();
        } else {
            alert("Error al insertar la sucursal: " + data.message);
        }
    } catch (error) {
        console.error("Error al insertar la sucursal:", error);
    }
}

async function updateSucursal(sucursal) {
    try {
        const response = await fetch(`${API_BASE_URL}/updateSucursal`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams(sucursal)
        });
        const data = await response.json();
        if (data.result === "success") {
            alert(data.message);
            getAllSucursales();
            cancelEdit();
        } else {
            alert("Error al actualizar la sucursal: " + data.message);
        }
    } catch (error) {
        console.error("Error al actualizar la sucursal:", error);
    }
}

async function deleteSucursal(idSucursal) {
    try {
        const response = await fetch(`${API_BASE_URL}/deleteSucursal`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({ idSucursal })
        });
        const data = await response.json();
        if (data.result === "success") {
            alert(data.message);
            getAllSucursales();
            cancelEdit();
        } else {
            alert("Error al eliminar la sucursal: " + data.message);
        }
    } catch (error) {
        console.error("Error al eliminar la sucursal:", error);
    }
}

getAllSucursales();
