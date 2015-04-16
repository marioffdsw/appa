package com.paolosport.appa.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.entities.Persona;

/**
 * Created by mario on 10/03/15.
 */
public class EmpleadoAdapter extends ArrayAdapter<Persona> {

    public EmpleadoAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public EmpleadoAdapter(Context context, int resource, List<Persona> personas) {
        super(context, resource, personas);
        Collections.sort( personas, new Comparator<Persona>() {
            @Override
            public int compare(Persona lhs, Persona rhs) {
                return lhs.getNombre().toUpperCase().compareTo(rhs.getNombre().toUpperCase());
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_list_empleados, null);
        }

        Persona p = getItem(position);

        if (p != null) {

            TextView txtNombreEmpleado = (TextView) v.findViewById(R.id.txtNombreEmpleado);
            TextView txtTelefonoPersona = (TextView) v.findViewById(R.id.txtTelefonoPersona);
            TextView txtCedulaPersona = (TextView) v.findViewById(R.id.txtCedulaPersona);
            ImageView fotoPersona = (ImageView) v.findViewById(R.id.fotoPersona);

            txtNombreEmpleado.setText(p.getNombre().toString());
            txtTelefonoPersona.setText( formatearTelefono(p.getTelefono().toString()));
            txtCedulaPersona.setText(p.getCedula().toString());

            Bitmap bm = null;
            if ( p.getUrl().equals( "prueba" ) ) {
                bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.puma);
            }
            else{
                bm = BitmapFactory.decodeFile( p.getUrl() + "mini" );
            }
            fotoPersona.setImageBitmap( bm );
            //fotoPersona.setImageBitmap(  );

        }
        return v;
    } // end method getView

    private String formatearTelefono( String sinFormato ){
        StringBuilder sb = new StringBuilder();

/*
            sb.append( sinFormato.substring(0,3) )
                .append(" ")
                .append(sinFormato.substring(3, 6))
                .append(" ")
                .append(sinFormato.substring(6, sinFormato.length()));
*/
        return sinFormato;
        //return sb.toString();
    } // end method formatearTelefono

} // end class PrestamoAdapter
