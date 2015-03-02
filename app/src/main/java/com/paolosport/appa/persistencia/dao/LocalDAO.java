package com.paolosport.appa.persistencia.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.entities.Local;

import java.util.ArrayList;

public class LocalDAO extends  BaseDAO<Local>{

    static final String TABLE_NAME = "local";
    static final String KEY_ID = "id";
    static String KEY_NOMBRE = "nombre";

    public LocalDAO(Context context, AdminSQLiteOpenHelper adminSQLiteOpenHelper ) {
        super(context, adminSQLiteOpenHelper );
    }

    // asume que la base de datos ha sido abierta previamente
    @Override
    public Estado create( Local local ) {

        try{

            ContentValues initialValues = new ContentValues();
            initialValues.put( KEY_ID, local.getId() );
            initialValues.put( KEY_NOMBRE, local.getNombre() );

            db.insert( TABLE_NAME, null, initialValues );
        }
        catch( Exception e ){
            return Estado.ERROR_INSERTAR;
        } // end catch

        return Estado.INSERTADO;
    }

    // asume que la base de datos ha sido abierta previamente
    @Override
    public Estado update( Local local ) {

        try{
            ContentValues updateValues = new ContentValues();
            updateValues.put( KEY_NOMBRE, local.getNombre() );

            db.update(TABLE_NAME, updateValues, KEY_ID + "=" + local.getId(), null);
        }
        catch( Exception e ){
            return Estado.ERROR_ACTUALIZAR;
        } // end catch

        return Estado.ACTUALIZADO;
    }

    // Asume que la base de datos ha sido abierta previamente
    @Override
    public Local retrieve( String id ) {

        Cursor cursor = db.query(TABLE_NAME,            // FROM
                new String[]{KEY_ID, KEY_NOMBRE},       // SELECT
                KEY_ID + "=" + id, null,               // WHERE
                null,                                   // GROUP BY
                null,                                   // HAVING
                null,                                   // ORDER BY
                null                                    // LIMIT
        );

        Local local = null;

        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            id = cursor.getString(0);
            String name = cursor.getString(1);

            local = new Local( id, name );
        }

        return local;
    } // end method retrieve

    @Override
    public ArrayList<Local> retrieveAll() {

        Cursor cursor = db.query(TABLE_NAME,            // FROM
                new String[]{KEY_ID, KEY_NOMBRE},       // SELECT
                null, null,                             // WHERE
                null,                                   // GROUP BY
                null,                                   // HAVING
                null,                                   // ORDER BY
                null                                    // LIMIT
        );

        Local local = null;

        ArrayList listaLocales = new ArrayList<Local>();
        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            // itera por todas las filas de la tabla y crea los objetos
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);

                local = new Local(id, name);
                listaLocales.add(local);
            } while( cursor.moveToNext() );
        }

        return listaLocales;
    }

    @Override
    public Estado delete( Local local ) {
        try{
            db.delete( TABLE_NAME, KEY_ID + "=" + local.getId(), null );
        }
        catch( Exception e ){
            return Estado.ERROR_ELIMINAR
        } // end catch

        return Estado.ELIMINADO;
    }
} // end class LocalDAO
