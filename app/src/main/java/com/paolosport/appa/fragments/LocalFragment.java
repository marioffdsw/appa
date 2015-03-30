package com.paolosport.appa.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.paolosport.appa.ListViewAdapters.LocalAdapter;
import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.spinnerLocalPaquete.SpinnerAdapterLocal;

import java.util.ArrayList;;

public class LocalFragment extends Fragment {

    // private OnFragmentInteractionListener mListener;
    AdminSQLiteOpenHelper helper;
    LocalDAO localDAO;
    private String id_local;
    private LinearLayout ll_listar_local,
                         ll_editar_local,
                         ll_eliminar_local,
                         ll_crear_local;
    private View view;
    private Button  btn_listar,
                    btn_eliminar,
                    btn_crear,
                    btn_editar,
                    btn_eliminar_local,
                    btn_crear_local,
                    btn_editar_local;

    private Spinner sp_local,
                    sp_local2;

    private TextView tv_id_local;

    private EditText et_nombre_local,
                     et_nombre_local_editado;
    private ListView lv_lista_locales;

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
        btn_listar = (Button)view.findViewById(R.id.button4);
        btn_eliminar = (Button)view.findViewById(R.id.button3);
        btn_editar = (Button)view.findViewById(R.id.button2);
        btn_crear = (Button)view.findViewById(R.id.button);

        btn_eliminar_local = (Button)view.findViewById(R.id.btn_eliminar_local);
        btn_editar_local = (Button)view.findViewById(R.id.btn_editar_local);
        btn_crear_local = (Button)view.findViewById(R.id.btn_crear_local);

        tv_id_local = (TextView)view.findViewById(R.id.tv_id_local);
        et_nombre_local = (EditText)view.findViewById(R.id.et_nombre_local);
        et_nombre_local_editado = (EditText)view.findViewById(R.id.et_nombre_local_editado);
        lv_lista_locales = (ListView)view.findViewById(R.id.lv_lista_locales);

        ll_crear_local = (LinearLayout)view.findViewById(R.id.ll_crear_local);
        ll_listar_local = (LinearLayout)view.findViewById(R.id.ll_listar_local);
        ll_editar_local = (LinearLayout)view.findViewById(R.id.ll_editar_local);
        ll_eliminar_local = (LinearLayout)view.findViewById(R.id.ll_eliminar_local);

        DatosPorDefecto();

        ll_listar_local.setVisibility(View.INVISIBLE);
        ll_eliminar_local.setVisibility(View.GONE);
        ll_crear_local.setVisibility(View.GONE);
        ll_editar_local.setVisibility(View.GONE);

        //-----------------------
        btn_eliminar_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarLocal();
            }
        });
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
        btn_listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listar();
            }
        });
        //-----------------------
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        //-----------------------
        });btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar();
            }
        //-----------------------
        });btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crear();
            }
        });
        //-----------------------

        return view;


    } // fin del metodo onCreateView

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    } // fin del metodo onAttach

    //?????????????????????????????????????????????????????????????????????????
    public void listar(){
        if(ll_listar_local.getVisibility()== view.INVISIBLE)
        {ll_listar_local.setVisibility(View.VISIBLE);}
        else{ll_listar_local.setVisibility(View.INVISIBLE);}
        mostrarEntradas();
        }
    //?????????????????????????????????????????????????????????????????????????
    public void eliminar(){
        ll_eliminar_local.setVisibility(View.VISIBLE);
        ll_crear_local.setVisibility(View.GONE);
        ll_editar_local.setVisibility(View.GONE);
    }
    //?????????????????????????????????????????????????????????????????????????
    public void editar(){
        ll_eliminar_local.setVisibility(View.GONE);
        ll_crear_local.setVisibility(View.GONE);
        ll_editar_local.setVisibility(view.VISIBLE);
    }
    //?????????????????????????????????????????????????????????????????????????
    public void crear(){
        ll_eliminar_local.setVisibility(View.GONE);
        ll_crear_local.setVisibility(View.VISIBLE);
        ll_editar_local.setVisibility(View.GONE);

        String id = Integer.toString(listaLocales.size()+1);
        tv_id_local.setText((id));

    }
    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void editarLocal(){
        String aux = id_local.toString();
        String nombre = et_nombre_local_editado.getText().toString();

        if((nombre == null) || (nombre.equals(""))){
            Toast.makeText(getActivity().getApplicationContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
        }
        else {
            Local local = new Local(aux, nombre);
            localDAO.open();
            try {
                localDAO.update(local);
                localDAO.close();
                et_nombre_local_editado.setText("");
                DatosPorDefecto();
                Toast.makeText(getActivity().getApplicationContext(), "Registro Editado", Toast.LENGTH_SHORT).show();

                if (ll_listar_local.getVisibility() == view.VISIBLE) {
                    ll_listar_local.setVisibility(View.INVISIBLE);
                    listar();
                }
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

            if(ll_listar_local.getVisibility()== view.VISIBLE)
              {   ll_listar_local.setVisibility(View.INVISIBLE);
                  listar();
              }
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "Registro NO Eliminado", Toast.LENGTH_SHORT).show();
        }

    } // end method eliminarLocal

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void crearLocal(){

        String id = tv_id_local.getText().toString();
        String nombre = et_nombre_local.getText().toString();
        if((nombre == null) || (nombre.equals(""))){
            Toast.makeText(getActivity().getApplicationContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
        }
        else{
        Local local = new Local( id, nombre );
        try {
            localDAO.open();
            localDAO.create(local);
            localDAO.close();
            Toast.makeText(getActivity().getApplicationContext(), "Registro Creado", Toast.LENGTH_SHORT).show();

            int auxId= Integer.parseInt(id)+1;
            id = String.valueOf(auxId);
            tv_id_local.setText(id);
            et_nombre_local.setText("");
            DatosPorDefecto();

            if (ll_listar_local.getVisibility() == view.VISIBLE) {
                ll_listar_local.setVisibility(View.INVISIBLE);
                listar();
            }
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

        sp_local= (android.widget.Spinner) view.findViewById(R.id.sp_local);
        sp_local2= (android.widget.Spinner) view.findViewById(R.id.sp_local2);

        sp_local.setAdapter(new SpinnerAdapterLocal(getActivity().getApplicationContext(),listaLocales));
        sp_local2.setAdapter(new SpinnerAdapterLocal(getActivity().getApplicationContext(),listaLocales));

        localDAO.close();
        sp_local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                id_local=((Local) adapterView.getItemAtPosition(position)).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

        sp_local2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                id_local=((Local) adapterView.getItemAtPosition(position)).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });


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
        StringBuilder sb = new StringBuilder();

        if(listaLocales!=null  && !listaLocales.isEmpty()){
            LocalAdapter lista = new LocalAdapter(getActivity().getApplicationContext(), R.layout.local_item,listaLocales);
            lv_lista_locales.setAdapter(lista);
        }
        else{
            sb.append("No hay registros");
            Toast.makeText(getActivity().getApplicationContext(),"No hay registros",Toast.LENGTH_SHORT).show();
        }

    } // end method mostrarEntradas

} // fin de la clase LocalFragment
