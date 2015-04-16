package com.paolosport.appa.persistencia.entities;

import java.sql.Timestamp;
import java.util.Date;

public class Prestamo {

    private String id;
    private String codigo;
    private String descripcion;
    private String foto;
    private String talla;
    private Timestamp fecha;
    private Persona empleado;
    private Local local;
    private Marca marca;

    public Prestamo(String id,String codigo, String descripcion,String foto, String talla, Timestamp fecha, Persona empleado, Local local, Marca marca) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.foto = foto;
        this.talla = talla;
        this.fecha = fecha;
        this.empleado = empleado;
        this.local = local;
        this.marca = marca;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
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