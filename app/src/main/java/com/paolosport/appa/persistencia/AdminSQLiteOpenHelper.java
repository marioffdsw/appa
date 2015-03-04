package com.paolosport.appa.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    /** DATA BASE HELPER PARAMETERS */
    static final String DATABASE_NAME = "appa.db";
    static final int DATABASE_VERSION = 1;


    /** DATABASE TABLE DEFINITION AND DROP STATEMENTS */
    static final String CREATE_TABlE_PERSONA = "CREATE TABLE persona (" +
            "cedula         TEXT         PRIMARY KEY," +
            "nombre         TEXT            NOT NULL," +
            "telefono       TEXT," +
            "foto           TEXT" + // uri de la foto, en los recursos
            ")";

    static final String DROP_TABLE_PERSONA = "DROP TABLE persona IF EXIST";

    static final String CREATE_TABLE_LOCAL = "CREATE TABLE local(" +
            "id             TEXT        PRIMARY KEY," +
            "nombre         TEXT        NOT NULL" +
            ")";

    static final String DROP_TABLE_LOCAL = "DROP TABLE local IF EXIST";

    static final String CREATE_TABLE_MARCA = "CREATE TABLE marca(" +
            "id             TEXT        PRIMARY KEY," +
            "nombre         TEXT        NOT NULL," +
            "logo           TEXT " + // URI del bitmap del recurso
            ")";

    static final String DROP_TABLE_MARCA = "DROP TABLE marca IF EXIST";

    static final String CREATE_TABLE_PRESTAMOS = "CREATE TABLE prestamos(" +
            "codigo         TEXT        PRIMARY KEY," +
            "descripcion    TEXT," +
            "talla          INTEGER     NOT NULL," +
            "fecha          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL," +
            "empleado       TEXT        NOT NULL," +
            "local          TEXT        NOT NULL," +
            "marca          TEXT        NOT NULL," +
            "FOREIGN KEY ( empleado ) REFERENCES persona( cedula )," +
            "FOREIGN KEY ( local) REFERENCES local( id )" +
            "FOREIGN KEY ( marca ) REFERENCES marca( id )" +
            ")";

    static final String DROP_TABLE_PRESTAMOS = "DROP TABLE prestamos IF EXIST";

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
}