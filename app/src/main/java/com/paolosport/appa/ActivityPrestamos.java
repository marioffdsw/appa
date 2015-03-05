package com.paolosport.appa;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.dao.PrestamoDAO;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.persistencia.entities.Persona;
import com.paolosport.appa.persistencia.entities.Prestamo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;


public class ActivityPrestamos extends ActionBarActivity {

    EditText txtCodigo;
    EditText txtTalla;
    EditText txtDescripcion;
    CheckBox radioEmpleado;
    CheckBox radioLocal;
    CheckBox radioMarca;
    TextView txtListaPrestamos;
    TextView txtCantidad;

    AdminSQLiteOpenHelper helper;
    LocalDAO localDAO;
    MarcaDAO marcaDAO;
    PersonaDAO personaDAO;
    PrestamoDAO prestamoDAO;

    private static final String TAG = "Activity Prestamos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_prestamos);

        txtCodigo = (EditText) findViewById( R.id.txtCodigo);
        txtTalla = (EditText) findViewById( R.id.txtTalla );
        txtDescripcion = (EditText) findViewById( R.id.txtDescripcion );
        radioEmpleado = (CheckBox) findViewById(R.id.radioEmpleado);
        radioMarca = (CheckBox) findViewById( R.id.radioMarca );
        radioLocal = (CheckBox) findViewById( R.id.radioLocal);
        txtListaPrestamos = (TextView) findViewById( R.id.txtListaPrestamos );
        txtCantidad = (TextView) findViewById( R.id.txtCantidad );

        helper = new AdminSQLiteOpenHelper(this);
        localDAO = new LocalDAO( this, helper );
        marcaDAO = new MarcaDAO( this, helper );
        personaDAO = new PersonaDAO( this, helper );
        prestamoDAO = new PrestamoDAO( this, helper );
        prestamoDAO.setLocalDAO( localDAO );
        prestamoDAO.setMarcaDAO( marcaDAO );
        prestamoDAO.setPersonaDAO( personaDAO );


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_prestamos, menu);
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

    public void crear( View view ){

        Persona empleado;
        Local local;
        Marca marca;


        if (radioEmpleado.isChecked()){
            empleado = new Persona( "108529072", "Mario F. Florez", "3150000000", "mario.jpg" );
        }
        else{

            empleado = new Persona( "99999990", "Homer Simpson", "555555555", "homero.jpg" );

        }

        if (radioMarca.isChecked()){

            marca = new Marca( "1", "Mike", "nike.jpg" );
        }
        else{
            marca = new Marca( "2", "Pumba", "pumba.jpg" );
        }

        if( radioLocal.isChecked() ) {
            local = new Local( "1", "Local 101" );
        }
        else {
            local = new Local( "2", "Local 204" );
        }

        personaDAO.open();
        try {
            personaDAO.create( empleado );
        }
        catch(Exception e) {
            Log.w( TAG, " error con empleado");
        }
        personaDAO.close();

        localDAO.open();
        try {
            localDAO.create( local );
        }
        catch ( Exception e ) {
            Log.w( TAG, "error con local");
        }
        localDAO.close();

        marcaDAO.open();
        try{
            marcaDAO.create( marca );
        }
        catch ( Exception e){
            Log.w( TAG ,"error con marca");
        }
        marcaDAO.close();;

        String codigo = txtCodigo.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        int talla = Integer.parseInt(txtTalla.getText().toString());
        Date date = new Date();
        Timestamp fecha = new Timestamp( date.getTime() );

        Prestamo prestamo = new Prestamo( codigo, descripcion, talla, fecha, empleado, local, marca );

        prestamoDAO.open();
        prestamoDAO.create(prestamo);
        prestamoDAO.close();

    } // end method crear

    public void mostrar( View view ){
        ArrayList<Prestamo> listaPrestamos;

        Log.i( "appa", "ActivityPrestamos.mostrar()" );

        prestamoDAO.open();
        listaPrestamos = prestamoDAO.retrieveAll();
        prestamoDAO.close();

        StringBuilder sb = new StringBuilder();

        if(listaPrestamos!=null  && !listaPrestamos.isEmpty()){
            txtCantidad.setText( String.valueOf( listaPrestamos.size() ) );
            for( Prestamo prestamo: listaPrestamos ){
                sb.append( "Empledo: " ).append( prestamo.getEmpleado().getNombre() ).append( " cedula: " ).append( prestamo.getEmpleado().getCedula() )
                    .append( "\nMarca: " ).append( prestamo.getMarca().getNombre() ).append( "\n" )
                    .append( "Local: " ).append( prestamo.getLocal().getNombre() ).append( "\n" )
                    .append( "Descripcion: " ).append( prestamo.getDescripcion() ).append( " talla: " ).append( prestamo.getTalla() )
                    .append( "\nFecha: ").append( prestamo.getFecha().toString() )
                    .append( "\n\n" );
            }
        }
        else{
            sb.append("Ya no hay registros");
        }

        txtListaPrestamos.setText( sb.toString() );
    } // end method mostrar
} // end class ActivityPrestamos
