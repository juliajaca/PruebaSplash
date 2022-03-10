package com.example.pruebasplash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class _2048_Logica extends LinearLayout implements Serializable {

    // Atributos
    private _2048_Card[][] cardsMapOld = new _2048_Card[Config.LINES_2048][Config.LINES_2048];
    private _2048_Card[][] cardsMap = new _2048_Card[Config.LINES_2048][Config.LINES_2048];
    private List<Point> emptyPoints = new ArrayList<Point>();
    private int scoreOld ;
    private OnTouchListener listenerSwipe = new OnTouchListener() {
        private float startX,startY,offsetX,offsetY;
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    offsetX = event.getX()-startX;
                    offsetY = event.getY()-startY;


                    if (Math.abs(offsetX)>Math.abs(offsetY)) {
                        if (offsetX<-5) {
                            swipeLeft();
                        }else if (offsetX>5) {
                            swipeRight();
                        }
                    }else{
                        if (offsetY<-5) {
                            swipeUp();
                        }else if (offsetY>5) {
                            swipeDown();
                        }
                    }

                    break;
            }
            return true;
        }

    };

    //Constructores
    public _2048_Logica(Context context) {
        super(context);
        initGameView();
    }

    public _2048_Logica(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    //Getters
    public int getScoreOld() {
        return scoreOld;
    }

    public _2048_Card[][] getCardsMap() {
        return cardsMap;
    }

    public _2048_Card[][] getCardsMapOld() {
        return cardsMapOld;
    }

    //METODOS
    private void calculateCardSize(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Config.CARD_WIDTH =(Math.min(width, height))/Config.LINES_2048;
    }

    private void initGameView(){
        calculateCardSize();
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xffbbada0);
        addCards(Config.CARD_WIDTH,Config.CARD_WIDTH);

    }

    private void addCards(int cardWidth,int cardHeight){
        _2048_Card c;

        LinearLayout line;
        LinearLayout.LayoutParams lineLp;

        for (int y = 0; y < Config.LINES_2048; y++) {
            line = new LinearLayout(getContext());
            //line.setGravity(Gravity.CENTER); // esto es nuevo
            lineLp = new LinearLayout.LayoutParams(-1, cardHeight);
            addView(line, lineLp);

            for (int x = 0; x < Config.LINES_2048; x++) {
                c = new _2048_Card(getContext());
                _2048_Card cOld =  new _2048_Card(getContext());
                line.addView(c, cardWidth, cardHeight);
                cardsMap[x][y] = c;
                cardsMapOld[x][y] = cOld;
            }
        }

    }

    public void startGame(){
        setOnTouchListener(listenerSwipe);
        scoreOld = new Integer(_2048_Pantalla.getMainActivity().getScore());

        _2048_Pantalla.getMainActivity().clearScore();
        _2048_Pantalla.getMainActivity().showBestScore(_2048_Pantalla.getMainActivity().getBestScore());

        for (int y = 0; y < Config.LINES_2048; y++) {
            for (int x = 0; x < Config.LINES_2048; x++) {
                cardsMap[x][y].setNum(0);
            }
        }

        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum(){
        emptyPoints.clear();

        for (int y = 0; y < Config.LINES_2048; y++) {
            for (int x = 0; x < Config.LINES_2048; x++) {
                if (cardsMap[x][y].getNum()<=0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        if (emptyPoints.size()>0) {
            Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
            cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);

            _2048_Pantalla.getMainActivity().getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
        }
    }

    private void swipeLeft(){
        saveMovement();
        scoreOld = new Integer(_2048_Pantalla.getMainActivity().getScore());
        boolean merge = false;

        for (int y = 0; y < Config.LINES_2048; y++) {
            for (int x = 0; x < Config.LINES_2048; x++) {

                for (int x1 = x+1; x1 < Config.LINES_2048; x1++) {
                    if (cardsMap[x1][y].getNum()>0) {

                        if (cardsMap[x][y].getNum()<=0) {

                            _2048_Pantalla.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y],cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;
                            merge = true;

                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            _2048_Pantalla.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);

                            _2048_Pantalla.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }

        }
        if (merge) {
            checkWin();
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeRight(){
        saveMovement();
        scoreOld = new Integer(_2048_Pantalla.getMainActivity().getScore());
        boolean merge = false;

        for (int y = 0; y < Config.LINES_2048; y++) {
            for (int x = Config.LINES_2048-1; x >=0; x--) {

                for (int x1 = x-1; x1 >=0; x1--) {
                    if (cardsMap[x1][y].getNum()>0) {

                        if (cardsMap[x][y].getNum()<=0) {
                            _2048_Pantalla.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            merge = true;
                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            _2048_Pantalla.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            _2048_Pantalla.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }

        }

        if (merge) {
            checkWin();
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeUp(){
      saveMovement();
        scoreOld = new Integer(_2048_Pantalla.getMainActivity().getScore());
        boolean merge = false;

        for (int x = 0; x < Config.LINES_2048; x++) {
            for (int y = 0; y < Config.LINES_2048; y++) {

                for (int y1 = y+1; y1 < Config.LINES_2048; y1++) {
                    if (cardsMap[x][y1].getNum()>0) {

                        if (cardsMap[x][y].getNum()<=0) {
                            _2048_Pantalla.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y--;
                            merge = true;
                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            _2048_Pantalla.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            _2048_Pantalla.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }

        }

        if (merge) {
            checkWin();
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeDown(){
        saveMovement();
        scoreOld = new Integer(_2048_Pantalla.getMainActivity().getScore());
        boolean merge = false;

        for (int x = 0; x < Config.LINES_2048; x++) {
            for (int y = Config.LINES_2048-1; y >=0; y--) {

                for (int y1 = y-1; y1 >=0; y1--) {
                    if (cardsMap[x][y1].getNum()>0) {

                        if (cardsMap[x][y].getNum()<=0) {
                            _2048_Pantalla.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            merge = true;
                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            _2048_Pantalla.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            _2048_Pantalla.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }

        }

        if (merge) {
            checkWin();
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete(){
        int vueltas =0;
        boolean complete = true;
        while(complete && vueltas < Config.LINES_2048 * Config.LINES_2048 ){
            for (int y = 0; y < Config.LINES_2048; y++) {
                for (int x = 0; x < Config.LINES_2048; x++) {
                    vueltas++;
                    if (cardsMap[x][y].getNum()==0||
                            (x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
                            (x<Config.LINES_2048-1&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
                            (y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
                            (y<Config.LINES_2048-1&&cardsMap[x][y].equals(cardsMap[x][y+1]))) {
                        complete = false;
                    }
                }
            }

        }


        if (complete) {
            _2048_Pantalla.getMainActivity().getChronometer().stop();
            _2048_Pantalla.getMainActivity().saveScore();
            notificarResultado("defeat");
        }

    }

    private void checkWin(){
        for (int y = 0; y < Config.LINES_2048; y++) {
            for (int x = 0; x < Config.LINES_2048; x++) {
                if (cardsMap[x][y].getNum()==2048) {
                    this.setOnTouchListener(null);
                    _2048_Pantalla.getMainActivity().getChronometer().stop();
                    _2048_Pantalla.getMainActivity().saveScore();
                    notificarResultado("victory");

                }
            }
        }
    }

    private void notificarResultado(String resultado){
        Toasty.Config.getInstance().setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL, 0, Config.CARD_WIDTH*3).apply();
        switch (resultado){
            case "victory":
                Toasty.success(_2048_Pantalla.getMainActivity().getApplicationContext(), "VICTORY", Toast.LENGTH_SHORT,true).show();
                break;
            case "defeat":
                Toasty.error(_2048_Pantalla.getMainActivity().getApplicationContext(), "DEFEAT", Toast.LENGTH_SHORT,true).show();
                break;
        }

    }

    private void saveMovement(){
        for (int y = 0; y < Config.LINES_2048; y++) {
            for (int x = 0; x < Config.LINES_2048; x++) {
                cardsMapOld[x][y].setNum(cardsMap[x][y].getNum());
            }
        }
    }
}
