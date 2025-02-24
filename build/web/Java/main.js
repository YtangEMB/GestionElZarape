function borrarToken() {
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
