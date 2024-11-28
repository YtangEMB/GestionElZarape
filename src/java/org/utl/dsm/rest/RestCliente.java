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
import org.utl.dsm.controller.ControllerCliente;
import org.utl.dsm.model.Cliente;

@Path("Cliente")
public class RestCliente extends Application {

    @Path("getAllCliente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCliente() {
        List<Cliente> lista = null;
        Gson gson = new Gson();
        String out;

        try {
            ControllerCliente ce = new ControllerCliente();
            lista = ce.getAllClienteFromView();
            out = gson.toJson(lista);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"result\":\"Error de servidor\"}";
            return Response.serverError().entity(out).build();
        }

        return Response.ok(out).build();
    }

    @Path("insertCliente")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCliente(
            @FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos,
            @FormParam("telefono") String telefono,
            @FormParam("nombreCiudad") String nombreCiudad,
            @FormParam("nombreUsuario") String nombreUsuario,
            @FormParam("contrasenia") String contrasenia) {

        String result;
        ControllerCliente ce = new ControllerCliente();

        try {
            result = ce.insertarCliente(nombre, apellidos, telefono, nombreCiudad, nombreUsuario, contrasenia);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al insertar al cliente\"}";
        }

        return Response.ok(result).build();
    }

    @Path("deleteCliente")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCliente(@FormParam("idCliente") int idCliente) {
        String result;
        ControllerCliente ce = new ControllerCliente();

        try {
            result = ce.deleteCliente(idCliente);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al eliminar al cliente\"}";
            return Response.serverError().entity(result).build();
        }

        return Response.ok("{\"result\":\"" + result + "\"}").build();
    }

    @Path("updateCliente")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCliente(
            @FormParam("idCliente") int idCliente,
            @FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos,
            @FormParam("telefono") String telefono,
            @FormParam("nombreCiudad") String nombreCiudad,
            @FormParam("nombreUsuario") String nombreUsuario,
            @FormParam("contrasenia") String contrasenia
    ) {
        String result;
        ControllerCliente cc = new ControllerCliente();

        try {
            result = cc.updateCliente(idCliente, nombre, apellidos, telefono, nombreCiudad, nombreUsuario, contrasenia);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al actualizar el cliente\"}";
        }

        return Response.ok(result).build();
    }
}
