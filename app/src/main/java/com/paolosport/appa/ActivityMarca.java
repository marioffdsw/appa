package com.paolosport.appa;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.entities.Marca;

import java.sql.Date;
import java.util.ArrayList;


public class ActivityMarca extends ActionBarActivity {

    EditText txtIDMarca;
    EditText txtNombreMarca;
    EditText txtURL;
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
        txtFecha = (TextView) findViewById( R.id.txtFecha );

        helper = new AdminSQLiteOpenHelper( this );
        marcaDAO = new MarcaDAO( this, helper );
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
