package org.utl.dsm.model;

public class Empleado {
    private int idEmpleado;
    private int idSucursal;
    private int idPersona;
    private int idUsuario;
    private int activo;

    public Empleado() {
    }

    public Empleado(int idEmpleado, int idSucursal, int idPersona, int idUsuario, int activo) {
        this.idEmpleado = idEmpleado;
        this.idSucursal = idSucursal;
        this.idPersona = idPersona;
        this.idUsuario = idUsuario;
        this.activo = activo;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getActivo() {
        return activo;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
