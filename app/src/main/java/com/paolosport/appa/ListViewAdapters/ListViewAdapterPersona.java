package com.paolosport.appa.ListViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.entities.Persona;

/**
 * Created by mario on 10/03/15.
 */
public class ListViewAdapterPersona extends ArrayAdapter<Persona> {
    LinearLayout layoutAnimado = null;
    public ListViewAdapterPersona(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListViewAdapterPersona(Context context, int resource, List<Persona> personas) {
        super(context, resource, personas);
        Collections.sort( personas, new Comparator<Persona>() {
            @Override
            public int compare(Persona lhs, Persona rhs) {
                return lhs.getNombre().toUpperCase().compareTo(rhs.getNombre().toUpperCase());
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_list_empleados, null);
        }
        layoutAnimado = (LinearLayout)v.findViewById(R.id.ll_texto);

        Persona p = getItem(position);

        if (p != null) {

            TextView txtNombreEmpleado = (TextView) v.findViewById(R.id.txtNombreEmpleado);
            TextView txtTelefonoPersona = (TextView) v.findViewById(R.id.txtTelefonoPersona);
            TextView txtCedulaPersona = (TextView) v.findViewById(R.id.txtCedulaPersona);
            ImageView fotoPersona = (ImageView) v.findViewById(R.id.fotoPersona);

            txtNombreEmpleado.setText(p.getNombre().toString());
            txtTelefonoPersona.setText( formatearTelefono(p.getTelefono().toString()));
            txtCedulaPersona.setText(p.getCedula().toString());

            Bitmap bm = null;
            if ( p.getUrl().equals( "prueba" ) ) {
                bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ico_persona_az);
            }
            else{
                bm = BitmapFactory.decodeFile( p.getUrl() + "mini" );
            }
            Bitmap aux = getRoundedCornerBitmap(bm,true);
            fotoPersona.setImageBitmap( aux );
            //fotoPersona.setImageBitmap(  );

        }
        animar(true);
        return v;
    } // end method getView

    private String formatearTelefono( String sinFormato ){
        StringBuilder sb = new StringBuilder();

/*
            sb.append( sinFormato.substring(0,3) )
                .append(" ")
                .append(sinFormato.substring(3, 6))
                .append(" ")
                .append(sinFormato.substring(6, sinFormato.length()));
*/
        return sinFormato;
        //return sb.toString();
    } // end method formatearTelefono

    public static Bitmap getRoundedCornerBitmap( Bitmap b, boolean square) {
        int width = 0;
        int height = 0;

        Bitmap bitmap = b;

        if(square){
            if( bitmap.getWidth() < bitmap.getHeight() ){
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

} // end class PrestamoAdapter
