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

    static final String TEST_PERSONA = "INSERT INTO persona " +
            "( cedula,      nombre,             telefono,       foto ) VALUES " +
            "( '1',           'Homero Simpson',     '5555555551',     'homero.jpg'  )," +
            "( '2',           'Marge Simpson',      '5555555552',     'marge.jpg'   )," +
            "( '3',           'Bart Simpson',       '5555555553',     'bart.jpg'    )," +
            "( '4',           'Lissa Simpson',      '5555555554',     'lissa.jpg'  )," +
            "( '5',           'Maggie Simpson',     '5555555555',     'maggie.jpg'  )";

    static final String CREATE_TABLE_LOCAL = "CREATE TABLE local(" +
            "id             TEXT        PRIMARY KEY," +
            "nombre         TEXT        NOT NULL" +
            ")";

    static final String INSERT_TABLE_LOCAL = "INSERT INTO local (id, nombre) VALUES "+
            "('1','CC. 16'),"+
            "('2','CC. PONTEVEDRA' ),"+
            "('3','CC. ANDES L.102'),"+
            "('4','CC. UNICENTRO'),"+
            "('5','CC. ANDES L.123');";

    static final String DROP_TABLE_LOCAL = "DROP TABLE local IF EXIST";

    static final String CREATE_TABLE_MARCA = "CREATE TABLE marca(" +
            "id             TEXT        PRIMARY KEY," +
            "nombre         TEXT        NOT NULL," +
            "logo           TEXT " + // URI del bitmap del recurso
            ")";


    static final String INSERT_TABLE_MARCA = "INSERT INTO marca (id, nombre, logo) VALUES "+
                                             "('1','adidas','adidas'),"+
                                             "('2','puma','puma' ),"+
                                             "('3','lecoq','lecoq'),"+
                                             "('4','Lacoste','lacoste' ),"+
                                             "('5','Nike','nike'),"+
                                             "('6','New Balance','nb'),"+
                                             "('7','Converse','converse'),"+
                                             "('8','Cat','cat'),"+
                                             "('9','Merrell','merrell'),"+
                                             "('10','HH','hh'),"+
                                             "('11','Timberland','timberland');";


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

    static final String TEST_PRESTAMOS = "INSERT INTO prestamos " +
            "( codigo,      descripcion,              talla,      empleado,     local,    marca   ) VALUES" +
            "( '1',         'blancos',                32,         '1',          '1',      '1'     )," +
            "( '2',         'negros',                 38,         '2',          '1',      '2'     )," +
            "( '3',         'azules',                 37,         '3',          '2',      '4'     )," +
            "( '4',         'naranjas',               27,         '4',          '5',      '3'     )," +
            "( '5',         'rojos',                  34,         '5',          '3',      '5'     )," +
            "( '6',         'manzana',                32,         '2',          '4',      '1'     )," +
            "( '7',         'banana',                 28,         '4',          '2',      '5'     )," +
            "( '8',         'pera',                   24,         '3',          '4',      '5'     )," +
            "( '9',         'sandia',                 38,         '1',          '5',      '1'     )," +
            "( '10',        'uva',                    22,         '2',          '2',      '3'     )," +
            "( '11',        'piña',                   39,         '5',          '3',      '2'     )," +
            "( '12',        'mandarina',              40,         '2',          '2',      '2'     )," +
            "( '13',        'limon',                  36,         '3',          '3',      '3'     )," +
            "( '14',        'fresa',                  38,         '4',          '4',      '4'     )";

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

        setupTestDataBase( db );

        // re-create tables
        onCreate( db );
    }

    public void setupTestDataBase( SQLiteDatabase db ) {
        db.execSQL( TEST_PERSONA );
        db.execSQL( INSERT_TABLE_LOCAL );
        db.execSQL( INSERT_TABLE_MARCA );
        db.execSQL( TEST_PRESTAMOS );
    } // end method setupTestDatabase
}