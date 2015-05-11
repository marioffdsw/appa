package com.paolosport.appa.ui;

import android.content.Context;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;

/**
 * Created by Andres on 17/04/2015.
 */
public class ItemPrestamo {
    String  destino,
            destino2,
            marca,
            marca2,
            persona,
            persona2,
            codigo,
            talla,
            descripcion,
            url_persona,
            origen;

    AdminSQLiteOpenHelper helper;
    MarcaDAO marcaDAO;
    LocalDAO localDAO;
    PersonaDAO personaDAO;
    private Context context;

    public ItemPrestamo() {
        super();
    }

    public ItemPrestamo(String codigo,String marca,String descripcion,String talla,String destino, String persona,String origen,Context context ) {
        super();
        this.destino = destino;
        this.marca = marca;
        this.persona = persona;
        this.codigo = codigo;
        this.talla = talla;
        this.context = context;
        this.descripcion = descripcion;
        this.origen = "";

        helper     = new AdminSQLiteOpenHelper(context);
        localDAO   = new LocalDAO( context.getApplicationContext(), helper );
        marcaDAO   = new MarcaDAO( context.getApplicationContext(), helper );
        personaDAO = new PersonaDAO( context.getApplicationContext(), helper );

        localDAO.open();
        destino2 = localDAO.retrieve(destino).getNombre().toString();
        localDAO.close();

        personaDAO.open();
        persona2 = personaDAO.retrieve(persona).getNombre();
        personaDAO.close();

        marcaDAO.open();
        marca2 = marcaDAO.retrieve(marca).getNombre();
        marcaDAO.close();

        personaDAO.open();
        url_persona = personaDAO.retrieve(persona).getUrl();
        personaDAO.close();

    }


    public String getUrlPersona(){
        return url_persona;
    }

    public String getMarca() {
        return marca2;
    }

    public String getMarcaId() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDestino() {
        return destino2;
    }

    public String getDestinoId() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getPersona() {
        return persona2;
    }

    public String getPersonaId() {
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

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

}
