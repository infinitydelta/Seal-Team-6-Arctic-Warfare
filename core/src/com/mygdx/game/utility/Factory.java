package com.mygdx.game.utility;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.components.*;

/**
 * Created by KS on 4/24/2015.
 */
public class Factory {

    //textures
    static Texture kenny;
    static Texture bg_tile;
    public static Texture whiteball;
    public static Texture objects;
    public static Texture sandTiles;

    public static Animation runAnimation;
    public static Animation idleAnmation;





    public static void loadAssets()
    {
        //textures
        kenny = new Texture("p1_stand.png");
        bg_tile = new Texture("blacktile.png");
        whiteball = new Texture("white ball.png");
        objects = new Texture("objects.png");
        sandTiles = new Texture("map.png");

        //animation
        Texture walk = new Texture("minimalObjects_32x32Tiles.png");
        TextureRegion[][] temp = TextureRegion.split(walk, 32, 32); //rows = 4; num cols = 3
        TextureRegion[] walkFrames = new TextureRegion[6];
        TextureRegion[] idleFrames = new TextureRegion[6];

        int index = 0;
        for (int i = 0; i < 1; i++) // column length, number of rows
        {
            for (int j = 0; j < 6; j++) //row length, number of columns
            {
                if (index < 6)
                {
                    //System.out.println("index: " + index);
                    //System.out.println("i: " + i + "; j: " + j);
                    walkFrames[index] = temp[i][j];
                    idleFrames[index] = temp[1][j];
                    idleFrames[index].flip(true, false);
                    index++;
                }
            }
        }
        runAnimation = new Animation(1/6f, walkFrames);

        idleAnmation = new Animation(1/2f, idleFrames);

    }

    public static Entity createPlayer(int x, int y)
    {
        Entity player = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y);
        player.add(p);

        //create a body for the player
        CircleShape circle = new CircleShape();
        circle.setRadius(.48f);
        CollisionComponent col = new CollisionComponent(GameScreen.world, BodyDef.BodyType.DynamicBody, circle, p);

        player.add(new MovementComponent(col, GameScreen.world, 0, 0, 0));

        player.add(new VisualComponent(runAnimation));
        player.add(new PlayerComponent(player));
        GameScreen.pooledEngine.addEntity(player);

        return player;
    }

    public static Entity createWeapon()
    {

        Entity weapon = GameScreen.pooledEngine.createEntity();
        TextureRegion weap = new TextureRegion(objects, 3 * 32, 1 * 32, 32, 32);
        weapon.add(new PositionComponent(0, 0));
        weapon.add(new VisualComponent(weap));
        GameScreen.pooledEngine.addEntity(weapon);

        Entity e = GameScreen.pooledEngine.createEntity();
        e.add(new PositionComponent(0, 0));

        return weapon;

    }
}
