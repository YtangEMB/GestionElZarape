package org.utl.dsm.model;

public class Tarjeta {
    private int idTarjeta;
    private String titular;
    private String numero;
    private String yy;
    private String mm;
    private String cvv;
    private String calle;
    private String numCalle;
    private String colonia;
    private String cp;
    private int activo;
    private int idCliente;
    private int idEstado;

    public Tarjeta() {
    }

    public Tarjeta(int idTarjeta, String titular, String numero, String yy, String mm, String cvv, String calle, String numCalle, String colonia, String cp, int activo, int idCliente, int idEstado) {
        this.idTarjeta = idTarjeta;
        this.titular = titular;
        this.numero = numero;
        this.yy = yy;
        this.mm = mm;
        this.cvv = cvv;
        this.calle = calle;
        this.numCalle = numCalle;
        this.colonia = colonia;
        this.cp = cp;
        this.activo = activo;
        this.idCliente = idCliente;
        this.idEstado = idEstado;
    }

    public int getIdTarjeta() {
        return idTarjeta;
    }

    public String getTitular() {
        return titular;
    }

    public String getNumero() {
        return numero;
    }

    public String getYy() {
        return yy;
    }

    public String getMm() {
        return mm;
    }

    public String getCvv() {
        return cvv;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumCalle() {
        return numCalle;
    }

    public String getColonia() {
        return colonia;
    }

    public String getCp() {
        return cp;
    }

    public int getActivo() {
        return activo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdTarjeta(int idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setNumCalle(String numCalle) {
        this.numCalle = numCalle;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
}
