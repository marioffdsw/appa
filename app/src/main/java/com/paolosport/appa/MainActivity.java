package com.paolosport.appa;

import android.content.Intent;
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

import java.sql.Timestamp;
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

    public void local(View view) {
        Intent i = new Intent(this, ActivityLocal.class );
        startActivity(i);
    }

    public void marca(View view) {
        Intent i = new Intent(this, ActivityMarca.class );
        startActivity(i);
    }

    public void persona(View view) {
        Intent i = new Intent(this, ActivityPersona.class );
        startActivity(i);
    }


    public void mostrarFecha( View view ){
        java.util.Date dateUtil = new java.util.Date();
        Timestamp date = new Timestamp( dateUtil.getTime() );

        txtFecha.setText( date.toString() );
    } // end method mostrarFecha

    public void prestamos ( View view ){
        Intent i = new Intent(this, ActivityPrestamos.class );
        startActivity(i);
    }

}
