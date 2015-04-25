package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by McLean on 4/24/2015.
 */
public class AIControllerComponent extends Component implements Pool.Poolable {
    static public int h[][];
    public int xIndex;
    public int yIndex;

    public int radius;
    public boolean active = false;

    @Override
    public void reset() {

    }


}
