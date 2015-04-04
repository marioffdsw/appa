package com.paolosport.appa.fragments;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.RealPathUtil;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.ListViewAdapters.ListViewAdapterMarca;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarcaFragment extends Fragment {

    // private OnFragmentInteractionListener mListener;
    AdminSQLiteOpenHelper helper;
    MarcaDAO marcaDAO;
    private String id_marca,url_marca,nombre_marca;
    private LinearLayout ll_listar_marca,
            ll_crear_marca,texto_animado;
    private View view;
    private ImageView iv_imagen_marca;

    private Button
            btn_crear_marca,
            btn_editar_marca,
            btn_cancelar_marca;

    private TextView    tv_url_marca,tv_ingrese_nombre,tv_seleccione_icono;
    private EditText    et_nombre_marca;
    private ListView    lv_lista_marcas,lv_marca;
    String seleccion;
    int posicion;

    ArrayList<Marca> listaMarcas;
    public MarcaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        if(view != null){
        }

    } // fin del metodo onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        //data base
        helper = new AdminSQLiteOpenHelper( getActivity().getApplicationContext() );
        marcaDAO = new MarcaDAO(getActivity().getApplicationContext(), helper );

        marcaDAO.open();
        listaMarcas = marcaDAO.retrieveAll();
        marcaDAO.close();

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_marca, container, false);

        btn_editar_marca = (Button)view.findViewById(R.id.btn_editar_marca);
        btn_crear_marca = (Button)view.findViewById(R.id.btn_crear_marca);
        btn_cancelar_marca = (Button)view.findViewById(R.id.btn_cancelar_marca);

        tv_seleccione_icono =(TextView)view.findViewById(R.id.tv_seleccione_icono);
        tv_ingrese_nombre = (TextView)view.findViewById(R.id.tv_ingrese_nombre);
        tv_url_marca = (TextView)view.findViewById(R.id.tv_url_marca);
        lv_lista_marcas = (ListView)view.findViewById(R.id.lv_lista_marcas);

        et_nombre_marca = (EditText)view.findViewById(R.id.et_nombre_marca);

        ll_crear_marca = (LinearLayout)view.findViewById(R.id.ll_crear_marca);
        ll_listar_marca = (LinearLayout)view.findViewById(R.id.ll_listar_marca);
        texto_animado = (LinearLayout)view.findViewById(R.id.texto_animado);

        iv_imagen_marca = (ImageView)view.findViewById(R.id.iv_imagen_marca);

        DatosPorDefecto();
        mostrarEntradas();

        ll_listar_marca.setVisibility(View.VISIBLE);
        ll_crear_marca.setVisibility(View.VISIBLE);

        //-----------------------
        btn_cancelar_marca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crear();

            }
        });
        //-----------------------
        btn_crear_marca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearMarca();
            }
        });
        //-----------------------
        btn_editar_marca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarMarca();
            }
        });
        //-----------------------
        iv_imagen_marca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicka();
            }
        });
        //-----------------------



        lv_lista_marcas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posicion = position;
                id_marca=((Marca) parent.getItemAtPosition(position)).getId();
                url_marca=((Marca) parent.getItemAtPosition(position)).getUrl();
                nombre_marca=((Marca) parent.getItemAtPosition(position)).getNombre();

                dialogo();
                return true;
            }
        });

        animar(true);
        return view;

    } // fin del metodo onCreateView

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    } // fin del metodo onAttach

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????
    public void eliminar(){
            dialogoConfirmacionEliminar();
    }
    //?????????????????????????????????????????????????????????????????????????
    public void editar(){

        tv_seleccione_icono.setText("Seleccione Nuevo Icono:");
        tv_ingrese_nombre.setText("Ingrese Nuevo Nombre:");

        et_nombre_marca.setText(nombre_marca);

        String url = url_marca.toString();
        Bitmap bmImg = BitmapFactory.decodeFile(url);
        Pattern pat = Pattern.compile(".*/.*");
        Matcher mat = pat.matcher(url);
        if (mat.matches()) {
            iv_imagen_marca.setImageBitmap(bmImg);
        } else {
            int path = getResources().getIdentifier(url,"drawable", "com.paolosport.appa");
            iv_imagen_marca.setImageResource(path);
        }
        btn_crear_marca.setVisibility(View.GONE);
        btn_editar_marca.setVisibility(View.VISIBLE);

    }
    //?????????????????????????????????????????????????????????????????????????
    public void crear(){
        ll_listar_marca.setVisibility(View.VISIBLE);
        btn_crear_marca.setVisibility(View.VISIBLE);
        btn_editar_marca.setVisibility(View.GONE);

        tv_ingrese_nombre.setText("Ingrese Nombre:");
        tv_seleccione_icono.setText("Seleccione Icono:");
        et_nombre_marca.setText("");
        iv_imagen_marca.setImageDrawable(null);

    }
    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void editarMarca(){

        String id = id_marca.toString();
        String nombre = et_nombre_marca.getText().toString();
        String url = url_marca.toString();

        if((nombre == null) || (nombre.equals(""))){
            Toast.makeText(getActivity().getApplicationContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
        }
        else {
            Marca marca = new Marca(id,nombre,url);
            marcaDAO.open();
            try {
                marcaDAO.update(marca);
                marcaDAO.close();
                et_nombre_marca.setText("");
                DatosPorDefecto();
                Toast.makeText(getActivity().getApplicationContext(), "Registro Editado", Toast.LENGTH_SHORT).show();
                    mostrarEntradas();
                    crear();
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Registro NO Editado", Toast.LENGTH_SHORT).show();
            }
        }//FinElse
    } // end method editarMarca

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void eliminarMarca(){
        String aux = id_marca.toString();
        Marca marca;
        marcaDAO.open();
        try {
            marca = marcaDAO.retrieve(aux);
            marcaDAO.delete(marca);
            marcaDAO.close();
            DatosPorDefecto();
            Toast.makeText(getActivity().getApplicationContext(),"Registro Eliminado",Toast.LENGTH_SHORT).show();
            mostrarEntradas();
            crear();
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "Registro NO Eliminado", Toast.LENGTH_SHORT).show();
        }

    } // end method eliminarMarca

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void crearMarca(){

        String nombre = et_nombre_marca.getText().toString();
        String url = tv_url_marca.getText().toString();

        if((nombre == null) || (nombre.equals(""))){
            Toast.makeText(getActivity().getApplicationContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
            return;
        }
        if((url == null) || (url.equals(""))){
            Toast.makeText(getActivity().getApplicationContext(), "Ingrese Imagen", Toast.LENGTH_SHORT).show();
            return;
        }

            Marca marca = new Marca( nombre,url );
            try {
                marcaDAO.open();
                marcaDAO.create(marca);
                marcaDAO.close();
                Toast.makeText(getActivity().getApplicationContext(), "Registro Creado", Toast.LENGTH_SHORT).show();

                et_nombre_marca.setText("");
                tv_url_marca.setText("");
                iv_imagen_marca.setImageDrawable(null);
                DatosPorDefecto();
                mostrarEntradas();

                Toast.makeText(getActivity().getApplicationContext(), "Registro Creado", Toast.LENGTH_SHORT).show();

            }
            catch (Exception e){
                Toast.makeText(getActivity().getApplicationContext(), "Registro NO Creado", Toast.LENGTH_SHORT).show();}

    }//end method crearMarca

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????
    private void DatosPorDefecto() {
        marcaDAO.open();
        final ArrayList<Marca> listaMarcas = marcaDAO.retrieveAll();
        marcaDAO.close();
    }//end method DatosPorDefecto

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????


    public void mostrarEntradas(){
        ArrayList<Marca> listaMarcas;
        marcaDAO.open();
        listaMarcas = marcaDAO.retrieveAll();
        marcaDAO.close();
        lv_marca= (ListView) view.findViewById(R.id.lv_lista_marcas);
        try{
            marcaDAO.open();
            DatosPorDefecto();
            marcaDAO.close();
        }
        catch (Exception e){e.printStackTrace();}
        StringBuilder sb = new StringBuilder();

        if(listaMarcas!=null  && !listaMarcas.isEmpty()){
            //ListViewAdapterMarca lista = new ListViewAdapterMarca(getActivity().getApplicationContext(), R.layout.spinner_list_item,listaMarcas);
            lv_lista_marcas.setAdapter(new ListViewAdapterMarca(getActivity().getApplicationContext(), listaMarcas));
            //lv_lista_marcas.setAdapter(lista);
        }
        else{
            sb.append("No hay registros");
            Toast.makeText(getActivity().getApplicationContext(),"No hay registros",Toast.LENGTH_SHORT).show();
        }

    } // end method mostrarEntradas

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public String cargarImagen(Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        File myPath = new File(dirImages, nombre);

        String ultimatePath=(dirImages+"/"+nombre);

        this.tv_url_marca.setText(ultimatePath);
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.PNG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return myPath.getAbsolutePath();
    }

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

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
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uriFromPath));
            Bitmap b = Bitmap.createScaledBitmap(bitmap,48,48,true);
            bitmap=b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Drawable d =new BitmapDrawable(getResources(),bitmap);
        iv_imagen_marca.setImageBitmap(bitmap);

        String nombre = realPath;
        String []cadenas = nombre.split("[/]");
        nombre = cadenas[(cadenas.length)-1];

        String ruta = cargarImagen(getActivity().getApplicationContext(), nombre, bitmap);
        tv_url_marca.setText(ruta);

        Log.d("HMKCODE", "Build.VERSION.SDK_INT:" + sdk);
        Log.d("HMKCODE", "URI Path:"+uriPath);
        Log.d("HMKCODE", "Real Path: "+realPath);
    }

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if(resCode == Activity.RESULT_OK && data != null){
            String realPath;
            if (Build.VERSION.SDK_INT < 11)
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(getActivity().getApplicationContext(), data.getData());
            else if (Build.VERSION.SDK_INT < 19)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(getActivity().getApplicationContext(), data.getData());
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(getActivity().getApplicationContext(), data.getData());
            setTextViews(Build.VERSION.SDK_INT, data.getData().getPath(),realPath);
        }
    }

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void onClicka() {

        // 1. on Upload click call ACTION_GET_CONTENT intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 2. pick image only
        intent.setType("image/*");
        // 3. start activity
        startActivityForResult(intent, 0);
        // define onActivityResult to do something with picked image
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

    public void dialogo() {

        final String[] items = {"Editar", "Eliminar","Cancelar"};

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Acción")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                          seleccion=items[item];

                        if("Editar".equals(seleccion)){editar();}
                        if("Eliminar".equals(seleccion)){eliminar();}
                        if("Cancelar".equals(seleccion)){           }
                    }
                });
        builder.show();
    }

    public void dialogoConfirmacionEliminar() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage("¿Confirma Eliminar Esta Marca?")
                .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        eliminarMarca();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        crear();
                        dialog.cancel();
                    }
                });
        builder.show();
    }

} // fin de la clase MarcaFragment