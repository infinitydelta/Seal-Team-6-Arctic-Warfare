package com.mygdx.game.utility;

import java.util.Random;

/**
 * Created by KS on 4/23/2015.
 */
public class RandomInt {

    static Random rand = new Random();

    public static void setSeed(long n)
    {
        rand.setSeed(n);
    }

    public static int Range(int low, int high)
    {
        return rand.nextInt(high - low) + low;
    }
}
