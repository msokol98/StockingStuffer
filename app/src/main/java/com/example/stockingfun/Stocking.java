package com.example.stockingfun;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.HashMap;
import java.util.Map;

public class Stocking {

    private Coordinate left;
    private Coordinate right;
    private Map<String, Coordinate> coordinates;
    private int screenHeight;
    private int screenWidth;
    private Activity activity;
    private final int STOCKING_Y;
    float density;
    private ImageView stockingImg;


    public Stocking(ImageView stockingImg, Activity activity, int height, int width) {
        this.activity = activity;
        this.stockingImg = stockingImg;
        this.screenHeight = height;
        this.screenWidth = width;

        density = activity.getResources()
                .getDisplayMetrics()
                .density;

        ConstraintLayout.LayoutParams lp =
                (ConstraintLayout.LayoutParams) stockingImg.getLayoutParams();

        coordinates = new HashMap<>();

        STOCKING_Y =  (screenHeight - PixelConverter.convert(80, activity)) - PixelConverter.convert(62, activity);

        int temp = (int) (Math.round(screenWidth/10.2857)) * -1;
        float density = activity.getResources()
                .getDisplayMetrics()
                .density;
        float temp2 =  temp/density;
        int deltaX1 = PixelConverter.convert((int) temp2, activity);

        int temp3 = (int) (Math.round(screenWidth/10.2857)) * -1;
        float temp4 =  temp3/density;
        int deltaX2 = PixelConverter.convert((int) temp4, activity);

        left = new Coordinate(lp.leftMargin+deltaX1, STOCKING_Y);
        right = new Coordinate(lp.leftMargin+deltaX2, STOCKING_Y);

        coordinates.put("left", left);
        coordinates.put("right", right);
    }

    public Map<String, Coordinate> getCoordinates() {
        return coordinates;
    }

    public ImageView getImg() {
        return stockingImg;
    }

    public void move(MotionEvent event, int width){
        int eid = event.getAction();
        switch (eid) {
            case MotionEvent.ACTION_MOVE:
                ConstraintLayout.LayoutParams mParams =
                        (ConstraintLayout.LayoutParams) getImg().getLayoutParams();

                int destination = (int) event.getRawX();
                if(destination < width-(screenWidth/5)) {
                    mParams.leftMargin = destination; //Moves stocking by changing margins
                    getImg().setLayoutParams(mParams);

                    int temp = (int) (Math.round(screenWidth/10.2857)) * -1;
                    float density = activity.getResources()
                            .getDisplayMetrics()
                            .density;
                    float temp2 =  temp/density;
                    int deltaX1 = PixelConverter.convert((int) temp2, activity);;
                    coordinates.put("left", left.changeCoordinate
                            (mParams.leftMargin + deltaX1, STOCKING_Y));

                    int deltaX2 = (int) Math.round(screenWidth/10.2857);

                    coordinates.put("right", right.changeCoordinate
                            (mParams.leftMargin + deltaX2, STOCKING_Y));
                }
                break;
            default:
                break;
        }
    }

    public ImageView getImage() {
        return this.stockingImg;
    }

    private void setImage(Activity activity) {
        stockingImg = new ImageView(activity);
        stockingImg.setId(View.generateViewId());
        stockingImg.setAdjustViewBounds(true);
        stockingImg.setElevation(5000);
        ConstraintLayout stock = activity.findViewById(R.id.gameView);
        stock.addView(stockingImg);
        stockingImg.setImageResource(activity.getResources().getIdentifier
                ("@drawable/thestocking", null, activity.getPackageName()));
        ConstraintSet mySet2 = new ConstraintSet();
        mySet2.constrainHeight(stockingImg.getId(),
                screenWidth/4);
        mySet2.constrainWidth(stockingImg.getId(),
                screenWidth/4);
        mySet2.connect(stockingImg.getId(), ConstraintSet.LEFT,
                ConstraintSet.MATCH_CONSTRAINT, ConstraintSet.LEFT, screenWidth/2);
        mySet2.connect(stockingImg.getId(), ConstraintSet.BOTTOM,
                ConstraintSet.MATCH_CONSTRAINT, ConstraintSet.BOTTOM, 50);
        mySet2.applyTo(stock);
    }

}
