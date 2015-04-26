package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by KS on 3/4/2015.
 */
public class VisualComponent extends Component implements Pool.Poolable {

    public TextureRegion region;
    public Sprite sprite;
    public Animation animation;

    public boolean animated = false;
    public float stateTime = 0;

    public VisualComponent(TextureRegion region)
    {
        this.region = region;
        sprite = new Sprite(region);
        float w = sprite.getWidth()/sprite.getHeight();
        float h = sprite.getHeight()/sprite.getHeight();
        sprite.setSize(w, h); //in world units
        sprite.setOrigin(w/2, h/2);
    }
    //public VisualComponent(Sprite sprite) {this.sprite = sprite;}
    public VisualComponent(Animation animation)
    {
        animated = true;
        this.animation = animation;
        sprite = new Sprite(animation.getKeyFrame(0));


    }

    public void setAnimation(Animation animation)
    {
        animated = true;
        this.animation = animation;
        boolean flip = false;
        if (sprite != null) {
             flip = sprite.isFlipX();
        }
        sprite = new Sprite(animation.getKeyFrame(0));
        sprite.flip(flip, false);
    }


    @Override
    public void reset() {
        if (region != null)
        {
            region.getTexture().dispose();
            region = null;
        }
        if (sprite != null)
        {
            sprite.getTexture().dispose();
            sprite = null;
        }
        if (animation != null)
        {
            animation = null;
        }
        animated = false;
        stateTime = 0;
    }

}
