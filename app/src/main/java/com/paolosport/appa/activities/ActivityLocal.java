package com.paolosport.appa.activities;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.spinnerLocalPaquete.SpinnerAdapterLocal;
import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.entities.Local;

import java.util.ArrayList;


public class ActivityLocal extends ActionBarActivity {

    EditText txtIDLocal;
    EditText txtNombreLocal;
    TextView txtListaLocales;
    private android.widget.Spinner spinner;

    AdminSQLiteOpenHelper helper;
    LocalDAO localDAO;

    boolean bandera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_local);

        txtIDLocal = (EditText) findViewById( R.id.txtIDLocal );
        txtNombreLocal = (EditText) findViewById( R.id.txtNombreLocal );
        txtListaLocales = (TextView) findViewById( R.id.txtListaLocales );


        helper = new AdminSQLiteOpenHelper( this );
        localDAO = new LocalDAO( this, helper );

        try{
            localDAO.open();
            DatosPorDefecto();
            localDAO.close();
        }
        catch (Exception e){e.printStackTrace();}
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



    private void DatosPorDefecto() {
        localDAO.open();

        ArrayList<Local> listaLocales = localDAO.retrieveAll();
        int[] colores = getResources().getIntArray(R.array.arregloColor);

        Resources r = this.getResources();
        int i=0;
        /*for(Local local:listaLocales) {
            items.add(new SpinnerLocal(local.getNombre(), colores[i]));
            i++;
            //muestra nombre de la marca y el nombre del url de la marca q obligatoriamente debe coincidir con el nombre del recurso
        }*/

       // SpinnerAdapterLocal colorSpinner = new SpinnerAdapterLocal(this, items);
        //prueba.setAdapter(colorSpinner);
       // spinner.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        spinner = (android.widget.Spinner) findViewById(R.id.spinnerLocal);
        spinner.setAdapter(new SpinnerAdapterLocal(this,listaLocales));
        localDAO.close();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(adapterView.getContext(), ((Local) adapterView.getItemAtPosition(position)).getNombre(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

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
