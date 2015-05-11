package com.paolosport.appa.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paolosport.appa.ui.ItemPrestamo;
import com.paolosport.appa.R;

import java.util.List;

/**
 * Created by Andres on 04/03/2015.
 */
    public class ListViewAdapterPrestamo extends BaseAdapter
    {
        LinearLayout layoutAnimado = null;
        private Context context;
        List<ItemPrestamo> datos = null;

        public ListViewAdapterPrestamo(Context context, List<ItemPrestamo> datos)
        {
            //se debe indicar el layout para el item que seleccionado (el que se muestra sobre el botón del botón)

            this.context = context;
            this.datos = datos;
        }

        @Override
        public int getCount() {
            return this.datos.size();
        }

        @Override
        public Object getItem(int position) {
            return this.datos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            if (convertView == null) {
                // Create a new view into the list.
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.prestamo_lv, parent, false);
            }

            // Set data into the view.
            //ImageView ivItem = (ImageView) rowView.findViewById(R.id.ivItem);

            TextView tv_destino_item = (TextView) rowView.findViewById(R.id.tv_destino_item);
            TextView tv_persona_item = (TextView) rowView.findViewById(R.id.tv_persona_item);
            TextView tv_codigo_item  = (TextView) rowView.findViewById(R.id.tv_codigo_item);
            TextView tv_marca_item  = (TextView) rowView.findViewById(R.id.tv_marca_item);
            TextView tv_talla_item   = (TextView) rowView.findViewById(R.id.tv_talla_item);
            TextView tv_descripcion_item   = (TextView) rowView.findViewById(R.id.tv_descripcion_item);


            tv_destino_item.setText(datos.get(position).getDestino());
            tv_persona_item.setText(datos.get(position).getPersona());
            tv_codigo_item.setText(datos.get(position).getCodigo());
            tv_marca_item.setText(datos.get(position).getMarca());
            tv_talla_item.setText(datos.get(position).getTalla());
            tv_descripcion_item.setText(datos.get(position).getDescripcion());

            //ivItem.setImageResource(item.getImage());

            return rowView;
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

            //duración en milisegundos
            animation.setDuration(300);
            set.addAnimation(animation);
            LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

            layoutAnimado.setLayoutAnimation(controller);
            layoutAnimado.startAnimation(animation);
            }
        }
    }

