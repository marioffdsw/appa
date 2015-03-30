package com.paolosport.appa.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paolosport.appa.R;

public class PersonaFragment extends Fragment {

    // private OnFragmentInteractionListener mListener;

    public PersonaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onActivityCreated(savedInstanceState);

    } // fin del metodo onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_persona, container, false);

        return view;
    } // fin del metodo onCreateView

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    } // fin del metodo onAttach

} // fin de la clase LocalFragment
