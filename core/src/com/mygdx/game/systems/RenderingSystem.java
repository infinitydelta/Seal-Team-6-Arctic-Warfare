package com.mygdx.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.components.PositionComponent;
import com.mygdx.game.components.VisualComponent;

/**
 * Created by KS on 3/4/2015.
 */
public class RenderingSystem extends IteratingSystem {

    private ImmutableArray<Entity> entities;

    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;

    private ComponentMapper<VisualComponent> vm = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);


    public RenderingSystem(OrthographicCamera camera)
    {
        super(Family.getFor(VisualComponent.class, PositionComponent.class));
        spriteBatch = new SpriteBatch();
        this.camera = camera;

    }

    public void addedToEngine(Engine engine)
    {
        entities = engine.getEntitiesFor(Family.getFor(VisualComponent.class, PositionComponent.class));
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void update(float deltaTime)
    {
        PositionComponent position;
        VisualComponent visual;

        camera.update();

        spriteBatch.begin();

        spriteBatch.setProjectionMatrix(camera.combined);
        for (int i = 0; i < entities.size(); i++) //shouldnt this be i=1
        {
            Entity e = entities.get(i);
            position = pm.get(e);
            visual = vm.get(e);

            if (visual.animated) //if visual component is animated
            {

                visual.stateTime += deltaTime;
                TextureRegion currentFrame = visual.animation.getKeyFrame(visual.stateTime, true);
                boolean flip = visual.sprite.isFlipX();
                visual.sprite = new Sprite(currentFrame);
                visual.sprite.flip(flip, false);

                visual.sprite.setSize (visual.sprite.getWidth()/visual.sprite.getHeight(), visual.sprite.getHeight()/visual.sprite.getHeight());
                //spriteBatch.draw(currentFrame, position.x, position.y);
                visual.stateTime %= 100000;
            }
            if (visual.sprite != null) {
            	visual.sprite.setRotation(visual.rotation);
                visual.sprite.setPosition(position.x, position.y);
                visual.sprite.draw(spriteBatch);
            }

        }

        spriteBatch.end();
    }
}
