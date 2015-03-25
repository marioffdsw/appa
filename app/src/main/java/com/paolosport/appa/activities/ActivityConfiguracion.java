package com.paolosport.appa.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.paolosport.appa.Item_objct;
import com.paolosport.appa.MyExpandableAdapter;
import com.paolosport.appa.NavigationAdapter;
import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.entities.Marca;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

public class ActivityConfiguracion extends ActionBarActivity {
    private String[] titulos;
    private DrawerLayout NavDrawerLayout;
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private TypedArray NavIcons;
    NavigationAdapter NavAdapter;

    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_configuracion);
        //Drawer Layout
        NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ExpandableListView expandableList = (ExpandableListView) findViewById(R.id.list);
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        setGroupParents();
        setChildData();
        MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, childItems);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        expandableList.setAdapter(adapter);
        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    expandableList.collapseGroup(previousItem );
                previousItem = groupPosition;
            }
        });
        // expandableList.setOnChildClickListener(adapter);

        //Lista
        NavList = (ListView) findViewById(R.id.lista);
        //Declaramos el header el caul sera el layout de header.xml
        View header = getLayoutInflater().inflate(R.layout.header, null);
        //Establecemos header
        NavList.addHeaderView(header);
        //Tomamos listado  de imgs desde drawable
        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);
        //Tomamos listado  de titulos desde el string-array de los recursos @string/nav_options
        titulos = getResources().getStringArray(R.array.nav_options);
        //Listado de titulos de barra de navegacion
        NavItms = new ArrayList<Item_objct>();
        //Agregamos objetos Item_objct al array
        //Perfil
        NavItms.add(new Item_objct(titulos[0], NavIcons.getResourceId(0, -1)));
        //Favoritos
        NavItms.add(new Item_objct(titulos[1], NavIcons.getResourceId(1, -1)));
        //Eventos
        NavItms.add(new Item_objct(titulos[2], NavIcons.getResourceId(2, -1)));
        //Lugares
        NavItms.add(new Item_objct(titulos[3], NavIcons.getResourceId(3, -1)));
        //Etiquetas
        NavItms.add(new Item_objct(titulos[4], NavIcons.getResourceId(4, -1)));
        //Configuracion
        NavItms.add(new Item_objct(titulos[5], NavIcons.getResourceId(5, -1)));
        //Share
        NavItms.add(new Item_objct(titulos[6], NavIcons.getResourceId(6, -1)));
        //Declaramos y seteamos nuestrp adaptador al cual le pasamos el array con los titulos
        NavAdapter= new NavigationAdapter(this,NavItms);
        NavList.setAdapter(NavAdapter);
        //Siempre vamos a mostrar el mismo titulo

        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int a = position;
                Item_objct iti=null;
                Toast.makeText(getBaseContext(), "jacuna", Toast.LENGTH_SHORT).show();
                System.out.println("aqui: "+a);
                iti=(Item_objct)NavList.getItemAtPosition(a);
                System.out.println("aqui: "+iti.getTitulo());

            }
        });
    }
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
