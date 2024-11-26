package org.utl.dsm.model;

import java.util.Date;

public class Ticket {
    private int idTicket;
    private String ticket;
    private Date fecha;
    private String pagado;
    private int idCliente;
    private int idSucursal;
    private int estatus;

    public Ticket() {
    }

    public Ticket(int idTicket, String ticket, Date fecha, String pagado, int idCliente, int idSucursal, int estatus) {
        this.idTicket = idTicket;
        this.ticket = ticket;
        this.fecha = fecha;
        this.pagado = pagado;
        this.idCliente = idCliente;
        this.idSucursal = idSucursal;
        this.estatus = estatus;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public String getTicket() {
        return ticket;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getPagado() {
        return pagado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
