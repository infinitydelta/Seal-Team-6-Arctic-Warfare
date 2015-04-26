package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by McLean on 4/24/2015.
 */
public class AIControllerComponent extends Component implements Pool.Poolable {

    public Integer xIndex = new Integer(0); //position indices
    public Integer yIndex = new Integer(0);
    public Integer xTarIndex = new Integer(30); //target position indices
    public Integer yTarIndex = new Integer(30);

    @Override
    public void reset() {

    }


}
