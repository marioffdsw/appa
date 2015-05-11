package com.paolosport.appa.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    /** DATA BASE HELPER PARAMETERS */
    static final String DATABASE_NAME = "appa.db";
    static final int DATABASE_VERSION = 11;


    /** DATABASE TABLE DEFINITION AND DROP STATEMENTS */
    static final String CREATE_TABlE_PERSONA = "CREATE TABLE persona (" +
            "cedula         TEXT         PRIMARY KEY," +
            "nombre         TEXT            NOT NULL," +
            "telefono       TEXT," +
            "foto           TEXT" + // uri de la foto, en los recursos
            ")";

    static final String DROP_TABLE_PERSONA = "DROP TABLE IF EXISTS persona";

    static final String TEST_PERSONA = "INSERT INTO persona " +
            "(  cedula,      nombre,               telefono,         foto ) VALUES " +
            "( '1',         'Homero Simpson',     '5555555551',     'prueba'  )," +
            "( '2',         'Marge Simpson',      '5555555552',     'prueba'   )," +
            "( '3',         'Bart Simpson',       '5555555553',     'prueba'    )," +
            "( '4',         'Lissa Simpson',      '5555555554',     'prueba'  )," +
            "( '5',         'Maggie Simpson',     '5555555555',     'prueba'  )";

    static final String CREATE_TABLE_LOCAL = "CREATE TABLE local(" +
            "id             INTEGER       PRIMARY KEY," +
            "nombre         TEXT        NOT NULL" +
            ")";

    static final String INSERT_TABLE_LOCAL = "INSERT INTO local (nombre) VALUES "+
            "('CC. 16'),"+
            "('CC. PONTEVEDRA' ),"+
            "('CC. ANDES L.102'),"+
            "('CC. UNICENTRO'),"+
            "('CC. ANDES L.123');";

    static final String DROP_TABLE_LOCAL = "DROP TABLE IF EXISTS local";

    static final String CREATE_TABLE_MARCA = "CREATE TABLE marca(" +
            "id             INTEGER       PRIMARY KEY AUTOINCREMENT," +
            "nombre         TEXT        NOT NULL," +
            "logo           TEXT " + // URI del bitmap del recurso
            ")";


    static final String INSERT_TABLE_MARCA = "INSERT INTO marca ( nombre, logo) VALUES "+
                                             "('Adidas','adidas'),"+
                                             "('Reebok','reebok'),"+
                                             "('Puma','puma' ),"+
                                             "('Le coq Sportif','lecoq'),"+
                                             "('Lacoste','lacoste' ),"+
                                             "('Nike','nike'),"+
                                             "('New Balance','nb'),"+
                                             "('Converse','converse'),"+
                                             "('Cat','cat'),"+
                                             "('Merrell','merrell'),"+
                                             "('HH','hh'),"+
                                             "('Dolomite','dolomite'),"+
                                             "('Swiss Brand','swiss');";


    static final String DROP_TABLE_MARCA = "DROP TABLE IF EXISTS marca";

    static final String CREATE_TABLE_PRESTAMOS = "CREATE TABLE prestamos(" +
            "id             INTEGER        PRIMARY KEY      AUTOINCREMENT," +
            "codigo         TEXT," +
            "descripcion    TEXT," +
            "foto           TEXT," +    //uri de la foto
            "talla          INTEGER     NOT NULL," +
            "fecha          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL," +
            "empleado       TEXT        NOT NULL," +
            "local          TEXT        NOT NULL," +
            "marca          TEXT        NOT NULL," +
            "origen         TEXT        NOT NULL," +
            "FOREIGN KEY ( empleado ) REFERENCES persona( cedula )," +
            "FOREIGN KEY ( local) REFERENCES local( id )" +
            "FOREIGN KEY ( marca ) REFERENCES marca( id )" +
            ")";

    static final String DROP_TABLE_PRESTAMOS = "DROP TABLE IF EXISTS prestamos";

    static final String TEST_PRESTAMOS = "INSERT INTO prestamos " +
            "( codigo,  descripcion,    foto,   talla,   fecha, empleado,   local,  marca,  origen   ) VALUES" +
            "( 'a1',     've1',         'v',    'bl',    '32',  '1',        '1',    '1',    'h'       )," +
            "( 'b2',     've2',         'v',    'bl',    '32',  '1',        '1',    '1',    'h'       )," +
            "( 'c3',     've3',         'v',    'bl',    '32',  '1',        '1',    '1',    'h'       )," +
            "( 'd4',     've4',         'v',    'bl',    '32',  '1',        '1',    '1',    'h'       )," +
            "( 'e5',     've5',         'v',    'bl',    '32',  '1',        '1',    '1',    'h'       );";


    // constructor, just call superclass constructor
    public AdminSQLiteOpenHelper( Context context ){
        super( context, DATABASE_NAME, null, DATABASE_VERSION  );
    } // end constructor

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating database
        db.execSQL( CREATE_TABlE_PERSONA );
        db.execSQL( CREATE_TABLE_LOCAL );
        db.execSQL( CREATE_TABLE_MARCA );
        db.execSQL( CREATE_TABLE_PRESTAMOS );

        setupTestDataBase( db );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {

        // drop every database table
        db.execSQL( DROP_TABLE_PRESTAMOS );
        db.execSQL( DROP_TABLE_LOCAL );
        db.execSQL( DROP_TABLE_MARCA );
        db.execSQL( DROP_TABLE_PERSONA );

        // re-create tables
        onCreate( db );
    }

    public void setupTestDataBase( SQLiteDatabase db ) {
        db.execSQL( TEST_PERSONA );
        db.execSQL( INSERT_TABLE_LOCAL );
        db.execSQL( INSERT_TABLE_MARCA );
        //db.execSQL( TEST_PRESTAMOS );
    } // end method setupTestDatabase
}