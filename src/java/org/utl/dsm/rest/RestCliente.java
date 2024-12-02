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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.utl.dsm.controller.ControllerCliente;
import org.utl.dsm.model.Cliente;

@Path("Cliente")
public class RestCliente extends Application {
    
    private final Gson gson = new Gson();

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
        
        Map<String, String> response = new HashMap<>();
        try {
            ControllerCliente ce = new ControllerCliente();
            String result = ce.insertarCliente(nombre, apellidos, telefono, nombreCiudad, nombreUsuario, contrasenia);
            response.put("result", "success");
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al insertar la sucursal: " + e.getMessage());
        }

        return Response.ok(gson.toJson(response)).build();
    }

    @Path("deleteCliente")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCliente(@FormParam("idCliente") int idCliente) {
        Map<String, String> response = new HashMap<>();
        try {
            ControllerCliente ce = new ControllerCliente();
            String result = ce.deleteCliente(idCliente);
            response.put("result", "success");
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al insertar la sucursal: " + e.getMessage());
        }

        return Response.ok(gson.toJson(response)).build();
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
        Map<String, String> response = new HashMap<>();
        try {
            ControllerCliente cc = new ControllerCliente();
            String result = cc.updateCliente(idCliente, nombre, apellidos, telefono, nombreCiudad, nombreUsuario, contrasenia);
            response.put("result", "success");
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al actualizar la sucursal: " + e.getMessage());
        }

        return Response.ok(gson.toJson(response)).build();
    }
}
