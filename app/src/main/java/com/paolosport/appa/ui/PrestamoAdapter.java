package com.paolosport.appa.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.dao.PrestamoDAO;
import com.paolosport.appa.persistencia.entities.Prestamo;

import org.w3c.dom.Text;

/**
 * Created by mario on 10/03/15.
 */
public class PrestamoAdapter extends BaseAdapter implements Filterable {

    Context mContext;
    WeakReference<ArrayList<Prestamo> > mListPrestamos;
    ArrayList<Prestamo> mList;
    ArrayList<Prestamo> mListaSeleccionados = new ArrayList<>();
    Filter mPrestamosFilter;

    public interface PrestamosSubject{
        ArrayList<Prestamo> getListPrestamos();
    }


    public PrestamoAdapter(Context context, int resource, ArrayList<Prestamo> prestamos) {
        mListPrestamos = new WeakReference<ArrayList<Prestamo>>( prestamos );
        mList = prestamos;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from( mContext );
            v = vi.inflate(R.layout.item_lst_prestamos, null);

        }

        Prestamo p = ( Prestamo ) getItem(position);

        if (p != null) {

            TextView txtDescripcionPrestamo = (TextView) v.findViewById(R.id.txtDescripcionPrestamo);
            TextView txtTallaPrestamo = (TextView) v.findViewById(R.id.txtTallaPrestamo);
            TextView txtFechaPrestamo = (TextView) v.findViewById(R.id.txtFechaPrestamo);
            TextView txtNombreEmpleado = (TextView) v.findViewById(R.id.txtNombreEmpleado);
            ImageView fotoEmpleado = (ImageView) v.findViewById( R.id.foto_empleado);
            ImageView fotoMarca = (ImageView) v.findViewById( R.id.foto_marca );
            TextView txtTelefonoEmpleado = (TextView) v.findViewById( R.id.txtTelefonoEmpleado );
            View colorView = (View) v.findViewById( R.id.cuadro_color2 );

            if( p.getEstado().substring(0,1).equals( "P" ) )
                colorView.setBackgroundColor( mContext.getResources().getColor(R.color.sombra_titulo_rojo) );
            else if( p.getEstado().substring(0,1).equals("V") || p.getEstado().substring(0,1).equals("D") )
                colorView.setBackgroundColor( mContext.getResources().getColor( R.color.sombra_titulo_verde) );


            if (txtDescripcionPrestamo != null) {
                txtDescripcionPrestamo.setText(p.getDescripcion());
            }
            if (txtNombreEmpleado != null) {

                txtNombreEmpleado.setText(p.getEmpleado().getNombre());
            }
            if (txtTallaPrestamo != null) {

                txtTallaPrestamo.setText( String.valueOf( p.getTalla() ) );
            }
            if (txtFechaPrestamo != null) {
                txtFechaPrestamo.setText(p.getFecha().toString());
            }
            if ( fotoEmpleado != null ){
                Bitmap bm = null;
                if ( p.getEmpleado().getUrl().equals( "prueba" ) ) {
                    bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ico_persona_az);
                }
                else{
                    bm = BitmapFactory.decodeFile( p.getEmpleado().getUrl() + "mini" );
                }
                Bitmap aux = getRoundedCornerBitmap(bm, true);
                fotoEmpleado.setImageBitmap( aux );

            }
            if( fotoMarca != null ){
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), p.getMarca().getIcon(mContext));
            }
        }

        return v;
    }

    public void ordenarPorEmpleado(){
        Collections.sort(mList, new Comparator<Prestamo>() {
            @Override
            public int compare(Prestamo lhs, Prestamo rhs) {
                return lhs.getEmpleado().getNombre().compareTo(rhs.getEmpleado().getNombre());
            }
        });
        notifyDataSetChanged();
    }

    public void ordenarPorEstado(){
        Collections.sort(mList, new Comparator<Prestamo>() {
            @Override
            public int compare(Prestamo lhs, Prestamo rhs) {
                return lhs.getEstado().compareToIgnoreCase(rhs.getEstado());
            }
        });
    }

    public void ordenarPorMarca() {
        Collections.sort(mList, new Comparator<Prestamo>() {
            @Override
            public int compare(Prestamo lhs, Prestamo rhs) {
                return lhs.getMarca().getNombre().compareToIgnoreCase( rhs.getMarca().getNombre() );
            }
        });
    }

    public void ordenarPorLocal(){
        Collections.sort(mList, new Comparator<Prestamo>() {
            @Override
            public int compare(Prestamo lhs, Prestamo rhs) {
                return lhs.getLocal().getNombre().compareTo(rhs.getLocal().getNombre());
            }
        });
        notifyDataSetChanged();
    }

    public void ordenarPorFecha(){
        Collections.sort(mList, new Comparator<Prestamo>() {
            @Override
            public int compare(Prestamo lhs, Prestamo rhs) {
                return lhs.getFecha().compareTo(rhs.getFecha());
            }
        });
        notifyDataSetChanged();
    }

    public void ordenarPorOrigen(){
        Collections.sort(mList, new Comparator<Prestamo>() {
            @Override
            public int compare(Prestamo lhs, Prestamo rhs) {
                return lhs.getOrigen().compareToIgnoreCase( rhs.getOrigen() );
            }
        });
    }

    public Filter getFilter(){

        if ( mPrestamosFilter  == null)
            mPrestamosFilter = new PrestamoEmpleadoFilter();

        return mPrestamosFilter;
    }

    private class PrestamoEmpleadoFilter extends Filter{
        @Override
        protected FilterResults performFiltering( CharSequence constraint ){

            // create a FilterResults object
            FilterResults results = new FilterResults();

            // if the constraint (search patterns) is null
            // or its length is 0, it's empty
            // then we just set the 'values' property to the
            // original listPrestamos which contains all of them
            if( constraint == null || constraint.length() == 0 ){
                results.values = mListPrestamos.get() ;
                results.count = mListPrestamos.get().size();
            }
            else{
                // realizar filtro
                ArrayList<Prestamo> prestamosFiltradosPorEmpleado = new ArrayList<>();

                // ahora recorremos todos los prestamos y verificamos que tengan la
                // cadena "patron de busqueda"
                for( Prestamo p : mListPrestamos.get() ){
                    if( p.getEmpleado().getNombre().toUpperCase().contains(
                            constraint.toString().toUpperCase() ) ){
                        prestamosFiltradosPorEmpleado.add( p );
                    }
                }

                // finalmente establecemos los valores filtrados
                // y su tamaño en los resultados
                results.values = prestamosFiltradosPorEmpleado;
                results.count = prestamosFiltradosPorEmpleado.size();
            }

            // retornamos los resultados de aplicar el filtro
            return results;

        } // end method performFiltering

        @Override
        protected void publishResults( CharSequence constraint, FilterResults results ){
            mList = ( ArrayList<Prestamo> ) results.values;
            notifyDataSetChanged();
        } // end method publishResults
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

    public void selecionarElementos( AdapterView<?> parent, View view, int position, long id ){
        if( isItemSelected(position ) ){
            mListaSeleccionados.remove( mList.get( position ) );
        }
        else{
            mListaSeleccionados.add( mList.get( position ) );
            Log.e( "PrestamoAdapter", "seleccionado" );
        }
    } // end method seleccionarElementos

    public boolean isItemSelected( int position ){
        return mListaSeleccionados.contains( mList.get( position ) );
    } // end method isItemSelected

    public void borrarSeleccion(){
        mListaSeleccionados.clear();
    } // end emthod borrarSeleccion

    public int selectedNumber(){
        return mListaSeleccionados.size();
    }

    public void showToast(){
        Toast.makeText( mContext,
            "items selected: " + mListaSeleccionados.size(),
            Toast.LENGTH_SHORT ).show();
    }

    public void vender( PrestamoDAO prestamoDAO ){
        /*
        for(){}
        prestamoDAO.open();
        prestamoDAO.delete(  )
        */
    } // end method vender

    public void devolver( PrestamoDAO prestamoDAO ){

    }

} // end class PrestamoAdapter