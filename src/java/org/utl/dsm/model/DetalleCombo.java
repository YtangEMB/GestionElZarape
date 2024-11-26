package org.utl.dsm.model;

public class DetalleCombo {
    private int idCombo;
    private int idProducto;
    private Double precio;

    public DetalleCombo() {
    }

    public DetalleCombo(int idCombo, int idProducto, Double precio) {
        this.idCombo = idCombo;
        this.idProducto = idProducto;
        this.precio = precio;
    }

    public int getIdCombo() {
        return idCombo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setIdCombo(int idCombo) {
        this.idCombo = idCombo;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
