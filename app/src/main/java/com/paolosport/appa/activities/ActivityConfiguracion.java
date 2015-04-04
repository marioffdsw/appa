package com.paolosport.appa.activities;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private ImageView iv_sesion;
    private TextView tv_sesion,tv_ini_sesion;
    private LinearLayout ll_gravity;

    public boolean sesion;
    Dialog customDialog=null;
    int id =0;

    static final int LOCAL_SELECTED = 1;
    static final int MARCA_SELECTED = 2;
    static final int PERSONA_SELECTED = 3;
    static final int SESION_SELECTED = 4;
    static final int REGRESAR_SELECTED = 5;
    static final int INICIAR_SESION = 6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        iv_sesion=(ImageView)findViewById(R.id.iv_sesion);

        tv_sesion=(TextView)findViewById(R.id.tv_sesion);
        tv_ini_sesion=(TextView)findViewById(R.id.tv_ini_sesion);
        ll_gravity = (LinearLayout)findViewById(R.id.ll_gravity);


        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        sesion=preferences.getBoolean("sesion",sesion);

        //Bundle bundle = getIntent().getExtras();
        //sesion= bundle.getBoolean("sesion");

        localFragment = new LocalFragment();
        marcaFragment = new MarcaFragment();
        personaFragment = new PersonaFragment();
        containerFragment = localFragment;
        fragmentManager = getSupportFragmentManager();

        if(sesion==true){
            tv_sesion.setText("Cerrar Sesión");
            iv_sesion.setImageResource(R.drawable.ico_sesion);
            tv_ini_sesion.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height=90;
            params.width=90;
            params.gravity = Gravity.TOP|Gravity.LEFT;
            ll_gravity.setLayoutParams(params);
        }
        else{
            tv_sesion.setText("Iniciar Sesión");
            iv_sesion.setImageResource(R.drawable.ic_persona);
            tv_ini_sesion.setVisibility(View.GONE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height=100;
            params.width=100;
            params.gravity = Gravity.CENTER;
            ll_gravity.setLayoutParams(params);
        }


        //onListSelection( MARCA_SELECTED );

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
        else if( index == SESION_SELECTED ){
            if(sesion==true){
                Toast.makeText(getApplicationContext(),"Sesión Terminada",Toast.LENGTH_SHORT).show();
                tv_sesion.setText("Iniciar Sesión");
                iv_sesion.setImageResource(R.drawable.ic_persona);
                tv_ini_sesion.setVisibility(View.GONE);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.height=100;
                params.width=100;
                params.gravity = Gravity.CENTER;
                ll_gravity.setLayoutParams(params);

                SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                sesion=false;
                editor.putBoolean("sesion",sesion);
                editor.commit();
                return;
            }
            mostrar(findViewById(id));
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
                if(sesion==false){
                    Toast.makeText(getApplicationContext(),"Sesión No Iniciada",Toast.LENGTH_SHORT).show();
                    break;
                }
                onListSelection( LOCAL_SELECTED );
                break;
            case R.id.item_marca:
                if(sesion==false){
                    Toast.makeText(getApplicationContext(),"Sesión No Iniciada",Toast.LENGTH_SHORT).show();
                    break;
                }
                onListSelection(MARCA_SELECTED);
                break;
            case R.id.item_persona:
                if(sesion==false){
                    Toast.makeText(getApplicationContext(),"Sesión No Iniciada",Toast.LENGTH_SHORT).show();
                    break;
                }
                onListSelection(PERSONA_SELECTED);
                break;
            case R.id.item_cerrar:
                onListSelection(SESION_SELECTED );
                break;
            case R.id.item_regresar:
                onListSelection(REGRESAR_SELECTED);
                break;
        } // end switch
    } // end method muestre

    public void mostrar(View v)
    {
        customDialog = new Dialog(this);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(true);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialogo_sesion);
        customDialog.show();
        ((Button) customDialog.findViewById(R.id.btn_aceptar_sesion)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                sesion=true;
                editor.putBoolean("sesion",sesion);
                editor.commit();

                customDialog.dismiss();
                if(sesion==true){
                    Toast.makeText(getApplicationContext(),"Sesión Iniciada",Toast.LENGTH_SHORT).show();
                    tv_sesion.setText("Cerrar Sesión");
                    iv_sesion.setImageResource(R.drawable.ico_sesion);
                    tv_ini_sesion.setVisibility(View.VISIBLE);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.height=90;
                    params.width=90;
                    params.gravity = Gravity.TOP|Gravity.LEFT;
                    ll_gravity.setLayoutParams(params);
                }
            }
        });
    }
} // fin de la clase ActivityConfiguracion
