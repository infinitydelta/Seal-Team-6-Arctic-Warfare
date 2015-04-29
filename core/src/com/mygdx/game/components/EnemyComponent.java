package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by McLean on 4/28/2015.
 */
public class EnemyComponent extends Component 
{
    public int health = 3;
    public Entity weapon;
}