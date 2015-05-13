package com.paolosport.appa.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.persistencia.dao.PrestamoDAO;
import com.paolosport.appa.ui.ItemPrestamo;
import com.paolosport.appa.ListViewAdapters.ListViewAdapterPrestamo;
import com.paolosport.appa.R;
import com.paolosport.appa.ui.SwipeDismissListViewTouchListener;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.persistencia.entities.Persona;
import com.paolosport.appa.persistencia.entities.Prestamo;
import com.paolosport.appa.spinnerLocalPaquete.SpinnerAdapterLocal;
import com.paolosport.appa.spinnerMarcaPaquete.SpinnerAdapterMarca;
import com.paolosport.appa.spinnerPersonaPaquete.SpinnerAdapterPersonaLista;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PrestamoFormFragment extends Fragment {

    View view;
    TimerTask timerTask;
    Dialog customDialog = null;
    String destino_item,marca_item,persona_item,origen_tarjeta;
    private Spinner sp_marca,   sp_local, sp_persona;
    private Button btn_registrar_item,
                   btn_cancelar_item,
                   btn_aceptar_pedido,
                   btn_cancelar_pedido;

    private EditText  et_codigo_item,et_descripcion_item;
    private TextView et_talla_item;
    private ListView lv_prestamo;

    AdminSQLiteOpenHelper helper;
    MarcaDAO marcaDAO;
    LocalDAO localDAO;
    PersonaDAO personaDAO;
    PrestamoDAO prestamoDAO;

    MediaPlayer mp_click,
                mp_click2,
                mp_borrar2,
                mp_borrar_3,
                mp_tarjeta,
                mp_registro_item2,
                mp_otro,
                mp_registro_pedido;

    Boolean tarjeta_result = false;
    List listaItems = new ArrayList();
    List tallas = new ArrayList();

    public PrestamoFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState ) {

        helper     = new AdminSQLiteOpenHelper( getActivity().getApplicationContext() );
        localDAO   = new LocalDAO( getActivity().getApplicationContext(), helper );
        marcaDAO   = new MarcaDAO( getActivity().getApplicationContext(), helper );
        personaDAO = new PersonaDAO( getActivity().getApplicationContext(), helper );
        prestamoDAO = new PrestamoDAO( getActivity().getApplicationContext(), helper );


        view= inflater.inflate(R.layout.fragment_prestamo_form, container, false);

        mp_click = MediaPlayer.create(getActivity(),R.raw.a_click);
        mp_click2 = MediaPlayer.create(getActivity(), R.raw.a_click2);
        mp_borrar2 = MediaPlayer.create(getActivity(), R.raw.a_borrar2);
        mp_borrar_3 = MediaPlayer.create(getActivity(), R.raw.a_borrar_3);
        mp_tarjeta = MediaPlayer.create(getActivity(), R.raw.a_tarjeta);
        mp_registro_item2 = MediaPlayer.create(getActivity(), R.raw.a_registro_item2);
        mp_otro = MediaPlayer.create(getActivity(), R.raw.a_otro);
        mp_registro_pedido = MediaPlayer.create(getActivity(), R.raw.a_regitro_pedido);

        et_codigo_item      =(EditText)view.findViewById(R.id.et_codigo_item);
        et_descripcion_item =(EditText)view.findViewById(R.id.et_descripcion_item);
        et_talla_item       =(TextView)view.findViewById(R.id.et_talla_item);

        btn_registrar_item  = (Button)view.findViewById(R.id.btn_registrar_item);
        btn_cancelar_item   = (Button)view.findViewById(R.id.btn_cancelar_item);
        btn_aceptar_pedido  = (Button)view.findViewById(R.id.btn_aceptar_pedido);
        btn_cancelar_pedido = (Button)view.findViewById(R.id.btn_cancelar_pedido);

        sp_marca= (android.widget.Spinner)view.findViewById(R.id.sp_marca_prestamo);
        sp_local= (android.widget.Spinner)view.findViewById(R.id.sp_local_prestamo);
        sp_persona= (android.widget.Spinner)view.findViewById(R.id.sp_persona_prestamo);


        lv_prestamo   = (ListView)view.findViewById(R.id.lv_prestamo);
        lv_prestamo.setSoundEffectsEnabled(false);
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        lv_prestamo,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mp_borrar_3.start();
                                    listaItems.remove(listaItems.get(position));
                                }
                                if (listaItems.isEmpty()){
                                    cancelarPedido();
                                }
                                //listaItems.notify();
                            }
                        });
        lv_prestamo.setOnTouchListener(touchListener);
        lv_prestamo.setOnScrollListener(touchListener.makeScrollListener());


        lv_prestamo.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemPrestamo tarjeta = (ItemPrestamo)parent.getItemAtPosition(position);
                mp_tarjeta.start();
                dialogoTarjeta(tarjeta);
                }
        });

        DatosPorDefecto();

        btn_registrar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_registro_item2.start();
                registrarItem();

            }
        });
        btn_cancelar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_click2.start();
                cancelarItem();
            }
        });
        btn_aceptar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aceptarPedido();
            }
        });
        btn_cancelar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelarPedido();
            }
        });

        et_talla_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_click.start();
                dialogoTeclado();
            }
        });
        et_descripcion_item.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    dialogoTeclado();
                    return true;
                }
                return false;
            }
        });

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



        sp_marca.setAdapter(new SpinnerAdapterMarca(getActivity().getApplicationContext(), listaMarcas));
        sp_local.setAdapter(new SpinnerAdapterLocal(getActivity().getApplicationContext(), listaLocales));
        sp_persona.setAdapter(new SpinnerAdapterPersonaLista(getActivity().getApplicationContext(),listaPersonas));

        marcaDAO.close();
        localDAO.close();
        personaDAO.close();

        sp_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                marca_item=((Marca) adapterView.getItemAtPosition(position)).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
            }
        });

        sp_local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                destino_item=((Local) adapterView.getItemAtPosition(position)).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

        sp_persona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                persona_item=((Persona) adapterView.getItemAtPosition(position)).getCedula();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        sp_marca.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mp_click.start();
                return false;
            }
        });
        sp_local.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mp_click.start();
                return false;
            }
        });
        sp_persona.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mp_click.start();
                return false;
            }
        });

    }//end method DatosPorDefecto


    public void registrarItem(){

        //dialogoRegistro();

        String codigo = et_codigo_item.getText().toString();
        String descripcion = et_descripcion_item.getText().toString();
        String talla = et_talla_item.getText().toString();
        String marca = marca_item.toString();
        String destino = destino_item.toString();
        String persona = persona_item.toString();
        String origen = "";


        if(( codigo == null) || ( codigo.equals(""))){codigo = "Sin Codigo";}
        if(( talla == null) || ( talla.equals(""))){talla = "Sin Talla";}
        if(( descripcion == null) || ( descripcion.equals(""))){descripcion="Sin Descripción";}

        if(talla.indexOf(",")== -1) {
            listaItems.add(new ItemPrestamo(codigo, marca, descripcion, talla, destino, persona,origen,getActivity().getApplicationContext()));
        }
        else {
            String[] aux = talla.split(",");
            for (int i = 0; i < aux.length; i++) {
            listaItems.add(new ItemPrestamo(codigo, marca, descripcion, aux[i], destino, persona,origen,getActivity().getApplicationContext()));
        }

        }
        lv_prestamo.setAdapter(new ListViewAdapterPrestamo(getActivity().getApplicationContext(),listaItems));
        btn_cancelar_pedido.setVisibility(View.VISIBLE);
        btn_aceptar_pedido.setVisibility(View.VISIBLE);
    }
    public void cancelarItem(){
        et_codigo_item.setText("");
        et_descripcion_item.setText("");
        et_talla_item.setText("");
    }
    private void aceptarPedido() {

        if(!listaItems.isEmpty()) {
            cancelarItem();
            crearPedido();//metodo para registar pedido
            listaItems.clear();

            dialogoAcepation();
            lv_prestamo.setAdapter(new ListViewAdapterPrestamo(getActivity().getApplicationContext(), listaItems));
            btn_cancelar_pedido.setVisibility(View.INVISIBLE);
            btn_aceptar_pedido.setVisibility(View.INVISIBLE);
        }
    }

    private void cancelarPedido() {
        if (!listaItems.isEmpty()) {
            cancelarItem();
            listaItems.clear();
            dialogoCancelacion();
            lv_prestamo.setAdapter(new ListViewAdapterPrestamo(getActivity().getApplicationContext(), listaItems));
            btn_cancelar_pedido.setVisibility(View.INVISIBLE);
            btn_aceptar_pedido.setVisibility(View.INVISIBLE);
        }
        else{
            btn_cancelar_pedido.setVisibility(View.INVISIBLE);
            btn_aceptar_pedido.setVisibility(View.INVISIBLE);
        }
    }

    private void dialogoTarjeta(final ItemPrestamo item_tarjeta) {

        customDialog = new Dialog(getActivity(), R.style.PauseDialog);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_tarjeta);
        customDialog.show();

        final TextView tv_persona_tarjeta = (TextView) customDialog.findViewById(R.id.tv_persona_tarjeta);
        final TextView tv_marca_tarjeta = (TextView) customDialog.findViewById(R.id.tv_marca_tarjeta);
        final TextView tv_codigo_tarjeta = (TextView) customDialog.findViewById(R.id.tv_codigo_tarjeta);
        final TextView tv_destino_tarjeta = (TextView) customDialog.findViewById(R.id.tv_destino_tarjeta);
        final TextView tv_origen_tarjeta = (TextView) customDialog.findViewById(R.id.tv_origen_tarjeta);
        final TextView tv_talla_tarjeta = (TextView) customDialog.findViewById(R.id.tv_talla_tarjeta);

        final RadioGroup rg_origen_tarjeta = (RadioGroup) customDialog.findViewById(R.id.rg_origen_tarjeta);
        final RadioButton r1 = (RadioButton) customDialog.findViewById(R.id.r1);
        final RadioButton r2 = (RadioButton) customDialog.findViewById(R.id.r2);
        final RadioButton r3 = (RadioButton) customDialog.findViewById(R.id.r3);

        tv_persona_tarjeta.setText(item_tarjeta.getPersona().toString());
        tv_marca_tarjeta.setText(item_tarjeta.getMarca().toString() + "  " + item_tarjeta.getDescripcion().toString());
        tv_codigo_tarjeta.setText(item_tarjeta.getCodigo().toString());
        tv_destino_tarjeta.setText(item_tarjeta.getDestino().toString());
        tv_talla_tarjeta.setText(item_tarjeta.getTalla().toString());
        tv_origen_tarjeta.setText(item_tarjeta.getOrigen().toString());

        rg_origen_tarjeta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (r1.isChecked() == true) {
                    mp_click.start();
                    tv_origen_tarjeta.setText("L.124");
                } else if (r2.isChecked() == true) {
                    mp_click.start();
                    tv_origen_tarjeta.setText("L.126");
                } else if (r3.isChecked() == true) {
                    mp_click.start();
                    tv_origen_tarjeta.setText("Bodega");
                }
            }
        });

        ((Button) customDialog.findViewById(R.id.btn_cancelar_teclado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mp_click2.start();
                customDialog.dismiss();
            }
        });

        ((Button) customDialog.findViewById(R.id.btn_aceptar_teclado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = tv_origen_tarjeta.getText().toString();
                if (aux != "" && aux != null && !aux.isEmpty()) {
                    mp_click.start();
                    tarjeta_result = true;
                    origen_tarjeta = aux;
                    item_tarjeta.setOrigen(aux);
                    customDialog.dismiss();
                }
               customDialog.dismiss();
            }
        });
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                customDialog.dismiss();}
            };
    }

    private void dialogoAcepation(){
        customDialog = new Dialog(getActivity(),R.style.SinFondoDialog);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_aceptacion);
        customDialog.show();

        Timer time = new Timer();
        initializeTimerTask();
        time.schedule(timerTask, 1200);
        mp_registro_pedido.start();
    }

    private void dialogoCancelacion(){
        customDialog = new Dialog(getActivity(),R.style.SinFondoDialog);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_cancelacion);
        customDialog.show();
        Timer time = new Timer();
        initializeTimerTask();
        time.schedule(timerTask,1200);
        mp_borrar2.start();
    }

    private void dialogoRegistro(){
        customDialog = new Dialog(getActivity(),R.style.RegistroDialogAnimation);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_anim_item);
        customDialog.show();
        Timer time = new Timer();
        initializeTimerTask();
        time.schedule(timerTask,600);
    }

    private void dialogoTeclado(){


        customDialog = new Dialog(getActivity(),R.style.PauseDialog);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialogo_teclado_talla);
        customDialog.show();


        final TextView tv_lista_tallas=(TextView)customDialog.findViewById(R.id.tv_lista_tallas);
        final TextView tv_talla_teclado  =(TextView)customDialog.findViewById(R.id.tv_talla_teclado);
        final FrameLayout fl_tallas = (FrameLayout)customDialog.findViewById(R.id.fl_tallas);

        tallas.clear();
        String aux = et_talla_item.getText().toString();
        if(aux!="" && aux!=null && !aux.isEmpty()){
            String[] arrayTallas = aux.split(",");

            for (int i = 0; i < arrayTallas.length; i++) {
                tallas.add(arrayTallas[i]);
                tv_lista_tallas.append(arrayTallas[i] + "\n");
            }
        }

        ((Button) customDialog.findViewById(R.id.btn_uno)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("1");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_dos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("2");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_tres)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("3");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_cuatro)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("4");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_cinco)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("5");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_seis)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("6");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_siete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("7");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_ocho)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("8");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_nueve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("9");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_cero)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("0");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_medio)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = tv_talla_teclado.getText().toString();

                if(aux.indexOf("½")>-1 ) {
                    aux = aux.replace("½","½");

                    tv_talla_teclado.setText("");
                    tv_talla_teclado.setText(aux);
                }
                else {
                    tv_talla_teclado.append("½");
                }
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_us)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = tv_talla_teclado.getText().toString();

                if(aux.indexOf("Eu")>-1 ||aux.indexOf("Us")>-1 ) {
                    aux = aux.replace("Eu","Us");
                    aux = aux.replace("Us","Us");

                    tv_talla_teclado.setText("");
                    tv_talla_teclado.setText(aux);
                }
                else {
                    tv_talla_teclado.append("Us ");
                }
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_uk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = tv_talla_teclado.getText().toString();
                if(aux.indexOf("Us")>-1 ||aux.indexOf("Eu")>-1  ) {
                    aux = aux.replace("Us","Eu");
                    aux = aux.replace("Eu","Eu");

                    tv_talla_teclado.setText("");
                    tv_talla_teclado.setText(aux);
                }
                else {
                    tv_talla_teclado.append("Eu ");
                }
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = tv_talla_teclado.getText().toString();

                if (aux != "" && aux != null && !aux.isEmpty()) {
                    if (aux.substring(aux.length()-1, aux.length()).equals("u")
                            || aux.substring(aux.length()-1, aux.length()).equals("s")) {
                        aux = aux.substring(0, aux.length() - 2);
                        tv_talla_teclado.setText(aux);
                    }
                    else if(aux.substring(aux.length()-1, aux.length()).equals(" ")){
                        aux = aux.substring(0, aux.length() - 3);
                        tv_talla_teclado.setText(aux);
                    }
                    else {
                        aux = aux.substring(0, aux.length() - 1);
                        tv_talla_teclado.setText(aux);
                    }
                }
            }
        });

        ((Button) customDialog.findViewById(R.id.btn_cancelar_teclado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp_click2.start();
                customDialog.dismiss();
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_aceptar_teclado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp_click.start();
                String aux = tv_lista_tallas.getText().toString();
                String aux2 = tv_talla_teclado.getText().toString();

                if(aux!="" && aux!=null && !aux.isEmpty()){

                    if(aux2!="" && aux2!=null && !aux2.isEmpty()){
                        tallas.add(aux2);
                    }
                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<tallas.size();i++){
                        sb.append(tallas.get(i).toString()+",");
                    }
                    if(tallas.size()>1){
                        aux= sb.toString();
                        aux = aux.substring(0,aux.length()-1);
                        et_talla_item.setText(aux);
                        customDialog.dismiss();
                        return;
                    }
                    et_talla_item.setText(sb.toString());
                    customDialog.dismiss();
                }
                else {
                    aux = tv_talla_teclado.getText().toString();
                    if(aux!="" && aux!=null && !aux.isEmpty()){
                        et_talla_item.setText(aux);
                    }
                    customDialog.dismiss();
                }
            }
        });

        ((Button) customDialog.findViewById(R.id.btn_otra_teclado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down);
                String aux = tv_talla_teclado.getText().toString();
                if(aux!="" && aux!=null && !aux.isEmpty()){
                    mp_otro.start();
                    tallas.add(aux);
                    tv_lista_tallas.append(aux+"\n");
                    tv_talla_teclado.setText("");
                    tv_talla_teclado.startAnimation(animation);}
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_borrar_teclado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp_borrar_3.start();
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
                tv_talla_teclado.setText("");
                tallas.clear();
                fl_tallas.startAnimation(animation);
                tv_lista_tallas.setText("");
                et_talla_item.setText("");
            }
        });


    }

    public void crearPedido(){

    for(int i=0;i<listaItems.size();i++){

        Persona empleado;
        Local local;
        Marca marca;

        String codigo = ((ItemPrestamo) listaItems.get(i)).getCodigo();
        String descripcion = ((ItemPrestamo) listaItems.get(i)).getDescripcion();
        String talla = ((ItemPrestamo) listaItems.get(i)).getTalla();
        String origen= ((ItemPrestamo) listaItems.get(i)).getOrigen();
        String foto="";

        personaDAO.open();
        localDAO.open();
        marcaDAO.open();

        empleado = personaDAO.retrieve(((ItemPrestamo) listaItems.get(i)).getPersonaId());
        local = localDAO.retrieve(((ItemPrestamo) listaItems.get(i)).getDestinoId());
        marca = marcaDAO.retrieve(((ItemPrestamo) listaItems.get(i)).getMarcaId());

        personaDAO.close();
        localDAO.close();
        marcaDAO.close();

        Date date = new Date();
        Timestamp fecha = new Timestamp( date.getTime() );

        Prestamo prest = new Prestamo( codigo,descripcion,foto,talla, fecha, empleado, local, marca,origen );
        prestamoDAO.open();
        prestamoDAO.create(prest);
        prestamoDAO.close();
    }

    } // end method crear

}



