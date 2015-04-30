package com.paolosport.appa.spinnerMarcaPaquete;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.entities.Marca;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andres on 04/03/2015.
 */
    public class SpinnerAdapterMarca extends ArrayAdapter<Marca>
    {
        private Context context;

        List<Marca> datos = null;
        Bitmap bmImg;

        public SpinnerAdapterMarca(Context context, List<Marca> datos)
        {
            //se debe indicar el layout para el item que seleccionado (el que se muestra sobre el botón del botón)
            super(context, R.layout.spinner_selected_item, datos);
            this.context = context;
            this.datos = datos;
           /* Collections.sort( datos, new Comparator<Marca>() {
                @Override
                public int compare(Marca uno, Marca dos) {
                    return new String( uno.getNombre() ).compareTo( dos.getNombre() );
                }
            });*/

        }

        //este método establece el elemento seleccionado sobre el botón del spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_selected_item,null);
            }

            Pattern pat = Pattern.compile(".*/.*");
            Matcher mat = pat.matcher(datos.get(position).getUrl());
//            Bitmap b = Bitmap.createScaledBitmap(bmImg,48,48,true);

            if (mat.matches()) {
                bmImg = BitmapFactory.decodeFile(datos.get(position).getUrl());
                ((TextView) convertView.findViewById(R.id.texto)).setText(datos.get(position).getNombre());
                ((ImageView) convertView.findViewById(R.id.icono)).setImageBitmap(bmImg);
            } else {
                ((TextView) convertView.findViewById(R.id.texto)).setText(datos.get(position).getNombre());
                ( (ImageView) convertView.findViewById( R.id.icono )).setImageResource( datos.get(position).getIcon(this.context));
            }

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
                row = layoutInflater.inflate(R.layout.spinner_selected_item, parent, false);
            }

            if (row.getTag() == null)
            {
                SpinnerHolderMarca redSocialHolder = new SpinnerHolderMarca();
                redSocialHolder.setIcono((ImageView) row.findViewById(R.id.icono));
                redSocialHolder.setTextView((TextView) row.findViewById(R.id.texto));
                row.setTag(redSocialHolder);
            }

            //rellenamos el layout con los datos de la fila que se está procesando
            Marca socialNetwork = datos.get(position);

            Pattern pat = Pattern.compile(".*/.*");
            Matcher mat = pat.matcher(datos.get(position).getUrl());
            if (mat.matches()) {
                bmImg = BitmapFactory.decodeFile(datos.get(position).getUrl());
                ((SpinnerHolderMarca) row.getTag()).getIcono().setImageBitmap(bmImg);
            } else {
                ((SpinnerHolderMarca) row.getTag()).getIcono().setImageResource(datos.get(position).getIcon(this.context));
               //( (ImageView) convertView.findViewById( R.id.icono )).setImageBitmap( BitmapFactory.decodeResource( context.getResources(),datos.get(position).getIcon(this.context) )  );

            }
            ((SpinnerHolderMarca) row.getTag()).getTextView().setText(socialNetwork.getNombre());

            return row;
        }


    }

