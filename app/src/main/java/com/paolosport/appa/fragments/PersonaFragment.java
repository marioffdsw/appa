package com.paolosport.appa.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.paolosport.appa.R;

public class PersonaFragment extends Fragment {

    // private OnFragmentInteractionListener mListener;
    private LinearLayout ll_listar_local,
            ll_editar_local,
            ll_eliminar_local,
            ll_crear_local;
    private View view;
    private Button btn_listar,btn_eliminar,btn_crear,btn_editar;
    public PersonaFragment() {
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
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_persona, container, false);
        btn_listar = (Button)view.findViewById(R.id.button4);
        btn_eliminar = (Button)view.findViewById(R.id.button3);
        btn_editar = (Button)view.findViewById(R.id.button2);
        btn_crear = (Button)view.findViewById(R.id.button);
        ll_crear_local = (LinearLayout)view.findViewById(R.id.ll_crear_local);
        ll_listar_local = (LinearLayout)view.findViewById(R.id.ll_listar_local);
        ll_editar_local = (LinearLayout)view.findViewById(R.id.ll_editar_local);
        ll_eliminar_local = (LinearLayout)view.findViewById(R.id.ll_eliminar_local);

        ll_listar_local.setVisibility(View.INVISIBLE);
        ll_eliminar_local.setVisibility(View.GONE);
        ll_crear_local.setVisibility(View.GONE);
        ll_editar_local.setVisibility(View.GONE);

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
    }
    //?????????????????????????????????????????????????????????????????????????
} // fin de la clase LocalFragment
