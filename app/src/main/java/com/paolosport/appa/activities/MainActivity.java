package com.paolosport.appa.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.activities.ActivityConfiguracion;
import com.paolosport.appa.fragments.PrestamoFormFragment;
import com.paolosport.appa.fragments.PrestamoLstFragment;

public class MainActivity extends ActionBarActivity {


    int id=0;
    Dialog customDialog=null;
    public PrestamoLstFragment prestamoLstFragment;

    public boolean sesion;
    public String pass ;
    public MenuItem conf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // setup action bar for tabs
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setIcon(R.drawable.logo_appa);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        ActionBar.Tab tab = actionBar.newTab()
                .setText(R.string.frm_prestamos)
                .setIcon(R.drawable.ico_lista_vacia)
                .setTabListener(new TabListener<PrestamoFormFragment>(
                        this, "artist", PrestamoFormFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.lst_prestamos)
                .setIcon(R.drawable.ico_lista_llena_edit)
                .setTabListener(new TabListener<PrestamoLstFragment>(
                        this, "album", PrestamoLstFragment.class));
        actionBar.addTab(tab);
        actionBar.setDisplayShowTitleEnabled( true );
        actionBar.setDisplayShowTitleEnabled( true );
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        sesion=preferences.getBoolean("sesion",sesion);
        pass = preferences.getString("pass",pass);

        try{
            if(pass==null) {
                pass = "123";
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pass", "123");
                editor.commit();
            }
            else{
                pass = preferences.getString("pass",pass);
            }
        }
        catch (Exception e){}
        try{
            if(sesion != true && sesion!=false) {
                sesion = true;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("sesion",true);
                editor.commit();
            }
            else{
                sesion = preferences.getBoolean("sesion",sesion);
            }
        }
        catch (Exception e){}
    }


    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        conf = menu.findItem(R.id.configuracion);
        if(sesion==true){
            conf.setEnabled(true);
            menu.findItem(R.id.iniciar_sesion).setTitle("cerrar Sesión") ;
            menu.findItem(R.id.iniciar_sesion).setIcon(R.drawable.ico_sesion_gr);
        }
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
                acercaDe(findViewById(id));
                break;
            case R.id.iniciar_sesion:
                SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
                pass = preferences.getString("pass",pass);

                new Handler().postAtFrontOfQueue(new Runnable() {
                    @Override
                    public void run() {
                        prestamoLstFragment.deselecionarPrestamos();
                    }
                });

                if(sesion==true){
                    conf.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"Sesión Terminada",Toast.LENGTH_SHORT).show();
                    item.setTitle("Iniciar Sesión") ;
                    item.setIcon(R.drawable.ic_action_person);
                    SharedPreferences.Editor editor=preferences.edit();
                    sesion=false;
                    editor.putBoolean("sesion", sesion);
                    editor.commit();

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            prestamoLstFragment.configurarLista();
                            prestamoLstFragment.ocultarOpciones();
                        }
                    });


                    break;
                }
                mostrar(findViewById(id), item);

               break;
            case R.id.cerrar_sesión:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void acercaDe(View v)
    {
        customDialog = new Dialog(this,R.style.PauseDialog);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_personal);
        customDialog.show();
        ((Button) customDialog.findViewById(R.id.aceptar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){customDialog.dismiss();}
        });
    }

    public static class TabListener<T extends Fragment > implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        MediaPlayer mp;
        /** Constructor used each time a new tab is created.
         * @param activity  The host Activity, used to instantiate the fragment
         * @param tag  The identifier tag for the fragment
         * @param clz  The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mp = MediaPlayer.create(mActivity, R.raw.a_tab);
            mTag = tag;
            mClass = clz;
        }

    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            mp.start();

            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }

    public void mostrar(View v,MenuItem item)
    {
        customDialog = new Dialog(this,R.style.PauseDialog);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_sesion);
        customDialog.show();

        final MenuItem i_sesion = item;
        final EditText password=(EditText)customDialog.findViewById(R.id.et_password);
        ((Button) customDialog.findViewById(R.id.btn_aceptar_sesion)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pass.equals(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Sesión Iniciada", Toast.LENGTH_SHORT).show();
                    i_sesion.setTitle("Cerrar Sesión");
                    i_sesion.setIcon(R.drawable.ico_sesion_gr);
                    conf.setEnabled(true);

                    customDialog.dismiss();
                    SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    sesion = true;
                    editor.putBoolean("sesion", sesion);
                    editor.commit();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
                    password.setText("");
                }

                if ( prestamoLstFragment != null ) {
                    prestamoLstFragment.configurarLista();
                    prestamoLstFragment.ocultarOpciones();
                }
            }
        });
    }
} // fin de la activity Main
