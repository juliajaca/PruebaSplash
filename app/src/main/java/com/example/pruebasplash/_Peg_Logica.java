package com.example.pruebasplash;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class _Peg_Logica extends LinearLayout {
    private _Peg_Token[][] tokensMap = new _Peg_Token[Config.LINES_PEG][Config.LINES_PEG];
    private _Peg_Token fichaInicial;
    private _Peg_Token huecoDestino;
    private _Peg_Token fichaMedia;
    private int numeroFichas = 0;


    private enum Estado {
        SELECCION_FICHA_1, SELECCION_FICHA_2, JUEGO_TERMINADO;
    }
    private Estado estadoJuego;

    private static _Peg_Logica mainActivity = null;

    public static _Peg_Logica getMainActivity() {
        return mainActivity;
    }

    public static final String SP_KEY_BEST_SCORE = "bestScore";


    public _Peg_Logica(Context context) {
        super(context);
        mainActivity = this;
        initGameView();
    }

    public _Peg_Logica(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = this;
        initGameView();
    }

    private void calculateCardSize(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Config.TOKEN_WIDTH =(Math.min(width, height)-10)/Config.LINES_PEG;
    }
    
    private void initGameView(){
        calculateCardSize();
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xffbbada0);
        addTokens(Config.TOKEN_WIDTH,Config.TOKEN_WIDTH);
    }

    private void addTokens(int cardWidth,int cardHeight){
        _Peg_Token c;

        LinearLayout line;
        LinearLayout.LayoutParams lineLp;

        for (int y = 0; y < Config.LINES_PEG; y++) {
            line = new LinearLayout(getContext());
            //line.setGravity(Gravity.CENTER); // esto es nuevo
            lineLp = new LinearLayout.LayoutParams(-1, cardHeight);
            addView(line, lineLp);

            for (int x = 0; x < Config.LINES_PEG; x++) {
                c = new _Peg_Token(getContext());
                c.setPosicion(new Point(x, y));
                line.addView(c, cardWidth, cardHeight);

                tokensMap[x][y] = c;
            }
        }

    }

    public void startGame(){
        estadoJuego = Estado.SELECCION_FICHA_1;
        for (int y = 0; y < Config.LINES_PEG; y++) {
            for (int x = 0; x < Config.LINES_PEG; x++) {
                tokensMap[x][y].setPosicion(new Point(x,y));
                Log.d("con", "se añaden los listener");
                tokensMap[x][y].setListener();
                if( (x == 0  || x ==1 || x ==5 || x ==6) &&
                        (y ==0 || y == 1 || y== 5 || y==6)) {
                    tokensMap[x][y].setEstadoFicha(_Peg_Token.TiposEstados.NADA);
                }
                else if( x == 3 && y == 3){
                    tokensMap[x][y].setEstadoFicha(_Peg_Token.TiposEstados.HUECO);
                    }else{
                    tokensMap[x][y].setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                    numeroFichas++;
                }
            }
        }
        _Peg_Pantalla.getMainActivity().addScore();

    }

    public  void jugar(Point posicionPulsada){
        _Peg_Token puntoTocado = tokensMap[posicionPulsada.x][posicionPulsada.y];
        if (estadoJuego == Estado.SELECCION_FICHA_1 &&
                puntoTocado.getEstadoFicha()== _Peg_Token.TiposEstados.FICHA) {
            Log.d("con", "la pocision es" + puntoTocado.getPosicion());
            estadoJuego = Estado.SELECCION_FICHA_2;
            puntoTocado.setEstadoFicha(_Peg_Token.TiposEstados.PULSADA);
            fichaInicial = puntoTocado;
        }else if(estadoJuego == Estado.SELECCION_FICHA_2){
            //pulso donde habia pulsado antes
            if(puntoTocado ==  fichaInicial){
                estadoJuego = Estado.SELECCION_FICHA_1;
                puntoTocado.setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                fichaInicial = null;
            }else{
                //evalua donde aterriza la ficha
                if(movimentoValido(fichaInicial, puntoTocado)){
                    //hace el salto
                    huecoDestino = puntoTocado;
                    _Peg_Animation animacion = _Peg_Pantalla.getMainActivity().getAnimLayer();
                    Log.d("con", ""+ animacion);
                    animacion.createScaleTo1(fichaMedia);
                    animacion.createMoveAnim(fichaInicial, huecoDestino,
                     fichaInicial.getPosicion().x, huecoDestino.getPosicion().x,
                      fichaInicial.getPosicion().y, huecoDestino.getPosicion().y);
                    //reseteo las fichas y el estado del juego
                    estadoJuego = Estado.SELECCION_FICHA_1;
                    fichaInicial = null;
                    huecoDestino = null;
                    fichaMedia = null;
                    numeroFichas--;
                    _Peg_Pantalla.getMainActivity().addScore();
                    /*
                    if (juegoTerminado()){
                       estadoJuego = estadoJuego.JUEGO_TERMINADO;
                        Toast.makeText(getContext(), "Juego terminado", Toast.LENGTH_SHORT).show();
                    }
                    */
                }else{
                    //hace un sonidito de movimiento invalido
                    //resetea las posiciones
                    estadoJuego = Estado.SELECCION_FICHA_1;
                    fichaInicial.setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                    fichaInicial = null;
                }
            }
        }

    }

    public boolean movimentoValido(_Peg_Token ficha1, _Peg_Token ficha2){
        int xInicial = ficha1.getPosicion().x;
        int yInicial = ficha1.getPosicion().y;
        int xFinal = ficha2.getPosicion().x;
        int yFinal = ficha2.getPosicion().y;

        if(ficha2.getEstadoFicha() == _Peg_Token.TiposEstados.FICHA ||
                ficha2.getEstadoFicha() == _Peg_Token.TiposEstados.NADA){
            return false;
        }else {
            if((xInicial == xFinal &&  Math.abs(yFinal-yInicial) == 2)
            || (yInicial == yFinal &&  Math.abs(xFinal-xInicial) == 2)){
                int xMedio = (xInicial + xFinal)/2;
                int yMedio = (yInicial + yFinal)/2;
                if(tokensMap[xMedio][yMedio].getEstadoFicha() == _Peg_Token.TiposEstados.FICHA){
                    fichaMedia = tokensMap[xMedio][yMedio];
                    return true;
                }

            }
            return  false;
        }
    }

    public boolean juegoTerminado() {
        Log.d("con", "mira si se ha terminado");
        if (numeroFichas == 1) {
            return true;
        }
        for (int x = 0; x < Config.LINES_PEG; x++) {
            for (int y = 0; y < Config.LINES_PEG; y++) {
                if (tokensMap[x][y].getEstadoFicha() == _Peg_Token.TiposEstados.FICHA) {
                    for (int k = 0; k < Config.NUM_MOVIMIENTOS_PEG.length; k++) {
                        int saltoX = x + Config.NUM_MOVIMIENTOS_PEG[k][0];
                        int saltoY = y + Config.NUM_MOVIMIENTOS_PEG[k][1];
                        if (saltoX >= 0 && saltoX < Config.LINES_PEG && saltoY >= 0 && saltoY < Config.LINES_PEG) {
                            if (movimentoValido(tokensMap[x][y], tokensMap[saltoX][saltoY])) {
                                Log.d("con", "Movimiento valido" + saltoX + saltoY);
                                return false;
                           }
                        }
                    }
                }
            }
        }
         return true;
    }

    public int getNumeroFichas() {
        return numeroFichas;
    }
}
