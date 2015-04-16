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
import com.paolosport.appa.persistencia.entities.Local;

import java.util.List;

/**
 * Created by Andres on 29/03/2015.
 */
public class ListViewAdapterLocal extends ArrayAdapter<Local> {
    LinearLayout layoutAnimado = null;

    public ListViewAdapterLocal(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    public ListViewAdapterLocal(Context context, int resource, List<Local> prestamos) {
        super(context, resource, prestamos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.local_item_lv, null);
        }

        Local p = getItem(position);

        if (p != null) {

            TextView txtIdLocal = (TextView) v.findViewById(R.id.txtIDLocal);
            TextView txtNombreLocal = (TextView) v.findViewById(R.id.txtNombreLocal);
            layoutAnimado = (LinearLayout)v.findViewById(R.id.ll_local_item);

            if (txtIdLocal != null) {
                txtIdLocal.setText(p.getId());
            }
            if (txtNombreLocal != null) {

                txtNombreLocal.setText(p.getNombre());
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
