package com.paolosport.appa.persistencia.entities;

public class Persona {
    private String cedula;
    private String nombre;
    private String telefono;
    private String url;

    public Persona(String cedula, String nombre, String telefono, String url) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.url = url;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
} // end class Persona
