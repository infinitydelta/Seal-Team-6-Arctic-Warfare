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
        if (low == high) return low;
        if (low > high)
        {
            return Range(high + 1, low + 1);
        }
        else
        {
            return rand.nextInt(high - low) + low;
        }
    }
}
