package com.example.stockingstuffer.gameplay;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.stockingstuffer.helpers.PixelConverter;

import java.util.HashMap;
import java.util.Map;

public class VerticalObject {

    /* Coal and Presents are the two vertical objects in the game.
       Their implementation is by and large the same, so one class is used
       for both presents and coal. An enumeration is used to separate
       the presents by color. Coal is included as a color, so it can be properly
       distinguished whenever necessary by the program.
     */

    enum Color {WHITE, BLUE, PURPLE, GREEN, YELLOW, COAL};
    private Color color;
    private ImageView img;
    private Coordinate middle;
    private Map<String, Coordinate> coordinates;
    private int screenHeight;
    private int screenWidth;
    private Activity act;

    public VerticalObject(ImageView img, Activity activity, int width, int height) {
        this.img = img;
        ConstraintLayout.LayoutParams presentParams =
                (ConstraintLayout.LayoutParams) getImg().getLayoutParams();

        this.screenWidth = width;
        this.screenHeight = height;
        this.act = activity;

        coordinates = new HashMap<>();

        int deltaX = (int) (screenWidth/20.57);
        middle = new Coordinate(presentParams.leftMargin-deltaX, 0);

        coordinates.put("middle", middle);
    }


    public ImageView getImg() {
        return img;
    }

    public void deleteImg() {
        img.setVisibility(View.GONE);
    }

    public Map<String, Coordinate> getCoordinates() {
        return coordinates;
    }
    public void changeY(int y) {

        /*This method is called when the animation updates. The x position is never changed.
        The y position is updated to reflect the progress of the animation. */

        int originalNumber = (int) (screenHeight/22.864);
        float density = act.getResources()
                .getDisplayMetrics()
                .density;
        int temp = (int) (originalNumber/density);
        int deltaY = PixelConverter.convert(temp, act);
        coordinates.put("middle", middle.changeCoordinate(middle.getX() , y-deltaY));
        coordinates.put("middle", middle.changeCoordinate(middle.getX() , y-deltaY));
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }
}
