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
import org.utl.dsm.controller.ControllerBebida;
import org.utl.dsm.model.Producto;

@Path("Bebida")
public class RestBebidas extends Application {

    @Path("getAllBebidas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBebidas() {
        List<Producto> lista = null;
        Gson gson = new Gson();
        String out = null;
        ControllerBebida ca = null;
        try {
            ca = new ControllerBebida();
            lista = ca.getAllBebidasFromView();
            out = gson.toJson(lista);
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"result":"Error de servidor"}
                  """;
        }
        return Response.ok(out).build();
    }
    
    @Path("insertBebida")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertBebida(
            @FormParam("nombre") String nombre,
            @FormParam("descripcion") String descripcion,
            @FormParam("foto") String foto,
            @FormParam("precio") int precio,
            @FormParam("categoria") String categoria) {
        
        String result;
        ControllerBebida ca = new ControllerBebida();
        
        try {
            result = ca.insertBebida(nombre, descripcion, foto, precio, categoria);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al insertar la Bebida\"}";
        }
        
        return Response.ok(result).build();
    }
    
    @Path("deleteBebida")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBebida(@FormParam("idProducto") int idProducto) {
        String result;
        ControllerBebida ca = new ControllerBebida();
        
        try {
            result = ca.deleteBebida(idProducto);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al eliminar el alimento\"}";
        }
        
        return Response.ok(result).build();
    }
    
    @Path("updateBebida")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBebida(
            @FormParam("idProducto") int idProducto,
            @FormParam("nombre") String nombre,
            @FormParam("descripcion") String descripcion,
            @FormParam("foto") String foto,
            @FormParam("precio") int precio,
            @FormParam("categoria") String categoria) {

        String result;
        ControllerBebida ca = new ControllerBebida();

        try {
            result = ca.updateBebida(idProducto, nombre, descripcion, foto, precio, categoria);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al actualizar la Bebida\"}";
        }

        return Response.ok(result).build();
    }
}

