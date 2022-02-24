package com.example.pruebasplash;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class _2048_Animation extends FrameLayout implements Serializable {

    public List<_2048_Card> cards = new ArrayList<_2048_Card>();

    public _2048_Animation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayer();
    }

    public _2048_Animation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayer();
    }

    public _2048_Animation(Context context) {
        super(context);

        initLayer();
    }

    private void initLayer(){
    }

    public void createMoveAnim(final _2048_Card from,final _2048_Card to,int fromX,int toX,int fromY,int toY){

        final _2048_Card c = getCard(from.getNum());

        LayoutParams lp = new LayoutParams(Config.CARD_WIDTH, Config.CARD_WIDTH);
        //lp.gravity = Gravity.CENTER; //nuevo
        lp.leftMargin = fromX*Config.CARD_WIDTH;
        lp.topMargin = fromY*Config.CARD_WIDTH;

        c.setLayoutParams(lp);

        if (to.getNum()<=0) {
            to.getLabel().setVisibility(View.INVISIBLE);
        }
        TranslateAnimation ta = new TranslateAnimation(0, Config.CARD_WIDTH*(toX-fromX), 0, Config.CARD_WIDTH*(toY-fromY));
        Toast.makeText(((Activity) getContext()), "fromX " + 0 +", tox "+Config.CARD_WIDTH*(toX-fromX)+", y la y "+Config.CARD_WIDTH*(toY-fromY), Toast.LENGTH_SHORT).show();
        ta.setDuration(200);
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                to.getLabel().setVisibility(View.VISIBLE);
                recycleCard(c);
            }
        });
        c.startAnimation(ta);
    }

    private _2048_Card getCard(int num){
        _2048_Card c;
        if (cards.size()>0) {
            c = cards.remove(0);
        }else{
            c = new _2048_Card(getContext());
            addView(c);
        }
        c.setVisibility(View.VISIBLE);
        c.setNum(num);
        return c;
    }
    private void recycleCard(_2048_Card c){
        c.setVisibility(View.INVISIBLE);
        c.setAnimation(null);
        cards.add(c);
    }


    public void createScaleTo1(_2048_Card target){
        ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(400);
        target.setAnimation(null);
        target.getLabel().startAnimation(sa);
    }
}
