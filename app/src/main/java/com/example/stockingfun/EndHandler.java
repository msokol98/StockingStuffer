package com.example.stockingfun;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

public class EndHandler {

    private static EndHandler theEnder;

    private EndHandler(final Activity activity, Handler handler) {
        PresentsAndCoalManager.clearScreen();
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(activity, Scores.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        activity.finish();
    }

    public static void endGame(final Activity activity, int screenWidth, int screenHeight, Handler handler) {

       if(theEnder == null) {
           theEnder = new EndHandler(activity, handler);
       }
    }

    //Set theEnder to null since starting a new game
    public static void restartGame() {
        theEnder = null;
        for(int i = 1; i < 3; i++) {
            theGame.getCoalImages(i).setVisibility(View.INVISIBLE);
        }
    }

}