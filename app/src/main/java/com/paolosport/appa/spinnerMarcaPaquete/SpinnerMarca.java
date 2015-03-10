package com.paolosport.appa.spinnerMarcaPaquete;

/**
 * Created by Andres on 04/03/2015.
 */
public class SpinnerMarca {

    private String name;
    private int icon;

    public SpinnerMarca(String nombre, int icono)
    {
        super();
        this.name = nombre;
        this.icon = icono;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getIcon()
    {
        return icon;
    }

    public void setIcon(int icon)
    {
        this.icon = icon;
    }
}
