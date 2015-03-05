package com.paolosport.appa;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.entities.Persona;

import java.util.ArrayList;


public class ActivityPersona extends ActionBarActivity {

    EditText txtCedulaPersona;
    EditText txtNombrePersona;
    EditText txtTelefonoPersona;
    EditText txtFotoPersona;

    TextView txtListaPersonas;

    AdminSQLiteOpenHelper helper;
    PersonaDAO personaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_persona);

        txtCedulaPersona = (EditText) findViewById( R.id.txtCedulaPersona );
        txtNombrePersona = (EditText) findViewById( R.id.txtNombrePersona );
        txtTelefonoPersona = (EditText) findViewById( R.id.txtTelefonoPersona );
        txtFotoPersona = (EditText) findViewById( R.id.txtFotoPersona );
        txtListaPersonas = (TextView) findViewById( R.id.txtListaPersonas );

        helper = new AdminSQLiteOpenHelper( this );
        personaDAO = new PersonaDAO( this, helper );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_persona, menu);
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

    public void guardarPersona( View view ){
        String id = txtCedulaPersona.getText().toString();
        String nombre = txtNombrePersona.getText().toString();
        String telefono = txtTelefonoPersona.getText().toString();
        String foto = txtFotoPersona.getText().toString();

        Persona persona = new Persona( id,nombre,telefono,foto );

        personaDAO.open();
        personaDAO.create( persona );
        personaDAO.close();

    } // end method guardarLocal

    public void mostrarEntradasPersona( View view ){

        ArrayList<Persona> listaPersonas;
        personaDAO.open();
        listaPersonas = personaDAO.retrieveAll();
        personaDAO.close();

        StringBuilder sb = new StringBuilder();

        if(listaPersonas!=null  && !listaPersonas.isEmpty()){
            for( Persona persona: listaPersonas ){
                sb.append( "CC: " ).append(persona.getCedula()).append( " Nombre: " ).append(persona.getNombre() ).append( " Telefono: " ).append(persona.getTelefono() ).append( " Foto: " ).append(persona.getUrl() ).append("\n\n");
            }
        }
        else{
            sb.append("Ya no hay registros");
        }

        txtListaPersonas.setText( sb.toString() );
    } // end method mostrarEntradas

    public void buscarPersona( View view ){

        String id = txtCedulaPersona.getText().toString();
        Persona persona;
        personaDAO.open();
        try {
            persona = personaDAO.retrieve(id);
            personaDAO.close();
            txtListaPersonas.setText("");
            txtListaPersonas.setText(persona.getNombre()+ " "+ persona.getTelefono()+ " " +persona.getUrl());
        }
        catch (Exception e){}
    } // end method buscar

    public void actualizarPersona( View view ){

        String id = txtCedulaPersona.getText().toString();
        String nombre = txtNombrePersona.getText().toString();
        String telefono = txtTelefonoPersona.getText().toString();
        String foto = txtFotoPersona.getText().toString();

        Persona persona = new Persona(id,nombre,telefono,foto);
        personaDAO.open();
        try {
            personaDAO.update(persona);
            personaDAO.close();
            txtListaPersonas.setText("");
        }
        catch (Exception e){}
    } // end method buscar

    public void eliminarPersona( View view ){

        String id = txtCedulaPersona.getText().toString();
        Persona persona;
        personaDAO.open();
        try {
            persona = personaDAO.retrieve(id);
            personaDAO.delete(persona);
            personaDAO.close();
            txtListaPersonas.setText( "" );
            mostrarEntradasPersona(view);
        }
        catch (Exception e){}

    } // end method eliminar
}
