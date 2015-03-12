package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by KS on 3/11/2015.
 */
public class Cursor extends Actor {

    int x, y;
    float w, h;

    TextureRegion region;
    Sprite sprite;

    public Cursor()
    {
        region = new TextureRegion(new Texture("white ball.png"));
        sprite = new Sprite(region);
        x = 0;
        y = 0;
        w = sprite.getWidth();
        h = sprite.getHeight();

        sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    }

    public void setPostion(int x, int y)
    {
        this.x = x;
        this.y = y;
        sprite.setPosition(x - w/2, y - h/2);
    }

    public void draw(Batch batch, float parentAlpha)
    {
        //batch.draw(region, x, y);
        sprite.draw(batch);
    }
}
