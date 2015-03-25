package com.paolosport.appa.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.paolosport.appa.R;

public class ActivityConfiguracion extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.beginTransaction();




    } // fin del metodo onCreate

    public void muestre(View v){

        RelativeLayout l=(RelativeLayout)v;

        Toast.makeText(this, "converse",Toast.LENGTH_SHORT).show();
    }
} // fin de la clase ActivityConfiguracion
