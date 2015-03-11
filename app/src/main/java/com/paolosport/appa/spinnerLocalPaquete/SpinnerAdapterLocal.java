package com.paolosport.appa.spinnerLocalPaquete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.entities.Local;
import com.paolosport.appa.spinnerMarcaPaquete.SpinnerHolder;

import java.util.List;

/**
 * Created by Andres on 04/03/2015.
 */
public class SpinnerAdapterLocal extends ArrayAdapter<Local>
{
    private Context context;

    List<Local> datos = null;

    public SpinnerAdapterLocal(Context context, List<Local> datos)
    {
        //se debe indicar el layout para el item que seleccionado (el que se muestra sobre el botón del botón)
        super(context, R.layout.spinner_selected_item, datos);
        this.context = context;
        this.datos = datos;
    }

    //este método establece el elemento seleccionado sobre el botón del spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_selected_item,null);
        }
        ((TextView) convertView.findViewById(R.id.texto)).setText(datos.get(position).getNombre());


        return convertView;
    }

    //gestiona la lista usando el View Holder Pattern. Equivale a la típica implementación del getView
    //de un Adapter de un ListView ordinario
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.spinner_list_item, parent, false);
        }

        if (row.getTag() == null)
        {
            SpinnerHolderLocal redSocialHolder = new SpinnerHolderLocal();
            redSocialHolder.setTextView((TextView) row.findViewById(R.id.texto));
            row.setTag(redSocialHolder);
        }

        //rellenamos el layout con los datos de la fila que se está procesando
         Local socialNetwork = datos.get(position);
        ((SpinnerHolderLocal) row.getTag()).getTextView().setText(socialNetwork.getNombre());

        return row;
    }


}

