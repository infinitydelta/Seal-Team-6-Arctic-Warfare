package com.mygdx.game.utility;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.GameScreen;

import com.mygdx.game.LoginScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.components.*;

import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.NetworkComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.PositionComponent;
import com.mygdx.game.components.VisualComponent;


/**
 * Created by KS on 4/24/2015.
 */
public class Factory {

    //collision masks

    final static short PLAYER_COL = 0x1;
    final static short ENEMY_COL = 0x1 << 1;
    public final static short PLAYER_PROJ_COL = 0x1 << 2;
    final static short ENEMY_PROJ_COL = 0x1 << 3;
    public final static short WALL = 0x1 << 4;

    //textures
    static Texture kenny;
    static Texture bg_tile;

    public static Texture penguin_walk;
    public static Texture penguin_idle;
    public static Animation penguin_idle_anim;
    public static Animation penguin_walk_anim;



    public static Texture whiteball;
    public static Texture objects;
    public static Texture gun1;
    public static Texture playerbullet;
    public static Texture worldTiles;
    public static Texture ammoElement;

    public static Animation runAnimation;
    public static Animation idleAnmation;

    public static Sound expl19;



    public static void loadAssets()
    {
        //textures
        kenny = new Texture("p1_stand.png");
        bg_tile = new Texture("blacktile.png");
        whiteball = new Texture("white ball.png");
        objects = new Texture("objects.png");
        gun1 = new Texture("gun1.png");
        playerbullet = new Texture("bullet.png");
        worldTiles = new Texture("map2.png");
        penguin_walk = new Texture("penguinWalk.png");
        penguin_idle = new Texture("penguinIdle.png");
        ammoElement = new Texture("ammo.png");
        //sound?
        expl19 = Gdx.audio.newSound(Gdx.files.internal("Sounds/Explosion19.wav"));
        //animation
        Texture walk = new Texture("minimalObjects_32x32Tiles.png");
        TextureRegion[][] temp = TextureRegion.split(walk, 32, 32); //rows = 4; num cols = 3
        TextureRegion[] walkFrames = new TextureRegion[6];
        TextureRegion[] idleFrames = new TextureRegion[6];

        //penguin animations
        TextureRegion[] penguinIdleFrames = new TextureRegion[5];
        TextureRegion[][] penguinTemp = TextureRegion.split(penguin_idle, 32, 32);
        for (int i = 0; i < 5; i ++)
        {
            penguinIdleFrames[i] = penguinTemp[0][i];
        }
        penguin_idle_anim = new Animation(1/8f, penguinIdleFrames);

        TextureRegion[] penguinWalkFrames = new TextureRegion[5];
        TextureRegion[][] penguinWalkTemp = TextureRegion.split(penguin_walk, 32, 32);
        for (int i = 0; i < 5; i ++)
        {
            penguinWalkFrames[i] = penguinWalkTemp[0][i];
        }
        penguin_walk_anim = new Animation(1/15f, penguinWalkFrames);

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

    public static Entity createPlayer(int x, int y, Integer playerNum)
    {
        Entity player = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y);
        player.add(p);

        //create a body for the player
        CircleShape circle = new CircleShape();
        circle.setRadius(.4f);
        short player_col = ENEMY_PROJ_COL | ENEMY_COL | WALL;
        CollisionComponent col = new CollisionComponent(GameScreen.world, BodyDef.BodyType.DynamicBody, circle, PLAYER_COL, player_col, p, player, 'p');

        MovementComponent m = new MovementComponent(col, GameScreen.world, 0, 0, 0);
        player.add(m);

        player.add(new VisualComponent(runAnimation));


        player.add(new PlayerComponent(LoginScreen.username, player));
        player.add(new NetworkComponent("player", GameScreen.networkPlayerNum, player.getId(), p, m));


        GameScreen.pooledEngine.addEntity(player);

        return player;
    }

    public static Entity createWeapon()
    {

        Entity weapon = GameScreen.pooledEngine.createEntity();
        TextureRegion weap = new TextureRegion(gun1, 0 * 32, 0 * 32, 32, 32);
        weapon.add(new PositionComponent(0, 0));
        weapon.add(new VisualComponent(weap));
        GameScreen.pooledEngine.addEntity(weapon);

        Entity e = GameScreen.pooledEngine.createEntity();
        e.add(new PositionComponent(0, 0));

        return weapon;

    }

    public static Entity createBullet(float x, float y, float angle, float vel, Integer playerNum)
    {
        Entity bullet = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y, angle);
        bullet.add(p);
        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(.2f, .1f);
        CircleShape circle = new CircleShape();
        circle.setRadius(.2f);
        
        float xVel = (float) Math.cos(angle) * vel;
        float yVel = (float) Math.sin(angle) * vel;
        short bullet_col = ENEMY_COL | WALL;
        CollisionComponent col = new CollisionComponent(GameScreen.world, BodyDef.BodyType.DynamicBody, circle, PLAYER_PROJ_COL, bullet_col, p, bullet, 'b');
        MovementComponent m = new MovementComponent(col, GameScreen.world, xVel, yVel, 0);
        bullet.add(m);
        //add visual
        TextureRegion b = new TextureRegion(playerbullet, 0, 0, 16, 16);
        VisualComponent vc = new VisualComponent(b);
        vc.sprite.setRotation((float)Math.toDegrees(angle));
        //vc.sprite.setScale(.8f);
        bullet.add(vc);
        //

        bullet.add(new NetworkComponent("player", playerNum, bullet.getId(), p, m));

        GameScreen.pooledEngine.addEntity(bullet);
        return bullet;
        //rectangle.dispose();
        //circle.dispose();
    }

    public static Entity createGround(float x, float y, int type)
    {
        Entity e = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y);
        e.add(p);
        TextureRegion t;
        switch(type)
        {
        case 0:
        	t = new TextureRegion(Factory.worldTiles, 0, 0, 32, 32);
        	break;
        case 1:
        	t = new TextureRegion(Factory.worldTiles, 1*32, 0, 32, 32);
        	break;
        case 2:
        	t = new TextureRegion(Factory.worldTiles, 2*32, 0, 32, 32);
        	break;
        case 3:
        	t = new TextureRegion(Factory.worldTiles, 3*32, 0, 32, 32);
        	break;
        case 4:
        	t = new TextureRegion(Factory.worldTiles, 0, 1*32, 32, 32);
        	break;
        default:
        	t = new TextureRegion(Factory.worldTiles, 0, 0, 32, 32);
        	break;
        }
        e.add(new VisualComponent(t));
        GameScreen.pooledEngine.addEntity(e);
        return e;
    }

    public static Entity createFakeWall(float x, float y)
    {
        Entity e = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y);
        e.add(p);
        TextureRegion t = new TextureRegion(Factory.worldTiles, 6*32, 1 * 32, 32, 32);
        e.add(new VisualComponent(t));
        GameScreen.pooledEngine.addEntity(e);
        return e;
    }

    public static Entity createWall(float x, float y, int type)
    {
        Entity wall = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y);
        wall.add(p);

        PolygonShape square = new PolygonShape();
        square.setAsBox(.5f, .5f);
        short all = PLAYER_COL | PLAYER_PROJ_COL | ENEMY_COL | ENEMY_PROJ_COL;
        CollisionComponent col = new CollisionComponent(GameScreen.world, BodyDef.BodyType.StaticBody, square, WALL, all, p, wall, 'w');

        TextureRegion t;
        switch(type)
        {
        case 0:
        	t = new TextureRegion(Factory.worldTiles, 0*32, 2 * 32, 32, 32);
        	break;
        case 1:
        	t = new TextureRegion(Factory.worldTiles, 5*32, 2 * 32, 32, 32);
        	break;
        case 2:
        	t = new TextureRegion(Factory.worldTiles, 6*32, 2 * 32, 32, 32);
        	break;
        case 3:
        	t = new TextureRegion(Factory.worldTiles, 7*32, 2 * 32, 32, 32);
        	break;
        case 4:
        	t = new TextureRegion(Factory.worldTiles, 5*32, 1 * 32, 32, 32);
        	break;
        case 5:
        	t = new TextureRegion(Factory.worldTiles, 6*32, 1 * 32, 32, 32);
        	break;
        case 6:
        	t = new TextureRegion(Factory.worldTiles, 7*32, 1 * 32, 32, 32);
        	break;
        case 7:
        	t = new TextureRegion(Factory.worldTiles, 5*32, 0 * 32, 32, 32);
        	break;
        case 8:
        	t = new TextureRegion(Factory.worldTiles, 6*32, 0 * 32, 32, 32);
        	break;
        case 9:
        	t = new TextureRegion(Factory.worldTiles, 7*32, 0 * 32, 32, 32);
        	break;
        case 10:
        	t = new TextureRegion(Factory.worldTiles, 1*32, 2 * 32, 32, 32);
        	break;
        case 11:
        	t = new TextureRegion(Factory.worldTiles, 2*32, 2 * 32, 32, 32);
        	break;
        case 12:
        	t = new TextureRegion(Factory.worldTiles, 3*32, 2 * 32, 32, 32);
        	break;
        case 13:
        	t = new TextureRegion(Factory.worldTiles, 4*32, 2 * 32, 32, 32);
        	break;
        case 14:
        	t = new TextureRegion(Factory.worldTiles, 3*32, 1 * 32, 32, 32);
        	break;
        case 15:
        	t = new TextureRegion(Factory.worldTiles, 4*32, 1 * 32, 32, 32);
        	break;
        default:
        	t = new TextureRegion(Factory.worldTiles, 4*32, 2 * 32, 32, 32);
        	break;
        }
        wall.add(new VisualComponent(t));
        GameScreen.pooledEngine.addEntity(wall);

        return wall;
    }



}
