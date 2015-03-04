package com.paolosport.appa;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.entities.Local;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    EditText txtIDLocal;
    EditText txtNombreLocal;
    TextView txtListaLocales;
    TextView txtFecha;

    AdminSQLiteOpenHelper helper;
    LocalDAO localDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtIDLocal = (EditText) findViewById( R.id.txtIDLocal );
        txtNombreLocal = (EditText) findViewById( R.id.txtNombreLocal );
        txtListaLocales = (TextView) findViewById( R.id.txtListaLocales );
        txtFecha = (TextView) findViewById( R.id.txtFecha );

        helper = new AdminSQLiteOpenHelper( this );
        localDAO = new LocalDAO( this, helper );

        Button btnFecha = (Button) findViewById( R.id.btnFecha );
        btnFecha.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date( System.currentTimeMillis() );

            double anio = date.getSeconds();

                txtFecha.setText( String.valueOf( anio ) );
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void guardarLocal( View view ){
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
        for( Local local: listaLocales ){
            sb.append( "ID: " ).append(local.getId()).append( " Nombre: " ).append(local.getNombre() ).append( "\n\n" );
        }

        txtListaLocales.setText( sb.toString() );
    } // end method mostrarEntradas
}
