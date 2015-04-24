package com.paolosport.appa;

/**
 * Created by Andres on 17/04/2015.
 */
public class ItemPrestamo {
    String destino,
            marca,
            persona,
            codigo,
            talla,
            descripcion;

    public ItemPrestamo() {
        super();
    }

    public ItemPrestamo(String codigo,String marca,  String descripcion,String talla,String destino, String persona ) {
        super();
        this.destino = destino;
        this.marca = marca;
        this.persona = persona;
        this.codigo = codigo;
        this.talla = talla;
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
