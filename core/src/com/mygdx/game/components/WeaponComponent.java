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
    int bullettype;
    
    WeaponGUIComponent wgc;

    public WeaponComponent(Entity entity, int max, float firerate, float reload, int bullettype)
    {
        automatic = false;
        reloading = false;
        this.entity = entity;
        fireRate = firerate;
        reloadtime = reload;
        magSize = max;
        currentclip = magSize;
        firetimer = 0;
        reloadtimer = 0;
        this.bullettype = bullettype;
        
    }

    @Override
    public void reset() {
        automatic = false;

    }
    public void update(float deltatime)
    {
    	if(currentclip == 0 && reloading == false)
    	{
    		reloading = true;
    	}
    	if(reloading)
    	{
    		if(wgc != null) wgc.empty();
    		if(reloadtimer < reloadtime)
    		{
    			reloadtimer += deltatime;
    		}
    		else
    		{
				reloading = false;
				reloadtimer = 0;
				currentclip = magSize;
				if(wgc != null) wgc.reload();
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
    		switch (bullettype) 
    		{
			case 1:
				Factory.createBullet(entity.getComponent(PositionComponent.class).x  , entity.getComponent(PositionComponent.class).y, angleInRad, 30f, GameScreen.networkPlayerNum, null);
				break;
			case 2:
				Factory.createEnemyBullet(entity.getComponent(PositionComponent.class).x  , entity.getComponent(PositionComponent.class).y, angleInRad, 15f, GameScreen.networkPlayerNum, null);
				break;
			default:
				Factory.createBullet(entity.getComponent(PositionComponent.class).x  , entity.getComponent(PositionComponent.class).y, angleInRad, 30f, GameScreen.networkPlayerNum, null);
				break;
			}
            Factory.expl19.play(.3f);
            firetimer = 0;
            currentclip--;
            if(wgc != null) wgc.fire();
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
