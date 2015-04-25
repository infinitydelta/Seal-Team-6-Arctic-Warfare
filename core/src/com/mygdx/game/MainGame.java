package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.PositionComponent;
import com.mygdx.game.components.VisualComponent;
import com.mygdx.game.networking.NetworkView;
import com.mygdx.game.systems.InputHandler;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.utility.Factory;

public class MainGame extends Game {

	MenuScreen menuScreen;
	//GameScreen gameScreen;
	
	static Texture kenny;
	static Texture bg_tile;
	public static Texture whiteball;
	static Texture objects;
	public static Texture sandTiles;

	public static Animation runAnimation;
	public static Animation idleAnmation;

	@Override
	public void create () 
	{
		//loadAssets();
		Factory.loadAssets();
		menuScreen = new MenuScreen(this);
		//gameScreen = new GameScreen(this);
		setScreen(menuScreen);

	}

	@Override
	public void render () 
	{
		super.render();
	}

	public void resize(int width, int height)
	{
		

	}
	private void loadAssets()
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
}
