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
    public Float speed = new Float(1);
    public float lastdx;
    public boolean mode = true; //true = attack, false = flee
    public boolean randWalk; //true = on, false = off
    boolean dead = false;

    public AIControllerComponent(Float speed) {
		this.speed = speed;
	}
    
    @Override
    public void reset() {

    }


}
