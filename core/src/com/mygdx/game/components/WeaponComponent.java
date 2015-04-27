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
    public boolean automatic;
    public float fireRate; //shots/sec
    public int magSize;
    public float reloadtime;
    public int weight;
    
    public float firetimer;
    public float reloadtimer;
    public boolean reloading;
    public int currentclip;

    //bullet properties
    public float bulletVelocity;
    public float force;
    Entity bullet;
    
    WeaponGUIComponent wgc;

    public WeaponComponent(Entity entity)
    {
        automatic = false;
        reloading = false;
        this.entity = entity;
        fireRate = 8;
        reloadtime = 1f;
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
				wgc.reload();
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
    		Factory.createBullet(entity.getComponent(PositionComponent.class).x  , entity.getComponent(PositionComponent.class).y, angleInRad, 30f, GameScreen.networkPlayerNum, null);
            Factory.expl19.play();
            firetimer = 0;
            currentclip--;
            wgc.fire();
    	}
    }
    public void setGUIComponent(WeaponGUIComponent wgc)
    {
    	this.wgc = wgc;
    }
    public int getMagSize()
    {
    	return magSize;
    }
}
