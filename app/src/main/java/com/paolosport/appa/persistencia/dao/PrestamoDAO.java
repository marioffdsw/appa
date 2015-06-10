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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class PrestamoDAO extends BaseDAO<Prestamo>{

    static final String TABLE_NAME = "prestamos";
    static final String KEY_ID = "id";
    static final String KEY_CODIGO = "codigo";
    static final String KEY_FOTO = "foto";
    static final String KEY_DESCRIPCION = "descripcion";
    static final String KEY_TALLA = "talla";
    static final String KEY_FECHA = "fecha";
    static final String KEY_EMPLEADO = "empleado";
    static final String KEY_LOCAL = "local";
    static final String KEY_MARCA = "marca";
    static final String KEY_ORIGEN = "origen";
    static final String KEY_ESTADO = "estado";


    private LocalDAO localDAO;
    private MarcaDAO marcaDAO;
    private PersonaDAO personaDAO;
    AdminSQLiteOpenHelper helper;

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

            Log.i(TAG + " prestamo", "PrestamoDAO.create()");

            ContentValues initialValues = new ContentValues();

            initialValues.put( KEY_CODIGO, prestamo.getCodigo() );
            initialValues.put( KEY_DESCRIPCION, prestamo.getDescripcion() );
            initialValues.put( KEY_FOTO, prestamo.getFoto() );
            initialValues.put(KEY_TALLA, prestamo.getTalla());

            Calendar calendar = prestamo.getFecha();
            // 2015-06-10 03:59:35
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            // int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
//            int month = calendar.get(Calendar.MONTH);
//            int year = calendar.get( Calendar.YEAR );
//            int second = calendar.get( Calendar.SECOND );
//            String hora = new SimpleDateFormat( "K:mm" ).format(calendar.getTime());
//            String fecha = year + "-" + (month < 10 ? "0" + month : month ) + "-" + (day < 10 ? "0" + day : day) + " " + hora + ":" + (second < 10 ? "0" + second : second );
//
//            initialValues.put( KEY_FECHA, fecha );
            initialValues.put( KEY_EMPLEADO, prestamo.getEmpleado().getCedula() );
            initialValues.put( KEY_LOCAL, prestamo.getLocal().getId() );
            initialValues.put( KEY_MARCA, prestamo.getMarca().getId() );
            initialValues.put( KEY_ORIGEN, prestamo.getOrigen() );
            initialValues.put( KEY_ESTADO, prestamo.getEstado() );

            long i = db.insert( TABLE_NAME, null, initialValues );
            if ( i == -1 )
                throw new Exception();
        }
        catch( Exception e ){

            Log.e( TAG + "prestamos", "Error insertar" );
            return Estado.ERROR_INSERTAR;
        } // end catch

        Log.i( TAG + "prestamos", "Insercion correcta PrestamoDAO.create()" );
        return Estado.INSERTADO;
    }

    @Override
    public Estado update(Prestamo prestamo) {
        try{
            ContentValues updateValues = new ContentValues();

            updateValues.put( KEY_CODIGO, prestamo.getCodigo() );
            updateValues.put( KEY_DESCRIPCION, prestamo.getDescripcion() );
            updateValues.put( KEY_FOTO, prestamo.getFoto() );
            updateValues.put( KEY_TALLA, prestamo.getTalla() );
            //updateValues.put( KEY_FECHA, prestamo.getFecha().toString() );
            updateValues.put( KEY_EMPLEADO, prestamo.getEmpleado().getCedula() );
            updateValues.put( KEY_LOCAL, prestamo.getLocal().getId() );
            updateValues.put( KEY_MARCA, prestamo.getMarca().getId() );
            updateValues.put( KEY_ORIGEN, prestamo.getOrigen() );
            updateValues.put( KEY_ESTADO, prestamo.getEstado() );

            db.update(TABLE_NAME, updateValues, KEY_ID + "=" + prestamo.getId(), null);
        }
        catch( Exception e ){
            return Estado.ERROR_ACTUALIZAR;
        } // end catch

        return Estado.ACTUALIZADO;
    }

    // asume que se se ha establecido los otros daos
    @Override
    public Prestamo retrieve(String id) {
        Cursor cursor= null;
        try{
            cursor = db.query(TABLE_NAME,            // FROM
                    new String[]{
                            KEY_ID,
                            KEY_CODIGO,
                            KEY_DESCRIPCION,
                            KEY_FOTO,
                            KEY_TALLA,
                            KEY_FECHA,
                            KEY_EMPLEADO,
                            KEY_LOCAL,
                            KEY_MARCA,
                            KEY_ORIGEN,
                            KEY_ESTADO
                    },       // SELECT
                    KEY_ID + "=" + id, null,           // WHERE
                    null,                                   // GROUP BY
                    null,                                   // HAVING
                    null,                                   // ORDER BY
                    null                                    // LIMIT
            );
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i(TAG, "NO HAY REGISTROS");
        }

        Prestamo prestamo = null;

        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            id = cursor.getString(0);
            String codigo = cursor.getString(1);
            String descripcion = cursor.getString(2);
            String foto = cursor.getString(3);
            String talla = cursor.getString(4);
            Calendar fecha = formatearFecha( cursor.getString( 5 ) );
            personaDAO.open();
            Persona empleado = personaDAO.retrieve( cursor.getString( 6 ) );
            personaDAO.close();
            localDAO.open();
            Local local = localDAO.retrieve( cursor.getString( 7 ) );
            localDAO.close();
            marcaDAO.open();
            Marca marca = marcaDAO.retrieve( cursor.getString( 8 ) );
            marcaDAO.close();
            String origen = cursor.getString(9);
            String estado = cursor.getString( 10 );

            prestamo = new Prestamo( id,codigo, descripcion, foto, talla, fecha, empleado, local, marca, origen, estado );
        }

        return prestamo;
    }

    // asume que se ha asignado los otros daos
    @Override
    public ArrayList<Prestamo> retrieveAll() {
        Cursor cursor = db.query(TABLE_NAME,              // FROM
                new String[]{
                        KEY_ID,
                        KEY_CODIGO,
                        KEY_DESCRIPCION,
                        KEY_FOTO,
                        KEY_TALLA,
                        KEY_FECHA,
                        KEY_EMPLEADO,
                        KEY_LOCAL,
                        KEY_MARCA,
                        KEY_ORIGEN,
                        KEY_ESTADO
                },       // SELECT
                null, null,           // WHERE
                null,                                   // GROUP BY
                null,                                   // HAVING
                null,                                   // ORDER BY
                null                                    // LIMIT
        );

        Prestamo prestamo = null;

        helper     = new AdminSQLiteOpenHelper( context );
        localDAO   = new LocalDAO( context, helper );
        marcaDAO   = new MarcaDAO( context, helper );
        personaDAO = new PersonaDAO( context, helper );

        ArrayList listaPrestamos = new ArrayList<Prestamo>();

        cursor.moveToFirst();

        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            // itera por todas las filas de la tabla y crea los objetos
            try {
                do {

                    String id= cursor.getString(0);
                    String codigo = cursor.getString(1);
                    String descripcion = cursor.getString(2);
                    String foto = cursor.getString(3);
                    String talla = cursor.getString(4);
                    Calendar fecha = formatearFecha( cursor.getString( 5 ) );

                    personaDAO.open();
                    Persona empleado = personaDAO.retrieve( cursor.getString( 6) );
                    personaDAO.close();

                    localDAO.open();
                    Local local = localDAO.retrieve( cursor.getString( 7 ) );
                    localDAO.close();

                    marcaDAO.open();
                    Marca marca = marcaDAO.retrieve( cursor.getString( 8 ) );
                    marcaDAO.close();

                    String origen = cursor.getString(9);
                    String estado = cursor.getString(10);

                    prestamo = new Prestamo( id,codigo, descripcion, foto, talla, fecha, empleado, local, marca, origen, estado );
                    listaPrestamos.add(prestamo);
                } while (cursor.moveToNext());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return listaPrestamos;
    }

    @Override
    public Estado delete(Prestamo prestamo) {
        try{
            db.delete( TABLE_NAME, KEY_ID + "=" + prestamo.getId(), null );
        }
        catch( Exception e ){
            e.printStackTrace();
            return Estado.ERROR_ELIMINAR;
        } // end catch

        return Estado.ELIMINADO;
    }



    private Calendar formatearFecha( String fecha ){
        Log.e( "fecha", fecha );
        int anio = Integer.parseInt(fecha.substring( 0, 4 ) );
        int mes = Integer.parseInt( fecha.substring( 5, 7 ) );
        int dia = Integer.parseInt( fecha.substring( 8, 10 ) );
        int hora = Integer.parseInt( fecha.substring( 11, 13 ) );
        int minutos = Integer.parseInt( fecha.substring( 14, 16 ) );
        int segundos =Integer.parseInt(fecha.substring( 17, 19 ) );
        Calendar calendar = new GregorianCalendar();
        calendar.set(anio, mes, dia, hora, minutos, segundos);

        return calendar;
    } // end method formatearFecha

    public Estado removeAll(){
        Cursor cursor=null;
        try
        {
            cursor = db.query(TABLE_NAME,            // FROM
                    new String[]{KEY_ID},         // SELECT
                    null, null,         // WHERE
                    null,                                   // GROUP BY
                    null,                                   // HAVING
                    null,                                   // ORDER BY
                    null                                    // LIMIT
            );
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i(TAG, "NO HAY REGISTROS");
        }
        cursor.moveToFirst();
        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            // itera por todas las filas de la tabla y crea los objetos
            try {
                do {
                    db.delete(TABLE_NAME, KEY_ID + "=" + cursor.getString(0), null );
                } while (cursor.moveToNext());
            }
            catch (Exception e){
                e.printStackTrace();
                return Estado.ERROR_ELIMINAR;
            }
        }
        return Estado.ELIMINADO;
    }


    public ArrayList<String> nombreLocales() {

        Cursor cursor= null;
        try{
            cursor = db.query(TABLE_NAME,            // FROM
                    new String[]{KEY_LOCAL},         // SELECT
                    null, null,         // WHERE
                    null,                                   // GROUP BY
                    null,                                   // HAVING
                    null,                                   // ORDER BY
                    null                                    // LIMIT
            );
        }

        catch (Exception e){
            e.printStackTrace();
            Log.i(TAG, "NO HAY REGISTROS");
        }

        helper     = new AdminSQLiteOpenHelper( context );
        localDAO   = new LocalDAO( context, helper );

        ArrayList<String> locales = new ArrayList<String>();

        cursor.moveToFirst();

        if ( cursor != null ){ // ha encontrado el local con la id entregada
            cursor.moveToFirst();

            // itera por todas las filas de la tabla y crea los objetos
            try {
                do {
                    localDAO.open();
                    Local local = localDAO.retrieve( cursor.getString( 0 ) );
                    localDAO.close();
                    locales.add(local.getNombre().toString());
                } while (cursor.moveToNext());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return locales;
    }//end method buscar nombreLocales

} // end class FPrestamoDAO