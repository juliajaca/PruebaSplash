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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class _Peg_Logica extends LinearLayout implements Serializable {
    private _Peg_Token[][] tokensMapOld = new _Peg_Token[Config.LINES_PEG][Config.LINES_PEG];
    private _Peg_Token[][] tokensMap = new _Peg_Token[Config.LINES_PEG][Config.LINES_PEG];
    private _Peg_Token fichaInicial;
    private _Peg_Token huecoDestino;
    private _Peg_Token fichaMedia;
    private int numeroFichas = Config.NUM_PEGS;

    public enum Estado implements  Serializable{
        SELECCION_FICHA_1, SELECCION_FICHA_2, JUEGO_TERMINADO;
    }

    public Estado getEstadoJuego() {
        return estadoJuego;
    }

    public void setEstadoJuego(Estado estadoJuego) {
        this.estadoJuego = estadoJuego;
    }

    private Estado estadoJuego;
    private  _Peg_Logica mainActivity = null;

    public  _Peg_Logica getMainActivity() {
        return mainActivity;
    }

    public _Peg_Logica(Context context) {
        super(context);
        mainActivity = this;
        initGameView();
    }

    public void setNumeroFichas(int numeroFichas) {
        this.numeroFichas = numeroFichas;
    }

    public _Peg_Token[][] getTokensMapOld() {
        return tokensMapOld;
    }

    public void setTokensMapOld(_Peg_Token[][] tokensMapOld) {
        this.tokensMapOld = tokensMapOld;
    }

    public _Peg_Token[][] getTokensMap() {
        return tokensMap;
    }

    public void setTokensMap(_Peg_Token[][] tokensMap) {
        this.tokensMap = tokensMap;
    }

    public _Peg_Token getFichaInicial() {
        return fichaInicial;
    }

    public void setFichaInicial(_Peg_Token fichaInicial) {
        this.fichaInicial = fichaInicial;
    }

    public _Peg_Token getHuecoDestino() {
        return huecoDestino;
    }

    public void setHuecoDestino(_Peg_Token huecoDestino) {
        this.huecoDestino = huecoDestino;
    }

    public _Peg_Token getFichaMedia() {
        return fichaMedia;
    }

    public void setFichaMedia(_Peg_Token fichaMedia) {
        this.fichaMedia = fichaMedia;
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
        _Peg_Pantalla aty = _Peg_Pantalla.getMainActivity();
        aty.clearScore();
        aty.showBestScore(aty.getBestScore());
        this.estadoJuego = Estado.SELECCION_FICHA_1;

        for (int y = 0; y < Config.LINES_PEG; y++) {
            for (int x = 0; x < Config.LINES_PEG; x++) {
                tokensMap[x][y].setPosicion(new Point(x, y));
                tokensMapOld[x][y].setPosicion(new Point(x, y));
                tokensMap[x][y].setListener();
                if ((x == 0 || x == 1 || x == 5 || x == 6) &&
                        (y == 0 || y == 1 || y == 5 || y == 6)) {
                    tokensMap[x][y].setEstadoFicha(_Peg_Token.TiposEstados.NADA);
                    tokensMapOld[x][y].setEstadoFicha(_Peg_Token.TiposEstados.NADA);
                } else if (x == 3 && y == 3) {
                    tokensMap[x][y].setEstadoFicha(_Peg_Token.TiposEstados.HUECO);
                    tokensMapOld[x][y].setEstadoFicha(_Peg_Token.TiposEstados.HUECO);
                } else {
                    tokensMap[x][y].setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                    tokensMapOld[x][y].setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                }
            }
        }


    }

    public void jugar(Point posicionPulsada) {
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
                    numeroFichas--;
                    _Peg_Pantalla.getMainActivity().getScore();
                    if (juegoTerminado()) {
                        estadoJuego = estadoJuego.JUEGO_TERMINADO;
                        if (numeroFichas == 1) {
                            Toast.makeText(getContext(), "Juego has ganado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Perdiste", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    //hace un sonidito de movimiento invalido resetea las posiciones
                    estadoJuego = Estado.SELECCION_FICHA_1;
                    fichaInicial.setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                    fichaInicial = null;
                }
            }
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
                                //Log.d("con", "Movimiento valido" + saltoX + saltoY);
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

    public int getNumeroFichas() {
        return numeroFichas;
    }

    private void guardarMovimient() {     // guardo el movimiento
        for (int y = 0; y < tokensMapOld.length; y++) {
            for (int x = 0; x < tokensMapOld[0].length; x++) {
                tokensMapOld[x][y].setEstadoFicha(tokensMap[x][y].getEstadoFicha());
            }
        }
        tokensMapOld[fichaMedia.getPosicion().x][fichaMedia.getPosicion().y].setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
        tokensMapOld[fichaInicial.getPosicion().x][fichaInicial.getPosicion().y].setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
        tokensMapOld[huecoDestino.getPosicion().x][huecoDestino.getPosicion().y].setEstadoFicha(_Peg_Token.TiposEstados.HUECO);
    }
}