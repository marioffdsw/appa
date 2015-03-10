package com.paolosport.appa.spinnerLocalPaquete;

/**
 * Created by Andres on 10/03/2015.
 */
public class SpinnerLocal {

    private String name;
    private int color;

    public SpinnerLocal(String nombre,int color)
    {
        super();
        this.name = nombre;
        this.color = color;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
