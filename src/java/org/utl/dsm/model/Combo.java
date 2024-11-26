package org.utl.dsm.model;

public class Combo {
    private int idCombo;
    private String nombre;
    private Double total;

    public Combo() {
    }

    public Combo(int idCombo, String nombre, Double total) {
        this.idCombo = idCombo;
        this.nombre = nombre;
        this.total = total;
    }

    public int getIdCombo() {
        return idCombo;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getTotal() {
        return total;
    }

    public void setIdCombo(int idCombo) {
        this.idCombo = idCombo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    
}
