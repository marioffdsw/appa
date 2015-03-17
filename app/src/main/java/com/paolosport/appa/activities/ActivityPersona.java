package com.paolosport.appa.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.entities.Persona;
import com.paolosport.appa.ui.FiltrosFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class ActivityPersona extends ActionBarActivity {

    EditText txtCedulaPersona;
    EditText txtNombrePersona;
    EditText txtTelefonoPersona;
    TextView txtListaPersonas;
    FiltrosFragment filtrosFrag;

    AdminSQLiteOpenHelper helper;
    PersonaDAO personaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);

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

        txtCedulaPersona = (EditText) findViewById(R.id.txtCedulaPersona);
        txtNombrePersona = (EditText) findViewById(R.id.txtNombrePersona);
        txtTelefonoPersona = (EditText) findViewById(R.id.txtTelefonoPersona);
        txtListaPersonas = (TextView) findViewById(R.id.txtListaPersonas);

        /* Cambia el tipo de entrada para que se coloque una letra y luego un numero */
        txtNombrePersona.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if( txtNombrePersona.getText().toString().length() == 0 ){
                    txtNombrePersona.setInputType( InputType.TYPE_CLASS_TEXT );
                }
                else{
                    txtNombrePersona.setInputType( InputType.TYPE_CLASS_NUMBER );
                }
            } // end method afterTextChanged

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        }); // end addTextChangedListener

        helper = new AdminSQLiteOpenHelper(this);
        personaDAO = new PersonaDAO(this, helper);

        mostrarEntradasPersona(txtCedulaPersona);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_persona, menu);
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

    public void guardarPersona(View view) {
        String id = txtCedulaPersona.getText().toString();
        String nombre = txtNombrePersona.getText().toString();
        String telefono = txtTelefonoPersona.getText().toString();

        Persona persona = new Persona(id, nombre, telefono, "prueba");

        personaDAO.open();
        personaDAO.create(persona);
        personaDAO.close();

        // actualizar ListaPersonas con la nueva entrada
        mostrarEntradasPersona(view);
    } // end method guardarLocal

    public void mostrarEntradasPersona(View view) {

        ArrayList<Persona> listaPersonas;
        personaDAO.open();
        listaPersonas = personaDAO.retrieveAll();
        personaDAO.close();

        StringBuilder sb = new StringBuilder();

        if (listaPersonas != null && !listaPersonas.isEmpty()) {
            for (Persona persona : listaPersonas) {
                sb.append("CC: ").append(persona.getCedula()).append(" Nombre: ").append(persona.getNombre()).append(" Telefono: ").append(persona.getTelefono()).append(" Foto: ").append(persona.getUrl()).append("\n\n");
            }
        } else {
            sb.append("Ya no hay registros");
        }

        txtListaPersonas.setText(sb.toString());
    } // end method mostrarEntradas

    public void buscarPersona(View view) {

        String id = txtCedulaPersona.getText().toString();
        Persona persona;
        personaDAO.open();
        try {
            persona = personaDAO.retrieve(id);
            personaDAO.close();
            txtListaPersonas.setText("");
            txtListaPersonas.setText(persona.getNombre() + " " + persona.getTelefono() + " " + persona.getUrl());
        } catch (Exception e) {
        }
    } // end method buscar

    public void actualizarPersona(View view) {

        String id = txtCedulaPersona.getText().toString();
        String nombre = txtNombrePersona.getText().toString();
        String telefono = txtTelefonoPersona.getText().toString();


        Persona persona = new Persona(id, nombre, telefono, "prueba");
        personaDAO.open();
        try {
            personaDAO.update(persona);
            personaDAO.close();
            txtListaPersonas.setText("");
        } catch (Exception e) {
        }
    } // end method buscar

    public void eliminarPersona(View view) {

        String id = txtCedulaPersona.getText().toString();
        Persona persona;
        personaDAO.open();
        try {
            persona = personaDAO.retrieve(id);
            personaDAO.delete(persona);
            personaDAO.close();
            txtListaPersonas.setText("");
            mostrarEntradasPersona(view);
        } catch (Exception e) {

        }

    } // end method eliminar

    public void limpiar(View view) {
        txtNombrePersona.setText("");
        txtCedulaPersona.setText("");
        txtTelefonoPersona.setText("");
    } // fin del metodo limpiar

    /** el metodo getImage, se encarga de lanzar un dialogo que pregunta al usuario si
     * obtener una imagen desde la camara o desde la galeria y se encarga de lanzar
     * el intent requerido y configurar el codigo de resultado */
    public void getImage(View view) {

        final CharSequence[] options = {getString(R.string.take_photo),
                getString(R.string.from_gallery),
                getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPersona.this);

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

} // end class ActivityPersona
