package com.paolosport.appa.activities;


import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.paolosport.appa.ExpandableListView.MyExpandableAdapter;
import com.paolosport.appa.R;
import java.util.ArrayList;


public class opcion_informacion extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba);

        mTitle = mDrawerTitle = getTitle();

        setGroupParents();
        setChildData();}


    public void setGroupParents() {
        parentItems.add("Android");
        parentItems.add("Core Java");
        parentItems.add("Desktop Java");
        parentItems.add("Enterprise Java");
    }
    public void setChildData() {

        // Android
        ArrayList<String> child = new ArrayList<String>();
        child.add("Core");
        child.add("Games");
        childItems.add(child);

        // Core Java
        child = new ArrayList<String>();
        child.add("Apache");
        child.add("Applet");
        child.add("AspectJ");
        child.add("Beans");
        child.add("Crypto");
        childItems.add(child);

        // Desktop Java
        child = new ArrayList<String>();
        child.add("Accessibility");
        child.add("AWT");
        child.add("ImageIO");
        child.add("Print");
        childItems.add(child);

        // Enterprise Java
        child = new ArrayList<String>();
        child.add("EJB3");
        child.add("GWT");
        child.add("Hibernate");
        child.add("JSP");
        childItems.add(child);
    }

}
