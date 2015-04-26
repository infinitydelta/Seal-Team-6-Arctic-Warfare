package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by KS on 3/14/2015.
 */
public class WeaponComponent extends Component implements Pool.Poolable {

    //weapon properties
    boolean automatic;
    int fireRate;
    int magSize;
    int weight;

    //bullet properties
    float bulletVelocity;
    float force;

    public WeaponComponent()
    {
        automatic = false;
    }

    @Override
    public void reset() {
        automatic = false;

    }
}
