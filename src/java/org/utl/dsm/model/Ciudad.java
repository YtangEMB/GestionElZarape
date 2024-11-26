package org.utl.dsm.model;

public class Ciudad {
    private int idCiudad;
    private String nombre;
    private int idEstado;

    public Ciudad() {
    }

    public Ciudad(int idCiudad, String nombre, int idEstado) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.idEstado = idEstado;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
}

