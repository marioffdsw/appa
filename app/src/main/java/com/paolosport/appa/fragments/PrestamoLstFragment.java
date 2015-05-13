package com.paolosport.appa.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.widget.SearchViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.dao.PrestamoDAO;
import com.paolosport.appa.persistencia.entities.Prestamo;
import com.paolosport.appa.ui.PrestamoAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.Inflater;

public class PrestamoLstFragment extends Fragment implements PrestamoAdapter.PrestamosSubject {

    private SearchViewCompat searchView;
    private Context context;

    private PersonaDAO personaDAO;
    private MarcaDAO marcaDAO;
    private LocalDAO localDAO;
    private PrestamoDAO prestamoDAO;
    private RelativeLayout opciones;

    private PrestamoAdapter adapter;
    private View view;

    private ListView listPrestamos;
    private ArrayList<Prestamo> lstPrestamos;

    public PrestamoLstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        view = inflater.inflate( R.layout.fragment_prestamo_lst, container, false );

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper( context.getApplicationContext() );
        personaDAO = new PersonaDAO( context.getApplicationContext(), adminSQLiteOpenHelper );
        marcaDAO = new MarcaDAO( context.getApplicationContext(), adminSQLiteOpenHelper );
        localDAO = new LocalDAO( context.getApplicationContext(), adminSQLiteOpenHelper );

        prestamoDAO = new PrestamoDAO( context.getApplicationContext(), adminSQLiteOpenHelper );
        prestamoDAO.setLocalDAO( localDAO );
        prestamoDAO.setMarcaDAO( marcaDAO );
        prestamoDAO.setPersonaDAO( personaDAO );

        prestamoDAO.open();
        lstPrestamos = prestamoDAO.retrieveAll();
        prestamoDAO.close();

        adapter = new PrestamoAdapter( context, R.layout.item_lst_prestamos, lstPrestamos );

        // si no hay datos sobre prestamos se notifica al usuario
        if( lstPrestamos == null || lstPrestamos.size() == 0 ) {
            Toast.makeText( context, "no hay datos", Toast.LENGTH_SHORT ).show();
        }

        if( lstPrestamos != null || lstPrestamos.size() != 0 ) {

            view.findViewById( R.id.layoutOops ).setVisibility( View.INVISIBLE );
            view.findViewById( R.id.lstPrestamos ).setVisibility( View.VISIBLE );
        }

        // se carga los datos que existan sobre los prestamos
        Activity activity = ( Activity ) context;
        listPrestamos = ( ListView ) view.findViewById( R.id.lstPrestamos );
        listPrestamos.setAdapter(adapter);

        SharedPreferences preferences = getActivity().getSharedPreferences("datos",
            Context.MODE_PRIVATE);
        boolean sesion = false;
        sesion= preferences.getBoolean("sesion",sesion);
        if ( sesion == true ) {
            listPrestamos.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
            listPrestamos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.selecionarElementos( parent, view, position, id );
                    adapter.showToast();
                    alternarOpciones();
                }
            });
        }

        opciones = (RelativeLayout) view.findViewById( R.id.opciones );
        view.findViewById( R.id.btnVendido ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.vender( prestamoDAO );
            }
        });
        view.findViewById( R.id.btnDevuelto ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.devolver( prestamoDAO );
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        context = activity;
    }

    @Override
    public ArrayList<Prestamo> getListPrestamos() {
        return lstPrestamos;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_lst_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
        MenuItem item=menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        View searchView =SearchViewCompat.newSearchView(getActivity());
        if (searchView != null) {
           SearchViewCompat.setOnQueryTextListener(searchView,new SearchViewCompat.OnQueryTextListenerCompat() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    deselecionarPrestamos();
                    alternarOpciones();
                    return true;
                }
           });
           item.setActionView(searchView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch ( item.getItemId() ){
            case R.id.ordenarPorEmpleado:
                adapter.ordenarPorEmpleado();
                deselecionarPrestamos();
                alternarOpciones();
                break;
            case R.id.ordenarPorEstado:
                adapter.ordenarPorEstado();
                deselecionarPrestamos();
                alternarOpciones();
                break;
            case R.id.ordenarPorFecha:
                adapter.ordenarPorFecha();
                deselecionarPrestamos();
                alternarOpciones();
                break;
            case R.id.ordenarPorLocal:
                adapter.ordenarPorLocal();
                deselecionarPrestamos();
                alternarOpciones();
                break;
            case R.id.ordenarPorMarca:
                adapter.ordenarPorMarca();
                deselecionarPrestamos();
                alternarOpciones();
                break;
            case R.id.ordenarPorOrigen:
                adapter.ordenarPorOrigen();
                deselecionarPrestamos();
                alternarOpciones();
                break;
        } // end switch

        return super.onOptionsItemSelected(item);

    }

    public void deselecionarPrestamos(){
        listPrestamos.requestLayout();
        listPrestamos.clearChoices();
        adapter.borrarSeleccion();
    } // end method deselecionarPrestamos

    public void alternarOpciones(){
        int seleccionados = adapter.selectedNumber();

        if( seleccionados > 0 ){
            mostrarOpciones();
        }
        else{
            ocultarOpciones();
        }
    } // end method alternarOpciones

    public void mostrarOpciones(){
        opciones.setVisibility( View.VISIBLE );
    } // end method mostrarOpciones

    public void ocultarOpciones(){
        opciones.setVisibility( View.GONE );

    } // end method ocultarOpciones

}
