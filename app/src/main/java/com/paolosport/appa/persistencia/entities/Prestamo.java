package com.paolosport.appa.persistencia.entities;

import java.util.Date;

public class Prestamo {
    private String codigo;
    private String descripcion;
    private int talla;
    private Date fecha;
    private Persona empleado;
    private Local local;
    private Marca marca;

    public Prestamo(String codigo, String descripcion, int talla, Date fecha, Persona empleado, Local local, Marca marca) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.talla = talla;
        this.fecha = fecha;
        this.empleado = empleado;
        this.local = local;
        this.marca = marca;
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

    public Persona getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Persona empleado) {
        this.empleado = empleado;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}