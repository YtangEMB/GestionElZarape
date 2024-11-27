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
import org.utl.dsm.controller.ControllerSucursal;
import org.utl.dsm.model.Sucursal;

@Path("Sucursal")
public class RestSucursal extends Application {

    private final Gson gson = new Gson();

    @Path("getAllSucursales")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSucursales() {
        List<Sucursal> lista;
        Map<String, Object> response = new HashMap<>();
        try {
            ControllerSucursal cs = new ControllerSucursal();
            lista = cs.getAllSucursalesFromView();
            response.put("result", "success");
            response.put("data", lista);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al obtener las sucursales");
        }
        return Response.ok(gson.toJson(response)).build();
    }
    
    @Path("insertSucursal")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertSucursal(
            @FormParam("nombre") String nombre,
            @FormParam("latitud") double latitud,
            @FormParam("longitud") double longitud,
            @FormParam("foto") String foto,
            @FormParam("urlWeb") String urlWeb,
            @FormParam("horarios") String horarios,
            @FormParam("calle") String calle,
            @FormParam("numCalle") String numCalle,
            @FormParam("colonia") String colonia,
            @FormParam("nombreCiudad") String nombreCiudad) {

        Map<String, String> response = new HashMap<>();
        try {
            ControllerSucursal cs = new ControllerSucursal();
            String result = cs.insertSucursal(nombre, latitud, longitud, foto, urlWeb, horarios, calle, numCalle, colonia, nombreCiudad);
            response.put("result", "success");
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al insertar la sucursal: " + e.getMessage());
        }
        return Response.ok(gson.toJson(response)).build();
    }
    
    @Path("deleteSucursal")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSucursal(@FormParam("idSucursal") int idSucursal) {
        Map<String, String> response = new HashMap<>();
        try {
            ControllerSucursal cs = new ControllerSucursal();
            String result = cs.deleteSucursal(idSucursal);
            response.put("result", "success");
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "error");
            response.put("message", "Error al eliminar la sucursal: " + e.getMessage());
        }
        return Response.ok(gson.toJson(response)).build();
    }
    
    @Path("updateSucursal")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSucursal(
        @FormParam("idSucursal") int idSucursal,
        @FormParam("nombre") String nombre,
        @FormParam("latitud") double latitud,
        @FormParam("longitud") double longitud,
        @FormParam("foto") String foto,
        @FormParam("urlWeb") String urlWeb,
        @FormParam("horarios") String horarios,
        @FormParam("calle") String calle,
        @FormParam("numCalle") String numCalle,
        @FormParam("colonia") String colonia,
        @FormParam("nombreCiudad") String nombreCiudad
    ) {
        Map<String, String> response = new HashMap<>();
        try {
            ControllerSucursal cs = new ControllerSucursal();
            String result = cs.updateSucursal(idSucursal, nombre, latitud, longitud, foto, urlWeb, horarios, calle, numCalle, colonia, nombreCiudad);
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

