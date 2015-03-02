package com.paolosport.appa.persistencia.dao;

import android.content.Context;
import android.util.Log;

import com.paolosport.appa.persistencia.entities.Marca;

import java.util.ArrayList;

/**
 * Created by Andres on 02/03/2015.
 */
public class MarcaDAO extends BaseDAO <Marca> {

    public MarcaDAO(Context context) {
        super(context);
    }

    @Override
    public String create(Marca marca) {
        String sql = "INSERT into marca values("+
            marca.getId() +"," +
            marca.getNombre() +"," +
            marca.getUrl() + ")";
        try {
            open();
            db.execSQL(sql);
            close();
        }
        catch (exeption e){
            Log.w(TAG, "error insertar");
        }
        return null;

    }

    @Override
    String update() {
        return null;
    }

    @Override
    Marca retrieve() {
        return null;
    }

    @Override
    ArrayList<Marca> retrieveAll() {
        return null;
    }

    @Override
    String delete() {
        return null;
    }
}
