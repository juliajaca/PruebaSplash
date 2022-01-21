package com.example.pruebasplash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;


import java.util.ArrayList;
import java.util.List;

public class _Peg_Animation extends FrameLayout {
    private List<_Peg_Token> tokens = new ArrayList<_Peg_Token>();
    public _Peg_Animation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public _Peg_Animation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public _Peg_Animation(Context context) {
        super(context);
    }

    public void createMoveAnim(final _Peg_Token from,final _Peg_Token to,int fromX,int toX,int fromY,int toY){

        final _Peg_Token c = getCard();

        LayoutParams lp = new LayoutParams(Config.TOKEN_WIDTH, Config.TOKEN_WIDTH);
        //lp.gravity = Gravity.CENTER; //nuevo
        lp.leftMargin = fromX*Config.TOKEN_WIDTH;
        lp.topMargin = fromY*Config.TOKEN_WIDTH;
        c.setLayoutParams(lp);

        /*
        AnimatorSet set = new AnimatorSet();
        Animator animator1 = ObjectAnimator.ofFloat(c, "scaleX", 0F, 0.9F);
        Animator animator2 = ObjectAnimator.ofFloat(c, "scaleY", 0f, 0.9f);
        Animator animator3 = ObjectAnimator.ofFloat(c, "translationX", 0f, Config.TOKEN_WIDTH*(toX-fromX));
        Animator animator4 = ObjectAnimator.ofFloat(c, "translationY", 0f, Config.TOKEN_WIDTH*(toY-fromY));
        set.playTogether(animator1,animator2);
        set.setDuration(1000).start();
*/
        TranslateAnimation ta = new TranslateAnimation(0, Config.TOKEN_WIDTH*(toX-fromX), 0, Config.TOKEN_WIDTH*(toY-fromY));
        ta.setDuration(2000);

        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                from.setEstadoFicha(_Peg_Token.TiposEstados.HUECO);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("CON", "entra");
                //ScaleAnimation sa = new ScaleAnimation(0.9f, 1f, 0.9f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
               // sa.setDuration(1000);
                //c.startAnimation(sa);
                to.setEstadoFicha(_Peg_Token.TiposEstados.FICHA);
                to.getLabel().setVisibility(View.VISIBLE);
                recycleCard(c);

            }
        });
        c.startAnimation(ta);
    }

    private _Peg_Token getCard(){
        _Peg_Token c;
        if (tokens.size()>0) {
            c = tokens.remove(0);
        }else{
            c = new _Peg_Token(getContext());
            c.getLabel().setBackgroundResource(R.drawable.con_ficha);
            c.setBackgroundColor(Color.TRANSPARENT);
            addView(c);
        }
        c.setVisibility(View.VISIBLE);
        return c;
    }

    private void recycleCard(_Peg_Token c){
        c.setVisibility(View.INVISIBLE);
        c.setAnimation(null);
        tokens.add(c);
    }

    public void createScaleTo1(_Peg_Token target){
        ScaleAnimation sa = new ScaleAnimation(1f, 0.1f, 1f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(400);
        target.setAnimation(null);
        sa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                target.setEstadoFicha(_Peg_Token.TiposEstados.HUECO);

            }
        });
        target.getLabel().startAnimation(sa);

    }
}
