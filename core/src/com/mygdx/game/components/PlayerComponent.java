package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.MainGame;
import com.mygdx.game.utility.Factory;

/**
 * Created by KS on 3/5/2015.
 */
public class PlayerComponent extends Component implements Pool.Poolable {

    //internal stuff
    Entity player;
    public Entity weapon;

    public String name;
    public int color;

    public PositionComponent position;
    public MovementComponent movement;
    public VisualComponent visual;
    public WeaponComponent weaponComponent;

    PositionComponent weaponPosition;
    

    public boolean up = false, down = false, left = false, right = false, reload = false;
    private boolean moveX = false, moveY = false;
    private float root2 = (float) Math.sqrt(2);
    int health = 50;
    public int maxHealth = 50;


    //gameplay values
    float speed = 10f;
    float diagSpeed = speed/root2;


    public PlayerComponent(String name, Entity player, int color)
    {
        this.name = name;
        this.player = player;
        this.color = color;
        position = player.getComponent(PositionComponent.class);
        movement = player.getComponent(MovementComponent.class); //need movement instantiated
        visual = player.getComponent(VisualComponent.class);
    }

    public void addWeapon(Entity weapon)
    {
        this.weapon = weapon;
        weaponPosition = weapon.getComponent(PositionComponent.class);
        weaponComponent = weapon.getComponent(WeaponComponent.class);
    }

    public void update()
    {
        movement.xVel = 0;
        movement.yVel = 0;
        moveX = false;
        moveY = false;

        if (left)
        {
            //if (!visual.sprite.isFlipX()) visual.sprite.flip(true, false);

            movement.xVel = -speed;
            moveX = true;
            movement.moveX = true;
        }
        if (right)
        {
            //if (visual.sprite.isFlipX()) visual.sprite.flip(true, false);
            movement.xVel = speed;
            moveX = true;
            movement.moveX = true;

        }
        if (up)
        {
            if (moveX) {
                movement.yVel = diagSpeed;
                movement.xVel /= root2;

            }
            else movement.yVel = speed;
            moveY = true;
            movement.moveY = true;

        }
        if (down)
        {
            if (moveX) {
                movement.yVel = -diagSpeed;
                movement.xVel /= root2;

            }
            else movement.yVel = -speed;
            moveY = true;
            movement.moveY = true;

        }
        movement.body.setLinearVelocity(movement.xVel, movement.yVel);
        if (!moveY && !moveX) visual.setAnimation(Factory.penguin_animations.get(color));
        else visual.setAnimation(Factory.penguin_walk_animations.get(color));

        if (weaponPosition != null)
        {
            weaponPosition.x = position.x;
            weaponPosition.y = position.y;
        }
        if(reload && weaponComponent != null)
        {
        	weaponComponent.reloading = true;
        }

    }


    @Override
    public void reset() {

    }
    public void takeDamage(int amt)
    {
    	health = Math.max(0, health-amt);
    	if(player.getComponent(HealthGUIComponent.class)!=null)player.getComponent(HealthGUIComponent.class).setValue(health);
    	Factory.hithurt8.play();
    }
    
}
