package com.google.firebase.udacity.friendlychat;

/**
 * Created by Jesse on 6/23/2017.
 */
import java.util.Random;
import android.graphics.Color;
import android.util.Log;

public class ColorWheel {
    public String[] mColors = {
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7"  // light gray
    };
    //field or member variables

    //Methods

    public int getColor() {


        String color;
        //Randomly select a fact

        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mColors.length);
        color = mColors[randomNumber];
        int colorAsInt= Color.parseColor(color);
        return colorAsInt;
    }

    public int getColor(String user){
        String color;
        //Randomly select a fact
        int num = 0;
        for(int i = 0; i < user.length(); i++){
            num+=user.charAt(i)*i;
        }
        color = mColors[num%mColors.length];
        int colorAsInt= Color.parseColor(color);
        Log.e("---------", "color should be "+colorAsInt);
        return colorAsInt;
    }

}
