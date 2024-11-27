package org.utl.dsm.model;

public class Sucursal {
    private int idSucursal;
    private String nombre;
    private double latitud;
    private double longitud;
    private String foto;
    private String urlWeb;
    private String horarios;
    private String direccion;
    private String calle;
    private String numCalle;
    private String colonia;
    private String ciudadNombre;
    private String estadoNombre;

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUrlWeb() {
        return urlWeb;
    }

    public void setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudadNombre() {
        return ciudadNombre;
    }

    public void setCiudadNombre(String ciudadNombre) {
        this.ciudadNombre = ciudadNombre;
    }

    public String getEstadoNombre() {
        return estadoNombre;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoNombre = estadoNombre;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumCalle() {
        return numCalle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setNumCalle(String numCalle) {
        this.numCalle = numCalle;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    
}
