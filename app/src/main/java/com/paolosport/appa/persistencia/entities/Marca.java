package com.paolosport.appa.persistencia.entities;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import com.paolosport.appa.R;

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

    public Marca(String nombre, String url){
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

    public int getIcon(Context context){
        int i= context.getResources().getIdentifier(getUrl(), "drawable", context.getPackageName());
        return i;
    }

    @Override
    public String toString(){
        return this.getNombre() ;
    }
}
