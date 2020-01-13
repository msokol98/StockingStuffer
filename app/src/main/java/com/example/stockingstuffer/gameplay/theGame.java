package com.example.stockingstuffer.gameplay;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.stockingfun.R;
import com.example.stockingstuffer.helpers.MainActivity;
import com.example.stockingstuffer.helpers.PixelConverter;

public class theGame extends AppCompatActivity {

    final Handler handler = new Handler();
    Runnable runnableCode;
    boolean firstLaunch = true;

    private static ImageView firstCoal;
    private static ImageView secondCoal;
    private static ImageView thirdCoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


      float density = this.getResources()
                .getDisplayMetrics()
                .density;

        //set up screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        //get size of the screen
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        final int screenWidth = size.x;
        final int screenHeight = size.y;


        //set up score tracker that appears in the top right of screen
        ConstraintLayout myConstraintLayout = (ConstraintLayout) this.findViewById(R.id.gameView);
        final TextView scoreTracker = new TextView(this);
        scoreTracker.setId(View.generateViewId());
        scoreTracker.setText("0");
        scoreTracker.setTextSize(20);
        scoreTracker.setTextColor(Color.WHITE);
        scoreTracker.setElevation(1000);
        myConstraintLayout.addView(scoreTracker);
        ConstraintSet mySet = new ConstraintSet();
        mySet.constrainHeight(scoreTracker.getId(),
                PixelConverter.convert((int) (200/density), this));
        mySet.constrainWidth(scoreTracker.getId(),
                PixelConverter.convert((int) (200/density), this));
        mySet.connect(scoreTracker.getId(), ConstraintSet.LEFT,
                ConstraintSet.MATCH_CONSTRAINT, ConstraintSet.LEFT, screenWidth - (PixelConverter.convert((int) (160/density), this)));
        mySet.connect(scoreTracker.getId(), ConstraintSet.TOP,
                R.id.toolbar, ConstraintSet.TOP, PixelConverter.convert((int) (50/density), this));
        mySet.applyTo(myConstraintLayout);

        //set coal to invisible
        firstCoal = (ImageView) findViewById(R.id.firstcoal);
        secondCoal = (ImageView) findViewById(R.id.secondcoal);
        thirdCoal = (ImageView) findViewById(R.id.thirdcoal);
        firstCoal.setVisibility(View.INVISIBLE);
        secondCoal.setVisibility(View.INVISIBLE);
        thirdCoal.setVisibility(View.INVISIBLE);



        //Instantiate the stocking. User moves stocking to catch presents and avoid coal.
        ImageView stockingImg = findViewById(R.id.Stocking);
        int imageResource = getResources().getIdentifier("@drawable/thestocking", null, this.getPackageName());

        //testing

        ConstraintLayout.LayoutParams lp =
                (ConstraintLayout.LayoutParams) stockingImg.getLayoutParams();
        ImageView reddot = new ImageView(this);
        reddot.setId(View.generateViewId());
        reddot.setAdjustViewBounds(true);
        reddot.setElevation(5000);
        ConstraintLayout reddotcl = this.findViewById(R.id.gameView);
        reddotcl.addView(reddot);
        reddot.setImageResource(this.getResources().getIdentifier
                ("@drawable/reddot", null, this.getPackageName()));
        ConstraintSet mySet3 = new ConstraintSet();
        mySet3.constrainHeight(reddot.getId(), 50);
        mySet.constrainWidth(reddot.getId(), 50);
        mySet3.connect(reddot.getId(), ConstraintSet.LEFT,
                ConstraintSet.MATCH_CONSTRAINT, ConstraintSet.LEFT, screenWidth/2);
        mySet3.connect(reddot.getId(), ConstraintSet.TOP,
                ConstraintSet.MATCH_CONSTRAINT, ConstraintSet.BOTTOM, (PixelConverter.convert(80, this)));
        mySet3.applyTo(reddotcl);



        stockingImg.setImageResource(imageResource);
        final Stocking stocking = new Stocking(stockingImg, this, screenHeight, screenWidth);

        //User touch moves stocking
        stocking.getImage().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                stocking.move(event, screenWidth);
                return true;
            }
        });

        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Setup animations
                final PresentsAndCoalManager vertObjManager = new PresentsAndCoalManager(theGame.this, screenWidth, screenHeight);
                vertObjManager.create(); //Creates presents and coal
                ObjectAnimator[] vertObjAnimators = vertObjManager.setup(); //randomizes drop order and returns animators

                //handle game animation
                AnimationController animator = new AnimationController
                        (vertObjManager, vertObjAnimators, stocking, theGame.this);
                animator.animate(screenHeight, screenWidth, scoreTracker, handler);

                //A new bunch of animations is spawned every eight seconds.
                //This code allows the game to be infinite.

                handler.postDelayed(this, 8000);
            }
        };

        handler.post(runnableCode);
    }

    protected void onPause() {
        super.onPause();

        //suspends spawning of new animations and pauses current ones
        handler.removeCallbacks(runnableCode);
        AnimationController.pauseAnimations();
    }

    protected void onResume() {
        super.onResume();
        if (!firstLaunch) {
            handler.postDelayed(runnableCode, 4000);
        }
        if (MainActivity.getMusicService() != null) {
            MainActivity.getMusicService().resumeMusic();
        }
        if(!firstLaunch) {
            AnimationController.resumeAnimations();

        }
        firstLaunch = false;

    }

    public static ImageView getCoalImages(int num) {
        switch(num) {
            case 1: return firstCoal;
            case 2: return secondCoal;
            case 3: return thirdCoal;
            default:
                return null;
        }
    }
}

