package com.paolosport.appa.activities;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.spinnerMarcaPaquete.SpinnerMarca;
import com.paolosport.appa.spinnerMarcaPaquete.SpinnerAdapterMarca;

import java.sql.Date;
import java.util.ArrayList;

public class ActivityMarca extends ActionBarActivity {

    EditText txtIDMarca;
    EditText txtNombreMarca;
    EditText txtURL;
    private android.widget.Spinner spinner;
    TextView txtFecha;

    TextView txtListaMarcas;

    AdminSQLiteOpenHelper helper;
    MarcaDAO marcaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_marca);

        txtIDMarca = (EditText) findViewById( R.id.txtIDMarca );
        txtNombreMarca = (EditText) findViewById( R.id.txtNombreMarca );
        txtURL = (EditText) findViewById( R.id.txtURL );
        txtListaMarcas = (TextView) findViewById( R.id.txtListaMarcas );

        helper = new AdminSQLiteOpenHelper( this );
        marcaDAO = new MarcaDAO( this, helper );

        try{
        marcaDAO.open();
        DatosPorDefecto();
        marcaDAO.close();
        }
        catch (Exception e){e.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_marca, menu);
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
        marcaDAO.open();

        ArrayList<Marca> listaMarcasSpinner = marcaDAO.retrieveAll();
        ArrayList<SpinnerMarca> items = new ArrayList<SpinnerMarca>();

        Resources r = this.getResources();

        for(Marca marca:listaMarcasSpinner) {
            items.add(new SpinnerMarca(marca.getNombre(),r.getIdentifier(marca.getUrl(),"drawable",getPackageName())));
            //muestra nombre de la marca y el nombre del url de la marca q obligatoriamente debe coincidir con el nombre del recurso
        }
        marcaDAO.close();

        spinner = (android.widget.Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new SpinnerAdapterMarca(this,items));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(adapterView.getContext(), ((SpinnerMarca) adapterView.getItemAtPosition(position)).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //nothing
            }
        });
    }

    public void guardarMarca( View view ){
        String id = txtIDMarca.getText().toString();
        String nombre = txtNombreMarca.getText().toString();
        String url = txtURL.getText().toString();

        Marca marca = new Marca( id, nombre,url );

        marcaDAO.open();
        marcaDAO.create( marca );
        marcaDAO.close();

    } // end method guardar

    public void mostrarEntradasMarca( View view ){

        ArrayList<Marca> listaMarcas;
        marcaDAO.open();
        listaMarcas = marcaDAO.retrieveAll();
        marcaDAO.close();

        StringBuilder sb = new StringBuilder();

        if(listaMarcas!=null ){
            for( Marca marca: listaMarcas ){
                sb.append( "ID: " ).append(marca.getId()).append( " Nombre: " ).append(marca.getNombre()).append( " Url: " ).append(marca.getUrl()).append("\n\n");
            }
        }
        else{
            sb.append("Ya no hay registros");
        }

        txtListaMarcas.setText( sb.toString() );
    } // end method mostrarEntradas

    public void buscarMarca( View view ){

        String id = txtIDMarca.getText().toString();
        Marca marca;
        marcaDAO.open();
        try {
            marcaDAO.open();
            marca = marcaDAO.retrieve(id);
            marcaDAO.close();
            txtListaMarcas.setText(marca.getNombre());
        }
        catch (Exception e){}
    } // end method buscar

    public void actualizarMarca( View view ){

        String id = txtIDMarca.getText().toString();
        String nombre = txtNombreMarca.getText().toString();
        String url = txtURL.getText().toString();


        Marca marca = new Marca(id,nombre,url);
        marcaDAO.open();
        try {
            marcaDAO.update(marca);
            marcaDAO.close();
            txtListaMarcas.setText("");
        }
        catch (Exception e){}
    } // end method buscar

    public void eliminarMarca( View view ){

        String id = txtIDMarca.getText().toString();
        Marca marca;
        marcaDAO.open();
        try {
            marca = marcaDAO.retrieve(id);
            marcaDAO.delete(marca);
            marcaDAO.close();
            txtListaMarcas.setText( "" );
            mostrarEntradasMarca(view);
        }
        catch (Exception e){}

    } // end method eliminar

}
