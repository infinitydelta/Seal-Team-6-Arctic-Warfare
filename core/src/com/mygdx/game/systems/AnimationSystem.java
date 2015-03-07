package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.components.AnimationComponent;

/**
 * Created by KS on 3/7/2015.
 */
public class AnimationSystem extends IteratingSystem {

    ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);

    public AnimationSystem()
    {
        super (Family.getFor(AnimationComponent.class));
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animationComponent = am.get(entity);
        animationComponent.stateTime += deltaTime;
        TextureRegion currentFrame = animationComponent.animation.getKeyFrame(animationComponent.stateTime, true);
        //currentFrame

    }
}
