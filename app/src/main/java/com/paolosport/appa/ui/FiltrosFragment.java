package com.paolosport.appa.ui;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paolosport.appa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FiltrosFragment extends Fragment {


    public FiltrosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filtros, container, false);
    }

    public void publishImage( Bitmap bitmap ){
        View view = this.getView();
        ImageView imageView = (ImageView) view.findViewById( R.id.imgContainer );
        imageView.setImageBitmap(bitmap);
    }
} // fin de la clase FiltrosFragment
