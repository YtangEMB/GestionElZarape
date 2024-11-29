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
import org.utl.dsm.controller.ControllerEmpleado;
import org.utl.dsm.model.Empleado;

@Path("Empleado")
public class RestEmpleado extends Application {
    
    private final Gson gson = new Gson();

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
            @FormParam("nombreCiudad") String nombreCiudad,
            @FormParam("nombreUsuario") String nombreUsuario,
            @FormParam("contrasenia") String contrasenia,
            @FormParam("nombreSucursal") String nombreSucursal) {
        
        Map<String, String> response = new HashMap<>();
        try {
            ControllerEmpleado ce = new ControllerEmpleado();
            String result = ce.insertarEmpleado(nombre, apellidos, telefono, nombreCiudad, nombreUsuario, contrasenia, nombreSucursal);
            response.put("result", "success");
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al insertar la sucursal: " + e.getMessage());
        }

        return Response.ok(gson.toJson(response)).build();
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
            return Response.serverError().entity(result).build();
        }

        return Response.ok("{\"result\":\"" + result + "\"}").build();
    }

    @Path("updateEmpleado")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmpleado(
            @FormParam("idEmpleado") int idEmpleado,
            @FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos,
            @FormParam("telefono") String telefono,
            @FormParam("nombreCiudad") String nombreCiudad,
            @FormParam("nombreUsuario") String nombreUsuario,
            @FormParam("contrasenia") String contrasenia,
            @FormParam("nombreSucursal") String nombreSucursal
    ) {
        Map<String, String> response = new HashMap<>();
        try {
            ControllerEmpleado ce = new ControllerEmpleado();
            String result = ce.updateEmpleado(idEmpleado, nombre, apellidos, telefono, nombreCiudad, nombreUsuario, contrasenia, nombreSucursal);
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
