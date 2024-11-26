package org.utl.dsm.model;

public class Persona {
    private int idPersona;
    private String nombre;
    private String apellidos;
    private String telefono;
    private int idCiudad; 

    public Persona() {
    }

    public Persona(int idPersona, String nombre, String apellidos, String telefono, int idCiudad) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.idCiudad = idCiudad;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }
    
}
