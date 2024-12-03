let selectedBebidaId = null;

function loadBebidasList() {
    fetch('http://localhost:8080/GestionElZarape/api/Bebida/getAllBebidas')
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
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
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
}

function deleteBebida() {
    if (!selectedBebidaId) {
        alert('No hay ningúna bebida seleccionada');
        return;
    }

    if (confirm('¿Estás seguro de eliminar esta bebida?')) {
        const formData = new URLSearchParams({idProducto: selectedBebidaId}).toString();

        fetch('http://localhost:8080/GestionElZarape/api/Bebida/deleteBebida', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
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

loadBebidasList();