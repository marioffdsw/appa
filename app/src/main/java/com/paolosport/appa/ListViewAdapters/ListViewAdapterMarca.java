package com.paolosport.appa.ListViewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andres on 04/03/2015.
 */
    public class ListViewAdapterMarca extends ArrayAdapter<Marca>
    {
        LinearLayout layoutAnimado = null;
        private Context context;
        List<Marca> datos = null;
        Bitmap bmImg;


        public ListViewAdapterMarca(Context context, List<Marca> datos)
        {
            //se debe indicar el layout para el item que seleccionado (el que se muestra sobre el botón del botón)
            super(context, R.layout.marca_item, datos);
            this.context = context;
            this.datos = datos;
        }

        //este método establece el elemento seleccionado sobre el botón del spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            if (convertView == null)
            {
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marca_item,null);
            }
            layoutAnimado = (LinearLayout)convertView.findViewById(R.id.ll_texto);
            Pattern pat = Pattern.compile(".*/.*");
            Matcher mat = pat.matcher(datos.get(position).getUrl());


            if (mat.matches()) {
                bmImg = BitmapFactory.decodeFile(datos.get(position).getUrl());
                ((TextView) convertView.findViewById(R.id.tv_icon_marca)).setText(datos.get(position).getNombre());
                ((ImageView) convertView.findViewById(R.id.iv_icon_marca)).setImageBitmap(bmImg);
            } else {
                ((TextView) convertView.findViewById(R.id.tv_icon_marca)).setText(datos.get(position).getNombre());
                ( (ImageView) convertView.findViewById( R.id.iv_icon_marca)).setImageBitmap( BitmapFactory.decodeResource( context.getResources(),datos.get(position).getIcon(this.context) )  );
                // ((ImageView) convertView.findViewById(R.id.icono)).setBackgroundResource(datos.get(position).getIcon(this.context));
            }
            animar(true);
            return convertView;
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

