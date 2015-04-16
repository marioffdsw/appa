package com.paolosport.appa.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paolosport.appa.R;

public class PrestamoLstFragment extends Fragment {

    public PrestamoLstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_prestamo_lst, container, false );
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
    }
}
