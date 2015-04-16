package com.paolosport.appa.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.ListViewAdapters.ListViewAdapterLocal;
import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.entities.Local;

import java.util.ArrayList;;

public class LocalFragment extends Fragment {

    // private OnFragmentInteractionListener mListener;
    AdminSQLiteOpenHelper helper;
    LocalDAO localDAO;

    private LinearLayout ll_crear_local,
                         texto_animado2;
    private View view;
    private Button  btn_crear_local,
                    btn_editar_local,
                    btn_cancelar_local;

    private EditText et_nombre_local;
    private ListView lv_lista_locales;
    private TextView tv_nombre_local;

    private String id_local,nombre_local;

    String seleccion;
    int posicion;

    ArrayList<Local> listaLocales;
    public LocalFragment() {
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
                             Bundle savedInstanceState) {
        //data base
        helper = new AdminSQLiteOpenHelper( getActivity().getApplicationContext() );
        localDAO = new LocalDAO(getActivity().getApplicationContext(), helper );

        localDAO.open();
        listaLocales = localDAO.retrieveAll();
        localDAO.close();

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_local, container, false);

        btn_editar_local = (Button)view.findViewById(R.id.btn_editar_local);
        btn_crear_local = (Button)view.findViewById(R.id.btn_crear_local);
        btn_cancelar_local = (Button)view.findViewById(R.id.btn_cancelar_local);


        et_nombre_local = (EditText)view.findViewById(R.id.et_nombre_local);
        lv_lista_locales = (ListView)view.findViewById(R.id.lv_lista_locales);
        tv_nombre_local= (TextView)view.findViewById(R.id.tv_nombre_local);

        ll_crear_local = (LinearLayout)view.findViewById(R.id.ll_crear_local);
        texto_animado2 = (LinearLayout)view.findViewById(R.id.texto_animado2);

        ll_crear_local.setVisibility(View.VISIBLE);
        DatosPorDefecto();
        mostrarEntradas();

        //-----------------------
        btn_crear_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearLocal();
            }
        });
        //-----------------------
        btn_editar_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarLocal();
            }
        });
        //-----------------------
        btn_cancelar_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crear();

            }
        });
        //-----------------------++

        lv_lista_locales.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posicion = position;

                id_local=((Local) parent.getItemAtPosition(position)).getId();
                nombre_local=((Local) parent.getItemAtPosition(position)).getNombre();

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

    //????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????
    public void eliminar(){
        dialogoConfirmacionEliminar();
    }

    //?????????????????????????????????????????????????????????????????????????
    public void crear(){

        ll_crear_local.setVisibility(View.VISIBLE);
        btn_crear_local.setVisibility(View.VISIBLE);
        btn_editar_local.setVisibility(View.GONE);

        tv_nombre_local.setText("Ingrese Nombre:");
        et_nombre_local.setText("");

    }
    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????
    public void editar(){

        tv_nombre_local.setText("Ingrese Nuevo Nombre:");
        et_nombre_local.setText(nombre_local);

        btn_crear_local.setVisibility(View.GONE);
        btn_editar_local.setVisibility(View.VISIBLE);
    }
    public void editarLocal(){

        tv_nombre_local.setText("Ingrese Nuevo Nombre:");

        String aux = id_local.toString();
        String nombre = et_nombre_local.getText().toString();

        if((nombre == null) || (nombre.equals(""))){
            Toast.makeText(getActivity().getApplicationContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
        }
        else {
            Local local = new Local(aux, nombre);
            localDAO.open();
            try {
                localDAO.update(local);
                localDAO.close();
                et_nombre_local.setText("");
                DatosPorDefecto();
                Toast.makeText(getActivity().getApplicationContext(), "Registro Editado", Toast.LENGTH_SHORT).show();
                crear();
                   mostrarEntradas();
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Registro NO Editado", Toast.LENGTH_SHORT).show();
            }
        }//FinElse
    } // end method editarLocal

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void eliminarLocal(){
        String aux = id_local.toString();
        Local local;
        localDAO.open();
        try {
            local = localDAO.retrieve(aux);
            localDAO.delete(local);
            localDAO.close();
            DatosPorDefecto();
            Toast.makeText(getActivity().getApplicationContext(),"Registro Eliminado",Toast.LENGTH_SHORT).show();
                  mostrarEntradas();
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "Registro NO Eliminado", Toast.LENGTH_SHORT).show();
        }

    } // end method eliminarLocal

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void crearLocal(){

        String nombre = et_nombre_local.getText().toString();
        if((nombre == null) || (nombre.equals(""))){
            Toast.makeText(getActivity().getApplicationContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
        }
        else{
        Local local = new Local( nombre );
        try {
            localDAO.open();
            localDAO.create(local);
            localDAO.close();

            et_nombre_local.setText("");
            DatosPorDefecto();

            Toast.makeText(getActivity().getApplicationContext(), "Registro Creado", Toast.LENGTH_SHORT).show();
            mostrarEntradas();

        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "Registro NO Creado", Toast.LENGTH_SHORT).show();}
        }//finElse
    }//end method crearLocal

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????
    private void DatosPorDefecto() {
        localDAO.open();
        ArrayList<Local> listaLocales = localDAO.retrieveAll();
        localDAO.close();
    }//end method DatosPorDefecto

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void mostrarEntradas(){
        ArrayList<Local> listaLocales;
        localDAO.open();
        listaLocales = localDAO.retrieveAll();
        localDAO.close();
        try{
            localDAO.open();
            DatosPorDefecto();
            localDAO.close();
        }
        catch (Exception e){e.printStackTrace();}

        if(listaLocales!=null  && !listaLocales.isEmpty()){
            ListViewAdapterLocal lista = new ListViewAdapterLocal(getActivity().getApplicationContext(), R.layout.local_item_lv,listaLocales);
            lv_lista_locales.setAdapter(lista);
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(),"No hay registros",Toast.LENGTH_SHORT).show();
        }

    } // end method mostrarEntradas


    //888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
    //888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888

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

        texto_animado2.setLayoutAnimation(controller);
        texto_animado2.startAnimation(animation);
    }

    //888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
    //888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888

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

    //888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
    //888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888

    public void dialogoConfirmacionEliminar() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage("¿Confirma Eliminar Esta Marca?")
                .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        eliminarLocal();
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

} // fin de la clase LocalFragment
