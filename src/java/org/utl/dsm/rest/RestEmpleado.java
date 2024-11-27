package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.utl.dsm.controller.ControllerEmpleado;
import org.utl.dsm.model.Empleado;
import org.utl.dsm.model.Persona;
import org.utl.dsm.model.Usuario;

@Path("Empleado")
public class RestEmpleado extends Application {

    @Path("getAllEmpleado")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEmpleado() {
        List<Empleado> lista = null;
        Gson gson = new Gson();
        String out;

        try {
            ControllerEmpleado ce = new ControllerEmpleado();
            lista = ce.getAllEmpleadoFromView();
            out = gson.toJson(lista);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"result\":\"Error de servidor\"}";
            return Response.serverError().entity(out).build();
        }

        return Response.ok(out).build();
    }

    @Path("insertEmpleado")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertEmpleado(
            @FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos,
            @FormParam("telefono") String telefono,
            @FormParam("idCiudad") int idCiudad,
            @FormParam("nombreUsuario") String nombreUsuario,
            @FormParam("contrasenia") String contrasenia,
            @FormParam("idSucursal") int idSucursal,
            @FormParam("idPersona") int idPersona,
            @FormParam("idUsuario") int idUsuario,
            @FormParam("idEmpleado") int idEmpleado
    ) {

        String result;
        ControllerEmpleado ce = new ControllerEmpleado();

        try {
            result = ce.insertEmpleado(nombre, apellidos, telefono, idCiudad, nombreUsuario, contrasenia, idSucursal, idPersona, idUsuario, idEmpleado);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al insertar el empleado\"}";
        }

        return Response.ok(result).build();
    }

    @Path("deleteEmpleado")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEmpleado(@FormParam("idEmpleado") int idEmpleado) {
        String result;
        ControllerEmpleado ce = new ControllerEmpleado();

        try {
            result = ce.deleteEmpleado(idEmpleado);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al eliminar al empleado\"}";
            return Response.serverError().entity(result).build(); // Respuesta de error
        }

        return Response.ok("{\"result\":\"" + result + "\"}").build(); // Respuesta exitosa
    }

    @Path("updateEmpleado")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmpleado(
            @FormParam("idEmpleado") int idEmpleado,
            @FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos,
            @FormParam("telefono") String telefono,
            @FormParam("idCiudad") int idCiudad,
            @FormParam("nombreUsuario") String nombreUsuario,
            @FormParam("contrsenia") String contrsenia,
            @FormParam("idSucursal") int idSucursal) {

        String result;
        ControllerEmpleado ce = new ControllerEmpleado();

        try {
            result = ce.updateEmpleado(idEmpleado, nombre, apellidos, telefono, idCiudad, nombreUsuario, contrsenia, idSucursal);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al actualizar al empleado\"}";
        }

        return Response.ok(result).build();
    }

}
