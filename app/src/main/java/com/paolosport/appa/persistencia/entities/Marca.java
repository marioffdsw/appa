package com.paolosport.appa.persistencia.entities;

/**
 * Created by mario on 2/03/15.
 */
public class Marca {

    private String id;
    private String nombre;
    private String url;

    public Marca(String id,String nombre, String url){
        this.id = id;
        this.nombre = nombre;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
