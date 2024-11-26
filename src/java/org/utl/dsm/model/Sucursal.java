package org.utl.dsm.model;

public class Sucursal {
    private int idSucursal;
    private String nombre;
    private String latitud;
    private String longitud;
    private String foto;
    private String urlWeb;
    private String horarios;
    private String calle;
    private String numCalle;
    private String colonia;
    private int idCiudad;
    private int activo;

    public Sucursal() {
    }

    public Sucursal(int idSucursal, String nombre, String latitud, String longitud, String foto, String urlWeb, String horarios, String calle, String numCalle, String colonia, int idCiudad, int activo) {
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
        this.urlWeb = urlWeb;
        this.horarios = horarios;
        this.calle = calle;
        this.numCalle = numCalle;
        this.colonia = colonia;
        this.idCiudad = idCiudad;
        this.activo = activo;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getFoto() {
        return foto;
    }

    public String getUrlWeb() {
        return urlWeb;
    }

    public String getHorarios() {
        return horarios;
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

    public int getIdCiudad() {
        return idCiudad;
    }

    public int getActivo() {
        return activo;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
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

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}

