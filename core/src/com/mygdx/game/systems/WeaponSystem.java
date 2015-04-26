package com.mygdx.game.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.WeaponComponent;

public class WeaponSystem extends IteratingSystem
{
	private ComponentMapper<WeaponComponent> wcMapper = ComponentMapper.getFor(WeaponComponent.class);
	public WeaponSystem()
	{
		super(Family.getFor(WeaponComponent.class));
	}
	
	public void processEntity(Entity entity, float deltatime)
	{
		WeaponComponent wc = wcMapper.get(entity);
		wc.update(deltatime);
	}

}
