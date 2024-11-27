let selectedFoodId = null;

function loadFoodList() {
    fetch('http://localhost:8080/GestionElZarape/api/Alimento/getAllAlimentos')
            .then(response => {
                if (!response.ok)
                    throw new Error('Error en la respuesta del servidor');
                return response.json();
            })
            .then(data => {
                const foodList = document.getElementById('food-list');
                foodList.innerHTML = '';

                if (data.data.length === 0) {
                    const emptyRow = document.createElement('tr');
                    emptyRow.innerHTML = '<td colspan="5">No hay alimentos registrados.</td>';
                    foodList.appendChild(emptyRow);
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
}

function selectFood(food) {
    selectedFoodId = food.idProducto;
    document.getElementById('idAlimento').value = food.idProducto || '';
    document.getElementById('food-name').value = food.nombre || '';
    document.getElementById('food-price').value = food.precio || '';
    document.getElementById('food-description').value = food.descripcion || '';
    document.getElementById('food-category').value = food.categoria.descripcion || '';
    document.getElementById('food-photo').value = food.foto || ''; // Aquí asignamos la URL de la foto

    // Cambiar el comportamiento de los botones
    document.getElementById('delete-btn').style.display = 'inline-block';
    document.getElementById('edit-btn').style.display = 'inline-block';
    document.getElementById('cancel-edit').style.display = 'inline-block';
    document.querySelector('.edit').style.display = 'inline-block'; // Mostrar botón de editar
}

function clearForm() {
    selectedFoodId = null;
    document.getElementById('food-form').reset();
    document.getElementById('edit-btn').style.display = 'none';
    document.getElementById('delete-btn').style.display = 'none';
    document.getElementById('cancel-edit').style.display = 'none';
    document.querySelector('.edit').style.display = 'none'; // Ocultar el botón de editar
}

function saveFood(event) {
    event.preventDefault();

    const foodData = {
        idProducto: selectedFoodId,
        nombre: document.getElementById('food-name').value.trim(),
        descripcion: document.getElementById('food-description').value.trim(),
        foto: document.getElementById('food-photo').value.trim(), // Tomar URL de la foto
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

    const formData = new URLSearchParams(foodData).toString();

    fetch(url, {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: formData,
    })
            .then(response => {
                if (!response.ok)
                    throw new Error('Error al guardar el alimento');
                return response.json();
            })
            .then(data => {
                alert(data.result || 'Operación exitosa');
                clearForm();
                loadFoodList();
            })
            .catch(error => console.error('Error al guardar el alimento:', error));
}

function deleteFood() {
    if (!selectedFoodId) {
        alert('No hay ningún alimento seleccionado');
        return;
    }

    if (confirm('¿Estás seguro de eliminar este alimento?')) {
        const formData = new URLSearchParams({idProducto: selectedFoodId}).toString();

        fetch('http://localhost:8080/GestionElZarape/api/Alimento/deleteAlimento', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: formData,
        })
                .then(response => {
                    if (!response.ok)
                        throw new Error('Error al eliminar el alimento');
                    return response.json();
                })
                .then(data => {
                    alert(data.result || 'Eliminación exitosa');
                    clearForm();
                    loadFoodList();
                })
                .catch(error => console.error('Error al eliminar el alimento:', error));
    }
}

document.addEventListener('DOMContentLoaded', () => {
    loadFoodList();
    document.getElementById('food-form').addEventListener('submit', saveFood);
    document.getElementById('edit-btn').addEventListener('click', saveFood);
    document.getElementById('delete-btn').addEventListener('click', deleteFood);
    document.getElementById('cancel-edit').addEventListener('click', clearForm);
});

document.getElementById('search-input').addEventListener('input', function () {
    const searchTerm = this.value.toLowerCase();
    filterFoodList(searchTerm);
});

function filterFoodList(searchTerm) {
    const foodList = document.getElementById('food-list');
    const rows = foodList.getElementsByTagName('tr');

    Array.from(rows).forEach(row => {
        const foodName = row.cells[0].textContent.toLowerCase();
        const foodCategory = row.cells[4].textContent.toLowerCase();

        if (foodName.includes(searchTerm) || foodCategory.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}
