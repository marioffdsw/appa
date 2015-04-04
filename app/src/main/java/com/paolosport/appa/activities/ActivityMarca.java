package com.paolosport.appa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.RealPathUtil;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.spinnerMarcaPaquete.SpinnerAdapterMarca;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityMarca extends ActionBarActivity {

    EditText txtIDMarca;
    EditText txtNombreMarca;
    EditText txtURL;
    private android.widget.Spinner spinner;

    TextView txtListaMarcas;

    AdminSQLiteOpenHelper helper;
    MarcaDAO marcaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_marca);

        Button btnAction = (Button)findViewById(R.id.btnPic);

        txtIDMarca = (EditText) findViewById( R.id.txtIDMarca );
        txtNombreMarca = (EditText) findViewById( R.id.txtNombreMarca );
        txtURL = (EditText) findViewById( R.id.txtURL );
        txtListaMarcas = (TextView) findViewById( R.id.txtListaMarcas );

        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);

        helper = new AdminSQLiteOpenHelper( this );
        marcaDAO = new MarcaDAO( this, helper );

        try{
        marcaDAO.open();
        DatosPorDefecto();
        marcaDAO.close();
        }
        catch (Exception e){e.printStackTrace();}
    }

    public void onClicka(View view) {

        // 1. on Upload click call ACTION_GET_CONTENT intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 2. pick image only
        intent.setType("image/*");
        // 3. start activity
        startActivityForResult(intent, 0);
        // define onActivityResult to do something with picked image
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_marca, menu);
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

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if(resCode == Activity.RESULT_OK && data != null){
            String realPath;
            if (Build.VERSION.SDK_INT < 11)
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
            else if (Build.VERSION.SDK_INT < 19)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
            setTextViews(Build.VERSION.SDK_INT, data.getData().getPath(),realPath);
        }
    }

    private void setTextViews(int sdk, String uriPath,String realPath){

        //this.txtSDK.setText("Build.VERSION.SDK_INT: "+sdk);
        //this.txtUriPath.setText("URI Path: "+uriPath);
        //this.txtURL.setText("Real Path: "+realPath);
        Uri uriFromPath = Uri.fromFile(new File(realPath));
        // you have two ways to display selected image
        // ( 1 ) imageView.setImageURI(uriFromPath);
        // ( 2 ) imageView.setImageBitmap(bitmap);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView iv = (ImageView)findViewById(R.id.imgView);
        iv.setImageBitmap(bitmap);

        String nombre = realPath;
        String []cadenas = nombre.split("[/]");
        nombre = cadenas[(cadenas.length)-1];

        String ruta = guardarImagen(getApplicationContext(), nombre, bitmap);
        Toast.makeText(getApplicationContext(), ruta, Toast.LENGTH_LONG).show();


        Log.d("HMKCODE", "Build.VERSION.SDK_INT:" + sdk);
        Log.d("HMKCODE", "URI Path:"+uriPath);
        Log.d("HMKCODE", "Real Path: "+realPath);
    }

    private String guardarImagen (Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        File myPath = new File(dirImages, nombre);

        String ultimatePath=(dirImages+"/"+nombre);

        this.txtURL.setText(ultimatePath);
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return myPath.getAbsolutePath();
    }

    private void DatosPorDefecto() {
        marcaDAO.open();

        ArrayList<Marca> listaMarcasSpinner = marcaDAO.retrieveAll();
        Resources r = this.getResources();
        /*for(Marca marca:listaMarcasSpinner) {
            items.add(new SpinnerMarca(marca.getNombre(),r.getIdentifier(marca.getUrl(),"drawable",getPackageName())));
            //muestra nombre de la marca y el nombre del url de la marca q obligatoriamente debe coincidir con el nombre del recurso
        }*/
        marcaDAO.close();

        spinner = (android.widget.Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new SpinnerAdapterMarca(this,listaMarcasSpinner));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(adapterView.getContext(), ((Marca) adapterView.getItemAtPosition(position)).getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //nothing
            }
        });
    }

    public void guardarMarca( View view ){
        String id = txtIDMarca.getText().toString();
        String nombre = txtNombreMarca.getText().toString();
        String url = txtURL.getText().toString();

        Marca marca = new Marca( id, nombre,url );

        marcaDAO.open();
        marcaDAO.create( marca );
        marcaDAO.close();

    } // end method guardar

    public void mostrarEntradasMarca( View view ){

        ArrayList<Marca> listaMarcas;
        marcaDAO.open();
        listaMarcas = marcaDAO.retrieveAll();
        marcaDAO.close();

        StringBuilder sb = new StringBuilder();

        if(listaMarcas!=null ){
            for( Marca marca: listaMarcas ){
                sb.append( "ID: " ).append(marca.getId()).append( " Nombre: " ).append(marca.getNombre()).append( " Url: " ).append(marca.getUrl()).append("\n\n");
            }
        }
        else{
            sb.append("Ya no hay registros");
        }

        txtListaMarcas.setText( sb.toString() );
    } // end method mostrarEntradas

    public void buscarMarca( View view ){

        String id = txtIDMarca.getText().toString();
        Marca marca;
        marcaDAO.open();
        try {
            marcaDAO.open();
            marca = marcaDAO.retrieve(id);
            marcaDAO.close();
            txtListaMarcas.setText(marca.getNombre());
        }
        catch (Exception e){}
    } // end method buscar

    public void actualizarMarca( View view ){

        String id = txtIDMarca.getText().toString();
        String nombre = txtNombreMarca.getText().toString();
        String url = txtURL.getText().toString();


        Marca marca = new Marca(id,nombre,url);
        marcaDAO.open();
        try {
            marcaDAO.update(marca);
            marcaDAO.close();
            txtListaMarcas.setText("");
        }
        catch (Exception e){}
    } // end method buscar

    public void eliminarMarca( View view ){

        String id = txtIDMarca.getText().toString();
        Marca marca;
        marcaDAO.open();
        try {
            marca = marcaDAO.retrieve(id);
            marcaDAO.delete(marca);
            marcaDAO.close();
            txtListaMarcas.setText( "" );
            mostrarEntradasMarca(view);
        }
        catch (Exception e){}

    } // end method eliminar

}
