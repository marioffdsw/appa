package com.paolosport.appa.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
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

public class PrestamoLstFragment extends Fragment {

    private Context context;

    private PersonaDAO personaDAO;
    private MarcaDAO marcaDAO;
    private LocalDAO localDAO;
    private PrestamoDAO prestamoDAO;

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
        listPrestamos.setAdapter( adapter );
        adapter.setNotifyOnChange( true );

        SharedPreferences preferences = getActivity().getSharedPreferences("datos",
            Context.MODE_PRIVATE);
        boolean sesion = false;
        sesion= preferences.getBoolean("sesion",sesion);
        if ( sesion == true ) {
            listPrestamos.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
            listPrestamos.setMultiChoiceModeListener( new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged( ActionMode mode, int position, long id, boolean checked ) {
                    final int checkedCount = listPrestamos.getCheckedItemCount();


                    getActivity().getActionBar().setTitle( checkedCount + " " +
                        "Seleccionados" );

                }

                @Override
                public boolean onCreateActionMode( ActionMode mode, Menu menu ) {
                    mode.getMenuInflater().inflate( R.menu.fragment_lst_menu, menu );
                    return false;
                }

                @Override
                public boolean onPrepareActionMode( ActionMode mode, Menu menu ) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked( ActionMode mode, MenuItem item ) {
                    return false;
                }

                @Override
                public void onDestroyActionMode( ActionMode mode ) {
                }
            } );
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        context = activity;
    }
}
