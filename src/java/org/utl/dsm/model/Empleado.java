package org.utl.dsm.model;

public class Empleado {
    private int idEmpleado;
    private int idSucursal;
    private int idPersona;
    private int idUsuario;
    private int activo;
    private Persona persona;
    private Ciudad ciudad;
    private Sucursal sucursal;
    private Usuario usuario;
    private Estado estado;
    

    public Empleado() {
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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
}
