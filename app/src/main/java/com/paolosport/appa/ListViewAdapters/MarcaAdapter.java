package com.paolosport.appa.ListViewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.entities.Marca;
import com.paolosport.appa.spinnerMarcaPaquete.SpinnerHolderMarca;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andres on 29/03/2015.
 */
public class MarcaAdapter extends ArrayAdapter<Marca> {

    LinearLayout layoutAnimado = null;
    List<Marca> datos = null;
    Bitmap bmImg;
    private Context context;

    public MarcaAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    public MarcaAdapter(Context context, int resource, List<Marca> prestamos) {
        super(context, resource, prestamos);
        this.datos=prestamos;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {

            LayoutInflater vi;
            //vi = LayoutInflater.from(getContext());
            //v = vi.inflate(R.layout.marca_item, null);
            v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marca_item,null);

        }

            ImageView txtIdMarca = (ImageView) v.findViewById(R.id.txtIDMarca);
            TextView txtNombreMarca = (TextView) v.findViewById(R.id.txtNombreMarca);
            layoutAnimado = (LinearLayout)v.findViewById(R.id.ll_marca_item);


            Pattern pat = Pattern.compile(".*/.*");
            Matcher mat = pat.matcher(datos.get(position).getUrl());
            bmImg = BitmapFactory.decodeFile(datos.get(position).getUrl());

            if (mat.matches()) {
                    txtIdMarca.setImageBitmap(bmImg);
                    //new getImageUrl((ImageView)(convertView.findViewById(R.id.icono))).execute(datos.get(position).getUrl());
                    txtNombreMarca.setText(datos.get(position).getNombre());

            }
            else{
                    txtIdMarca.setBackgroundResource(datos.get(position).getIcon(this.context));
                    txtNombreMarca.setText(datos.get(position).getNombre());

            }

            //animar(true);



        return v;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        if (row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.marca_item, parent, false);
        }

        if (row.getTag() == null)
        {
            SpinnerHolderMarca redSocialHolder = new SpinnerHolderMarca();
            redSocialHolder.setIcono((ImageView) row.findViewById(R.id.txtIDMarca));
            redSocialHolder.setTextView((TextView) row.findViewById(R.id.txtNombreMarca));
            row.setTag(redSocialHolder);
        }

        //rellenamos el layout con los datos de la fila que se está procesando
        Marca socialNetwork = datos.get(position);

        Pattern pat = Pattern.compile(".*/.*");
        Matcher mat = pat.matcher(datos.get(position).getUrl());
        bmImg = BitmapFactory.decodeFile(datos.get(position).getUrl());
        if (mat.matches()) {
            ((SpinnerHolderMarca) row.getTag()).getIcono().setImageBitmap(bmImg);
            //new getImageUrl((ImageView)(convertView.findViewById(R.id.icono))).execute(datos.get(position).getUrl());

        } else {
            ((SpinnerHolderMarca) row.getTag()).getIcono().setImageResource(socialNetwork.getIcon(this.context));
        }
        ((SpinnerHolderMarca) row.getTag()).getTextView().setText(socialNetwork.getNombre());


        return row;
    }

    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            //desde la esquina inferior derecha a la superior izquierda
            //animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 3.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            animation=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }

        //duración en milisegundos
        animation.setDuration(300);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        layoutAnimado.setLayoutAnimation(controller);
        layoutAnimado.startAnimation(animation);
    }


} // end class PrestamoAdapter
