package org.utl.dsm.model;

public class DetalleTicket {
    private int idTicket;
    private int cantidad;
    private Double precio;
    private int idCombo;
    private int idProducto;

    public DetalleTicket() {
    }

    public DetalleTicket(int idTicket, int cantidad, Double precio, int idCombo, int idProducto) {
        this.idTicket = idTicket;
        this.cantidad = cantidad;
        this.precio = precio;
        this.idCombo = idCombo;
        this.idProducto = idProducto;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public int getIdCombo() {
        return idCombo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setIdCombo(int idCombo) {
        this.idCombo = idCombo;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
