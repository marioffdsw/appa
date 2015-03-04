package com.paolosport.appa.persistencia.dao;

/**
 * Created by Andres on 02/03/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.persistencia.entities.Persona;

import java.util.ArrayList;

public class PersonaDAO extends BaseDAO <Persona> {

    static final String TABLE_NAME = "persona";
    static final String KEY_CEDULA = "cedula";
    static final String KEY_NOMBRE = "nombre";
    static final String KEY_TELEFONO = "telefono";
    static final String KEY_URL = "url";


    public PersonaDAO(Context context, AdminSQLiteOpenHelper adminSQLiteOpenHelper) {
        super(context,adminSQLiteOpenHelper);
    }

    @Override
    public Estado create(Persona persona) {
        String sql = "INSERT into Persona values("+
                persona.getCedula() +"," +
                persona.getNombre() +"," +
                persona.getTelefono() +"," +
                persona.getUrl() + ")";
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
    public Estado update(Persona persona) {
        try{
            ContentValues updateValues = new ContentValues();
            updateValues.put(KEY_NOMBRE, persona.getNombre());
            updateValues.put(KEY_TELEFONO, persona.getNombre());
            updateValues.put(KEY_URL, persona.getNombre());

            db.update(TABLE_NAME, updateValues, KEY_CEDULA + "=" + persona.getCedula(), null);
        }
        catch( Exception e ){
            return Estado.ERROR_ACTUALIZAR;
        } // end catch

        return Estado.ACTUALIZADO;}

    @Override
    public Persona retrieve( String id ) {

        Cursor cursor= null;
        try {
            cursor = db.query(TABLE_NAME,                // FROM
                    new String[]{KEY_CEDULA, KEY_NOMBRE, KEY_TELEFONO, KEY_URL},   // SELECT
                    KEY_CEDULA + "=" + id, null,                    // WHERE
                    null,                                       // GROUP BY
                    null,                                       // HAVING
                    null,                                       // ORDER BY
                    null                                        // LIMIT
            );
        }
        catch (Exception e){}
        Persona persona = null;

        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            id = cursor.getString(0);
            String name = cursor.getString(1);
            String tel = cursor.getString(2);
            String url = cursor.getString(3);


            persona = new Persona( id, name ,tel, url );
        }

        return persona;
    } // end method retrieve


    @Override
    public ArrayList<Persona> retrieveAll() {

        Cursor cursor = db.query(TABLE_NAME,                // FROM
                new String[]{KEY_CEDULA, KEY_NOMBRE, KEY_TELEFONO, KEY_URL},   // SELECT
                null, null,                                 // WHERE
                null,                                       // GROUP BY
                null,                                       // HAVING
                null,                                       // ORDER BY
                null                                        // LIMIT
        );

        Persona persona = null;

        ArrayList listaPersonas = new ArrayList<Persona>();
        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            // itera por todas las filas de la tabla y crea los objetos
            try {
                do {
                    String id = cursor.getString(0);
                    String name = cursor.getString(1);
                    String tel = cursor.getString(2);
                    String url = cursor.getString(3);

                    persona = new Persona(id, name, tel, url);
                    listaPersonas.add(persona);
                } while (cursor.moveToNext());
            }
            catch (Exception e){}
        }

        return listaPersonas;
    }

    @Override
    public Estado delete(Persona persona) {
        try
        {
            db.delete( TABLE_NAME, KEY_CEDULA + "=" + persona.getCedula(), null );
        }
        catch (Exception e){
            return Estado.ERROR_ELIMINAR;
        }

        return Estado.ELIMINADO;
    }
}

