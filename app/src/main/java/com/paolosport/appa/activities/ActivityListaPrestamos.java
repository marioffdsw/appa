package com.paolosport.appa.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.dao.PrestamoDAO;
import com.paolosport.appa.persistencia.entities.Prestamo;
import com.paolosport.appa.ui.FiltrosFragment;
import com.paolosport.appa.ui.PrestamoAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ActivityListaPrestamos extends ActionBarActivity {

    FiltrosFragment filtrosFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_prestamos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** Se configura el FiltrosFragment */
        // se crea un nuevo FiltrosFragment
        filtrosFrag = new FiltrosFragment();

        // se usa el fragmentManager y se comienza una transaccion para incluir el fragment en
        // en la Activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, filtrosFrag);

        // se hacepta los cambios para que se ejecute la transacción
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();

        /** Se crea, los objetos de gestion de la base de datos */
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getApplicationContext());
        MarcaDAO marcaDAO = new MarcaDAO(getApplicationContext(), adminSQLiteOpenHelper);
        LocalDAO localDAO = new LocalDAO(getApplicationContext(), adminSQLiteOpenHelper);
        PersonaDAO personaDAO = new PersonaDAO(getApplicationContext(), adminSQLiteOpenHelper);
        PrestamoDAO prestamoDAO = new PrestamoDAO(getApplicationContext(), adminSQLiteOpenHelper);
        prestamoDAO.setPersonaDAO(personaDAO);
        prestamoDAO.setMarcaDAO(marcaDAO);
        prestamoDAO.setLocalDAO(localDAO);

        /** se carga los datos sobre los prestamos en la lista */
        // Se obtiene los datos de los prestamos
        prestamoDAO.open();
        ArrayList<Prestamo> prestamos = prestamoDAO.retrieveAll();
        prestamoDAO.close();

        // se crea el adaptador de la vista
        PrestamoAdapter adapter = new PrestamoAdapter(this, R.layout.prestamo_item, prestamos);

        // si no hay datos sobre prestamos se notifica al usuario
        if (prestamos == null)
            Toast.makeText(this, "no hay datos", Toast.LENGTH_SHORT).show();

        // se carga los datos que existan sobre los prestamos
        ListView lstPrestamos = (ListView) findViewById(R.id.lstPrestamos);
        lstPrestamos.setAdapter(adapter);
    } // fin del metodo onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_list, menu);
        return true;
    } // fin del metodo onCreateOptionsMenu

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
    } // fin del emtodo onOptionsItemSelected

    /** el metodo getImage, se encarga de lanzar un dialogo que pregunta al usuario si
     * obtener una imagen desde la camara o desde la galeria y se encarga de lanzar
     * el intent requerido y configurar el codigo de resultado */
    public void getImage(View view) {

        final CharSequence[] options = {getString(R.string.take_photo),
                getString(R.string.from_gallery),
                getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityListaPrestamos.this);

        builder.setTitle(getString(R.string.choose_image));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.take_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals(getString(R.string.from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    } // end method  getImage

    /** el metodo onActivityResult se encarga de recibir la imagen de cualquiera de las dos opciones,
     * obtiene el path al archivo guardado y ademas entrega el bitmap al fragment para que este lo muestre
     * en una ImageView */
    //TODO conectar este metodo con el de guardar el archivo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // verifica si el resultado es correcto o fue cancelada la operacion
        if (resultCode == Activity.RESULT_OK) {

            /** verifica el codigo de solicitud, con este se sabe si la imagen resultado viene
             *  de la galeria o de la camara y se procesa adecuadamente */
            if (requestCode == 1) { // el bitmap viene de la camara
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                } // fin de if

                // trata de obtener el bitmap a partir de la informacion entregada por la camara
                // ya que esta la guarda y entrega un archivo temporal que debera ser guardado
                try {

                    // se obtiene el bitmap de el archivo que entrega la camara
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    // se publica la imagen en el fragment para que la muestre en un ImageView
                    filtrosFrag.publishImage(bitmap);

                    // se obtiene la ruta que el sistema asigna a la aplicacion y se crea el nombre de archivo
                    // utilizando la fecha actual
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete(); // se elimina el archivo temporal
                    OutputStream outFile = null;

                    // se guarda el archivo en la carpeta de la app asignada por el sistema
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // fin de if
            else if (requestCode == 2) { /** si se obtiene el resultado de la galeria de imagenes */

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = null;

                try {
                    Uri uri = Uri.parse("file://" + picturePath);
                    thumbnail = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // se publica la imagen el el fragment para ser mostrada
                filtrosFrag.publishImage(thumbnail);
            } // fin else if ( codigo de respuesta )
        } // fin de else (respuesta correcta)
    } // fin del metodo onActivityResult
} // fin de la ActivityListaPrestamos