package com.example.stockingstuffer.helpers;

import android.content.Context;

public class PixelConverter {

    // pixels = dp * density
    // dp = pixels/density

    public static int convert(int dp, Context c) {
        float density = c.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }


}