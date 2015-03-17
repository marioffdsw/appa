package com.paolosport.appa.ui;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paolosport.appa.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
