package com.paolosport.appa.activities;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView tv_ini_sesion;
    private LinearLayout ll_gravity;

    public boolean sesion;
    public String pass ;

    Dialog customDialog=null;
    int id =0;

    static final int LOCAL_SELECTED = 1;
    static final int MARCA_SELECTED = 2;
    static final int PERSONA_SELECTED = 3;
    static final int REGRESAR_SELECTED = 5;
    static final int CUENTA_SELECTED = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        tv_ini_sesion=(TextView)findViewById(R.id.tv_ini_sesion);
        ll_gravity = (LinearLayout)findViewById(R.id.ll_gravity);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        sesion=preferences.getBoolean("sesion",sesion);
        pass = preferences.getString("pass",pass);

        localFragment = new LocalFragment();
        marcaFragment = new MarcaFragment();
        personaFragment = new PersonaFragment();
        containerFragment = localFragment;
        fragmentManager = getSupportFragmentManager();

        tv_ini_sesion.setVisibility(View.VISIBLE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height=90;
        params.width=90;
        params.gravity = Gravity.TOP|Gravity.LEFT;
        ll_gravity.setLayoutParams(params);
    } // fin del metodo onCreate

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
        else if( index == REGRESAR_SELECTED  ){
            finish();
        }
        else if( index == CUENTA_SELECTED  ){
            contraseña(findViewById(id));
        }


    } // end method onListSelection

    public void changeFragment( Fragment newFragment ){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.configuration_fragment_container, newFragment);
        containerFragment = newFragment;
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.disallowAddToBackStack();
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
            case R.id.item_regresar:
                onListSelection(REGRESAR_SELECTED);
                break;
            case R.id.item_cuenta:
                onListSelection(CUENTA_SELECTED);
                break;

        } // end switch
    } // end method muestre




    public void contraseña(View v)
    {
        customDialog = new Dialog(this,R.style.PauseDialog);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_contrasena);
        customDialog.show();
        final EditText oldPassword=(EditText)customDialog.findViewById(R.id.et_old_password);
        final EditText newPassword=(EditText)customDialog.findViewById(R.id.et_new_password);
        final TextView textoPass  =(TextView)customDialog.findViewById(R.id.tv_pass);

        ((Button) customDialog.findViewById(R.id.btn_cambiar_pass)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pass.equals(oldPassword.getText().toString()) &&
                        newPassword.getText().toString()!=null &&
                        newPassword.getText().toString()!="" &&
                        !newPassword.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(), "Contraseña Actualizada", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pass", newPassword.getText().toString());
                    editor.commit();
                    textoPass.setText("");
                    customDialog.dismiss();

                }

                else {
                    if( newPassword.getText().toString()==null ||
                        newPassword.getText().toString()=="" ||
                        newPassword.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingrese Nueva Contraseña", Toast.LENGTH_SHORT).show();
                    }
                    else if (!pass.equals(oldPassword.getText().toString())){
                        textoPass.setText("Antigua Contraseña Incorrecta");
                        oldPassword.setText("");
                    }
                }

            }
        });
    }
} // fin de la clase ActivityConfiguracion
