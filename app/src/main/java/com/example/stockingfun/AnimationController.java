package com.example.stockingfun;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;

import java.util.List;

public class AnimationController {

    private PresentsAndCoalManager vertObjManager;
    private ObjectAnimator[] vertObjAnimators;
    private Stocking stocking;
    private Activity activity;

    public AnimationController(PresentsAndCoalManager vom, ObjectAnimator[] vertObjAnimators, Stocking stocking, Activity activity) {
        this.vertObjManager = vom;
        this.vertObjAnimators = vertObjAnimators;
        this.stocking = stocking;
        this.activity = activity;
    }

    public void animate (final int screenHeight, final int screenWidth, final TextView scoreTracker, final Handler handler) {

        int counter = 0;
        for(int i = 0; i < vertObjManager.getLength(); i++) {
            for(int j = 0; j < 5; j++) {

                final int a = i;
                final int b = vertObjManager.getDropOrderElement(i, j); //get index of present about to drop

                    vertObjAnimators[counter].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {

                            GameFunctionalityImpl g = new GameFunctionalityImpl(activity, stocking, vertObjManager, a, b);

                            if(g.isCoalCountThree()) {
                                EndHandler.endGame(activity, screenWidth, screenHeight, handler);
                            }
                            float progress = (float) animation.getAnimatedValue();
                            int y = (int) progress;
                            vertObjManager.getVertObj(a, b).changeY(y);
                            if(g.isColliding(scoreTracker, activity, screenWidth)) {
                                vertObjManager.getVertObj(a, b).deleteImg();
                                animation.cancel();
                        }
                        }
                    });
                    vertObjAnimators[counter].start();
                    counter++;
                }
            }
        }


        public static void pauseAnimations() {
            pauseOrResumeAnimations("pause");
        }
        public static void resumeAnimations() {
            pauseOrResumeAnimations("resume");
        }

        private static void pauseOrResumeAnimations(String pauseOrResume) {

            //get all of the animations
            List<ObjectAnimator[]> objectAnimatorsArray = PresentsAndCoalManager.getAllAnimations();

            //iterate through the animations, and either pause or resume each animations
            //this method is called when the onPause() or onResume() method is called by the activity

            if(objectAnimatorsArray != null) {
                for(int i = 0; i < objectAnimatorsArray.size(); i++) {
                    for(int j = 0; j < objectAnimatorsArray.get(i).length; j++) {
                        switch(pauseOrResume) {
                            case "pause":
                                objectAnimatorsArray.get(i)[j].pause();
                                break;
                            case "resume":
                                objectAnimatorsArray.get(i)[j].resume();
                                break;
                        }
                    }
                }
            }
        }

}
