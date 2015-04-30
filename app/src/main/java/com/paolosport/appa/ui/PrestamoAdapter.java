package com.paolosport.appa.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.entities.Prestamo;

/**
 * Created by mario on 10/03/15.
 */
public class PrestamoAdapter extends ArrayAdapter<Prestamo> {

    public PrestamoAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public PrestamoAdapter(Context context, int resource, ArrayList<Prestamo> prestamos) {
        super(context, resource, prestamos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_lst_prestamos, null);

        }

        Prestamo p = getItem(position);

        if (p != null) {

            TextView txtDescripcionPrestamo = (TextView) v.findViewById(R.id.txtDescripcionPrestamo);
            TextView txtTallaPrestamo = (TextView) v.findViewById(R.id.txtTallaPrestamo);
            TextView txtFechaPrestamo = (TextView) v.findViewById(R.id.txtFechaPrestamo);
            TextView txtNombreEmpleado = (TextView) v.findViewById(R.id.txtNombreEmpleado);

            if (txtDescripcionPrestamo != null) {
                txtDescripcionPrestamo.setText(p.getDescripcion());
            }
            if (txtNombreEmpleado != null) {

                txtNombreEmpleado.setText(p.getEmpleado().getNombre());
            }
            if (txtTallaPrestamo != null) {

                txtTallaPrestamo.setText( String.valueOf( p.getTalla() ) );
            }
            if (txtFechaPrestamo != null) {
                txtFechaPrestamo.setText(p.getFecha().toString());
            }
        }


        return v;
    }

} // end class PrestamoAdapter
