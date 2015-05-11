package com.paolosport.appa.spinnerPersonaPaquete;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.entities.Persona;

import java.util.List;

/**
 * Created by Andres on 04/03/2015.
 */
    public class SpinnerAdapterPersonaLista extends ArrayAdapter<Persona>
    {
        private Context context;

        List<Persona> datos = null;
        Bitmap bmImg;

        public SpinnerAdapterPersonaLista(Context context, List<Persona> datos)
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

            Bitmap bm = null;

            if ( datos.get(position).getUrl().equals( "prueba" ) ) {
                bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ico_persona_az);
            }
            else{
                bm = BitmapFactory.decodeFile( datos.get(position).getUrl() + "mini" );
            }

            ((ImageView) convertView.findViewById(R.id.icono)).setImageBitmap(bm);
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
                row = layoutInflater.inflate(R.layout.spinner_list_item_persona, parent, false);
            }

            if (row.getTag() == null)
            {
                SpinnerHolderPersona redSocialHolder = new SpinnerHolderPersona();
                redSocialHolder.setIcono((ImageView) row.findViewById(R.id.icono));
                redSocialHolder.setTextView((TextView) row.findViewById(R.id.texto));
                row.setTag(redSocialHolder);
            }

            //rellenamos el layout con los datos de la fila que se está procesando
            Persona socialNetwork = datos.get(position);

            Bitmap bm = null;



            if ( datos.get(position).getUrl().equals( "prueba" ) ) {
                bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ico_persona_az);
            }
            else{
                bm = BitmapFactory.decodeFile( datos.get(position).getUrl() + "mini" );
            }


            Bitmap aux = getRoundedCornerBitmap(bm,true);
            ((SpinnerHolderPersona) row.getTag()).getIcono().setImageBitmap(aux);



            ((SpinnerHolderPersona) row.getTag()).getTextView().setText(socialNetwork.getNombre());

            return row;
        }


        public static Bitmap getRoundedCornerBitmap( Bitmap b, boolean square) {
        int width = 0;
        int height = 0;

        Bitmap bitmap = b;

        if(square){
            if(bitmap.getWidth() < bitmap.getHeight()){
                width = bitmap.getWidth();
                height = bitmap.getWidth();
            } else {
                width = bitmap.getHeight();
                height = bitmap.getHeight();
            }
        } else {
            height = bitmap.getHeight();
            width = bitmap.getWidth();
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = 90;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    }

