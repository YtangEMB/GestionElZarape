document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("food-form");
    const foodList = document.getElementById("food-list");
    const searchInput = document.getElementById("search-input");
    const cancelEditButton = document.getElementById("cancel-edit");

    // URL de la API
    const apiUrl = "http://localhost:8080/GestionElZarape/api/Alimento";  // Cambia esto por la URL real de tu API

    // Función para obtener la lista de alimentos
    function getAllAlimentos() {
        fetch(apiUrl + "/getAllAlimentos")
            .then(response => response.json())
            .then(data => {
                foodList.innerHTML = "";
                data.forEach(food => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td>${food.nombre}</td>
                        <td>${food.precio}</td>
                        <td>${food.descripcion}</td>
                        <td><img src="${food.foto}" alt="${food.nombre}" width="50"></td>
                        <td>${food.categoria.descripcion}</td>
                        <td>
                            <button class="btn btn-primary edit-btn" data-id="${food.idAlimento}">Editar</button>
                            <button class="btn btn-danger delete-btn" data-id="${food.idAlimento}">Eliminar</button>
                        </td>
                    `;
                    foodList.appendChild(row);
                });

                // Añadir eventos para editar y eliminar
                document.querySelectorAll(".edit-btn").forEach(button => {
                    button.addEventListener("click", handleEditFood);
                });

                document.querySelectorAll(".delete-btn").forEach(button => {
                    button.addEventListener("click", handleDeleteFood);
                });
            })
            .catch(error => console.error('Error:', error));
    }

    // Función para agregar un alimento
    form.addEventListener("submit", function(e) {
        e.preventDefault();

        const formData = new FormData(form);
        const food = {
            nombre: formData.get("food-name"),
            precio: parseFloat(formData.get("food-price")),
            descripcion: formData.get("food-description"),
            categoria: formData.get("food-category"),
            foto: formData.get("food-photo") ? formData.get("food-photo").name : null
        };

        fetch(apiUrl + "/addAlimento", {
            method: "POST",
            body: JSON.stringify(food),
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(response => response.json())
        .then(data => {
            alert("Alimento agregado correctamente");
            form.reset();
            getAllAlimentos();  // Recargar la lista
        })
        .catch(error => console.error('Error:', error));
    });

    // Función para editar un alimento
    function handleEditFood(event) {
        const id = event.target.getAttribute("data-id");

        fetch(apiUrl + "/getAlimentoById/" + id)
            .then(response => response.json())
            .then(food => {
                document.getElementById("food-name").value = food.nombre;
                document.getElementById("food-price").value = food.precio;
                document.getElementById("food-description").value = food.descripcion;
                document.getElementById("food-category").value = food.categoria.descripcion;

                cancelEditButton.style.display = "inline";
                form.onsubmit = function(e) {
                    e.preventDefault();
                    updateFood(id);
                };
            })
            .catch(error => console.error('Error:', error));
    }

    // Función para actualizar un alimento
    function updateFood(id) {
        const formData = new FormData(form);
        const food = {
            nombre: formData.get("food-name"),
            precio: parseFloat(formData.get("food-price")),
            descripcion: formData.get("food-description"),
            categoria: formData.get("food-category"),
            foto: formData.get("food-photo") ? formData.get("food-photo").name : null
        };

        fetch(apiUrl + "/updateAlimento/" + id, {
            method: "PUT",
            body: JSON.stringify(food),
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(response => response.json())
        .then(data => {
            alert("Alimento actualizado correctamente");
            form.reset();
            cancelEditButton.style.display = "none";
            getAllAlimentos();  // Recargar la lista
        })
        .catch(error => console.error('Error:', error));
    }

    // Función para eliminar un alimento
    function handleDeleteFood(event) {
        const id = event.target.getAttribute("data-id");

        if (confirm("¿Estás seguro de que quieres eliminar este alimento?")) {
            fetch(apiUrl + "/deleteAlimento/" + id, {
                method: "DELETE"
            })
            .then(response => response.json())
            .then(data => {
                alert("Alimento eliminado correctamente");
                getAllAlimentos();  // Recargar la lista
            })
            .catch(error => console.error('Error:', error));
        }
    }

    // Función para buscar alimentos
    searchInput.addEventListener("input", function() {
        const query = searchInput.value.toLowerCase();
        const rows = foodList.getElementsByTagName("tr");
        
        Array.from(rows).forEach(row => {
            const nameCell = row.cells[0];
            const categoryCell = row.cells[4];
            if (nameCell && categoryCell) {
                const name = nameCell.textContent.toLowerCase();
                const category = categoryCell.textContent.toLowerCase();
                if (name.includes(query) || category.includes(query)) {
                    row.style.display = "";
                } else {
                    row.style.display = "none";
                }
            }
        });
    });

    // Cancelar la edición
    cancelEditButton.addEventListener("click", function() {
        form.reset();
        cancelEditButton.style.display = "none";
        form.onsubmit = function(e) {
            e.preventDefault();
            addFood();
        };
    });

    // Inicializar la lista de alimentos
    getAllAlimentos();
});
