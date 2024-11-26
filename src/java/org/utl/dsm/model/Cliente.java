package org.utl.dsm.model;

public class Cliente {
    private int idCliente;
    private int idPersona;
    private int activo;
    private int idUsuario;

    public Cliente() {
    }

    public Cliente(int idCliente, int idPersona, int activo, int idUsuario) {
        this.idCliente = idCliente;
        this.idPersona = idPersona;
        this.activo = activo;
        this.idUsuario = idUsuario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public int getActivo() {
        return activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
