package com.paolosport.appa.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.entities.Persona;

import com.paolosport.appa.ui.EmpleadoAdapter;
import com.paolosport.appa.ui.FiltrosFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;



public class PersonaFragment extends Fragment {

    Context context;
    FiltrosFragment filtrosFrag = new FiltrosFragment();
    View view;
    FrameLayout fotoFragmentContainer;
    private PersonaDAO personaDAO;
    private ArrayList<Persona> listaPersonas;

    private EditText txtNombrePersona;
    private EditText txtCedulaPersona;
    private EditText txtTelefonoPersona;
    private ListView lstPersonas;
    private EmpleadoAdapter adapter;
    private Persona persona;

    // private OnFragmentInteractionListener mListener;

    public PersonaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onActivityCreated(savedInstanceState);

    } // fin del metodo onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_persona, container, false);

        /** Se crea, los objetos de gestion de la base de datos */
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context.getApplicationContext());
        personaDAO = new PersonaDAO(context.getApplicationContext(), adminSQLiteOpenHelper);

        /** se carga los datos sobre los prestamos en la lista */
        // Se obtiene los datos de los prestamos
        personaDAO.open();
        listaPersonas = personaDAO.retrieveAll();
        personaDAO.close();

        // se crea el adaptador de la vista
        adapter = new EmpleadoAdapter(context, R.layout.item_list_empleados, listaPersonas);

        // si no hay datos sobre prestamos se notifica al usuario
        if (listaPersonas == null)
            Toast.makeText(context, "no hay datos", Toast.LENGTH_SHORT).show();

        if(  listaPersonas != null ){
            Toast.makeText( context, String.valueOf(listaPersonas.size()).toString(), Toast.LENGTH_SHORT ).show();
            view.findViewById( R.id.layoutOops).setVisibility( View.INVISIBLE );
            view.findViewById( R.id.lstEmpleados).setVisibility( View.VISIBLE );
        }
        // se carga los datos que existan sobre los prestamos
        Activity activity = (Activity) context;
        lstPersonas = (ListView) view.findViewById( R.id.lstEmpleados );
        lstPersonas.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        lstPersonas.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                persona = listaPersonas.get( position );

                txtNombrePersona.setText( persona.getNombre() );
                txtCedulaPersona.setText( persona.getCedula() );
                txtTelefonoPersona.setText( persona.getTelefono() );
            }
        });

        txtNombrePersona = (EditText) view.findViewById( R.id.txtNombrePersona );
        txtCedulaPersona = (EditText) view.findViewById( R.id.txtCedulaPersona );
        txtTelefonoPersona = (EditText) view.findViewById( R.id.txtTelefonoPersona );

        Button guardar = (Button) view.findViewById( R.id.btnGuardar );
        guardar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarPersona();
            }
        });

        Button actualizar = (Button) view.findViewById( R.id.btnActualizar );
        actualizar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPersona();
            }
        });

        Button eliminar = (Button) view.findViewById( R.id.btnEliminar );
        eliminar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPersona();
            }
        });

        final Button cancelar = (Button) view.findViewById( R.id.btnCancelar );
        cancelar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar();
            }
        });

        ImageView imgFoto = (ImageView) view.findViewById( R.id.imgContainer );
        imgFoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(v);
            }
        });

        return view;
    } // fin del metodo onCreateView

    public void guardarPersona(){
        String id = txtCedulaPersona.getText().toString();
        String nombre = txtNombrePersona.getText().toString();
        String telefono = txtTelefonoPersona.getText().toString();

        Persona persona = new Persona(id, nombre, telefono, "prueba");

        personaDAO.open();
        PersonaDAO.Estado estado = personaDAO.create(persona);
        personaDAO.close();

        if ( estado == PersonaDAO.Estado.INSERTADO ) {
            listaPersonas.add(persona);
            adapter.notifyDataSetChanged();
        }
        else{
            Toast.makeText( context,
                ":( Error al insertar persona\n" +
                "Es posible que un empleado con ese numero de cedula ya haya sido registrado",
            Toast.LENGTH_LONG ).show();
        }
    }

    public void actualizarPersona(){

        if ( persona == null ){
            Toast.makeText( context, "Selecciona un empleado de la lista primero", Toast.LENGTH_SHORT ).show();
            return;
        }

        String id = txtCedulaPersona.getText().toString();
        String nombre = txtNombrePersona.getText().toString();
        String telefono = txtTelefonoPersona.getText().toString();

        String cedulaAntigua = persona.getCedula();
        persona.setCedula( id );
        persona.setNombre( nombre );
        persona.setTelefono( telefono );

        personaDAO.open();
        PersonaDAO.Estado estado = personaDAO.update( persona, cedulaAntigua );
        personaDAO.close();

        if ( estado == PersonaDAO.Estado.ACTUALIZADO ) {
            adapter.notifyDataSetChanged();
        }
        else{
            Toast.makeText( context,
                    ":( Error al actualizar los datos del empleado" +
                    "\n\tEs posible que el empleado tenga pedidos pendientes",
                    Toast.LENGTH_LONG ).show();
        }
    }

    public void eliminarPersona(){

        if ( persona == null ){
            Toast.makeText( context, "Selecciona un empleado de la lista primero", Toast.LENGTH_SHORT ).show();
            return;
        }

        personaDAO.open();
        PersonaDAO.Estado estado = personaDAO.delete( persona );
        personaDAO.close();

        if ( estado == PersonaDAO.Estado.ELIMINADO ) {

            adapter.remove( persona );
            cancelar();
            adapter.notifyDataSetChanged();
        }
        else{
            Toast.makeText( context,
                    ":( Error al actualizar los datos del empleado",
                    Toast.LENGTH_LONG ).show();
        }
    }

    public void cancelar() {
        persona = null;
        txtCedulaPersona.setText( "" );
        txtNombrePersona.setText( "" );
        txtTelefonoPersona.setText( "" );
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    } // fin del metodo onAttach


    /** el metodo getImage, se encarga de lanzar un dialogo que pregunta al usuario si
     * obtener una imagen desde la camara o desde la galeria y se encarga de lanzar
     * el intent requerido y configurar el codigo de resultado */
    public void getImage(View view) {

        final CharSequence[] options = {getString(R.string.take_photo),
                getString(R.string.from_gallery),
                getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    publishImage(bitmap);

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
                Cursor c = context.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = null;

                try {
                    Uri uri = Uri.parse("file://" + picturePath);
                    thumbnail = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // se publica la imagen el el fragment para ser mostrada
                publishImage(thumbnail);
            } // fin else if ( codigo de respuesta )
        } // fin de else (respuesta correcta)
    } // fin del metodo onActivityResult

    public void publishImage( Bitmap bitmap ){
        ImageView imageView = (ImageView) view.findViewById( R.id.imgContainer );
        imageView.setImageBitmap(bitmap);
    }
} // fin de la clase LocalFragment
