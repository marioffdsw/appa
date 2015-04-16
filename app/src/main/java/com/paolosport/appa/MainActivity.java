package com.paolosport.appa;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.paolosport.appa.activities.ActivityConfiguracion;
import com.paolosport.appa.activities.ActivityListaPrestamos;
import com.paolosport.appa.activities.ActivityLocal;
import com.paolosport.appa.activities.ActivityMarca;
import com.paolosport.appa.activities.ActivityPersona;
import com.paolosport.appa.activities.opcion_informacion;
import com.paolosport.appa.fragments.PrestamoFormFragment;
import com.paolosport.appa.fragments.PrestamoLstFragment;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.persistencia.entities.Persona;
import com.paolosport.appa.spinnerLocalPaquete.SpinnerAdapterLocal;
import com.paolosport.appa.spinnerMarcaPaquete.SpinnerAdapterMarca;
import com.paolosport.appa.spinnerPersonaPaquete.SpinnerAdapterPersonaLista;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {

    AdminSQLiteOpenHelper helper;

    int id=0;
    Dialog customDialog=null;
    public boolean sesion;
    private ImageView imageView;

    MarcaDAO marcaDAO;
    LocalDAO localDAO;
    PersonaDAO personaDAO;

    HorizontalListView lv_persona;

    private Spinner sp_marca,   sp_local, sp_persona;
    String id_marca,url_marca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        sesion=preferences.getBoolean("sesion",sesion);

        helper = new AdminSQLiteOpenHelper( this );
        localDAO = new LocalDAO( this, helper );
        marcaDAO = new MarcaDAO(this,helper);
        personaDAO = new PersonaDAO(this,helper);
        // setup action bar for tabs
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        if(sesion==true){
            Toast.makeText(getApplicationContext(), "Sesión inicada", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        }
        DatosPorDefecto();
        CargarDatosListaPersonas();


        /*imageView = (ImageView) findViewById(R.id.imageView1);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.catico);
        Bitmap b = Bitmap.createScaledBitmap(bitmap,100,100,true);
        bitmap=b;

        if(bitmap!=null)

        {

            int targetWidth = 65;
            int targetHeight = 65;
            Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,Bitmap.Config.ARGB_8888);
            BitmapShader shader;
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);
            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(((float) targetWidth - 1) / 2,
                    ((float) targetHeight - 1) / 2,
                    (Math.min(((float) targetWidth),((float) targetHeight)) / 2),Path.Direction.CCW);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            paint.setStyle(Paint.Style.STROKE);
            canvas.clipPath(path);
            Bitmap sourceBitmap = bitmap;
            canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),sourceBitmap.getHeight()),
                    new Rect(0, 0, targetWidth,targetHeight), null);


            imageView.setImageBitmap(targetBitmap);   //set the circular image to your imageview
        }
       /* else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }*/
    }


    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????
    private void DatosPorDefecto() {
        marcaDAO.open();
        localDAO.open();

        final ArrayList<Marca> listaMarcas = marcaDAO.retrieveAll();
        final ArrayList<Local> listaLocales = localDAO.retrieveAll();


        sp_marca= (android.widget.Spinner)findViewById(R.id.sp_marca_prestamo);
        sp_local= (android.widget.Spinner)findViewById(R.id.sp_local_prestamo);

        sp_marca.setAdapter(new SpinnerAdapterMarca(this,listaMarcas));
        sp_local.setAdapter(new SpinnerAdapterLocal(this,listaLocales));

        marcaDAO.close();
        localDAO.close();

        sp_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                id_marca=((Marca) adapterView.getItemAtPosition(position)).getId();
                url_marca=((Marca) adapterView.getItemAtPosition(position)).getUrl();

                //et_nombre_marca_editado.setText(((Marca) adapterView.getItemAtPosition(position)).getNombre());

                String url = url_marca.toString();

                Bitmap bmImg = BitmapFactory.decodeFile(listaMarcas.get(position).getUrl());
                Pattern pat = Pattern.compile(".*/.*");
                Matcher mat = pat.matcher(url);

                /*if (mat.matches()) {
                    iv_editada.setImageBitmap(bmImg);
                } else {
                    int path = getResources().getIdentifier(url,"drawable", "com.paolosport.appa");
                    iv_editada.setImageResource(path);
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

        sp_local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //id_marca=((Marca) adapterView.getItemAtPosition(position)).getId();
                //url_marca=((Marca) adapterView.getItemAtPosition(position)).getUrl();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }//end method DatosPorDefecto


















































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
                //i.putExtra("sesion",sesion);
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

    public static class TabListener<T extends Fragment > implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        /** Constructor used each time a new tab is created.
         * @param activity  The host Activity, used to instantiate the fragment
         * @param tag  The identifier tag for the fragment
         * @param clz  The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
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
