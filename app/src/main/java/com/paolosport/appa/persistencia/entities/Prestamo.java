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
    private String origen;
    private String estado;

    public Prestamo(String id,String codigo, String descripcion,String foto, String talla, Timestamp fecha, Persona empleado, Local local, Marca marca,String origen, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.foto = foto;
        this.talla = talla;
        this.fecha = fecha;
        this.empleado = empleado;
        this.local = local;
        this.marca = marca;
        this.origen = origen;
        setEstado( estado );
    }
    public Prestamo(String id,String codigo, String descripcion,String foto, String talla, Timestamp fecha, Persona empleado, Local local, Marca marca,String origen ) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.foto = foto;
        this.talla = talla;
        this.fecha = fecha;
        this.empleado = empleado;
        this.local = local;
        this.marca = marca;
        this.origen = origen;
        setEstado( estado );
    }
    public Prestamo(String codigo, String descripcion,String foto, String talla, Timestamp fecha, Persona empleado, Local local, Marca marca,String origen, String estado ) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.foto = foto;
        this.talla = talla;
        this.fecha = fecha;
        this.empleado = empleado;
        this.local = local;
        this.marca = marca;
        this.origen = origen;
        setEstado( estado);
    }
    public Prestamo(String codigo, String descripcion,String foto, String talla, Timestamp fecha, Persona empleado, Local local, Marca marca,String origen ) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.foto = foto;
        this.talla = talla;
        this.fecha = fecha;
        this.empleado = empleado;
        this.local = local;
        this.marca = marca;
        this.origen = origen;
        setEstado( estado);
    }


    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
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

    public String getEstado() {

        switch (this.estado){

            case "P":
                return "Prestado";
            case "V":
                return "Vendido";
            case "D":
                return "Devuelto";
            default:
                return "Prestado";
        } // end switch
    }

    public void setEstado(String estado) {

        if (estado != null) {
            estado.toUpperCase();
            estado = estado.substring(0, 1);

            switch ( estado ){

                case "P":
                case "V":
                case "D":
                    break;
                default:
                    estado = "P";
                    break;
            }
        }
        else{
            estado = "P";
        }
        this.estado = estado;
    }
}