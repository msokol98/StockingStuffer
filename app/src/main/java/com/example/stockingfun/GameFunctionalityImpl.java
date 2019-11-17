package com.example.stockingfun;

import android.app.Activity;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

public class GameFunctionalityImpl {

    private int stockingLeftX;
    private int stockingRightX;
    private int stockingMidX;
    private int stockingY;

    private int vertObjX;
    private int vertObjY;

    private VerticalObject.Color type;

    private static int coalCount = 0;
    private static int score = 0;

    float density;

    private static Activity activity;

    public GameFunctionalityImpl(Activity act, Stocking stocking, PresentsAndCoalManager manager, int i, int j) {

        activity = act;

        stockingLeftX = stocking.getCoordinates().get("left").getX();
        stockingRightX = stocking.getCoordinates().get("right").getX();
        stockingMidX = (stockingLeftX + stockingRightX)/2;
        stockingY = stocking.getCoordinates().get("right").getY();

        vertObjX = manager.getVertObj(i, j).getCoordinates().get("middle").getX();
        vertObjY = manager.getVertObj(i, j).getCoordinates().get("middle").getY();

        type = manager.getVertObj(i, j).getColor();

        density = activity.getResources()
                .getDisplayMetrics()
                .density;
    }

    public GameFunctionalityImpl() { }

    public boolean isCoalCountThree() {
        if(coalCount == 3) {
            return true;
        }
        return false;
    }
    public boolean isColliding(TextView scoreTracker, Activity activity, int screenWidth) {
        if(!hasReachedStocking()) {
            return false;
        }
        if(!inHorizontalRange()) {
            return false;
        }
        if(type == VerticalObject.Color.COAL) {
            vibrate(activity);
            coalCount++;
            addCoalImage(coalCount, activity, screenWidth);
        } else {
            updateScore(scoreTracker);
        }
        return true;
    }
    private boolean hasReachedStocking() {
        if(Math.abs(vertObjY - stockingY)  < 50) {
            return true;
        }
        return false;
    }
    private boolean inHorizontalRange() {
        if(vertObjX > stockingRightX) {
            return false;
        } else if (vertObjX < stockingLeftX) {
            return false;
        }
        return true;
    }
    private void updateScore(TextView scoreTracker) {
        StringBuilder sb = new StringBuilder(scoreTracker.getText().length());
        sb.append(scoreTracker.getText());
        int currentScore = Integer.parseInt(sb.toString());
        String newScore = Integer.toString(currentScore+10);
        scoreTracker.setText(newScore);
        score += 10;
    }
    private void addCoalImage(int coalCount, Activity activity, int screenWidth) {
       try {
           theGame.getCoalImages(coalCount).setVisibility(View.VISIBLE);
       } catch (NullPointerException e) {
           throw new RuntimeException("null reference");
       }
    }
    private void vibrate(Activity activity) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) activity.getSystemService(Activity.VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(500, 255));
        } else {
            ((Vibrator) activity.getSystemService(Activity.VIBRATOR_SERVICE)).vibrate(500);
        }
    }

    public int getScore() {
        return score;
    }

    public static void resetGame() {
        coalCount = 0;
        score = 0;
    }

}


