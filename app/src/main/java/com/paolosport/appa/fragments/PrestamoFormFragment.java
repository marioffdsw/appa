package com.paolosport.appa.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paolosport.appa.ItemPrestamo;
import com.paolosport.appa.ListViewAdapters.ListViewAdapterPrestamo;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrestamoFormFragment extends Fragment {

    View view;

    TimerTask timerTask;

    private Spinner sp_marca,   sp_local, sp_persona;
    FrameLayout layoutAnimado = null;

    Dialog customDialog=null;

    String destino_item,marca_item,codigo_item,descripcion_item,talla_item,persona_item;

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

    Boolean prestamo = false;
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

        view= inflater.inflate(R.layout.fragment_prestamo_form, container, false);

        et_codigo_item      =(EditText)view.findViewById(R.id.et_codigo_item);
        et_descripcion_item =(EditText)view.findViewById(R.id.et_descripcion_item);
        et_talla_item       =(TextView)view.findViewById(R.id.et_talla_item);

        btn_registrar_item  = (Button)view.findViewById(R.id.btn_registrar_item);
        btn_cancelar_item   = (Button)view.findViewById(R.id.btn_cancelar_item);
        btn_aceptar_pedido  = (Button)view.findViewById(R.id.btn_aceptar_pedido);
        btn_cancelar_pedido = (Button)view.findViewById(R.id.btn_cancelar_pedido);

        lv_prestamo   = (ListView)view.findViewById(R.id.lv_prestamo);
        layoutAnimado = (FrameLayout)view.findViewById(R.id.fl_lista);



        DatosPorDefecto();

        btn_registrar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarItem();

            }
        });
        btn_cancelar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                dialogoTeclado();

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


        sp_marca= (android.widget.Spinner)view.findViewById(R.id.sp_marca_prestamo);
        sp_local= (android.widget.Spinner)view.findViewById(R.id.sp_local_prestamo);
        sp_persona= (android.widget.Spinner)view.findViewById(R.id.sp_persona_prestamo);

        sp_marca.setAdapter(new SpinnerAdapterMarca(getActivity().getApplicationContext(),listaMarcas));
        sp_local.setAdapter(new SpinnerAdapterLocal(getActivity().getApplicationContext(),listaLocales));
        sp_persona.setAdapter(new SpinnerAdapterPersonaLista(getActivity().getApplicationContext(),listaPersonas));

        marcaDAO.close();
        localDAO.close();
        personaDAO.close();

        sp_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                marca_item=((Marca) adapterView.getItemAtPosition(position)).getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

        sp_local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                destino_item=((Local) adapterView.getItemAtPosition(position)).getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_persona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                persona_item=((Persona) adapterView.getItemAtPosition(position)).getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }//end method DatosPorDefecto


    public void registrarItem(){

        String codigo = et_codigo_item.getText().toString();
        String marca = marca_item.toString();
        String descripcion = et_descripcion_item.getText().toString();
        String talla = et_talla_item.getText().toString();
        String destino = destino_item.toString();
        String persona = persona_item.toString();

        if(( codigo == null) || ( codigo.equals(""))){codigo = "Sin Codigo";}
        if(( talla == null) || ( talla.equals(""))){talla = "sin Talla";}
        if(( descripcion == null) || ( descripcion.equals(""))){descripcion="sin Descripción";}

        listaItems.add(new ItemPrestamo(codigo,marca,descripcion,talla,destino,persona));
        lv_prestamo.setAdapter(new ListViewAdapterPrestamo(getActivity().getApplicationContext(),listaItems));
        animar(true);

    }
    public void cancelarItem(){
        et_codigo_item.setText("");
        et_descripcion_item.setText("");
        et_talla_item.setText("");
    }
    private void aceptarPedido() {
        prestamo=true;
        //registraPedido();//metodo para registar pedido
        listaItems.clear();
        dialogoAcepation();
        lv_prestamo.setAdapter(new ListViewAdapterPrestamo(getActivity().getApplicationContext(),listaItems));
    }

    private void cancelarPedido() {
        prestamo=true;
        listaItems.clear();
        dialogoCancelacion();
        lv_prestamo.setAdapter(new ListViewAdapterPrestamo(getActivity().getApplicationContext(),listaItems));
    }

    private void dialogoTeclado(){


            customDialog = new Dialog(getActivity(),R.style.PauseDialog);
            customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            customDialog.setCancelable(false);
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
                tv_talla_teclado.append("½");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_us)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("Us ");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_uk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_talla_teclado.append("Eu ");
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = tv_talla_teclado.getText().toString();
                if(aux!="" && aux!=null && !aux.isEmpty()){
                aux=aux.substring(0,aux.length()-1);
                tv_talla_teclado.setText(aux);}
            }
        });

            ((Button) customDialog.findViewById(R.id.btn_cancelar_teclado)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialog.dismiss();
                }
            });
        ((Button) customDialog.findViewById(R.id.btn_aceptar_teclado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                tallas.add(aux);
                tv_lista_tallas.append(aux+"\n");
                tv_talla_teclado.setText("");
                tv_talla_teclado.startAnimation(animation);}
            }
        });
        ((Button) customDialog.findViewById(R.id.btn_borrar_teclado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
                tv_talla_teclado.setText("");
                tallas.clear();
                fl_tallas.startAnimation(animation);
                tv_lista_tallas.setText("");
                et_talla_item.setText("");
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
        time.schedule(timerTask,1200);
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
    }

    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar) {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            //duración en milisegundos
            animation.setDuration(400);
            set.addAnimation(animation);
            LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

            layoutAnimado.setLayoutAnimation(controller);
            layoutAnimado.startAnimation(animation);
        }
    }
}
