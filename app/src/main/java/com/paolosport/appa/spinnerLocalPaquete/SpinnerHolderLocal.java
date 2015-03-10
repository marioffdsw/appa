package com.paolosport.appa.spinnerLocalPaquete;

import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by Andres on 09/03/2015.
 */
public class SpinnerHolderLocal {
    private Color color;
    private TextView textView;

    public Color getColor() {
        return color;
    }
    public void setColor(Color color)
    {
        this.color = color;
    }

    public TextView getTextView()
    {
        return textView;
    }
    public void setTextView(TextView textView)
    {
        this.textView = textView;
    }
}