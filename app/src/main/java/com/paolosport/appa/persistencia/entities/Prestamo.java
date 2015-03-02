package com.paolosport.appa.persistencia.entities;

import java.util.Date;

public class Prestamo {
    private String codigo;
    private String descripcion;
    private int talla;
    private Date fecha;
    private String empleado;
    private String local;

    public Prestamo(String codigo, String descripcion, int talla, Date fecha, String empleado, String local) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.talla = talla;
        this.fecha = fecha;
        this.empleado = empleado;
        this.local = local;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTalla() {
        return talla;
    }

    public void setTalla(int talla) {
        this.talla = talla;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}