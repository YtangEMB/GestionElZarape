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
import org.utl.dsm.controller.ControllerAlimento;
import org.utl.dsm.model.Alimento;
import org.utl.dsm.model.Producto;

@Path("Alimento")
public class RestAlimento extends Application {

    @Path("getAllAlimentos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAlimentos() {
        List<Producto> lista = null;
        Gson gson = new Gson();
        String out = null;
        ControllerAlimento ca = null;
        try {
            ca = new ControllerAlimento();
            lista = ca.getAllAlimentosFromView();
            out = gson.toJson(lista);
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"result":"Error de servidor"}
                  """;
        }
        return Response.ok(out).build();
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
        
        String result;
        ControllerAlimento ca = new ControllerAlimento();
        
        try {
            result = ca.insertAlimento(nombre, descripcion, foto, precio, categoria);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al insertar el alimento\"}";
        }
        
        return Response.ok(result).build();
    }
    
    @Path("deleteAlimento")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAlimento(@FormParam("idProducto") int idProducto) {
        String result;
        ControllerAlimento ca = new ControllerAlimento();
        
        try {
            result = ca.deleteAlimento(idProducto);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al eliminar el alimento\"}";
        }
        
        return Response.ok(result).build();
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

        String result;
        ControllerAlimento ca = new ControllerAlimento();

        try {
            result = ca.updateAlimento(idProducto, nombre, descripcion, foto, precio, categoria);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\":\"Error al actualizar el alimento\"}";
        }

        return Response.ok(result).build();
    }
}
