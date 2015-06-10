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
import java.sql.Date;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.dao.BaseDAO;
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

    private static final String VENDIDO = "Vendido";
    private static final String DEVUELTO = "Devuelto";
    private static final String PRESTADO = "Prestado";

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
            //ImageView fotoEmpleado = (ImageView) v.findViewById( R.id.foto_empleado);
            ImageView fotoMarca = (ImageView) v.findViewById( R.id.foto_marca );
            View colorView = v.findViewById( R.id.cuadro_color2 );
            TextView txtOrigenPrestamo = (TextView) v.findViewById( R.id.txtOrigenPrestamo );
            TextView txtDestinoPrestamo = (TextView) v.findViewById( R.id.txtDestinoPrestamo );
            TextView txtCodigoPrestamo = (TextView) v.findViewById( R.id.txtCodigoPrestamo );



            if( p.getEstado().substring(0,1).equals("V") )
                colorView.setBackgroundColor( mContext.getResources().getColor( R.color.sombra_titulo) );
            else if( p.getEstado().substring(0,1).equals("D") )
                colorView.setBackgroundColor( mContext.getResources().getColor( R.color.sombra_titulo_verde) );
            else // if( p.getEstado().substring(0,1).equals( "P" ) )
                colorView.setBackgroundColor( mContext.getResources().getColor(R.color.sombra_titulo_rojo) );


            if (txtDescripcionPrestamo != null) {
                txtDescripcionPrestamo.setText("Descripción: " +p.getDescripcion());
            }
            if (txtNombreEmpleado != null) {

                txtNombreEmpleado.setText("Encargado: " +p.getEmpleado().getNombre());
            }
            if (txtTallaPrestamo != null) {
                txtTallaPrestamo.setText( "Talla: " +String.valueOf(p.getTalla()) );
            }
            if( txtOrigenPrestamo != null ){
                txtOrigenPrestamo.setText( "Origen:  " + p.getOrigen() );
            }
            if( txtDestinoPrestamo != null ){
                txtDestinoPrestamo.setText( "Destino: " + p.getLocal().getNombre().toString() );
            }
            if( txtCodigoPrestamo != null ){
                txtCodigoPrestamo.setText( "Referencia: " + p.getCodigo() );
            }
            if (txtFechaPrestamo != null) {

                //Date date = new Date( p.getFecha().getTime() );
                //Log.e( "Prestamo", date.toString() );
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(p.getFecha().getTimeInMillis());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
                int month = calendar.get(Calendar.MONTH);
                month = month + 1;
                int year = calendar.get( Calendar.YEAR );
                DateFormatSymbols simbols = new DateFormatSymbols();

                String[] weekdays = simbols.getShortWeekdays();
                String[] months = simbols.getMonths();

                for( String wday : weekdays )
                    Log.e( "dias de la semana", wday );
                for( String m : months )
                    Log.e( "meses del año", m );

                String hora = new SimpleDateFormat( "K:mm" ).format( calendar.getTime() );
                int amPm = Integer.parseInt(new SimpleDateFormat("H").format(calendar.getTime()));

                String amPmCadena = amPm > 12 ? "pm" : "am";

                String fechaAMostrar = weekdays[weekDay] + ", " +
                    day + " de " + months[month - 1] + " de " + year +
                    "\n" + hora + " " +amPmCadena;

                txtFechaPrestamo.setText( fechaAMostrar );
            }

            if( fotoMarca != null ){

                Pattern pat = Pattern.compile(".*/.*");
                Matcher mat = pat.matcher(p.getMarca().getUrl());
//            Bitmap b = Bitmap.createScaledBitmap(bmImg,48,48,true);

                Bitmap bmImg;
                if (mat.matches()) {
                    bmImg = BitmapFactory.decodeFile(p.getMarca().getUrl());
                    fotoMarca.setImageBitmap(bmImg);
                } else {
                    fotoMarca.setImageResource(p.getMarca().getIcon(mContext));
                }

            }
        }
        Log.e( "view", "adapter entrega una view" );
        return v;
    }

    public void ordenarPorEmpleado(){
        Collections.sort(mList, new Comparator<Prestamo>() {
            @Override
            public int compare(Prestamo lhs, Prestamo rhs) {
                return lhs.getEmpleado().getNombre().compareToIgnoreCase(rhs.getEmpleado().getNombre());
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
        notifyDataSetChanged();
    }

    public void ordenarPorMarca() {
        Collections.sort(mList, new Comparator<Prestamo>() {
            @Override
            public int compare(Prestamo lhs, Prestamo rhs) {
                return lhs.getMarca().getNombre().compareToIgnoreCase(rhs.getMarca().getNombre());
            }
        });
        notifyDataSetChanged();
    }

    public void ordenarPorLocal(){
        Collections.sort(mList, new Comparator<Prestamo>() {
            @Override
            public int compare(Prestamo lhs, Prestamo rhs) {
                return lhs.getLocal().getNombre().compareToIgnoreCase(rhs.getLocal().getNombre());
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
                return lhs.getOrigen().compareToIgnoreCase(rhs.getOrigen());
            }
        });
        notifyDataSetChanged();
    }

    public Filter getFilter(){

        if ( mPrestamosFilter  == null)
            mPrestamosFilter = new FilterWithOptions();

        return mPrestamosFilter;
    }

    public void setFilter(){
        mPrestamosFilter = null;
    } // end setFilter method

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
        final float roundPx = 100;

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

        for( Prestamo p : mListaSeleccionados ){
            p.setEstado( VENDIDO );
            prestamoDAO.open();
            BaseDAO.Estado estado = prestamoDAO.update(p);
            prestamoDAO.close();

        } // end for
        borrarSeleccion();
        notifyDataSetChanged();
    } // end method vender

    public void eliminar( int position, PrestamoDAO prestamoDAO ){
        Prestamo p = mList.get( position );

        prestamoDAO.open();
        BaseDAO.Estado estado = prestamoDAO.delete( p );
        prestamoDAO.close();

        mList.remove( p );
        mListaSeleccionados.remove( p );
        mListPrestamos.get().remove( p );
        notifyDataSetChanged();
    } // end method eliminar

    public void devolver( PrestamoDAO prestamoDAO ){

        for( Prestamo p : mListaSeleccionados ){
            p.setEstado( DEVUELTO );
            prestamoDAO.open();
            BaseDAO.Estado estado = prestamoDAO.update( p );
            prestamoDAO.close();
        } // end for
        borrarSeleccion();
        notifyDataSetChanged();

    }

    public void prestar( PrestamoDAO prestamoDAO ){

        for( Prestamo p : mListaSeleccionados ){
            p.setEstado( PRESTADO );
            prestamoDAO.open();
            BaseDAO.Estado estado = prestamoDAO.update( p );
            prestamoDAO.close();

        } // end for
        borrarSeleccion();
        notifyDataSetChanged();
    }

    public class FilterWithOptions extends Filter {

        private Object[] parameters;
        public boolean filtrarPorEmpleado = true;
        public boolean filtrarPorLocal = true;
        public boolean filtrarPorMarca = true;
        public boolean filtrarPorOrigen = true;
        public boolean filtrarPorNombreLocal = true;
        public boolean filtrarPorDescripcion = true;
        public boolean filtrarPorEstado = true;

        public void setParameters(Object[] parameters) {
            this.parameters = parameters;
        }

        @Override
        protected FilterResults performFiltering( CharSequence constraint ){

            // create a FilterResults object
            FilterResults results = new FilterResults();

            // if the constraint (search patterns) is null
            // or its length is 0, it's empty
            // then we just set the 'values' property to the
            // original listPrestamos which contains all of them
            PrestamosFilterAlgorithm component = new ComponentFilterAlgorithm();

            if( filtrarPorEmpleado ){
                component = new DecoratorEmpleadoFilterAlgorithm( component, constraint.toString() );
            }
            if ( filtrarPorEstado ){
                component = new DecoratorEstadoFilterAlgorithm( component, constraint.toString() );
            }
            if( filtrarPorLocal ){
                component = new DecoratorLocalFilterAlgorithm( component, constraint.toString() );
            }
            if( filtrarPorNombreLocal ){
                component = new DecoratorNameLocalFilterAlgorithm( component, constraint.toString() );
            }
            if( filtrarPorMarca ){
                component = new DecoratorMarcaFilterAlgorithm( component, constraint.toString() );
            }
            if( filtrarPorOrigen ){
                component = new DecoratorOrigenFilterAlgorithm( component, constraint.toString() );
            }
            if( filtrarPorDescripcion ){
                component = new DecoratorDescripcionFilterAlgorithm( component, constraint.toString() );
            }
            if( parameters != null ){
                component = new DecoratorFechaFilterAlgorithm( component, parameters );
            }


            ArrayList<Prestamo> prestamosFiltrados = component.filter();;
            results.values = prestamosFiltrados;
            results.count = prestamosFiltrados.size();

            return results;

        } // end method performFiltering

        @Override
        protected void publishResults( CharSequence constraint, FilterResults results ){
            mList = ( ArrayList<Prestamo> ) results.values;
            notifyDataSetChanged();
        } // end method publishResults
    } // end filterClass

    private interface PrestamosFilterAlgorithm {
        ArrayList<Prestamo> filter();
    }

    private class ComponentFilterAlgorithm implements PrestamosFilterAlgorithm {
        public ArrayList<Prestamo> filter(){
            return mListPrestamos.get();
        } // end method filter
    } // end class ComponentFilterAlgorithm

    private class DecoratorEmpleadoFilterAlgorithm implements PrestamosFilterAlgorithm {

        PrestamosFilterAlgorithm mFilter;
        String searchPattern = "";

        public DecoratorEmpleadoFilterAlgorithm( PrestamosFilterAlgorithm filter, String nombreABuscar ){
            mFilter = filter;
            searchPattern = nombreABuscar;
        } // end constructor

        public ArrayList<Prestamo> filter(){

            // realizamos el filtrado en el Component
            ArrayList<Prestamo> lstPrestamos = mFilter.filter();

            // Ahora realizamos el filtrado propio del Decorator
            ArrayList<Prestamo> prestamosFiltradosPorEmpleado = new ArrayList<>();

            // si hay algo que filtrar, lo filtra, si no retornara null
            if( !( searchPattern == null || searchPattern.length() == 0 ) ){
                // ahora recorremos todos los prestamos y verificamos que tengan la
                // cadena "patron de busqueda"
                String[] palabrasABuscar = searchPattern.split( " " );
                for( Prestamo p : lstPrestamos ){
                    for( String palabra : palabrasABuscar ){
                        if( p.getEmpleado().getNombre().toUpperCase().contains( palabra.toUpperCase() ) ){
                            prestamosFiltradosPorEmpleado.add( p );
                            break;
                        }
                    } // end for interno
                } // end for externo
            } // end if

            // retornamos los resultados de aplicar el filtro
            if( prestamosFiltradosPorEmpleado.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorEmpleado;
        } // end method filter
    } // end class DecoratorEmpleadoFilterAlgorithm

    public class DecoratorLocalFilterAlgorithm implements PrestamosFilterAlgorithm {

        PrestamosFilterAlgorithm mFilter;
        String searchPattern = "";

        public DecoratorLocalFilterAlgorithm( PrestamosFilterAlgorithm filter, String nombreABuscar ){
            mFilter = filter;
            searchPattern = nombreABuscar;
        } // end constructor

        public ArrayList<Prestamo> filter(){
            // realizamos el filtrado en el Component
            ArrayList<Prestamo> lstPrestamos = mFilter.filter();

            // Ahora realizamos el filtrado propio del Decorator
            ArrayList<Prestamo> prestamosFiltradosPorLocal = new ArrayList<>();

            // si hay algo que filtrar, lo filtra, si no retornara null
            if( !( searchPattern == null || searchPattern.length() == 0 ) ){
                // ahora recorremos todos los prestamos y verificamos que tengan la
                // cadena "patron de busqueda"
                String[] palabrasABuscar = searchPattern.split( " " );

                for( Prestamo p : lstPrestamos ){
                    for( String palabra : palabrasABuscar ){
                        if( p.getLocal().getNombre().toUpperCase().contains( palabra.toUpperCase() ) && !palabra.toUpperCase().equals( "CC." ) ){
                            prestamosFiltradosPorLocal.add( p );
                            break;
                        }
                    } // end for interno
                } // end for externo
            } // end if

            // retornamos los resultados de aplicar el filtro
            if( prestamosFiltradosPorLocal.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorLocal;
        } // end method filter
    } // end class DecoratorLocalFilterAlgorithm


    public class DecoratorNameLocalFilterAlgorithm implements PrestamosFilterAlgorithm {

        PrestamosFilterAlgorithm mFilter;
        String searchPattern = "";

        public DecoratorNameLocalFilterAlgorithm( PrestamosFilterAlgorithm filter, String nombreABuscar ){
            mFilter = filter;
            searchPattern = nombreABuscar;
        } // end constructor

        public ArrayList<Prestamo> filter(){
            // realizamos el filtrado en el Component
            ArrayList<Prestamo> lstPrestamos = mFilter.filter();

            // Ahora realizamos el filtrado propio del Decorator
            ArrayList<Prestamo> prestamosFiltradosPorLocal = new ArrayList<>();

            // si hay algo que filtrar, lo filtra, si no retornara null
            if( !( searchPattern == null || searchPattern.length() == 0 ) ){
                // ahora recorremos todos los prestamos y verificamos que tengan la
                // cadena "patron de busqueda"

                for( Prestamo p : lstPrestamos ){
                    if( p.getLocal().getNombre().toUpperCase().contains( searchPattern.toUpperCase() ) ){
                        prestamosFiltradosPorLocal.add( p );
                    }
                } // end for externo
            } // end if

            // retornamos los resultados de aplicar el filtro
            if( prestamosFiltradosPorLocal.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorLocal;
        } // end method filter
    } // end class DecoratorLocalFilterAlgorithm



    public class DecoratorOrigenFilterAlgorithm implements PrestamosFilterAlgorithm {

        PrestamosFilterAlgorithm mFilter;
        String searchPattern = "";

        public DecoratorOrigenFilterAlgorithm( PrestamosFilterAlgorithm filter, String nombreABuscar ){
            mFilter = filter;
            searchPattern = nombreABuscar;
        } // end constructor

        public ArrayList<Prestamo> filter(){
            // realizamos el filtrado en el Component
            ArrayList<Prestamo> lstPrestamos = mFilter.filter();

            // Ahora realizamos el filtrado propio del Decorator
            ArrayList<Prestamo> prestamosFiltradosPorOrigen = new ArrayList<>();

            // si hay algo que filtrar, lo filtra, si no retornara null
            if( !( searchPattern == null || searchPattern.length() == 0 ) ){
                // ahora recorremos todos los prestamos y verificamos que tengan la
                // cadena "patron de busqueda"
                String[] palabrasABuscar = searchPattern.split( " " );
                for( Prestamo p : lstPrestamos ){
                    for( String palabra : palabrasABuscar ){
                        if( p.getOrigen().toUpperCase().contains( palabra.toUpperCase() ) ){
                            prestamosFiltradosPorOrigen.add( p );
                            break;
                        }
                    } // end for interno
                } // end for externo
            } // end if

            // retornamos los resultados de aplicar el filtro
            if( prestamosFiltradosPorOrigen.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorOrigen;
        } // end method filter
    } // end class DecoratorOrigenFilterAlgorithm

    public class DecoratorDescripcionFilterAlgorithm implements PrestamosFilterAlgorithm {

        PrestamosFilterAlgorithm mFilter;
        String searchPattern = "";

        public DecoratorDescripcionFilterAlgorithm( PrestamosFilterAlgorithm filter, String nombreABuscar ){
            mFilter = filter;
            searchPattern = nombreABuscar;
        } // end constructor

        public ArrayList<Prestamo> filter(){
            // realizamos el filtrado en el Component
            ArrayList<Prestamo> lstPrestamos = mFilter.filter();

            // Ahora realizamos el filtrado propio del Decorator
            ArrayList<Prestamo> prestamosFiltradosPorOrigen = new ArrayList<>();

            // si hay algo que filtrar, lo filtra, si no retornara null
            if( !( searchPattern == null || searchPattern.length() == 0 ) ){
                // ahora recorremos todos los prestamos y verificamos que tengan la
                // cadena "patron de busqueda"
                String[] palabrasABuscar = searchPattern.split( " " );
                for( Prestamo p : lstPrestamos ){
                    for( String palabra : palabrasABuscar ){
                        if( p.getOrigen().toUpperCase().contains( palabra.toUpperCase() ) ){
                            prestamosFiltradosPorOrigen.add( p );
                            break;
                        }
                    } // end for interno
                } // end for externo
            } // end if

            // retornamos los resultados de aplicar el filtro
            if( prestamosFiltradosPorOrigen.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorOrigen;
        } // end method filter
    } // end class DecoratorDescripcionFilterAlgorithm

    public class DecoratorEstadoFilterAlgorithm implements PrestamosFilterAlgorithm {

        PrestamosFilterAlgorithm mFilter;
        String searchPattern = "";

        public DecoratorEstadoFilterAlgorithm( PrestamosFilterAlgorithm filter, String nombreABuscar ){
            mFilter = filter;
            searchPattern = nombreABuscar;
        } // end constructor

        public ArrayList<Prestamo> filter(){
            // realizamos el filtrado en el Component
            ArrayList<Prestamo> lstPrestamos = mFilter.filter();

            // Ahora realizamos el filtrado propio del Decorator
            ArrayList<Prestamo> prestamosFiltradosPorEstado = new ArrayList<>();

            // si hay algo que filtrar, lo filtra, si no retornara null
            if( !( searchPattern == null || searchPattern.length() == 0 ) ){
                // ahora recorremos todos los prestamos y verificamos que tengan la
                // cadena "patron de busqueda"
                // String[] palabrasABuscar = searchPattern.split( " " );
                for( Prestamo p : lstPrestamos ){
                        if( p.getEstado().toUpperCase().contains( searchPattern.toString().toUpperCase() ) ){
                            prestamosFiltradosPorEstado.add( p );
                        } // end for interno
                } // end for externo
            } // end if

            // retornamos los resultados de aplicar el filtro
            if( prestamosFiltradosPorEstado.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorEstado;
        } // end method filter
    } // end class DecoratorEstadoFilterAlgorithm

    private class DecoratorMarcaFilterAlgorithm implements PrestamosFilterAlgorithm {

        PrestamosFilterAlgorithm mFilter;
        String searchPattern = "";

        public DecoratorMarcaFilterAlgorithm( PrestamosFilterAlgorithm filter, String nombreABuscar ){
            mFilter = filter;
            searchPattern = nombreABuscar;
        } // end constructor

        public ArrayList<Prestamo> filter(){

            // realizamos el filtrado en el Component
            ArrayList<Prestamo> lstPrestamos = mFilter.filter();

            // Ahora realizamos el filtrado propio del Decorator
            ArrayList<Prestamo> prestamosFiltradosPorMarca = new ArrayList<>();

            // si hay algo que filtrar, lo filtra, si no retornara null
            if( !( searchPattern == null || searchPattern.length() == 0 ) ){
                // ahora recorremos todos los prestamos y verificamos que tengan la
                // cadena "patron de busqueda"
                String[] palabrasABuscar = searchPattern.split( " " );
                for( Prestamo p : lstPrestamos ){
                    for( String palabra : palabrasABuscar ){
                        if( p.getMarca().getNombre().toUpperCase().contains( palabra.toUpperCase() ) ){
                            prestamosFiltradosPorMarca.add( p );
                            break;
                        }
                    } // end for interno
                } // end for externo
            } // end if

            // retornamos los resultados de aplicar el filtro
            if( prestamosFiltradosPorMarca.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorMarca;
        } // end method filter
    } // end class DecoratorEmpleadoFilterAlgorithm

    public class DecoratorFechaFilterAlgorithm implements PrestamosFilterAlgorithm {

        PrestamosFilterAlgorithm mFilter;
        Object[] objParameters;

        public DecoratorFechaFilterAlgorithm( PrestamosFilterAlgorithm filter, Object[] objParameters ){
            mFilter = filter;
            this.objParameters = objParameters;
        } // end constructor

        public ArrayList<Prestamo> filter(){
            ArrayList<Prestamo> lstPrestamos = mFilter.filter();

            // Ahora realizamos el filtrado propio del Decorator
            ArrayList<Prestamo> prestamosFiltradosPorFecha = new ArrayList<>();

            //TODO cambiar esta instruccion por la equivalente para la Enumeration
            switch( ((Integer) objParameters[0]).intValue() ){
                case 0:
                    prestamosFiltradosPorFecha = filtrarPorDia(lstPrestamos);
                    break;
                case 1:
                    prestamosFiltradosPorFecha = filtrarPorMes(lstPrestamos);
                    break;
                case 2:
                    prestamosFiltradosPorFecha  = filtrarPorRango(lstPrestamos);
                    break;
            } // end switch

            if( prestamosFiltradosPorFecha.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorFecha;
        } // end method filter

        private ArrayList<Prestamo> filtrarPorDia( ArrayList<Prestamo> lstPrestamos ){

            ArrayList<Prestamo> prestamosFiltradosPorFecha = new ArrayList<>();

            Calendar cal = Calendar.getInstance();
            if( objParameters[1] != null )
                cal.setTime( (Date) objParameters[1] );
            else
                cal.setTime( new java.util.Date() ); // filtrara el dia con el presente dia (hoy)

            int diaAFiltrar = cal.get( Calendar.DAY_OF_MONTH );
            int mesAFiltrar = cal.get( Calendar.MONTH );
            int añoAFiltrar = cal.get( Calendar.YEAR );

            for( Prestamo p : lstPrestamos ){
                cal.setTime( p.getFecha().getTime() );
                int diaEnElMesDelPrestamo = cal.get( Calendar.DAY_OF_MONTH );
                int mesDelPrestamo = cal.get( Calendar.MONTH );
                int añoDelPrestamo = cal.get( Calendar.YEAR );

                if ( diaEnElMesDelPrestamo == diaAFiltrar &&
                        mesDelPrestamo == mesAFiltrar &&
                        añoDelPrestamo == añoAFiltrar ){
                    prestamosFiltradosPorFecha.add(p);
                }
            } // end for
            if( prestamosFiltradosPorFecha.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorFecha;
        } // end method filtrarPorDia

        private ArrayList<Prestamo> filtrarPorMes( ArrayList<Prestamo> lstPrestamos ){

            ArrayList<Prestamo> prestamosFiltradosPorFecha = new ArrayList<>();
            for( Prestamo p : lstPrestamos ){
                Calendar cal = Calendar.getInstance();
                cal.setTime( p.getFecha().getTime() );
                int mesDelPrestamo = cal.get( Calendar.MONTH );
                int añoDelPrestamo = cal.get( Calendar.YEAR );

                cal.setTime( (Date) objParameters[1] );
                int mesAFiltrar = cal.get( Calendar.MONTH );
                int añoAFiltrar = cal.get( Calendar.YEAR );

                if ( mesDelPrestamo == mesAFiltrar &&
                        añoDelPrestamo == añoAFiltrar ){
                    prestamosFiltradosPorFecha.add(p);
                }
            } // end for
            if( prestamosFiltradosPorFecha.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorFecha;
        } // end method filtrarPorMes

        private ArrayList<Prestamo> filtrarPorRango( ArrayList<Prestamo> lstPrestamos ){

            ArrayList<Prestamo> prestamosFiltradosPorFecha = new ArrayList<>();
            for( Prestamo p : lstPrestamos ){

                Calendar min, max;   // assume these are set to something
                max = ( Calendar ) objParameters[1];
                min = ( Calendar ) objParameters[2];

                // los ordenamos
                if( max.before( min ) ){
                    Calendar aux = max;
                    max = min;
                    min = aux;
                }
                Date d = new Date( p.getFecha().getTimeInMillis() );
                if (  d.after( new Date( min.getTimeInMillis()) ) && d.before( new Date( max.getTimeInMillis() ) )  ){
                    prestamosFiltradosPorFecha.add(p);
                }
            } // end for
            if( prestamosFiltradosPorFecha.size() == 0 )
                return lstPrestamos;
            return prestamosFiltradosPorFecha;
        } // end method filtrarPorMes
    } // end class DecoratorFechaFilterAlgorithm
} // end class PrestamoAdapter