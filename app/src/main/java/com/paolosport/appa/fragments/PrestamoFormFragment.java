package com.paolosport.appa.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.persistencia.entities.Persona;
import com.paolosport.appa.spinnerLocalPaquete.SpinnerAdapterLocal;
import com.paolosport.appa.spinnerMarcaPaquete.SpinnerAdapterMarca;
import com.paolosport.appa.spinnerPersonaPaquete.SpinnerAdapterPersonaLista;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrestamoFormFragment extends Fragment {

    View view;

    private Spinner sp_marca,   sp_local, sp_persona;
    String id_marca,url_marca;


    private ImageView imageView;

    AdminSQLiteOpenHelper helper;
    MarcaDAO marcaDAO;
    LocalDAO localDAO;
    PersonaDAO personaDAO;

    public PrestamoFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        super.onActivityCreated( savedInstanceState );

        if(view != null){
        }
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState ) {



        helper     = new AdminSQLiteOpenHelper( getActivity().getApplicationContext() );
        localDAO   = new LocalDAO( getActivity().getApplicationContext(), helper );
        marcaDAO   = new MarcaDAO( getActivity().getApplicationContext(), helper );
        personaDAO = new PersonaDAO( getActivity().getApplicationContext(), helper );
        view= inflater.inflate(R.layout.fragment_prestamo_form, container, false);

        DatosPorDefecto();
        // Inflate the layout for this fragment

        return view;
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
    }

    private void DatosPorDefecto() {
        marcaDAO.open();
        localDAO.open();
        personaDAO.open();

        final ArrayList<Marca> listaMarcas = marcaDAO.retrieveAll();
        final ArrayList<Local> listaLocales = localDAO.retrieveAll();
        final ArrayList<Persona> listaPersonas = personaDAO.retrieveAll();


        sp_marca= (android.widget.Spinner)view.findViewById(R.id.sp_marca_prestamo);
        sp_local= (android.widget.Spinner)view.findViewById(R.id.sp_local_prestamo);
        sp_persona= (android.widget.Spinner)view.findViewById(R.id.sp_persona_prestamo);

        sp_marca.setAdapter(new SpinnerAdapterMarca(getActivity().getApplicationContext(),listaMarcas));
        sp_local.setAdapter(new SpinnerAdapterLocal(getActivity().getApplicationContext(),listaLocales));
        sp_persona.setAdapter(new SpinnerAdapterPersonaLista(getActivity().getApplicationContext(),listaPersonas));

        marcaDAO.close();
        localDAO.close();
        personaDAO.close();

        sp_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                id_marca=((Marca) adapterView.getItemAtPosition(position)).getId();
                url_marca=((Marca) adapterView.getItemAtPosition(position)).getUrl();

                //et_nombre_marca_editado.setText(((Marca) adapterView.getItemAtPosition(position)).getNombre());

                String url = url_marca.toString();

                Bitmap bmImg = BitmapFactory.decodeFile(listaMarcas.get(position).getUrl());
                Pattern pat = Pattern.compile(".*/.*");
                Matcher mat = pat.matcher(url);

                /*if (mat.matches()) {
                    iv_editada.setImageBitmap(bmImg);
                } else {
                    int path = getResources().getIdentifier(url,"drawable", "com.paolosport.appa");
                    iv_editada.setImageResource(path);
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

        sp_local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //id_marca=((Marca) adapterView.getItemAtPosition(position)).getId();
                //url_marca=((Marca) adapterView.getItemAtPosition(position)).getUrl();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }//end method DatosPorDefecto

}
