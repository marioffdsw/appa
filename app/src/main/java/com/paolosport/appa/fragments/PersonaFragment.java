package com.paolosport.appa.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.entities.Persona;
import com.paolosport.appa.ListViewAdapters.ListViewAdapterPersona;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PersonaFragment extends Fragment {

    Context context;
    View view;
    private PersonaDAO personaDAO;
    private ArrayList< Persona > listaPersonas;
    private ImageView imgFoto;
    String url;

    private EditText txtNombrePersona;
    private EditText txtCedulaPersona;
    private EditText txtTelefonoPersona;
    private ListView lstPersonas;
    private ListViewAdapterPersona adapter;
    private Persona persona;

    private Button btnGuardar;
    private Button btnNuevo;
    private Button btnCancelar;
    private Button btnActualizar;
    private Button btnEliminar;

    private LinearLayout texto_animado;
    // private OnFragmentInteractionListener mListener;

    public PersonaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        super.onActivityCreated( savedInstanceState );

    } // fin del metodo onCreate

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_persona, container, false );

        /** Se crea, los objetos de gestion de la base de datos */
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper( context.getApplicationContext() );
        personaDAO = new PersonaDAO( context.getApplicationContext(), adminSQLiteOpenHelper );

        /** se carga los datos sobre los prestamos en la lista */
        // Se obtiene los datos de los prestamos
        personaDAO.open();
        listaPersonas = personaDAO.retrieveAll();
        personaDAO.close();

        // se crea el adaptador de la vista
        texto_animado = (LinearLayout)view.findViewById(R.id.texto_animado);
        adapter = new ListViewAdapterPersona( context, R.layout.item_list_empleados, listaPersonas );

        // si no hay datos sobre prestamos se notifica al usuario
        if( listaPersonas == null ) {
            Toast.makeText( context, "no hay datos", Toast.LENGTH_SHORT ).show();
        }

        if( listaPersonas != null ) {
            //Toast.makeText( context, String.valueOf( listaPersonas.size() ).toString(),
            //    Toast.LENGTH_SHORT ).show();

            view.findViewById( R.id.layoutOops ).setVisibility( View.INVISIBLE );
            view.findViewById( R.id.lstEmpleados ).setVisibility( View.VISIBLE );
            //Toast.makeText( context, listaPersonas.get( 10 ).getUrl(), Toast.LENGTH_LONG ).show();
        }
        // se carga los datos que existan sobre los prestamos
        Activity activity = ( Activity ) context;
        lstPersonas = ( ListView ) view.findViewById( R.id.lstEmpleados );
        lstPersonas.setAdapter( adapter );
        adapter.setNotifyOnChange( true );

        lstPersonas.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView< ? > parent, View view, int position, long id ) {
                persona = listaPersonas.get( position );

                txtNombrePersona.setText( persona.getNombre() );
                txtNombrePersona.setEnabled( true );
                txtCedulaPersona.setText( persona.getCedula() );
                txtCedulaPersona.setEnabled( true );
                txtTelefonoPersona.setText( persona.getTelefono() );
                txtTelefonoPersona.setEnabled( true );
                imgFoto.setImageBitmap( BitmapFactory.decodeFile( persona.getUrl() ) );
                imgFoto.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        getImage( imgFoto );
                    }
                } );

                btnNuevo.setVisibility( View.GONE );
                btnCancelar.setVisibility( View.VISIBLE );
                btnActualizar.setVisibility( View.VISIBLE );
                btnEliminar.setVisibility( View.VISIBLE );
            }
        } );

        txtNombrePersona = ( EditText ) view.findViewById( R.id.txtNombrePersona );
        txtCedulaPersona = ( EditText ) view.findViewById( R.id.txtCedulaPersona );
        txtTelefonoPersona = ( EditText ) view.findViewById( R.id.txtTelefonoPersona );

        btnNuevo = ( Button ) view.findViewById( R.id.btnNuevo );
        btnNuevo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                nuevaPersona();
            }
        } );

        btnGuardar = ( Button ) view.findViewById( R.id.btnGuardar );
        btnGuardar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                crearPersona();
            }
        } );

        btnActualizar = ( Button ) view.findViewById( R.id.btnActualizar );
        btnActualizar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                actualizarPersona();
            }
        } );

        btnEliminar = ( Button ) view.findViewById( R.id.btnEliminar );
        btnEliminar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                eliminarPersona();
            }
        } );

        btnCancelar = ( Button ) view.findViewById( R.id.btnCancelar );
        btnCancelar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                cancelar();
            }
        } );

        imgFoto = ( ImageView ) view.findViewById( R.id.imgContainer );
        imgFoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                getImage( v );
            }
        } );

        cancelar();
        animar(true);
        return view;
    } // fin del metodo onCreateView

    public void nuevaPersona(){
        txtCedulaPersona.setEnabled( true );
        txtNombrePersona.setEnabled( true );
        txtTelefonoPersona.setEnabled( true );
        imgFoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                getImage( imgFoto );
            }
        } );
        btnNuevo.setVisibility( View.GONE );
        btnGuardar.setVisibility( View.VISIBLE );
        btnCancelar.setVisibility( View.VISIBLE );
    }

    public void crearPersona() {

        String id = txtCedulaPersona.getText().toString();
        String nombre = txtNombrePersona.getText().toString();
        String telefono = txtTelefonoPersona.getText().toString();
        Bitmap foto = ((BitmapDrawable) imgFoto.getDrawable()).getBitmap();


        if(!((id==null || id.isEmpty())||(nombre==null || nombre.isEmpty()))){
            Log.e("hola","entroooooooo");
            Persona persona = new Persona(id, nombre, telefono, "prueba");
            persona.setUrl(guardarImagen(context, persona.getCedula(), foto));

            personaDAO.open();
            PersonaDAO.Estado estado = personaDAO.create(persona);
            personaDAO.close();

            if (estado == PersonaDAO.Estado.INSERTADO) {
                listaPersonas.add(persona);
                ordenarAlfabeticamente();
            } else {
                Toast.makeText(context, ":( Error al insertar persona\n" + "Es posible que un empleado con ese numero de cedula ya haya sido registrado", Toast.LENGTH_LONG).show();
            }
            cancelar();

        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Complete los Campos", Toast.LENGTH_SHORT).show();
            cancelar();
        }
    }

    public void actualizarPersona() {

        if( persona == null ) {
            Toast.makeText( context, "Selecciona un empleado de la lista primero", Toast.LENGTH_SHORT ).show();
            return;
        }

        String id = txtCedulaPersona.getText().toString();
        String nombre = txtNombrePersona.getText().toString();
        String telefono = txtTelefonoPersona.getText().toString();
        Bitmap foto = ( ( BitmapDrawable ) imgFoto.getDrawable() ).getBitmap();

        String cedulaAntigua = persona.getCedula();
        persona.setCedula( id );
        persona.setNombre( nombre );
        persona.setTelefono( telefono );
        persona.setUrl( guardarImagen(context, persona.getCedula(), foto) );

        personaDAO.open();
        PersonaDAO.Estado estado = personaDAO.update( persona, cedulaAntigua );
        personaDAO.close();

        if( estado == PersonaDAO.Estado.ACTUALIZADO ) {
            ordenarAlfabeticamente();
        } else {
            Toast.makeText( context, ":( Error al actualizar los datos del empleado" + "\n\tEs posible que el empleado tenga pedidos pendientes", Toast.LENGTH_LONG ).show();
        }
        cancelar();
    }

    public void eliminarPersona() {

        if( persona == null ) {
            Toast.makeText( context, "Selecciona un empleado de la lista primero", Toast.LENGTH_SHORT ).show();
            return;
        }

        personaDAO.open();
        PersonaDAO.Estado estado = personaDAO.delete( persona );
        personaDAO.close();

        if( estado == PersonaDAO.Estado.ELIMINADO ) {

            adapter.remove( persona );
            cancelar();
            ordenarAlfabeticamente();
        } else {
            Toast.makeText( context, ":( Error al actualizar los datos del empleado", Toast.LENGTH_LONG ).show();
        }
        cancelar();
    }

    public void cancelar() {
        persona = null;
        txtCedulaPersona.setText( "" );
        txtCedulaPersona.setEnabled( false );
        txtNombrePersona.setText( "" );
        txtNombrePersona.setEnabled( false );
        txtTelefonoPersona.setText( "" );
        txtTelefonoPersona.setEnabled( false );
        imgFoto.setImageDrawable( getActivity().getResources().getDrawable( R.drawable.ico_persona_gr ) );
        imgFoto.setOnClickListener( null );
        btnNuevo.setVisibility( View.VISIBLE );
        btnGuardar.setVisibility( View.INVISIBLE);
        btnCancelar.setVisibility( View.INVISIBLE );
        btnActualizar.setVisibility( View.INVISIBLE );
        btnEliminar.setVisibility( View.GONE );
    }

    public void ordenarAlfabeticamente(){
        Collections.sort( listaPersonas, new Comparator< Persona >() {
            @Override
            public int compare( Persona lhs, Persona rhs ) {
                return lhs.getNombre().toUpperCase().compareTo( rhs.getNombre().toUpperCase() );
            }
        } );
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        context = activity;
    } // fin del metodo onAttach


    /**
     * el metodo getImage, se encarga de lanzar un dialogo que pregunta al usuario si
     * obtener una imagen desde la camara o desde la galeria y se encarga de lanzar
     * el intent requerido y configurar el codigo de resultado
     */
    public void getImage( View view ) {

        final CharSequence[] options = { getString( R.string.take_photo ), getString( R.string.from_gallery ), getString( R.string.cancel ) };

        AlertDialog.Builder builder = new AlertDialog.Builder( context );

        builder.setTitle( getString( R.string.choose_image ) );
        builder.setItems( options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialog, int item ) {
                if( options[ item ].equals( getString( R.string.take_photo ) ) ) {
                    Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                    intent.putExtra( MediaStore.EXTRA_VIDEO_QUALITY, 0 );
                    File f = new File( android.os.Environment.getExternalStorageDirectory(), "temp.jpg" );
                    intent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( f ) );
                    startActivityForResult( intent, 1 );
                } else if( options[ item ].equals( getString( R.string.from_gallery ) ) ) {
                    Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                    startActivityForResult( intent, 2 );
                } else if( options[ item ].equals( getString( R.string.cancel ) ) ) {
                    dialog.dismiss();
                }
            }
        } );
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
                    bitmapOptions.inJustDecodeBounds = false;
                    bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmapOptions.inDither = true;
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);
                    bitmap = Bitmap.createScaledBitmap( bitmap, 640, 480, false );

                    // se publica la imagen en el fragment para que la muestre en un ImageView
                    publishImage( bitmap );

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
                Cursor c = context.getContentResolver().query(selectedImage, filePath, null, null,
                    null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = null;

                try {
                    Uri uri = Uri.parse("file://" + picturePath);
                    thumbnail = BitmapFactory.decodeStream(context.getContentResolver()
                        .openInputStream( uri ));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // se publica la imagen el el fragment parda ser mostrada
                publishImage( thumbnail );
            } // fin else if ( codigo de respuesta )
        } // fin de else (respuesta correcta)
    } // fin del metodo onActivityResult

    public void publishImage( Bitmap bitmap ) {
        ImageView imageView = ( ImageView ) view.findViewById( R.id.imgContainer );
        imageView.setImageBitmap( bitmap );
    }

    public String guardarImagen( Context context, String nombre, Bitmap imagen ) {
        ContextWrapper cw = new ContextWrapper( context );
        File dirImages = cw.getDir( "Imagenes", Context.MODE_PRIVATE );
        File dirImagesMini = cw.getDir( "Imagenes", Context.MODE_PRIVATE );
        File myPath = new File( dirImages, nombre );
        File myPathMini = new File( dirImagesMini, nombre + "mini" );

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream( myPath );
            imagen = Bitmap.createScaledBitmap( imagen, 200, 200, false );
            imagen.compress( Bitmap.CompressFormat.PNG, 10, fos );
            fos.flush();
            fos = new FileOutputStream( myPathMini );
            Bitmap mini = Bitmap.createScaledBitmap( imagen, 48, 48, false );
            imagen.compress( Bitmap.CompressFormat.PNG, 10, fos );
            fos.flush();

        } catch( FileNotFoundException ex ) {
            ex.printStackTrace();
        } catch( IOException ex ) {
            ex.printStackTrace();
        }
        url= myPath.getAbsolutePath();
        return myPath.getAbsolutePath();
    }
    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            //desde la esquina inferior derecha a la superior izquierda
            //animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 3.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            animation=new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }

        //duración en milisegundos
        animation.setDuration(200);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        texto_animado.setLayoutAnimation(controller);
        texto_animado.startAnimation(animation);
    }
} // fin de la clase LocalFragment

