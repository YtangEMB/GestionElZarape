document.addEventListener("DOMContentLoaded", async () => {
    await verificarSesion();
});

async function verificarSesion() {
    const authData = localStorage.getItem("authData");

    if (authData) {
        try {
            const { usuario, token } = JSON.parse(authData);

            if (usuario && token) {
                const tokenValido = await validarTokenEnServidor(usuario, token);
                
                if (tokenValido) {
                    console.log("Sesión válida, redirigiendo...");
                    window.location.href = "Main.html";
                    return;
                } else {
                    console.log("Token inválido, eliminando sesión.");
                    localStorage.removeItem("authData");
                }
            }
        } catch (error) {
            console.error("Error al procesar los datos de sesión:", error);
            localStorage.removeItem("authData");
        }
    }
    console.log("No hay sesión activa, seguir con el login.");
}

async function validarTokenEnServidor(usuario, token) {
    try {
        const response = await fetch(`http://localhost:8080/GestionElZarape/api/Login/validarToken?nombreU=${usuario}&token=${token}`);
        const result = await response.json();
        return result.valido;
    } catch (error) {
        console.error("Error al validar el token:", error);
        return false;
    }
}

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

    setTimeout(() => {
        alerta.remove();
    }, 10000);
}

async function obtenerToken(usuario) {
    try {
        const response = await fetch(`http://localhost:8080/GestionElZarape/api/Login/cheeky?nombreU=${usuario}`);

        const textResponse = await response.text();
        console.log("Respuesta de la API:", textResponse);

        const parsedToken = JSON.parse(textResponse);
        return parsedToken;
    } catch (error) {
        console.error("Error al obtener el token:", error);
        return null;
    }
}

async function validarLogin() {
    const usuarioInput = document.getElementById("usuario").value;
    const contraseniaInput = document.getElementById("contrasenia").value;

    if (!usuarioInput || !contraseniaInput) {
        mostrarAlerta("Por favor, llena todos los campos.", "warning");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/GestionElZarape/api/Login/validarLogin", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                "usuario": usuarioInput,
                "contrasenia": contraseniaInput
            })
        });

        const result = await response.json();

        if (result.result === "success") {
            mostrarAlerta(`Bienvenido, ${result.nombre}`, "success");

            const token = await obtenerToken(usuarioInput);
            
            if (token) {
                localStorage.setItem("authData", JSON.stringify({
                    usuario: usuarioInput, 
                    token: token 
                }));

                window.location.href = "Main.html";
            } else {
                mostrarAlerta("No se pudo obtener el token de autenticación.", "warning");
            }
        } else {
            mostrarAlerta(result.message, "danger");
        }
    } catch (error) {
        console.error("Error al validar el login:", error);
        mostrarAlerta("Ocurrió un error al validar el inicio de sesión.", "danger");
    }
}
