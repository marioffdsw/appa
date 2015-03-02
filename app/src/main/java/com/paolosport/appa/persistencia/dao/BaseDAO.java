package com.paolosport.appa.persistencia.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.entities.Marca;

import java.util.ArrayList;

abstract class BaseDAO < T > {

    private String TAG = "BaseDao";
    public static enum Estado { ERROR_INSERTAR, INSERTADO };

    final Context context;
    AdminSQLiteOpenHelper dbHelper;
    SQLiteDatabase db;

    public BaseDAO(Context context) {
        this.context = context;
        dbHelper = new AdminSQLiteOpenHelper( context );
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
    abstract String update( T element );
    abstract T retrieve();
    abstract ArrayList<T> retrieveAll();
    abstract String delete( T element );

} // end class BaseDAO