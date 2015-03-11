package com.paolosport.appa.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.dao.PrestamoDAO;
import com.paolosport.appa.persistencia.entities.Prestamo;
import com.paolosport.appa.ui.PrestamoAdapter;

import java.util.ArrayList;

public class ActivityList extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getApplicationContext());
        MarcaDAO marcaDAO = new MarcaDAO( getApplicationContext(), adminSQLiteOpenHelper );
        LocalDAO localDAO = new LocalDAO( getApplicationContext(), adminSQLiteOpenHelper );
        PersonaDAO personaDAO = new PersonaDAO( getApplicationContext(), adminSQLiteOpenHelper );
        PrestamoDAO prestamoDAO= new PrestamoDAO( getApplicationContext(), adminSQLiteOpenHelper );
        prestamoDAO.setPersonaDAO(personaDAO);
        prestamoDAO.setMarcaDAO(marcaDAO);
        prestamoDAO.setLocalDAO(localDAO);

        prestamoDAO.open();
        ArrayList<Prestamo> prestamos = prestamoDAO.retrieveAll();
        prestamoDAO.close();

        PrestamoAdapter adapter = new PrestamoAdapter( this, R.layout.prestamo_item, prestamos );

        if ( prestamos == null )
            Toast.makeText( this, "no hay datos", Toast.LENGTH_SHORT );

        ListView lstPrestamos = (ListView) findViewById( R.id.lstPrestamos );
        lstPrestamos.setAdapter( adapter );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
