package com.paolosport.appa.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.fragments.LocalFragment;
import com.paolosport.appa.fragments.MarcaFragment;
import com.paolosport.appa.fragments.PersonaFragment;
import com.paolosport.appa.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ActivityConfiguracion extends ActionBarActivity {

    FragmentManager fragmentManager;
    Fragment containerFragment;
    LocalFragment localFragment;
    MarcaFragment marcaFragment;
    PersonaFragment personaFragment;

    static final int LOCAL_SELECTED = 1;
    static final int MARCA_SELECTED = 2;
    static final int PERSONA_SELECTED = 3;
    static final int CERRAR_SELECTED = 4;
    static final int REGRESAR_SELECTED = 5;

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
                Button b = (Button) view;
                onListSelection(LOCAL_SELECTED);
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
        else if( index == CERRAR_SELECTED ){
            finish();
        }
        else if( index == REGRESAR_SELECTED  ){
            finish();
        }

    } // end method onListSelection

    public void changeFragment( Fragment newFragment ){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.configuration_fragment_container, newFragment );
        containerFragment = newFragment;
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();

    } // fin del metodo onCreate

    public void muestre(View v){
        RelativeLayout l=(RelativeLayout)v;
        switch( l.getId() ){
            case R.id.item_local:
                onListSelection( LOCAL_SELECTED );
                break;
            case R.id.item_marca:
                onListSelection(MARCA_SELECTED);
                break;
            case R.id.item_persona:
                onListSelection(PERSONA_SELECTED);
                break;
            case R.id.item_cerrar:
                onListSelection(CERRAR_SELECTED);
                break;
            case R.id.item_regresar:
                onListSelection(REGRESAR_SELECTED);
                break;
        } // end switch
    } // end method muestre


} // fin de la clase ActivityConfiguracion
