package com.paolosport.appa.fragments;

import android.app.Activity;
import android.net.Uri;
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

import com.paolosport.appa.ListViewAdapters.MarcaAdapter;
import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.spinnerMarcaPaquete.SpinnerAdapterMarca;


import java.util.ArrayList;

public class MarcaFragment extends Fragment {

    // private OnFragmentInteractionListener mListener;
    AdminSQLiteOpenHelper helper;
    MarcaDAO marcaDAO;
    private String id_marca;
    private LinearLayout ll_listar_marca,
            ll_editar_marca,
            ll_eliminar_marca,
            ll_crear_marca;
    private View view;
    private Button  btn_listar,
            btn_eliminar,
            btn_crear,
            btn_editar,
            btn_eliminar_marca,
            btn_crear_marca,
            btn_editar_marca;

    private Spinner     sp_marca,   sp_marca2;
    private TextView    tv_id_marca,   tv_url_marca;
    private EditText    et_nombre_marca,   et_nombre_marca_editado;
    private ListView    lv_lista_marcas;

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
                             Bundle savedInstanceState) {
        //data base
        helper = new AdminSQLiteOpenHelper( getActivity().getApplicationContext() );
        marcaDAO = new MarcaDAO(getActivity().getApplicationContext(), helper );

        marcaDAO.open();
        listaMarcas = marcaDAO.retrieveAll();
        marcaDAO.close();

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_marca, container, false);
        btn_listar = (Button)view.findViewById(R.id.button4);
        btn_eliminar = (Button)view.findViewById(R.id.button3);
        btn_editar = (Button)view.findViewById(R.id.button2);
        btn_crear = (Button)view.findViewById(R.id.button);

        btn_eliminar_marca = (Button)view.findViewById(R.id.btn_eliminar_marca);
        btn_editar_marca = (Button)view.findViewById(R.id.btn_editar_marca);
        btn_crear_marca = (Button)view.findViewById(R.id.btn_crear_marca);

        tv_id_marca = (TextView)view.findViewById(R.id.tv_id_marca);
        tv_url_marca = (TextView)view.findViewById(R.id.tv_url_marca);
        et_nombre_marca = (EditText)view.findViewById(R.id.et_nombre_marca);
        et_nombre_marca_editado = (EditText)view.findViewById(R.id.et_nombre_marca_editado);
        lv_lista_marcas = (ListView)view.findViewById(R.id.lv_lista_marcas);

        ll_crear_marca = (LinearLayout)view.findViewById(R.id.ll_crear_marca);
        ll_listar_marca = (LinearLayout)view.findViewById(R.id.ll_listar_marca);
        ll_editar_marca = (LinearLayout)view.findViewById(R.id.ll_editar_marca);
        ll_eliminar_marca = (LinearLayout)view.findViewById(R.id.ll_eliminar_marca);

        DatosPorDefecto();

        ll_listar_marca.setVisibility(View.INVISIBLE);
        ll_eliminar_marca.setVisibility(View.GONE);
        ll_crear_marca.setVisibility(View.GONE);
        ll_editar_marca.setVisibility(View.GONE);

        //-----------------------
        btn_eliminar_marca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarMarca();
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
        if(ll_listar_marca.getVisibility()== view.INVISIBLE)
        {ll_listar_marca.setVisibility(View.VISIBLE);}
        else{ll_listar_marca.setVisibility(View.INVISIBLE);}
        mostrarEntradas();
    }
    //?????????????????????????????????????????????????????????????????????????
    public void eliminar(){
        ll_eliminar_marca.setVisibility(View.VISIBLE);
        ll_crear_marca.setVisibility(View.GONE);
        ll_editar_marca.setVisibility(View.GONE);

    }
    //?????????????????????????????????????????????????????????????????????????
    public void editar(){
        ll_eliminar_marca.setVisibility(View.GONE);
        ll_crear_marca.setVisibility(View.GONE);
        ll_editar_marca.setVisibility(view.VISIBLE);
    }
    //?????????????????????????????????????????????????????????????????????????
    public void crear(){
        ll_eliminar_marca.setVisibility(View.GONE);
        ll_crear_marca.setVisibility(View.VISIBLE);
        ll_editar_marca.setVisibility(View.GONE);

        String id = Integer.toString(listaMarcas.size()+1);
        tv_id_marca.setText((id));

    }
    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void editarMarca(){
        String aux = id_marca.toString();
        String nombre = et_nombre_marca_editado.getText().toString();
        String url = tv_url_marca.getText().toString();

        if((nombre == null) || (nombre.equals(""))){
            Toast.makeText(getActivity().getApplicationContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
        }
        else {
            Marca marca = new Marca(aux, nombre,url);
            marcaDAO.open();
            try {
                marcaDAO.update(marca);
                marcaDAO.close();
                et_nombre_marca_editado.setText("");
                DatosPorDefecto();
                Toast.makeText(getActivity().getApplicationContext(), "Registro Editado", Toast.LENGTH_SHORT).show();

                if (ll_listar_marca.getVisibility() == view.VISIBLE) {
                    ll_listar_marca.setVisibility(View.INVISIBLE);
                    listar();
                }
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

            if(ll_listar_marca.getVisibility()== view.VISIBLE)
            {   ll_listar_marca.setVisibility(View.INVISIBLE);
                listar();
            }
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "Registro NO Eliminado", Toast.LENGTH_SHORT).show();
        }

    } // end method eliminarMarca

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void crearMarca(){

        String id = tv_id_marca.getText().toString();
        String nombre = et_nombre_marca.getText().toString();
        String url = tv_url_marca.getText().toString();

        if((nombre == null) || (nombre.equals(""))){
            Toast.makeText(getActivity().getApplicationContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
        }
        else{
            Marca marca = new Marca( id, nombre,url );
            try {
                marcaDAO.open();
                marcaDAO.create(marca);
                marcaDAO.close();
                Toast.makeText(getActivity().getApplicationContext(), "Registro Creado", Toast.LENGTH_SHORT).show();

                tv_id_marca.setText((Integer.parseInt(id))+1);
                et_nombre_marca.setText("");
                DatosPorDefecto();

                if (ll_listar_marca.getVisibility() == view.VISIBLE) {
                    ll_listar_marca.setVisibility(View.INVISIBLE);
                    listar();
                }
            }
            catch (Exception e){
                Toast.makeText(getActivity().getApplicationContext(), "Registro NO Creado", Toast.LENGTH_SHORT).show();}
        }//finElse
    }//end method crearMarca

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????
    private void DatosPorDefecto() {
        marcaDAO.open();
        ArrayList<Marca> listaMarcas = marcaDAO.retrieveAll();

        sp_marca= (android.widget.Spinner) view.findViewById(R.id.sp_marca);
        sp_marca2= (android.widget.Spinner) view.findViewById(R.id.sp_marca2);

        sp_marca.setAdapter(new SpinnerAdapterMarca(getActivity().getApplicationContext(),listaMarcas));
        sp_marca2.setAdapter(new SpinnerAdapterMarca(getActivity().getApplicationContext(),listaMarcas));

        marcaDAO.close();
        sp_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                id_marca=((Marca) adapterView.getItemAtPosition(position)).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

        sp_marca2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                id_marca=((Marca) adapterView.getItemAtPosition(position)).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });


    }//end method DatosPorDefecto

    //?????????????????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????

    public void mostrarEntradas(){
        ArrayList<Marca> listaMarcas;
        marcaDAO.open();
        listaMarcas = marcaDAO.retrieveAll();
        marcaDAO.close();
        try{
            marcaDAO.open();
            DatosPorDefecto();
            marcaDAO.close();
        }
        catch (Exception e){e.printStackTrace();}
        StringBuilder sb = new StringBuilder();

        if(listaMarcas!=null  && !listaMarcas.isEmpty()){
            MarcaAdapter lista = new MarcaAdapter(getActivity().getApplicationContext(), R.layout.marca_item,listaMarcas);
            lv_lista_marcas.setAdapter(lista);
        }
        else{
            sb.append("No hay registros");
            Toast.makeText(getActivity().getApplicationContext(),"No hay registros",Toast.LENGTH_SHORT).show();
        }

    } // end method mostrarEntradas

} // fin de la clase MarcaFragment