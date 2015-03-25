package com.paolosport.appa.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.paolosport.appa.LocalFragment;
import com.paolosport.appa.MarcaFragment;
import com.paolosport.appa.PersonaFragment;
import com.paolosport.appa.R;

public class ActivityConfiguracion extends ActionBarActivity {

    FragmentManager fragmentManager;
    Fragment containerFragment;
    LocalFragment localFragment;
    MarcaFragment marcaFragment;
    PersonaFragment personaFragment;

    static final int LOCAL_SELECTED = 1;
    static final int MARCA_SELECTED = 2;
    static final int PERSONA_SELECTED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        localFragment = new LocalFragment();
        marcaFragment = new MarcaFragment();
        personaFragment = new PersonaFragment();
        containerFragment = localFragment;
        fragmentManager = getSupportFragmentManager();

        onListSelection( MARCA_SELECTED );

    } // fin del metodo onCreate


    public void seleccionarCategoria( View view ){
        switch( view.getId() ){
            case R.id.btnLocal:
                onListSelection( LOCAL_SELECTED );
                break;
            case R.id.btnMarca:
                onListSelection( MARCA_SELECTED );
                break;
        } // end switch
    }

    public void onListSelection(int index) {
        if ( index == LOCAL_SELECTED && !containerFragment.getClass().equals( localFragment ) ){
            changeFragment( localFragment );
        } // fin de if
        else if ( index == MARCA_SELECTED && !containerFragment.getClass().equals( marcaFragment ) ){
            changeFragment( marcaFragment );
        }
        else if( index == PERSONA_SELECTED && !containerFragment.getClass().equals( personaFragment ) ){
            changeFragment( personaFragment );
        }
    } // end method onListSelection

    public void changeFragment( Fragment newFragment ){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.configuration_fragment_container, newFragment );
        containerFragment = newFragment;
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }
} // fin de la clase ActivityConfiguracion
