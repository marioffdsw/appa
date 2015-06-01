package com.paolosport.appa.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.paolosport.appa.ListViewAdapters.ListViewAdapterLocal;
import com.paolosport.appa.R;
import com.paolosport.appa.activities.MainActivity;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.dao.PrestamoDAO;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.persistencia.entities.Prestamo;
import com.paolosport.appa.ui.PrestamoAdapter;
import java.util.ArrayList;
import java.util.Calendar;

public class PrestamoLstFragment extends Fragment implements PrestamoAdapter.PrestamosSubject {

    private SearchViewCompat searchView;
    private Context context;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    private PersonaDAO personaDAO;
    private MarcaDAO marcaDAO;
    private LocalDAO localDAO;
    private PrestamoDAO prestamoDAO;
    private RelativeLayout opciones;
    String seleccion;

    private PrestamoAdapter adapter;
    private View view;

    private ListView listPrestamos;
    private ArrayList<Prestamo> lstPrestamos;
    Dialog customDialog = null;

    private int mYear;
    private int mMonth;
    private int mDay;

    private int año;
    private int mes;
    private int dia;


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

        // Get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


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
        listPrestamos.setSelector(R.drawable.selection_prestamos);
        listPrestamos.setAdapter(adapter);

        configurarLista();

        opciones = (RelativeLayout) view.findViewById( R.id.opciones );
        view.findViewById( R.id.btnVendido ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.vender(prestamoDAO);
                deselecionarPrestamos();
                alternarOpciones();
            }
        });
        view.findViewById( R.id.btnDevuelto ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.devolver(prestamoDAO);
                deselecionarPrestamos();
                alternarOpciones();
            }
        });

        view.findViewById( R.id.btnPrestado ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.prestar(prestamoDAO);
                deselecionarPrestamos();
                alternarOpciones();
            }
        });
        // Inflate the layout for this fragment


        mDrawerLayout = (DrawerLayout) view.findViewById( R.id.drawer_layout );
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,  GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                MainActivity activity =  (MainActivity) getActivity();
                ActionBar actionBar = activity.getSupportActionBar();
                actionBar.setTitle("Appa");

                getActivity().invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }

            public void onDrawerOpened(View drawerView) {
                MainActivity activity =  (MainActivity) getActivity();
                ActionBar actionBar = activity.getSupportActionBar();
                actionBar.setTitle("Configura la Busqueda");
                getActivity().invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }
        };


        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        Button button = (Button) view.findViewById( R.id.button );

        FrameLayout frameLayout = (FrameLayout) view.findViewById( R.id.frame_filtros );
        frameLayout.setOnClickListener(null);

        localDAO.open();
        final ArrayList<Local> lstLocales = localDAO.retrieveAll();
        localDAO.close();

        ListView filtroLocales = (ListView) view.findViewById( R.id.filtro_locales );
        filtroLocales.setAdapter( new ListViewAdapterLocal( context, R.layout.local_item_lv, lstLocales ));
        filtroLocales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Local local = lstLocales.get( position );
                PrestamoAdapter.FilterWithOptions filter = (PrestamoAdapter.FilterWithOptions) adapter.getFilter();
                filter.setParameters( null );
                filter.filtrarPorMarca = false;
                filter.filtrarPorLocal = false;
                filter.filtrarPorEmpleado = false;
                filter.filtrarPorNombreLocal = true;
                filter.filtrarPorOrigen = false;
                filter.filtrarPorDescripcion = false;
                filter.filter( local.getNombre() );
                deselecionarPrestamos();
                alternarOpciones();
                filter.filtrarPorMarca = true;
                filter.filtrarPorLocal = true;
                filter.filtrarPorEmpleado = true;
                filter.filtrarPorNombreLocal = false;
                filter.filtrarPorOrigen = true;
                filter.filtrarPorDescripcion = true;
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        deselecionarPrestamos();

    } // end method onStart

    @Override
    public void onResume(){
        super.onResume();
        configurarLista();
    } // end method onResume

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        // TODO des-registrar el broadcast receiver
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        context = activity;
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.prestamoLstFragment = this;
    }

    @Override
    public void onDetach() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.prestamoLstFragment = null;
        super.onDetach();
    }

    @Override
    public ArrayList<Prestamo> getListPrestamos() {
        return lstPrestamos;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_lst_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item=menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        View searchView =SearchViewCompat.newSearchView(getActivity());
        if (searchView != null) {
           SearchViewCompat.setOnQueryTextListener(searchView,new SearchViewCompat.OnQueryTextListenerCompat() {
                @Override /** This is a demo of how to use the filter, REMEMBER setFilter() to create a new filter  */
                public boolean onQueryTextChange(String newText) {
                    PrestamoAdapter.FilterWithOptions filter = (PrestamoAdapter.FilterWithOptions) adapter.getFilter();
                    filter.setParameters( null );
                    filter.filter( newText );
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
            case R.id.calendario:
                dialogoCalendario();
                //customDialog=new DatePickerDialog(context, mDateSetListener, mYear, mMonth,mDay);
                //customDialog.show();
        } // end switch

        return super.onOptionsItemSelected(item);

    }
    //para mostrar el resultado de la fecha
    private void updateDisplay() {
        Toast.makeText(context,mDay+"/"+mMonth+"/"+mYear,Toast.LENGTH_SHORT).show();
       /* mDateDisplay.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("-").append(mDay).append("-")
                .append(mYear).append(" "));*/
    }

    private void dialogoCalendario() {
        customDialog = new Dialog(getActivity());
        //customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setTitle("Seleccione Modo");
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_calendario);
        customDialog.show();

        final ImageView iv_dia = (ImageView)customDialog.findViewById(R.id.iv_dia);
        final ImageView iv_mes = (ImageView)customDialog.findViewById(R.id.iv_mes);
        final ImageView iv_rango = (ImageView)customDialog.findViewById(R.id.iv_rango);

        iv_dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                customDialog =  new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth,mDay);
                customDialog.show();
            }
        });
        iv_mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                customDialog =  new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth,mDay);
                customDialog.show();
            }
        });

        iv_rango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                dialogoRango();
            }
        });
    }

    private void dialogoRango() {
        customDialog = new Dialog(getActivity());
        //customDialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        customDialog.setTitle("Seleccione El Rango");
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_rango);
        customDialog.show();

        final DatePicker dp_inicio = (DatePicker)customDialog.findViewById(R.id.dp_inicio);
        final DatePicker dp_fin    = (DatePicker)customDialog.findViewById(R.id.dp_fin);
        final FrameLayout fl_listo_rango=(FrameLayout)customDialog.findViewById(R.id.fl_listo_rango);

        fl_listo_rango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();

                mDay    =dp_inicio.getDayOfMonth();
                mMonth  =dp_inicio.getMonth();
                mYear   =dp_inicio.getYear();

                dia =dp_fin.getDayOfMonth();
                mes =dp_fin.getMonth();
                año =dp_fin.getYear();

                Toast.makeText(context,mDay+"/"+mMonth+"/"+mYear+" --- "+dia+"/"+mes+"/"+año,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

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

    public void dialogo( final String buscarReferencia, final int position ) {

        final String[] items = {"Buscar Referencia", "Eliminar","Cancelar"};

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Acción")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        seleccion=items[item];

                        if("Buscar Referencia".equals(seleccion)){buscarReferencia( buscarReferencia );}
                        if("Eliminar".equals(seleccion)){dialogoEliminar( position );}
                        if("Cancelar".equals(seleccion)){           }
                    }
                });
        builder.show();
    }

    public void dialogoEliminar( final int position ) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage("¿Realmente deseas eliminar este registro?")
                .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliminar(position);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public void buscarReferencia( String buscarReferencia ){
        Intent i = new Intent("android.intent.action.VIEW",
                Uri.parse("http://www.google.com" + "/search?q=" + buscarReferencia + "&source=lnms&tbm=isch&sa=X&ei=YDtZVaCgGNLHsQTGrIHwDQ&ved=0CAcQ_AUoAQ&biw=1247&bih=580"));
        startActivity(i);
    }

    public void eliminar( int position ){
        adapter.eliminar( position, prestamoDAO );
    } // end method position

    public void configurarLista(){
        SharedPreferences preferences = getActivity().getSharedPreferences("datos",
                Context.MODE_PRIVATE);
        boolean sesion = false;
        deselecionarPrestamos();
        sesion= preferences.getBoolean("sesion",sesion);
        if ( sesion == true ) {
            listPrestamos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listPrestamos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.selecionarElementos(parent, view, position, id);
                    alternarOpciones();
                }
            });

            listPrestamos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(((Prestamo) parent.getItemAtPosition(position)).getMarca().getNombre()).toString();
                    sb.append("+");
                    sb.append(((Prestamo) parent.getItemAtPosition(position)).getCodigo()).toString();
                    String referencia = sb.toString();
                    dialogo(referencia, position);
                    return false;
                }
            });
        }
        else {
            listPrestamos.setChoiceMode(ListView.CHOICE_MODE_NONE);
            deselecionarPrestamos();
            listPrestamos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

            listPrestamos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                   return false;
                }
            });
        }
    }
}