package com.gymmanagement.model;

public class Payment {
    private int id;
    private int clientId;
    private String fechaPago;
    private double monto;
    private String tipoPago;

    public Payment() {
    }

    public Payment(int id, int clientId, String fechaPago, double monto, String tipoPago) {
        this.id = id;
        this.clientId = clientId;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.tipoPago = tipoPago;
    }

    public Payment(int clientId, String fechaPago, double monto, String tipoPago) {
        this.clientId = clientId;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.tipoPago = tipoPago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    @Override
    public String toString() {
        return "Payment{id=" + id + ", clientId=" + clientId + ", fechaPago='" + fechaPago + '\'' +
                ", monto=" + monto + ", tipoPago='" + tipoPago + '\'' + '}';
    }
}
