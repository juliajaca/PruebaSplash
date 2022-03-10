package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class Spaslh extends AppCompatActivity {
    // Initialize variable
    ImageView ivTop, ivHeart, ivBeat, ivBottom;
    TextView textView;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //when runnable is run, set the text
            textView.setText(charSequence.subSequence(0, index++));
            //check condition
            if (index <= charSequence.length()) {
                //When index is equial to text length   --> run handler
                handler.postDelayed(runnable, delay);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spaslh);

        //Assign variable
        ivTop = findViewById(R.id.iv_top);
        ivHeart = findViewById(R.id.iv_heart);
        ivBeat = findViewById(R.id.iv_beat);
        ivBottom = findViewById(R.id.iv_bottom);
        textView = findViewById(R.id.text_view);

        //set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        runAnimations();

        //Initializa handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //redirect to main activity
                startActivity(new Intent(Spaslh.this, Login.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                // finish activity
                finish();
            }
        }, 6000);
    }

    private void runAnimations() {
        // Initializa top animation
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.top_wave);

        //start top animacion
        ivTop.setAnimation(animation1);

        //Initialze  object animator
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(ivHeart,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));

        //set duration
        objectAnimator.setDuration(500);

        //set repeat count
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //set repeat mode
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //Start animation
        objectAnimator.start();

        //Set animate text
        animatText("JuliGames presents");
        //load GIF
        Glide.with(this).asGif()
                .load("https://upload.wikimedia.org/wikipedia/commons/1/1f/Wave_equation_1D_fixed_endpoints.gif")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivBeat);
        //initializa botton animation
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bottom_wavw);
        //Start nimation
        ivBottom.setAnimation(animation2);
    }

    public void animatText(CharSequence cs) {
        //Create animated text method
        //set text
        charSequence = cs;
        //clear index
        index = 0;
        //clear text
        textView.setText("");
        //remove callback
        handler.removeCallbacks(runnable);
        // run handler
        handler.postDelayed(runnable, delay);
    }
}