package com.paolosport.appa;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.entities.Local;

import java.util.ArrayList;


public class ActivityLocal extends ActionBarActivity {

    EditText txtIDLocal;
    EditText txtNombreLocal;
    TextView txtListaLocales;

    AdminSQLiteOpenHelper helper;
    LocalDAO localDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_local);

        txtIDLocal = (EditText) findViewById( R.id.txtIDLocal );
        txtNombreLocal = (EditText) findViewById( R.id.txtNombreLocal );
        txtListaLocales = (TextView) findViewById( R.id.txtListaLocales );

        helper = new AdminSQLiteOpenHelper( this );
        localDAO = new LocalDAO( this, helper );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_local, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardar( View view ){
        String id = txtIDLocal.getText().toString();
        String nombre = txtNombreLocal.getText().toString();

        Local local = new Local( id, nombre );

        localDAO.open();
        localDAO.create( local );
        localDAO.close();

    } // end method guardarLocal

    public void mostrarEntradas( View view ){

        ArrayList<Local> listaLocales;
        localDAO.open();
        listaLocales = localDAO.retrieveAll();
        localDAO.close();

        StringBuilder sb = new StringBuilder();

        if(listaLocales!=null  && !listaLocales.isEmpty()){
            for( Local local: listaLocales ){
                sb.append( "ID: " ).append(local.getId()).append( " Nombre: " ).append(local.getNombre() ).append( "\n\n" );
            }
        }
        else{
            sb.append("Ya no hay registros");
        }

        txtListaLocales.setText( sb.toString() );
    } // end method mostrarEntradas

    public void buscar( View view ){

        String id = txtIDLocal.getText().toString();
        Local local;
        localDAO.open();
        try {
            local = localDAO.retrieve(id);
            localDAO.close();
            txtListaLocales.setText("");
            txtListaLocales.setText(local.getNombre());
        }
        catch (Exception e){}
    } // end method buscar

    public void actualizar( View view ){

        String id = txtIDLocal.getText().toString();
        String nombre = txtNombreLocal.getText().toString();


        Local local = new Local(id,nombre);
        localDAO.open();
        try {
            localDAO.update(local);
            localDAO.close();
            txtListaLocales.setText("");
        }
        catch (Exception e){}
    } // end method buscar

    public void eliminar( View view ){

        String id = txtIDLocal.getText().toString();
        Local local;
        localDAO.open();
        try {
            local = localDAO.retrieve(id);
            localDAO.delete(local);
            localDAO.close();
            txtListaLocales.setText( "" );
            mostrarEntradas(view);
        }
        catch (Exception e){}

    } // end method eliminar
}
