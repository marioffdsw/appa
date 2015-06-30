package com.paolosport.appa.ListViewAdapters;

import android.content.Context;
import android.content.res.TypedArray;
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
import com.paolosport.appa.persistencia.entities.Local;

import java.util.List;

/**
 * Created by Andres on 29/03/2015.
 */
public class ListViewAdapterLocalDrawer extends ArrayAdapter<Local> {
    private TypedArray NavIcons;

    public ListViewAdapterLocalDrawer(Context context, int textViewResourceId) {
        super(context, textViewResourceId);

    }


    public ListViewAdapterLocalDrawer(Context context, int resource, List<Local> prestamos) {
        super(context, resource, prestamos);
        NavIcons = getContext().getResources().obtainTypedArray(R.array.navigation_iconos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.drawer_local, null);
        }

        Local p = getItem(position);

        if (p != null) {

            TextView txtNombreLocal = (TextView) v.findViewById(R.id.txtNombreLocal);
            //ImageView bm = (ImageView)v.findViewById((R.id.iv_icon_local));

            if (txtNombreLocal != null) {
                txtNombreLocal.setText(p.getNombre());
            }
        }

        return v;
    }

} // end class PrestamoAdapter
