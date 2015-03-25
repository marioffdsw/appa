package com.paolosport.appa;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.paolosport.appa.activities.ActivityConfiguracion;
import com.paolosport.appa.activities.ActivityListaPrestamos;
import com.paolosport.appa.activities.ActivityLocal;
import com.paolosport.appa.activities.ActivityMarca;
import com.paolosport.appa.activities.ActivityPersona;
import com.paolosport.appa.activities.opcion_informacion;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;


public class MainActivity extends ActionBarActivity {

    AdminSQLiteOpenHelper helper;
    LocalDAO localDAO;
    int id=0;
    Dialog customDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new AdminSQLiteOpenHelper( this );
        localDAO = new LocalDAO( this, helper );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.logo_appa);
        actionBar.setDisplayUseLogoEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.configuracion:
                Intent i = new Intent(this, ActivityConfiguracion.class);
                startActivity(i);
                break;
            case R.id.acercade:
                mostrar(findViewById(id));
                break;
            case R.id.Ayuda:
                Intent a = new Intent(this, opcion_informacion.class);
                startActivity(a);
               break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrar(View v)
    {
        customDialog = new Dialog(this);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialogo_personal);

        customDialog.show();
        ((Button) customDialog.findViewById(R.id.aceptar)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view){customDialog.dismiss();}
        });
    }

    /** el metodo local, lanza una activity que permite introducir
     *  información sobre un nuevo empleado */
    //TODO cambiar la signatura del metodo, y del boton asociado
    // el metodo debera cambiar de lugar al refactorizar la app para un mejor flujo de trabajo
    public void local(View view) {
        Intent i = new Intent(this, ActivityLocal.class );
        startActivity(i);
    }

    /** el metodo marca, lanza una activity que permite introducir
     *  información sobre un nuevo empleado */
    //TODO cambiar la signatura del metodo, y del boton asociado
    // el metodo debera cambiar de lugar al refactorizar la app para un mejor flujo de trabajo
    public void marca(View view) {
        Intent i = new Intent(this, ActivityMarca.class );
        startActivity(i);
    } // fin del metodo marca

    /** el metodo persona, lanza una activity que permite introducir
     *  información sobre un nuevo empleado */
        //TODO cambiar la signatura del metodo, y del boton asociado
        // el metodo debera cambiar de lugar al refactorizar la app para un mejor flujo de trabajo
      public void persona(View view) {
        Intent i = new Intent(this, ActivityPersona.class );
        startActivity(i);
    } // fin del metodo persona


    /** el metodo prestamos, lanza una activity que permite introducir
     *  información sobre un nuevo empleado */
    //TODO cambiar la signatura del metodo, y del boton asociado
    // el metodo debera cambiar de lugar al refactorizar la app para un mejor flujo de trabajo
    public void prestamos ( View view ){
        Intent i = new Intent(this, ActivityPrestamos.class );
        startActivity(i);
    } // fin del metodo prestamos


    /** el metodo listaPrestamos, lanza una activity que permite introducir
     *  información sobre un nuevo empleado */
    //TODO cambiar la signatura del metodo, y del boton asociado
    // el metodo debera cambiar de lugar al refactorizar la app para un mejor flujo de trabajo
    public void listaPrestamos( View view ){
        Intent i = new Intent( this, ActivityListaPrestamos.class );
        startActivity( i );
    } // fin del metodo listarPrestamos
} // fin de la activity Main
