package org.utl.dsm.model;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String contrasenia;
    private int activo;
    private int rol;
    private String lastToken;
    private String dateLastToken;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String contrasenia, int activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.activo = activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public int getActivo() {
        return activo;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getRol() {
        return rol;
    }

    public String getLastToken() {
        return lastToken;
    }

    public String getDateLastToken() {
        return dateLastToken;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public void setLastToken(String lastToken) {
        this.lastToken = lastToken;
    }

    public void setDateLastToken(String dateLastToken) {
        this.dateLastToken = dateLastToken;
    }
    
}
