package com.tuplv.mynote;

import android.graphics.Color;

public class InvertingColor {
    public static int invertingColor(int color) {
        int invertedRed = 255 - Color.red(color);
        int invertedGreen = 255 - Color.green(color);
        int invertedBlue = 255 - Color.blue(color);

        return Color.rgb(invertedRed, invertedGreen, invertedBlue);
    }
}
