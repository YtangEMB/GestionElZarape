function mostrarAlerta(mensaje, tipo = "info") {
    const alertContainer = document.getElementById("alert-container");

    const alerta = document.createElement("div");
    alerta.className = `alert alert-${tipo} alert-dismissible fade show`;
    alerta.role = "alert";
    alerta.innerHTML = `
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;

    alertContainer.appendChild(alerta);

    // Elimina la alerta automáticamente después de 5 segundos
    setTimeout(() => {
        alerta.remove();
    }, 5000);
}

async function validarLogin() {
    const usuarioInput = document.getElementById("usuario").value;
    const contraseniaInput = document.getElementById("contrasenia").value;

    if (!usuarioInput || !contraseniaInput) {
        mostrarAlerta("Por favor, llena todos los campos.", "warning");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/GestionElZarape/api/Empleado/getAllEmpleado", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            mostrarAlerta("Error al conectarse al servidor.", "danger");
            return;
        }

        const empleados = await response.json();

        const empleado = empleados.find(emp => 
            emp.usuario.nombre === usuarioInput && emp.usuario.contrasenia === contraseniaInput
        );

        if (empleado) {
            mostrarAlerta(`Bienvenido, ${empleado.persona.nombre}`, "success");
            window.location.href = "Main.html";
        } else {
            mostrarAlerta("Usuario o contraseña incorrectos.", "danger");
        }
    } catch (error) {
        console.error("Error al validar el login:", error);
        mostrarAlerta("Ocurrió un error al validar el inicio de sesión.", "danger");
    }
}
