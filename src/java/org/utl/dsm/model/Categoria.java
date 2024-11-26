package org.utl.dsm.model;

public class Categoria {
    private int idCategoria;
    private String descripcion;
    private String tipo;
    private int activo;

    public Categoria() {
    }

    public Categoria(int idCategoria, String descripcion, String tipo, int activo) {
        this.idCategoria = idCategoria;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.activo = activo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public int getActivo() {
        return activo;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
    
}
