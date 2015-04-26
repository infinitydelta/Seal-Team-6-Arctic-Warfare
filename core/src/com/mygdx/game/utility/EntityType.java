package com.mygdx.game.utility;


import com.badlogic.ashley.core.Entity;

/**
 * Created by KS on 4/25/2015.
 */
public class EntityType {
    public Entity e;
    public char type;

    public EntityType(Entity e, char type)
    {
        this.e = e;
        this.type = type;
    }
}
