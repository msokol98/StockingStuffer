package com.example.stockingstuffer.gameplay;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.stockingfun.R;
import com.example.stockingstuffer.helpers.PixelConverter;
import com.example.stockingstuffer.helpers.Randomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PresentsAndCoalManager {

    enum type {PRESENT, COAL};
    private type t;
    private VerticalObject[][] vertObjs;
    private Activity activity;
    private int screenWidth;
    private int screenHeight;
    private int[][] dropOrder;
    private static List<ObjectAnimator[]> objectAnimatorsArray; //make private after
    private static List<VerticalObject> totalVertObjs;
    float density;

    public PresentsAndCoalManager(Activity activity, int screenWidth, int screenHeight) {
        this.activity = activity;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        objectAnimatorsArray = new ArrayList<>();
        totalVertObjs = new ArrayList<>();

        density = activity.getResources()
                .getDisplayMetrics()
                .density;

    }
    public static List<ObjectAnimator[]> getAllAnimations() {
        return objectAnimatorsArray;
    }

    public int getLength() {
        return vertObjs.length;
    }
    public int getDropOrderElement(int i, int j) {
        return dropOrder[i][j];
    }
    public VerticalObject getVertObj(int i, int j) {
        return vertObjs[i][j];
    }

    public void create() {
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.gameView);
        vertObjs = new VerticalObject[5][5];
        dropOrder = new int[vertObjs.length][vertObjs[0].length];

        for (int i = 0; i < vertObjs.length; i++) {
            for (int j = 0; j < vertObjs[i].length; j++) {

                //Create image for each present
                ImageView image = new ImageView(activity);
                image.setId(View.generateViewId());

                image.setAdjustViewBounds(true);

                cl.addView(image);

                int sourceNum = setPresentOrCoal(image); //determines present or coal, returns appropriate imageResource

                int lMargin = setColumns(j);
                constrain(image, lMargin, cl, sourceNum); //and then constraints are added to it

                vertObjs[i][j] = new VerticalObject(image, activity, screenWidth, screenHeight); //create present
                totalVertObjs.add(getVertObj(i, j));

                switch(sourceNum) {
                    case 0: vertObjs[i][j].setColor(VerticalObject.Color.WHITE);
                    break;
                    case 1:  vertObjs[i][j].setColor(VerticalObject.Color.BLUE);
                    break;
                    case 2: vertObjs[i][j].setColor(VerticalObject.Color.YELLOW);
                    break;
                    case 3: vertObjs[i][j].setColor(VerticalObject.Color.PURPLE);
                    break;
                    case 4: vertObjs[i][j].setColor(VerticalObject.Color.GREEN);
                    break;
                    case 5: vertObjs[i][j].setColor(VerticalObject.Color.COAL);
                    break;
                    default:
                        vertObjs[i][j].setColor(null);
                }
            }
        }
    }
    public ObjectAnimator[] setup() {
        //Array that holds animations for each present/coal
        ObjectAnimator[] animators = new ObjectAnimator[vertObjs.length * vertObjs[0].length];
        objectAnimatorsArray.add(animators);

        //randomize the order by which presents and coal fall
        return randomizeDropOrder(screenHeight, animators);

    }
    private int randomizePresentColor(ImageView image) {

        //Randomize colors for bunch of 5;
        Random rand = new Random();
        int randInt = rand.nextInt(5);

        switch (randInt) {
            case 0:
                image.setImageResource(activity.getResources().getIdentifier
                        ("@drawable/present", null, activity.getPackageName()));
                return 0;
            case 1:
                image.setImageResource(activity.getResources().getIdentifier
                        ("@drawable/bluepresent", null, activity.getPackageName()));
                return 1;
            case 2:
                image.setImageResource(activity.getResources().getIdentifier
                        ("@drawable/yellowpresent", null, activity.getPackageName()));
                return 2;
            case 3:
                image.setImageResource(activity.getResources().getIdentifier
                        ("@drawable/purplepresent", null, activity.getPackageName()));
                return 3;
            case 4:
                image.setImageResource(activity.getResources().getIdentifier
                        ("@drawable/greenpresent", null, activity.getPackageName()));
                return 4;
            default:
                image.setImageResource(activity.getResources().getIdentifier
                        (null, null, activity.getPackageName()));
                return -1;
        }
    }
    private int setCoal(ImageView image) {
        image.setImageResource(activity.getResources().getIdentifier
                ("@drawable/coal", null, activity.getPackageName()));
        return 5;
    }
    private int setPresentOrCoal(ImageView image) {
        Random rand = new Random();
        int randInt = rand.nextInt(100);
        if(randInt > 50) {
            return randomizePresentColor(image);
        } else {
            return setCoal(image);
        }
    }
    private int setColumns(int j) { //sets column placement by changing margins
        int lMargin;
        switch (j) {
            case 0:
                lMargin = (screenWidth / 5) - (screenWidth / 6);
                return lMargin;
            case 1:
                lMargin = 2 * (screenWidth / 5) - (screenWidth / 6);
                return lMargin;
            case 2:
                lMargin = 3 * (screenWidth / 5) - (screenWidth / 6);
                return lMargin;
            case 3:
                lMargin = 4 * (screenWidth / 5) - (screenWidth / 6);
                return lMargin;
            case 4:
                lMargin = 5 * (screenWidth / 5) - (screenWidth / 6);
                return lMargin;
            default:
                lMargin = -1;
                return lMargin;
        }
    }
    private void constrain(ImageView image, int lMargin, ConstraintLayout cl, int sourceNum) {
        ConstraintSet set = new ConstraintSet();
        int dp;
        if(sourceNum == 5) {
            dp = 48;
        } else if(sourceNum == 4) {
            dp = 56;
        } else {
            dp = 54;
        }
        set.constrainHeight(image.getId(), PixelConverter.convert(dp, activity));
        set.constrainWidth(image.getId(), PixelConverter.convert(dp, activity));
        set.connect(image.getId(), ConstraintSet.LEFT,
                ConstraintSet.MATCH_CONSTRAINT, ConstraintSet.LEFT, lMargin);
        set.connect(image.getId(), ConstraintSet.TOP,
                R.id.toolbar, ConstraintSet.TOP, 0);
        set.applyTo(cl);
    }
    private ObjectAnimator[] randomizeDropOrder(int screenHeight, ObjectAnimator[] animators) {
        Randomizer r = new Randomizer();
        int counter = 0;

        for (int i = 0; i < vertObjs.length; i++) {

            List<Integer> indices = new ArrayList<>();
            indices.add(0);
            indices.add(1);
            indices.add(2);
            indices.add(3);
            indices.add(4);

            for (int j = 0; j < vertObjs[i].length; j++) {
                int chosenElement = r.getRandomElement(indices);
                int indexForRemoval = indices.indexOf(chosenElement);
                indices.remove(indexForRemoval);

                int deltaY = (int) (screenHeight/14.29);
                ObjectAnimator ani = ObjectAnimator.ofFloat
                        (vertObjs[i][chosenElement].getImg(), "translationY", screenHeight + deltaY + 50);
                ani.setDuration(3500);
                ani.setStartDelay(counter * 300);

                ani.setInterpolator(new LinearInterpolator());
                animators[counter] = ani;
                counter++;

                dropOrder[i][j] = chosenElement;
            }
        }
        return animators.clone();
    }
    private static void clearAnimations() {
        for(int i = 0; i < objectAnimatorsArray.size(); i++) {
            for(int j = 0; j < objectAnimatorsArray.get(i).length; j++) {
                objectAnimatorsArray.get(i)[j].cancel();
            }
        }
    }
    private static void clearVertObjs() {
        for(int i = 0; i < totalVertObjs.size(); i++) {
            totalVertObjs.get(i).changeY(0);
            totalVertObjs.get(i).deleteImg();
        }
    }
    public static void clearScreen() {
        clearAnimations();
        clearVertObjs();
    }
}
