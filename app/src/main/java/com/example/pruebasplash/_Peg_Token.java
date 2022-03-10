package com.example.pruebasplash;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.Serializable;

public class _Peg_Token extends FrameLayout {

    private _Peg_Logica logica;
    private TextView label;
    private View background;
    public enum TiposEstados {
        NADA, FICHA, HUECO, PULSADA;
    }
    private TiposEstados estadoFicha;
    private Point posicion;

    public Point getPosicion() {
        return posicion;
    }

    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }

    public TiposEstados getEstadoFicha() {
        return estadoFicha;
    }

    public TextView getLabel() {
        return label;
    }

    public _Peg_Token(Context context) {
        super(context);
        darEstilos();
    }


    public _Peg_Token(Context context, _Peg_Logica logica) {
        super(context);
        this.logica = logica;
        darEstilos();

    }

    private void darEstilos(){
        LayoutParams lp = null;

        background = new View(getContext());
        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        //background.setBackgroundColor(0x33ffffff);
        addView(background, lp);

        label = new TextView(getContext());
        label.setTextSize(28);
        label.setGravity(Gravity.CENTER);
        //label.setText(num);

        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);

    }
    public void setListener(){
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logica.jugar(posicion);
            }
        });
    }


    public void setEstadoFicha(TiposEstados input) {
        estadoFicha = input;

        switch (estadoFicha) {
            case NADA:
                background.setBackgroundColor(getResources().getColor(R.color.white));
                label.setBackgroundResource(0);
                break;
            case HUECO:
                background.setBackgroundColor(getResources().getColor(R.color.taupe_grey));
                label.setBackgroundResource(R.drawable.sin_ficha);
                break;
            case FICHA:
                background.setBackgroundColor(getResources().getColor(R.color.taupe_grey));
                label.setBackgroundResource(R.drawable.con_ficha);
                break;
            case PULSADA:
                background.setBackgroundColor(getResources().getColor(R.color.taupe_grey));
                label.setBackgroundResource(R.drawable.pulsada);
                break;
            default:
                background.setBackgroundColor(0xff3c3a32);
                break;
        }
    }



}
