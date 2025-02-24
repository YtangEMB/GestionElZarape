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
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.utl.dsm.controller.ControllerAlimento;
import org.utl.dsm.model.Producto;

@Provider
@Path("Alimento")
public class RestAlimento extends Application {
    
    private final Gson gson = new Gson();

    @Path("getAllAlimentos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAlimentos() {
        List<Producto> lista;
        Map<String, Object> response = new HashMap<>();
        try {
            ControllerAlimento ca = new ControllerAlimento();
            lista = ca.getAllAlimentosFromView();
            response.put("result", "success");
            response.put("data", lista);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al obtener los alimentos");
        }
        return Response.ok(gson.toJson(response)).build();
    }

    @Path("insertAlimento")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertAlimento(
            @FormParam("nombre") String nombre,
            @FormParam("descripcion") String descripcion,
            @FormParam("foto") String foto,
            @FormParam("precio") int precio,
            @FormParam("categoria") String categoria) {

        Map<String, String> response = new HashMap<>();
        try {
            ControllerAlimento ca = new ControllerAlimento();
            String result = ca.insertAlimento(nombre, descripcion, foto, precio, categoria);
            response.put("result", "success");
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al insertar el alimento");
        }
        return Response.ok(gson.toJson(response)).build();
    }

    @Path("deleteAlimento")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAlimento(@FormParam("idProducto") int idProducto) {
        Map<String, String> response = new HashMap<>();
        try {
            ControllerAlimento ca = new ControllerAlimento();
            String result = ca.deleteAlimento(idProducto);
            response.put("result", "success");
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al eliminar el alimento");
        }
        return Response.ok(gson.toJson(response)).build();
    }

    @Path("updateAlimento")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAlimento(
            @FormParam("idProducto") int idProducto,
            @FormParam("nombre") String nombre,
            @FormParam("descripcion") String descripcion,
            @FormParam("foto") String foto,
            @FormParam("precio") int precio,
            @FormParam("categoria") String categoria) {

        Map<String, String> response = new HashMap<>();
        try {
            ControllerAlimento ca = new ControllerAlimento();
            String result = ca.updateAlimento(idProducto, nombre, descripcion, foto, precio, categoria);
            response.put("result", "success");
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al actualizar el alimento");
        }
        return Response.ok(gson.toJson(response)).build();
    }
}
