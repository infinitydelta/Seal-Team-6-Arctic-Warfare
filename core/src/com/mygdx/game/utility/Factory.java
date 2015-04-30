package com.mygdx.game.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.audio.Music;

import javafx.scene.shape.Line;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.GameScreen;
import com.mygdx.game.LoginScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.components.*;
import com.mygdx.game.utility.RandomInt;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;


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

    public static Texture red_penguin_walk;
    public static Texture red_penguin_idle;
    public static Animation red_penguin_idle_anim;
    public static Animation red_penguin_walk_anim;

    public static Texture purple_penguin_walk;
    public static Texture purple_penguin_idle;
    public static Animation purple_penguin_idle_anim;
    public static Animation purple_penguin_walk_anim;
    
    public static Texture orange_penguin_walk;
    public static Texture  orange_penguin_idle;
    public static Animation orange_penguin_idle_anim;
    public static Animation orange_penguin_walk_anim;
    
    public static Texture green_penguin_walk;
    public static Texture green_penguin_idle;
    public static Animation green_penguin_idle_anim;
    public static Animation green_penguin_walk_anim;
    
    public static ArrayList<Texture> penguin_textures;
    public static ArrayList<Animation> penguin_animations;
    public static ArrayList<Animation> penguin_walk_animations;
    
    public static Texture seal_walk;
    public static Texture seal_idle;
    public static Animation seal_idle_anim;
    public static Animation seal_walk_anim;
    public static Texture seal_die;
    public static Animation seal_die_anim;


    public static Texture whiteball;
    public static Texture objects;
    public static Texture gun1;
    public static Texture gun2;
    public static Texture playerbullet;
    public static Texture enemybullet;
    public static Texture worldTiles;
    public static Texture ammoElement;
    public static Texture heart;
    public static Texture hfront;
    public static Texture hback;
    public static Texture mag;

    public static Animation runAnimation;
    public static Animation idleAnmation;
    
    public static Texture bulletDestroy;
    public static Animation bulletDestroyAnim;
    public static Texture enemyBulletDestroy;
    public static Animation enemyBulletDestroyAnim;
    
    public static Texture menuBG;

    public static Sound expl19;

    public static Sound reload;

    public static Sound hithurt8;

    public static Music paris;

    public static void loadAssets()
    {
    	penguin_animations = new ArrayList<Animation>();
    	penguin_walk_animations = new ArrayList<Animation>();
    	penguin_textures = new ArrayList<Texture>();
    	
        //textures
        kenny = new Texture("p1_stand.png");
        bg_tile = new Texture("blacktile.png");
        whiteball = new Texture("white ball.png");
        objects = new Texture("objects.png");
        gun1 = new Texture("gun1.png");
        gun2 = new Texture("gun2.png");
        playerbullet = new Texture("bullet.png");
        enemybullet = new Texture("enemybullet.png");
        worldTiles = new Texture("map2.png");
        penguin_walk = new Texture("penguinWalk.png");
        penguin_idle = new Texture("penguinIdle.png");
        penguin_textures.add(penguin_idle);
        seal_walk = new Texture("sealWalk.png");
        seal_idle = new Texture("sealIdle.png");
        seal_die = new Texture("sealDie.png");
        ammoElement = new Texture("ammo.png");
        heart = new Texture("heart.png");
        hfront = new Texture("red.png");
        hback = new Texture("hback.png");
        bulletDestroy = new Texture("bulletdestroy.png");
        enemyBulletDestroy = new Texture("enemybulletdestroy.png");
        
        menuBG = new Texture("intro_screen.png");
        mag = new Texture("mag.png");


        //colored pengus
        red_penguin_walk = new Texture("redPenguinWalk.png");
        red_penguin_idle = new Texture("redPenguinIdle.png");
        penguin_textures.add(red_penguin_idle);

        purple_penguin_walk = new Texture("purplePenguinWalk.png");
        purple_penguin_idle = new Texture("purplePenguinIdle.png");
        penguin_textures.add(purple_penguin_idle);
        
        orange_penguin_walk = new Texture("orangePenguinWalk.png");
        orange_penguin_idle = new Texture("orangePenguinIdle.png");
        penguin_textures.add(orange_penguin_idle);
        
        green_penguin_walk = new Texture("greenPenguinWalk.png");
        green_penguin_idle = new Texture("greenPenguinIdle.png");
        penguin_textures.add(green_penguin_idle);
        
        
        
        //sound?
        expl19 = Gdx.audio.newSound(Gdx.files.internal("Sounds/Explosion19.wav"));

        reload = Gdx.audio.newSound(Gdx.files.internal("Sounds/m1_garand_ping.mp3"));

        hithurt8 = Gdx.audio.newSound(Gdx.files.internal("Sounds/Hit_Hurt8.wav"));

        paris = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Hotline Miami OST - Paris.mp3"));

        //animation
        Texture walk = new Texture("minimalObjects_32x32Tiles.png");
        TextureRegion[][] temp = TextureRegion.split(walk, 32, 32); //rows = 4; num cols = 3
        TextureRegion[] walkFrames = new TextureRegion[6];
        TextureRegion[] idleFrames = new TextureRegion[6];
        
        //bullet animation
        TextureRegion[] bulletframes = new TextureRegion[8];
        TextureRegion[][] bulletTemp = TextureRegion.split(bulletDestroy, 16, 16);
        for(int i = 0; i < 8; i++)
        {
        	bulletframes[i] = bulletTemp[0][i];
        }
        bulletDestroyAnim = new Animation(1/60f, bulletframes);
        bulletDestroyAnim.setPlayMode(PlayMode.NORMAL);
        
        TextureRegion[] enemybulletframes = new TextureRegion[8];
        TextureRegion[][] enemybulletTemp = TextureRegion.split(enemyBulletDestroy, 16, 16);
        for(int i = 0; i < 8; i++)
        {
        	enemybulletframes[i] = enemybulletTemp[0][i];
        }
        enemyBulletDestroyAnim = new Animation(1/60f, enemybulletframes);
        enemyBulletDestroyAnim.setPlayMode(PlayMode.NORMAL);

        //penguin animations
        penguin_idle_anim = makePenguIdleAnim(penguin_idle);
        penguin_animations.add(penguin_idle_anim);
        red_penguin_idle_anim = makePenguIdleAnim(red_penguin_idle);
        penguin_animations.add(red_penguin_idle_anim);
        purple_penguin_idle_anim = makePenguIdleAnim(purple_penguin_idle);
        penguin_animations.add(purple_penguin_idle_anim);
        orange_penguin_idle_anim = makePenguIdleAnim(orange_penguin_idle);
        penguin_animations.add(orange_penguin_idle_anim);
        green_penguin_idle_anim = makePenguIdleAnim(green_penguin_idle);
        penguin_animations.add(green_penguin_idle_anim);
        
        penguin_walk_anim = makePenguWalkAnim(penguin_walk);
        penguin_walk_animations.add(penguin_walk_anim);
        red_penguin_walk_anim = makePenguWalkAnim(red_penguin_walk);
        penguin_walk_animations.add(red_penguin_walk_anim);
        purple_penguin_walk_anim = makePenguWalkAnim(purple_penguin_walk);
        penguin_walk_animations.add(purple_penguin_walk_anim);
        orange_penguin_walk_anim = makePenguWalkAnim(orange_penguin_walk);
        penguin_walk_animations.add(orange_penguin_walk_anim);
        green_penguin_walk_anim = makePenguWalkAnim(green_penguin_walk);
        penguin_walk_animations.add(green_penguin_walk_anim);

      //seel animations
        TextureRegion[] sealIdleFrames = new TextureRegion[5];
        TextureRegion[][] sealTemp = TextureRegion.split(seal_idle, 32, 32);
        for (int i = 0; i < 5; i ++)
        {
            sealIdleFrames[i] = sealTemp[0][i];
        }
        seal_idle_anim = new Animation(1/8f, sealIdleFrames);

        TextureRegion[] sealWalkFrames = new TextureRegion[5];
        TextureRegion[][] sealWalkTemp = TextureRegion.split(seal_walk, 32, 32);
        for (int i = 0; i < 5; i ++)
        {
            sealWalkFrames[i] = sealWalkTemp[0][i];
        }
        seal_walk_anim = new Animation(1/15f, sealWalkFrames);

        TextureRegion[] sealDieFrames = new TextureRegion[5];
        TextureRegion[][] dieTemp = TextureRegion.split(seal_die, 32, 32);
        for (int i = 0; i < 5; i ++)
        {
            sealDieFrames[i] = dieTemp[0][i];
        }
        seal_die_anim = new Animation(1/8f, sealDieFrames);
        seal_die_anim.setPlayMode(Animation.PlayMode.NORMAL);
        
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
    
    private static Animation makePenguIdleAnim(Texture pengu){
    	TextureRegion[] penguinIdleFrames = new TextureRegion[5];
        TextureRegion[][] penguinTemp = TextureRegion.split(pengu, 32, 32);
        for (int i = 0; i < 5; i ++)
        {
            penguinIdleFrames[i] = penguinTemp[0][i];
        }
        return new Animation(1/8f, penguinIdleFrames);
    }
    
    private static Animation makePenguWalkAnim(Texture pengu){
    	TextureRegion[] penguinWalkFrames = new TextureRegion[5];
        TextureRegion[][] penguinWalkTemp = TextureRegion.split(pengu, 32, 32);
        for (int i = 0; i < 5; i ++)
        {
            penguinWalkFrames[i] = penguinWalkTemp[0][i];
        }
        return new Animation(1/15f, penguinWalkFrames);
    }


    public static Entity createPlayer(float x, float y, Integer playerNum, Long id)

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
        
        player.add(new VisualComponent(penguin_animations.get(playerNum%5)));

        player.add(new PlayerComponent(LoginScreen.username, player, playerNum%5));
        
        if (id == null) {
        	id = player.getId();
        	HashMap<String, Object> newEntData = new HashMap<String, Object>();
        	if (GameScreen.networkPlayerNum == 0) {
    			newEntData.put("type", "player");
    			newEntData.put("playerNum", playerNum);
    			newEntData.put("ownerID", id);
    			newEntData.put("xPos", x);
    			newEntData.put("yPos", y);
    			newEntData.put("xVel", 0f);
    			newEntData.put("yVel", 0f);
    			//GameScreen.allEntities.add(newEntData);
            }
            //GameScreen.myEntities.add(newEntData);
        }
        player.add(new NetworkComponent("player", playerNum, id, p, m));
        
        Entity weapon = Factory.createWeapon(1, playerNum, id);
        player.getComponent(PlayerComponent.class).addWeapon(weapon);


        GameScreen.pooledEngine.addEntity(player);
        GameScreen.pooledEngine.addEntity(weapon);
        
        HashMap<String, Object> newEntData = new HashMap<String, Object>();
		newEntData.put("type", "player");
		newEntData.put("playerNum", playerNum);
		newEntData.put("ownerID", id);
		newEntData.put("xPos", x);
		newEntData.put("yPos", y);
		newEntData.put("xVel", 0f);
		newEntData.put("yVel", 0f);
		GameScreen.allEntities.add(newEntData);

        return player;
    }
    
    public static Entity createSeal(float x, float y, Integer playerNum, Long id)
    {
        Entity seal = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y);
        seal.add(p);

        //create a body for the seal
        CircleShape circle = new CircleShape();
        circle.setRadius(.4f);
        short seal_col = PLAYER_COL | PLAYER_PROJ_COL | WALL | ENEMY_COL;
        CollisionComponent col = new CollisionComponent(GameScreen.world, BodyDef.BodyType.DynamicBody, circle, ENEMY_COL, seal_col, p, seal, 'e');

        MovementComponent m = new MovementComponent(col, GameScreen.world, 0, 0, 0);

        seal.add(m);
        seal.add(new EnemyComponent());
        seal.add(new VisualComponent(seal_idle_anim));
        seal.add(new AIControllerComponent(-1.5f));
        
        if (id == null) {
        	id = seal.getId();
        	HashMap<String, Object> newEntData = new HashMap<String, Object>();
        	if (GameScreen.networkPlayerNum == 0) {
    			newEntData.put("type", "seal");
    			newEntData.put("playerNum", playerNum);
    			newEntData.put("ownerID", id);
    			newEntData.put("xPos", x);
    			newEntData.put("yPos", y);
    			newEntData.put("xVel", 0f);
    			newEntData.put("yVel", 0f);
    			//GameScreen.allEntities.add(newEntData);
            }
            //GameScreen.myEntities.add(newEntData);
        }
        seal.add(new NetworkComponent("seal", playerNum, id, p, m));
        
        Entity weapon = Factory.createWeapon(2, playerNum, id);
        seal.getComponent(EnemyComponent.class).weapon = weapon;
        
        GameScreen.pooledEngine.addEntity(seal);
        GameScreen.pooledEngine.addEntity(weapon);

        HashMap<String, Object> newEntData = new HashMap<String, Object>();
		newEntData.put("type", "seal");
		newEntData.put("playerNum", playerNum);
		newEntData.put("ownerID", id);
		newEntData.put("xPos", x);
		newEntData.put("yPos", y);
		newEntData.put("xVel", 0f);
		newEntData.put("yVel", 0f);
		GameScreen.allEntities.add(newEntData);
        
        return seal;
    }

    public static Entity createNetworkPlayer(float x, float y, Integer playerNum, Long id, int color)
    {
        Entity networkPlayer = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y);
        networkPlayer.add(p);

        //create a body for the player
        CircleShape circle = new CircleShape();
        circle.setRadius(.4f);
        short player_col = ENEMY_PROJ_COL | ENEMY_COL | WALL;
        CollisionComponent col = new CollisionComponent(GameScreen.world, BodyDef.BodyType.DynamicBody, circle, PLAYER_COL, player_col, p, networkPlayer, 'p');

        MovementComponent m = new MovementComponent(col, GameScreen.world, 0, 0, 0);
        networkPlayer.add(m);

        
        TextureRegion b = new TextureRegion(penguin_textures.get(color), 0, 0, 32, 32);
        networkPlayer.add(new VisualComponent(b));

        if (id == null) {
        	id = networkPlayer.getId();
        }
        networkPlayer.add(new NetworkComponent("player", playerNum, id, p, m));
        
        Entity weapon = Factory.createWeapon(1, playerNum, id);
        networkPlayer.getComponent(PlayerComponent.class).addWeapon(weapon);

        HashMap<String, Object> newEntData = new HashMap<String, Object>();
		newEntData.put("type", "player");
		newEntData.put("playerNum", playerNum);
		newEntData.put("ownerID", id);
		newEntData.put("xPos", x);
		newEntData.put("yPos", y);
		newEntData.put("xVel", 0f);
		newEntData.put("yVel", 0f);
		GameScreen.allEntities.add(newEntData);

        return networkPlayer;
    }

    public static Entity createWeapon(int type, Integer playerNum, Long id)
    {

        Entity weapon = GameScreen.pooledEngine.createEntity();
        PositionComponent pc = new PositionComponent(0, 0);
        weapon.add(pc);
        
        switch(type)
        {
        case 1:
	        TextureRegion weap = new TextureRegion(gun1, 0 * 32, 0 * 32, 32, 32);
	        
	        weapon.add(new VisualComponent(weap));
	        int max = 20;
	        if(LoginScreen.guest)
	        {
        		max = 10;
        	}
        	weapon.add(new WeaponComponent(weapon, max, 8f, .8f, type));
        	break;
        case 2:
        	TextureRegion weap2 = new TextureRegion(gun2, 0 * 32, 0 * 32, 32, 32);
	        weapon.add(new VisualComponent(weap2));
        	weapon.add(new WeaponComponent(weapon, 10, 6f, 1f, type));
        	break;
        default:
        	TextureRegion weapd = new TextureRegion(gun1, 0 * 32, 0 * 32, 32, 32);
	        
	        weapon.add(new VisualComponent(weapd));
	        int maxd = 20;
	        if(LoginScreen.guest)
	        {
        		max = 10;
        	}
        	weapon.add(new WeaponComponent(weapon, maxd, 8f, 1f, type));
        	break;
        }
        
        //GameScreen.pooledEngine.addEntity(weapon);
        
        HashMap<String, Object> newEntData = new HashMap<String, Object>();
		newEntData.put("type", "weapon");
		newEntData.put("playerNum", playerNum);
		newEntData.put("ownerID", id);
		newEntData.put("xPos", pc.x);
		newEntData.put("yPos", pc.y);
		newEntData.put("xVel", 0f);
		newEntData.put("yVel", 0f);
		GameScreen.allEntities.add(newEntData);

        return weapon;

    }

    public static Entity createBullet(float x, float y, float angle, float vel, Integer playerNum, Long id)
    {
        Entity bullet = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y, angle);
        bullet.add(p);
        CircleShape circle = new CircleShape();
        circle.setRadius(.2f);
        float xVel = (float) Math.cos(angle) * vel;
        float yVel = (float) Math.sin(angle) * vel;
        short bullet_col = ENEMY_COL | WALL;
        synchronized (GameScreen.world) {
        	CollisionComponent col = new CollisionComponent(GameScreen.world, BodyDef.BodyType.DynamicBody, circle, PLAYER_PROJ_COL, bullet_col, p, bullet, 'b');
	        MovementComponent m = new MovementComponent(col, GameScreen.world, xVel, yVel, 0);
	        bullet.add(m);
	        //add visual
	        TextureRegion b = new TextureRegion(playerbullet, 0, 0, 16, 16);
	        VisualComponent vc = new VisualComponent(b);
	
	        vc.rotation = ((float)Math.toDegrees(angle));
	        //vc.sprite.setScale(.8f);
	        bullet.add(vc);
	        //
	
	        if (id == null) {
	        	id = bullet.getId();
	        }
	        bullet.add(new NetworkComponent("bullet", playerNum, id, p, m));
	        GameScreen.pooledEngine.addEntity(bullet);
	        
	        HashMap<String, Object> newEntData = new HashMap<String, Object>();
	        newEntData.put("type", "bullet");
			newEntData.put("playerNum", playerNum);
			newEntData.put("ownerID", id);
			newEntData.put("xPos", x);
			newEntData.put("yPos", y);
			newEntData.put("xVel", 0f);
			newEntData.put("yVel", 0f);
			GameScreen.allEntities.add(newEntData);
			if (GameScreen.networkPlayerNum == playerNum) {
				GameScreen.myEntities.add(newEntData);
			}
        }
        return bullet;
        //rectangle.dispose();
        //circle.dispose();
    }
    
    public static Entity createEnemyBullet(float x, float y, float angle, float vel, Integer playerNum, Long id)
    {
        Entity ebullet = GameScreen.pooledEngine.createEntity();
        PositionComponent p = new PositionComponent(x, y, angle);
        ebullet.add(p);
        CircleShape circle = new CircleShape();
        circle.setRadius(.2f);
        float xVel = (float) Math.cos(angle) * vel;
        float yVel = (float) Math.sin(angle) * vel;
        short bullet_col = PLAYER_COL | WALL;
        synchronized (GameScreen.world) {
        	CollisionComponent col = new CollisionComponent(GameScreen.world, BodyDef.BodyType.DynamicBody, circle, ENEMY_PROJ_COL, bullet_col, p, ebullet, 'b');
	        MovementComponent m = new MovementComponent(col, GameScreen.world, xVel, yVel, 0);
	        ebullet.add(m);
	        //add visual
	        TextureRegion b = new TextureRegion(enemybullet, 0, 0, 16, 16);
	        VisualComponent vc = new VisualComponent(b);
	
	        vc.rotation = ((float)Math.toDegrees(angle));
	        //vc.sprite.setScale(.8f);
	        ebullet.add(vc);
	        //
	
	        if (id == null) 
	        {
	        	id = ebullet.getId();
	        }
	        ebullet.add(new NetworkComponent("enemybullet", playerNum, id, p, m));
	        GameScreen.pooledEngine.addEntity(ebullet);
	        
	        HashMap<String, Object> newEntData = new HashMap<String, Object>();
	        newEntData.put("type", "enemybullet");
			newEntData.put("playerNum", playerNum);
			newEntData.put("ownerID", id);
			newEntData.put("xPos", x);
			newEntData.put("yPos", y);
			newEntData.put("xVel", 0f);
			newEntData.put("yVel", 0f);
			GameScreen.allEntities.add(newEntData);
			if (GameScreen.networkPlayerNum == playerNum) {
				GameScreen.myEntities.add(newEntData);
			}
        }
        return ebullet;
        //rectangle.dispose();
        //circle.dispose();
    }

    public static Entity createMag(float x, float y)
    {
        Entity mag = GameScreen.pooledEngine.createEntity();
        float angle = RandomInt.Range(0, 360);
        angle = (float) Math.toRadians(angle);
        PositionComponent p = new PositionComponent(x, y, angle);
        mag.add(p);
        VisualComponent v = new VisualComponent(new TextureRegion(Factory.mag));
        mag.add(v);
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(.1f, .05f);
        CollisionComponent c = new CollisionComponent(GameScreen.world, BodyDef.BodyType.DynamicBody, rect, PLAYER_PROJ_COL, WALL, p, mag, 'm');
        MovementComponent m = new MovementComponent(c, GameScreen.world, (float)Math.cos(angle), (float) Math.sin(angle), 0);
        //m.body.applyAngularImpulse(100, true);
        //m.body.applyLinearImpulse((float)Math.cos(angle), (float) Math.sin(angle), 0, 0, true);
        mag.add(m);
        GameScreen.pooledEngine.addEntity(mag);
        return mag;
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
        case 5:
        	t = new TextureRegion(Factory.worldTiles, 0, 0, 32, 32);
        	break;
        case 6:
        	t = new TextureRegion(Factory.worldTiles, 4*32, 0, 32, 32);
        	break;
        case 7:
        	t = new TextureRegion(Factory.worldTiles, 1*32, 1*32, 32, 32);
        	break;
        case 8:
        	t = new TextureRegion(Factory.worldTiles, 2*32, 1*32, 32, 32);
        	break;
        default:
        	t = new TextureRegion(Factory.worldTiles, 0, 0, 32, 32);
        	break;
        }
        e.add(new VisualComponent(t));
        GameScreen.pooledEngine.addEntity(e);
        GameScreen.map.add(e);
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
        GameScreen.map.add(e);

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
        wall.add(col);
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
        GameScreen.map.add(wall);

        return wall;
    }
    public static Entity createBulletDestroyed(float x, float y, float rotation)
    {
    	Entity bulletDestroyed = new Entity();
    	
    	PositionComponent p = new PositionComponent(x, y);
    	bulletDestroyed.add(p);
    	
    	VisualComponent vc = new VisualComponent(Factory.bulletDestroyAnim);
    	vc.animated = true;
    	vc.rotation = rotation;
    	vc.destroyAfterPlay = true;
    	vc.stateTime = 0f;
    	vc.animation.setPlayMode(PlayMode.NORMAL);
    	vc.sprite.setOriginCenter();
    	bulletDestroyed.add(vc);
    	GameScreen.pooledEngine.addEntity(bulletDestroyed);
    	
    	return bulletDestroyed;
    }
    public static Entity createEnemyBulletDestroyed(float x, float y, float rotation)
    {
    	Entity bulletDestroyed = new Entity();
    	
    	PositionComponent p = new PositionComponent(x, y);
    	bulletDestroyed.add(p);
    	
    	VisualComponent vc = new VisualComponent(Factory.enemyBulletDestroyAnim);
    	vc.animated = true;
    	vc.rotation = rotation;
    	vc.destroyAfterPlay = true;
    	vc.stateTime = 0f;
    	vc.animation.setPlayMode(PlayMode.NORMAL);
    	vc.sprite.setOriginCenter();
    	bulletDestroyed.add(vc);
    	GameScreen.pooledEngine.addEntity(bulletDestroyed);
    	
    	return bulletDestroyed;
    }
    
    public static Entity createDeadSeal(float x, float y)
    {
    	Entity deadSeal = new Entity();
    	
    	PositionComponent p = new PositionComponent(x, y);
    	deadSeal.add(p);
    	
    	VisualComponent vc = new VisualComponent(Factory.seal_die_anim);
    	vc.animated = true;
    	vc.playOneShot = true;
    	vc.stateTime = 0f;
    	vc.animation.setPlayMode(PlayMode.NORMAL);
    	deadSeal.add(vc);
    	
    	GameScreen.pooledEngine.addEntity(deadSeal);
    	return deadSeal;
    }



}