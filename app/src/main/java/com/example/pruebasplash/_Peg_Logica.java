package com.example.pruebasplash;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class _Peg_Logica extends LinearLayout implements Serializable {
    private String[][] tableroJuego ;
    private int numPegs ;
    private _Peg_Token[][] tokensMapOld = new _Peg_Token[Config.LINES_PEG][Config.LINES_PEG];
    private _Peg_Token[][] tokensMap = new _Peg_Token[Config.LINES_PEG][Config.LINES_PEG];
    private _Peg_Token fichaInicial;
    private _Peg_Token huecoDestino;
    private _Peg_Token fichaMedia;
    private int scoreOld;

    public int getScoreOld() {
        return scoreOld;
    }

    public enum Estado implements  Serializable{
        SELECCION_FICHA_1, SELECCION_FICHA_2, JUEGO_TERMINADO;
    }

    public void setEstadoJuego(Estado estadoJuego) {
        this.estadoJuego = estadoJuego;
    }

    private Estado estadoJuego;
    private  _Peg_Logica mainActivity = null;
    private _Peg_Pantalla aty ;


    public _Peg_Logica(Context context) {
        super(context);
        mainActivity = this;
        initGameView();
    }


    public _Peg_Token[][] getTokensMapOld() {
        return tokensMapOld;
    }

    public _Peg_Token[][] getTokensMap() {
        return tokensMap;
    }

    public _Peg_Logica(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = this;
        initGameView();
    }

    private void calculateCardSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Config.TOKEN_WIDTH = (Math.min(width, height) - 10) / Config.LINES_PEG;
    }

    private void initGameView() {
        calculateCardSize();
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.white));
        addTokens(Config.TOKEN_WIDTH, Config.TOKEN_WIDTH);
    }

    private void addTokens(int cardWidth, int cardHeight) {
        _Peg_Token t;
        _Peg_Token tOld;
        LinearLayout line;
        LinearLayout.LayoutParams lineLp;

        for (int y = 0; y < Config.LINES_PEG; y++) {
            line = new LinearLayout(getContext());
            //line.setGravity(Gravity.CENTER); // esto es nuevo
            lineLp = new LinearLayout.LayoutParams(-1, cardHeight);
            addView(line, lineLp);

            for (int x = 0; x < Config.LINES_PEG; x++) {
                t = new _Peg_Token(getContext(), mainActivity);
                tOld = new _Peg_Token(getContext(), mainActivity);
                t.setPosicion(new Point(x, y));
                tOld.setPosicion(new Point(x, y));
                line.addView(t, cardWidth, cardHeight);

                tokensMap[x][y] = t;
                tokensMapOld[x][y] = tOld;
            }
        }
    }

    public void startGame() {
        numPegs = 0;
        aty = _Peg_Pantalla.getMainActivity();
        aty.clearScore();
        aty.showBestScore(aty.getBestScore());
        scoreOld = new Integer(aty.getScore());
        this.estadoJuego = Estado.SELECCION_FICHA_1;

        for (int y = 0; y < Config.LINES_PEG; y++) {
            for (int x = 0; x < Config.LINES_PEG; x++) {
                tokensMap[x][y].setPosicion(new Point(x, y));
                tokensMapOld[x][y].setPosicion(new Point(x, y));
                tokensMap[x][y].setListener();
                switch (aty.getTABLERO_JUEGO()[y][x]){
                    case "HUECO":
                        tokensMap[x][y].setEstadoFicha(_Peg_Token.TiposEstados.HUECO);
                        tokensMapOld[x][y].setEstadoFicha(_Peg_Token.TiposEstados.HUECO);
                        break;
                    case "FICHA":
                        tokensMap[x][y].setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                        tokensMapOld[x][y].setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                        numPegs++;
                        break;
                    case "NADA":
                        tokensMap[x][y].setEstadoFicha(_Peg_Token.TiposEstados.NADA);
                        tokensMapOld[x][y].setEstadoFicha(_Peg_Token.TiposEstados.NADA);

                        break;
                }

            }
        }
        Log.d("con" , "hat " +numPegs +" pegs");


    }

    public void jugar(Point posicionPulsada) {
        scoreOld = new Integer(aty.getScore());
        _Peg_Token puntoTocado = tokensMap[posicionPulsada.x][posicionPulsada.y];
        if (estadoJuego == Estado.SELECCION_FICHA_1 &&
                puntoTocado.getEstadoFicha() == _Peg_Token.TiposEstados.FICHA) {
            estadoJuego = Estado.SELECCION_FICHA_2;
            puntoTocado.setEstadoFicha(_Peg_Token.TiposEstados.PULSADA);
            fichaInicial = puntoTocado;
        } else if (estadoJuego == Estado.SELECCION_FICHA_2) {
            //pulso donde habia pulsado antes
            if (puntoTocado == fichaInicial) {
                estadoJuego = Estado.SELECCION_FICHA_1;
                puntoTocado.setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                fichaInicial = null;
            } else {
                //evalua donde aterriza la ficha
                if (movimentoValido(fichaInicial, puntoTocado)) {
                    //hace el salto
                    huecoDestino = puntoTocado;
                    guardarMovimient();
                    _Peg_Animation animacion = _Peg_Pantalla.getMainActivity().getAnimLayer();
                    animacion.createScaleTo1(fichaMedia);
                    animacion.createMoveAnim(fichaInicial, huecoDestino,
                            fichaInicial.getPosicion().x, huecoDestino.getPosicion().x,
                            fichaInicial.getPosicion().y, huecoDestino.getPosicion().y);

                    //reseteo las fichas y el estado del juego
                    estadoJuego = Estado.SELECCION_FICHA_1;
                    fichaInicial = null;
                    huecoDestino = null;
                    fichaMedia = null;
                    _Peg_Pantalla.getMainActivity().addScore(1);
                } else {
                    //hace un sonidito de movimiento invalido resetea las posiciones
                    estadoJuego = Estado.SELECCION_FICHA_1;
                    fichaInicial.setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                    fichaInicial = null;
                }
            }
        }

    }

    public void notificarFinal(){
        _Peg_Pantalla.getMainActivity().getChronometer().stop();
        aty.saveScore();
        // impido que el usuario haga undo
        copiarTokensMapOld();
        scoreOld = new Integer(aty.getScore());
        Toasty.Config.getInstance().setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL, 0, Config.TOKEN_WIDTH*12).apply();
        if (aty.getScore() == numPegs-1) {
                Toasty.success(aty.getApplicationContext(), "VICTORY", Toast.LENGTH_SHORT,true).show();

        } else {
            Toasty.error(aty.getApplicationContext(), "DEFEAT", Toast.LENGTH_SHORT, true).show();
        }
    }

    public boolean movimentoValido(_Peg_Token ficha1, _Peg_Token ficha2) {
        int xInicial = ficha1.getPosicion().x;
        int yInicial = ficha1.getPosicion().y;
        int xFinal = ficha2.getPosicion().x;
        int yFinal = ficha2.getPosicion().y;

        if (ficha2.getEstadoFicha() == _Peg_Token.TiposEstados.FICHA ||
                ficha2.getEstadoFicha() == _Peg_Token.TiposEstados.NADA) {
            return false;
        } else {
            if ((xInicial == xFinal && Math.abs(yFinal - yInicial) == 2)
                    || (yInicial == yFinal && Math.abs(xFinal - xInicial) == 2)) {
                int xMedio = (xInicial + xFinal) / 2;
                int yMedio = (yInicial + yFinal) / 2;
                if (tokensMap[xMedio][yMedio].getEstadoFicha() == _Peg_Token.TiposEstados.FICHA) {
                    fichaMedia = tokensMap[xMedio][yMedio];
                    return true;
                }
            }
            return false;
        }
    }

    public boolean juegoTerminado() {
        if (aty.getScore() == numPegs-1) {

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
                                Log.d("con", "Movimiento valido en peg"+ tokensMap[x][y].getPosicion().x + tokensMap[x][y].getPosicion().y+ "a x = "+ saltoX + "e Y: "+ saltoY);
                                return false;
                            }
                        }
                    }
                }
            }
        }

        Log.d("con", "Ha terminado");
        return true;
    }



    private void guardarMovimient() {     // guardo el movimiento
      copiarTokensMapOld();
      actualizarTokensMapOld();

    }

    private void copiarTokensMapOld(){
        for (int y = 0; y < tokensMapOld.length; y++) {
            for (int x = 0; x < tokensMapOld[0].length; x++) {
                tokensMapOld[x][y].setEstadoFicha(tokensMap[x][y].getEstadoFicha());
            }
        }
    }

    private void actualizarTokensMapOld(){
        tokensMapOld[fichaMedia.getPosicion().x][fichaMedia.getPosicion().y].setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
        tokensMapOld[fichaInicial.getPosicion().x][fichaInicial.getPosicion().y].setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
        tokensMapOld[huecoDestino.getPosicion().x][huecoDestino.getPosicion().y].setEstadoFicha(_Peg_Token.TiposEstados.HUECO);

    }
}