package org.utl.dsm.model;

public class Bebida {
    private int idBebida;
    private int idProducto;

    public Bebida() {
    }

    public Bebida(int idBebida, int idProducto) {
        this.idBebida = idBebida;
        this.idProducto = idProducto;
    }

    public int getIdBebida() {
        return idBebida;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdBebida(int idBebida) {
        this.idBebida = idBebida;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
