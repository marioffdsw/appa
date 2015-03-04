package com.paolosport.appa.persistencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.persistencia.entities.Persona;
import com.paolosport.appa.persistencia.entities.Prestamo;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by mario on 2/03/15.
 */
public class PrestamoDAO extends BaseDAO<Prestamo>{

    static final String TABLE_NAME = "prestamos";
    static final String KEY_CODIGO = "codigo";
    static final String KEY_DESCRIPCION = "descripcion";
    static final String KEY_TALLA = "talla";
    static final String KEY_FECHA = "fecha";
    static final String KEY_EMPLEADO = "empleado";
    static final String KEY_LOCAL = "local";
    static final String KEY_MARCA = "marca";

    private LocalDAO localDAO;
    private MarcaDAO marcaDAO;
    private PersonaDAO personaDAO;

    public PrestamoDAO(Context context, AdminSQLiteOpenHelper helper) {
        super(context, helper);
    }

    public void setLocalDAO(LocalDAO localDAO) {
        this.localDAO = localDAO;
    }

    public void setMarcaDAO(MarcaDAO marcaDAO) {
        this.marcaDAO = marcaDAO;
    }

    public void setPersonaDAO(PersonaDAO personaDAO) {
        this.personaDAO = personaDAO;
    }

    @Override
    public Estado create( Prestamo prestamo ) {

        try{

            ContentValues initialValues = new ContentValues();
            initialValues.put( KEY_CODIGO, prestamo.getCodigo() );
            initialValues.put( KEY_DESCRIPCION, prestamo.getDescripcion() );
            initialValues.put( KEY_TALLA, prestamo.getTalla() );
            initialValues.put( KEY_FECHA, prestamo.getFecha().toString() );
            initialValues.put( KEY_EMPLEADO, prestamo.getEmpleado().getCedula() );
            initialValues.put( KEY_LOCAL, prestamo.getLocal().getId() );
            initialValues.put( KEY_MARCA, prestamo.getMarca().getId() );

            db.insert( TABLE_NAME, null, initialValues );
        }
        catch( Exception e ){
            return Estado.ERROR_INSERTAR;
        } // end catch

        return Estado.INSERTADO;
    }

    @Override
    public Estado update(Prestamo prestamo) {
        try{
            ContentValues updateValues = new ContentValues();
            updateValues.put( KEY_CODIGO, prestamo.getCodigo() );
            updateValues.put( KEY_DESCRIPCION, prestamo.getDescripcion() );
            updateValues.put( KEY_TALLA, prestamo.getTalla() );
            updateValues.put( KEY_FECHA, prestamo.getFecha().toString() );
            updateValues.put( KEY_EMPLEADO, prestamo.getEmpleado().getCedula() );
            updateValues.put( KEY_LOCAL, prestamo.getLocal().getId() );
            updateValues.put( KEY_MARCA, prestamo.getMarca().getId() );

            db.update(TABLE_NAME, updateValues, KEY_CODIGO + "=" + prestamo.getCodigo(), null);
        }
        catch( Exception e ){
            return Estado.ERROR_ACTUALIZAR;
        } // end catch

        return Estado.ACTUALIZADO;
    }

    // asume que se se ha establecido los otros daos
    @Override
    public Prestamo retrieve(String key) {
        Cursor cursor= null;
        try{
            cursor = db.query(TABLE_NAME,            // FROM
                    new String[]{ KEY_CODIGO,
                            KEY_DESCRIPCION,
                            KEY_TALLA,
                            KEY_FECHA,
                            KEY_EMPLEADO,
                            KEY_LOCAL,
                            KEY_MARCA },       // SELECT
                    KEY_CODIGO + "=" + key, null,           // WHERE
                    null,                                   // GROUP BY
                    null,                                   // HAVING
                    null,                                   // ORDER BY
                    null                                    // LIMIT
            );
        }
        catch (Exception e){
            Log.i(TAG, "NO HAY REGISTROS");}

        Prestamo prestamo = null;

        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            key = cursor.getString(0);
            String descripcion = cursor.getString(1);
            Integer talla = cursor.getInt(2);
            Timestamp fecha = formatearFecha( cursor.getString( 3 ) );
            Persona empleado = personaDAO.retrieve( cursor.getString( 4 ) );
            Local local = localDAO.retrieve( cursor.getString( 5 ) );
            Marca marca = marcaDAO.retrieve( cursor.getString( 6 ) );

            prestamo = new Prestamo( key, descripcion, talla, fecha, empleado, local, marca );
        }

        return prestamo;
    }

    // asume que se ha asignado los otros daos
    @Override
    public ArrayList<Prestamo> retrieveAll() {
        Cursor cursor = db.query(TABLE_NAME,            // FROM
                new String[]{ KEY_CODIGO,
                        KEY_DESCRIPCION,
                        KEY_TALLA,
                        KEY_FECHA,
                        KEY_EMPLEADO,
                        KEY_LOCAL,
                        KEY_MARCA },       // SELECT
                null, null,           // WHERE
                null,                                   // GROUP BY
                null,                                   // HAVING
                null,                                   // ORDER BY
                null                                    // LIMIT
        );

        Prestamo prestamo = null;

        ArrayList listaPrestamos = new ArrayList<Prestamo>();
        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            // itera por todas las filas de la tabla y crea los objetos
            try {
                do {
                    String key = cursor.getString(0);
                    String descripcion = cursor.getString(1);
                    Integer talla = cursor.getInt(2);
                    Timestamp fecha = formatearFecha( cursor.getString( 3 ) );
                    Persona empleado = personaDAO.retrieve( cursor.getString( 4 ) );
                    Local local = localDAO.retrieve( cursor.getString( 5 ) );
                    Marca marca = marcaDAO.retrieve( cursor.getString( 6 ) );

                    prestamo = new Prestamo( key, descripcion, talla, fecha, empleado, local, marca );
                    listaPrestamos.add(prestamo);
                } while (cursor.moveToNext());
            }
            catch (Exception e){

            }
        }

        return listaPrestamos;
    }

    @Override
    public Estado delete(Prestamo prestamo) {
        try{
            db.delete( TABLE_NAME, KEY_CODIGO + "=" + prestamo.getCodigo(), null );
        }
        catch( Exception e ){
            return Estado.ERROR_ELIMINAR;
        } // end catch

        return Estado.ELIMINADO;
    }

    private Timestamp formatearFecha( String fecha ){
        int anio = Integer.parseInt(fecha.substring( 0, 4 ) );
        int mes = Integer.parseInt( fecha.substring( 5, 7 ) );
        int dia = Integer.parseInt( fecha.substring( 8, 10 ) );
        int hora = Integer.parseInt( fecha.substring( 11, 13 ) );
        int minutos = Integer.parseInt( fecha.substring( 14, 16 ) );
        int segundos =Integer.parseInt(fecha.substring( 17, 19 ) );

        return new Timestamp( anio, mes, dia, hora, minutos, segundos, 0 );
    } // end method formatearFecha
} // end class PrestamoDAO