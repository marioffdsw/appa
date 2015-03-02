package com.paolosport.appa.persistencia.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;

import java.util.ArrayList;

abstract class BaseDAO < T > {

    static protected String TABLE_NAME;


    static final private String TAG = "BaseDao";
    static public enum Estado { ERROR_INSERTAR, INSERTADO, ERROR_ACTUALIZAR, ACTUALIZADO };

    final Context context;
    AdminSQLiteOpenHelper dbHelper;
    SQLiteDatabase db;

    public BaseDAO(Context context, AdminSQLiteOpenHelper helper) {
        this.context = context;
        dbHelper = helper;
    } // end constructor

    public BaseDAO open(){
        Log.w( TAG, "abriendo la base de datos" );
        db = dbHelper.getWritableDatabase();
        return this;
    } // end method open

    public void close(){
        Log.w( TAG, "cerrando la base de datos" );
        dbHelper.close();
    } // end method close

    abstract Estado create( T element );
    abstract Estado update( T element );
    abstract T retrieve( String key );
    abstract ArrayList<T> retrieveAll();
    abstract String delete( T element );

} // end class BaseDAO