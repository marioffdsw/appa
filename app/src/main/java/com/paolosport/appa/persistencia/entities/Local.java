package com.paolosport.appa.persistencia.entities;

public class Local {
    private String id;
    private String nombre;

    public Local( String id, String nombre ){
        this.id = id;
        this.nombre = nombre;
    } // end constructor

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
} // end class Local
