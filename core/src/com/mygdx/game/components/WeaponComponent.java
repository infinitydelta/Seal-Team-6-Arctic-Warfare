package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.GameScreen;
import com.mygdx.game.utility.Factory;

/**
 * Created by KS on 3/14/2015.
 */
public class WeaponComponent extends Component implements Pool.Poolable 
{
	Entity entity;

    //weapon properties
    boolean automatic;
    float fireRate; //shots/sec
    int magSize;
    float reloadtime;
    int weight;
    
    float firetimer;
    float reloadtimer;
    boolean reloading;
    int currentclip;

    //bullet properties
    float bulletVelocity;
    float force;
    Entity bullet;

    public WeaponComponent(Entity entity)
    {
        automatic = false;
        reloading = false;
        this.entity = entity;
        fireRate = 4;
        reloadtime = .75f;
        magSize = 20;
        currentclip = magSize;
        firetimer = 0;
        reloadtimer = 0;
        
    }

    @Override
    public void reset() {
        automatic = false;

    }
    public void update(float deltatime)
    {
    	if(currentclip == 0)
    	{
    		reloading = true;
    	}
    	if(reloading)
    	{
    		if(reloadtimer < reloadtime)
    		{
    			reloadtimer += deltatime;
    		}
    		else
    		{
				reloading = false;
				reloadtimer = 0;
				currentclip = magSize;
			}
    	}
    	else if(firetimer < 1/fireRate)
    	{
    		firetimer += deltatime;
    	}
    }
    public void fire(float angleInRad)
    {
    	if(!reloading && firetimer > 1/fireRate)
    	{
    		Factory.createBullet(entity.getComponent(PositionComponent.class).x  , entity.getComponent(PositionComponent.class).y, angleInRad, 50f, GameScreen.networkPlayerNum);
            Factory.expl19.play();
            firetimer = 0;
            currentclip--;
    	}
    }
}
