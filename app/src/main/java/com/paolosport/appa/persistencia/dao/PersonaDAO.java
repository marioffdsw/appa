package com.paolosport.appa.persistencia.dao;

/**
 * Created by Andres on 02/03/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.entities.Persona;

import java.util.ArrayList;
public class PersonaDAO extends BaseDAO <Persona> {

    static final String TABLE_NAME = "persona";
    static final String KEY_CEDULA = "cedula";
    static final String KEY_NOMBRE = "nombre";
    static final String KEY_TELEFONO = "telefono";
    static final String KEY_URL = "foto";


    public PersonaDAO(Context context, AdminSQLiteOpenHelper adminSQLiteOpenHelper) {
        super(context,adminSQLiteOpenHelper);
    }

    @Override
    public Estado create(Persona persona) {

        try{
            ContentValues initialValues = new ContentValues();
            initialValues.put( KEY_CEDULA, persona.getCedula() );
            initialValues.put( KEY_NOMBRE, persona.getNombre() );
            initialValues.put(KEY_TELEFONO, persona.getTelefono());
            initialValues.put(KEY_URL, persona.getUrl());

            long i = db.insert( TABLE_NAME, null, initialValues );
            if ( i == -1 )
                throw new Exception( "Error al insertar" );
        }
        catch( Exception e ){
            return Estado.ERROR_INSERTAR;
        } // end catch

        return Estado.INSERTADO;
    }

    @Override
    public Estado update(Persona persona) {
        try{
            ContentValues updateValues = new ContentValues();
            updateValues.put(KEY_NOMBRE, persona.getNombre());
            updateValues.put(KEY_TELEFONO, persona.getTelefono());
            updateValues.put(KEY_URL, persona.getUrl());

            db.update(TABLE_NAME, updateValues, KEY_CEDULA + "=" + persona.getCedula(), null);
        }
        catch( Exception e ){
            return Estado.ERROR_ACTUALIZAR;
        } // end catch

        return Estado.ACTUALIZADO;
    }

    public Estado update(Persona persona, String cedulaAntigua ) {
        try{
            ContentValues updateValues = new ContentValues();
            updateValues.put(KEY_NOMBRE, persona.getNombre());
            updateValues.put( KEY_CEDULA, persona.getCedula() );
            updateValues.put(KEY_TELEFONO, persona.getTelefono());
            updateValues.put(KEY_URL, persona.getUrl());

            long i = db.update(TABLE_NAME, updateValues, KEY_CEDULA + "=" + cedulaAntigua, null);
            if ( i == -1 )
                throw new Exception();
        }
        catch( Exception e ){
            return Estado.ERROR_ACTUALIZAR;
        } // end catch

        return Estado.ACTUALIZADO;
    }

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
        catch (Exception e){
            e.printStackTrace();
        }
        Persona persona = null;

        if ( cursor.moveToFirst() ){ // ha encontrado el local con la id entregada


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
        if (  cursor.moveToFirst() ){ // ha encontrado el local con la id entregada

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

