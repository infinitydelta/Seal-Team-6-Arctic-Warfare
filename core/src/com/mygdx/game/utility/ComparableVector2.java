package com.mygdx.game.utility;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mac on 4/26/2015.
 */
public class ComparableVector2 extends Vector2 implements Comparable<Vector2> {

    public ComparableVector2(){
        super();
    }

    public ComparableVector2(float xp, float yp){
        super(xp,yp);
    }

    @Override
    public int compareTo(Vector2 o) {
        if(this.x == o.x && this.y == o.y){
            return 1;
        }else{
            return -1;
        }
    }
}
