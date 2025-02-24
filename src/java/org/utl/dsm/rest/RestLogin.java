package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.utl.dsm.controller.ControllerLogin;
import org.utl.dsm.model.Empleado;

@Path("Login")
public class RestLogin {
    
    private final Gson gson = new Gson();
    
    // Método para validar el login
    @Path("validarLogin")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarLogin(
            @FormParam("usuario") String usuario,
            @FormParam("contrasenia") String contrasenia) {

        Map<String, String> response = new HashMap<>();
        try {
            ControllerLogin ce = new ControllerLogin();
            Empleado empleado = ce.validarLogin(usuario, contrasenia);

            if (empleado != null) {
                response.put("result", "success");
                response.put("message", "Login exitoso");
                response.put("idEmpleado", String.valueOf(empleado.getIdEmpleado()));
                response.put("nombre", empleado.getPersona().getNombre());
            } else {
                response.put("result", "error");
                response.put("message", "Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al validar el login: " + e.getMessage());
        }

        return Response.ok(gson.toJson(response)).build();
    }
    
    
    @Path("cheeky")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response checkingUser(@QueryParam("nombreU") @DefaultValue("") String nombreU) {

        String out = null;
        String usuario = null;
        ControllerLogin cu = new ControllerLogin();

        try {
            usuario = cu.checkUsers(nombreU);
            out = new Gson().toJson(usuario);
        } catch (Exception e) {
            out = """
            {"error": "Por ahi no joven"}
            """;
            System.out.println(e.getMessage());
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("eliminartoken")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public Response deleteToken(@QueryParam("nombreU") String nombreU) {
        if (nombreU == null || nombreU.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El nombre de usuario es obligatorio\"}")
                    .build();
        }

        ControllerLogin cu = new ControllerLogin();
        String out;

        try {
            boolean success = cu.deleteToken(nombreU);
            if (success) {
                out = "{\"message\": \"Token eliminado exitosamente\"}";
                return Response.status(Response.Status.OK).entity(out).build();
            } else {
                out = "{\"error\": \"No se encontró el usuario o ya no tiene un token\"}";
                return Response.status(Response.Status.NOT_FOUND).entity(out).build();
            }
        } catch (Exception e) {
            out = "{\"error\": \"Error al eliminar el token\"}";
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    @Path("validarToken")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarToken(@QueryParam("nombreU") String usuario, @QueryParam("token") String token) {
        Gson gson = new Gson();
        String out;

        try {
            ControllerLogin ce = new ControllerLogin();
            boolean esValido = ce.verificarToken(usuario, token);

            out = gson.toJson(Collections.singletonMap("valido", esValido));
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"valido\": false, \"error\": \"Error de servidor\"}";
            return Response.serverError().entity(out).build();
        }

        return Response.ok(out).build();
    }
}
