package com.paolosport.appa.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.entities.Marca;

import java.util.List;

/**
 * Created by Andres on 29/03/2015.
 */
public class MarcaAdapter extends ArrayAdapter<Marca> {
    LinearLayout layoutAnimado = null;

    public MarcaAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    public MarcaAdapter(Context context, int resource, List<Marca> prestamos) {
        super(context, resource, prestamos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.marca_item, null);
        }

        Marca p = getItem(position);

        if (p != null) {

            TextView txtIdMarca = (TextView) v.findViewById(R.id.txtIDMarca);
            TextView txtNombreMarca = (TextView) v.findViewById(R.id.txtNombreMarca);
            layoutAnimado = (LinearLayout)v.findViewById(R.id.ll_marca_item);

            if (txtIdMarca != null) {
                txtIdMarca.setText(p.getId());
            }
            if (txtNombreMarca != null) {

                txtNombreMarca.setText(p.getNombre());
            }
        animar(true);
        }


        return v;
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
