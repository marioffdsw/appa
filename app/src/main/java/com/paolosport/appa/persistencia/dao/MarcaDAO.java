package com.paolosport.appa.persistencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.persistencia.entities.Marca;

import java.util.ArrayList;

/**
 * Created by Andres on 02/03/2015.
 */
public class MarcaDAO extends BaseDAO <Marca> {

    static final String TABLE_NAME = "marca";
    static final String KEY_ID = "id";
    static final String KEY_NOMBRE = "nombre";
    static final String KEY_URL = "url";

    public MarcaDAO(Context context, AdminSQLiteOpenHelper adminSQLiteOpenHelper) {
        super(context,adminSQLiteOpenHelper);
    }

    @Override
    public Estado create(Marca marca) {
        String sql = "INSERT into marca values("+
            marca.getId() +"," +
            marca.getNombre() +"," +
            marca.getUrl() + ")";
        try {
            open();
            db.execSQL(sql);
            close();
        }
        catch (Exception e){
            return Estado.ERROR_INSERTAR;
        }
        return Estado.INSERTADO;
    }

    @Override
    public Estado update(Marca marca) {
        try{
            ContentValues updateValues = new ContentValues();
            updateValues.put(KEY_NOMBRE, marca.getNombre());
            updateValues.put(KEY_URL, marca.getUrl());


            db.update(TABLE_NAME, updateValues, KEY_ID + "=" + marca.getId(), null);
        }
        catch( Exception e ){
            return Estado.ERROR_ACTUALIZAR;
        } // end catch

        return Estado.ACTUALIZADO;}

    @Override
    public Marca retrieve( String id ) {

        Cursor cursor = db.query(TABLE_NAME,                // FROM
                new String[]{KEY_ID, KEY_NOMBRE,KEY_URL},   // SELECT
                KEY_ID + "=" + id, null,                    // WHERE
                null,                                       // GROUP BY
                null,                                       // HAVING
                null,                                       // ORDER BY
                null                                        // LIMIT
        );

        Marca marca = null;

        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            id = cursor.getString(0);
            String name = cursor.getString(1);
            String url = cursor.getString(2);

            marca = new Marca( id, name ,url );
        }

        return marca;
    } // end method retrieve


    @Override
    public ArrayList<Marca> retrieveAll() {

        Cursor cursor = db.query(TABLE_NAME,                // FROM
                new String[]{KEY_ID, KEY_NOMBRE,KEY_URL},   // SELECT
                null, null,                                 // WHERE
                null,                                       // GROUP BY
                null,                                       // HAVING
                null,                                       // ORDER BY
                null                                        // LIMIT
        );

        Marca marca = null;

        ArrayList listaMarcas = new ArrayList<Marca>();
        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            // itera por todas las filas de la tabla y crea los objetos
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String url = cursor.getString(2);

                marca = new Marca(id, name, url);
                listaMarcas.add(marca);
            } while( cursor.moveToNext() );
        }

        return listaMarcas;
    }

    @Override
    public Estado delete(Marca marca) {
        try
        {
            db.delete( TABLE_NAME, KEY_ID + "=" + marca.getId(), null );
        }
        catch (Exception e){
            return Estado.ERROR_ELIMINAR;
        }

        return Estado.ELIMINADO;
    }
}