package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.PositionComponent;
import com.mygdx.game.components.VisualComponent;
import com.mygdx.game.systems.InputHandler;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerSystem;
import com.mygdx.game.systems.RenderingSystem;

public class MainGame extends ApplicationAdapter {

	static final int WORLD_WIDTH = 100;
	static final int WORLD_HEIGHT = 100;

	OrthographicCamera camera;
	FitViewport viewport;

	PooledEngine pooledEngine;
	InputHandler input;

	Entity player;
	Entity cursor;

	Texture kenny;
	Texture bg_tile;
	Texture whiteball;

	public static Animation runAnimation;
	public static Animation idleAnmation;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(20, 20 * (h/w) );
		viewport = new FitViewport(20, 20 * (h/w), camera); // 20 world units wide
		camera.position.set(0, 0, 0);
		camera.update();


		loadAssets();


		//engine
		pooledEngine = new PooledEngine(10, 1000, 10, 5000);
		pooledEngine.addSystem(new PlayerSystem());
		pooledEngine.addSystem(new MovementSystem());
		pooledEngine.addSystem(new RenderingSystem(camera));

		for (int i = 0; i < 50; i ++)
		{
			for (int j = 0; j < 50; j++)
			{
				Entity te = pooledEngine.createEntity();
				te.add(new PositionComponent(i, j));
				te.add(new VisualComponent(new TextureRegion(bg_tile)));
				pooledEngine.addEntity(te);

			}

		}



		//create player entity
		player = pooledEngine.createEntity();
		player.add(new PositionComponent(0, 0));
		player.add(new MovementComponent(0, 0, 0));

		TextureRegion tx = new TextureRegion(kenny);
		player.add(new VisualComponent(runAnimation));
		player.add(new PlayerComponent(player));
		pooledEngine.addEntity(player);




		Entity e = pooledEngine.createEntity();
		e.add(new PositionComponent(0, 0));
		e.add(new VisualComponent(tx));
		//pooledEngine.addEntity(e);


		cursor = pooledEngine.createEntity();
		cursor.add(new PositionComponent(0, 0));
		cursor.add(new VisualComponent(new TextureRegion(whiteball)));
		//pooledEngine.addEntity(cursor);


		input = new InputHandler(player, cursor); //handle input of 1 single player
		Gdx.input.setInputProcessor(input);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		PositionComponent position = player.getComponent(PositionComponent.class);
		camera.position.set(position.x, position.y, 0);
		camera.update();

		pooledEngine.update(Gdx.graphics.getDeltaTime());

	}

	public void resize(int width, int height)
	{
		viewport.update(width, height);

	}


	private void loadAssets()
	{
		kenny = new Texture("p1_stand.png");
		bg_tile = new Texture("blacktile.png");
		whiteball = new Texture("white ball.png");

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
					System.out.println("index: " + index);
					System.out.println("i: " + i + "; j: " + j);
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
