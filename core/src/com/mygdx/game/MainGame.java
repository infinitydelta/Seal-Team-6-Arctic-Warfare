package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.actors.Cursor;
import com.mygdx.game.components.*;
import com.mygdx.game.systems.InputHandler;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerSystem;
import com.mygdx.game.systems.RenderingSystem;

public class MainGame extends ApplicationAdapter {


	static final int WORLD_WIDTH = 100;
	static final int WORLD_HEIGHT = 100;

	static final int CAM_WIDTH = 20;


	OrthographicCamera camera;
	FitViewport viewport;

	PooledEngine pooledEngine;
	Stage stage;
	World world;
	InputHandler input;
	Entity player;

	Box2DDebugRenderer debugRenderer;

	Entity weapon;

	//box2d
	BodyDef bodyDef;


	static Texture kenny;
	static Texture bg_tile;
	public static Texture whiteball;
	static Texture objects;
	public static Texture sandTiles;

	public static Animation runAnimation;
	public static Animation idleAnmation;

	@Override
	public void create () {


		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(CAM_WIDTH, CAM_WIDTH* (h/w) );
		viewport = new FitViewport(20, 20 * (h/w), camera); // 20 world units wide
		camera.position.set(0, 0, 0);
		camera.update();

		//stage for ui
		stage = new Stage();

		loadAssets();


		//change mouse cursor to picture
		Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
		Gdx.input.setCursorImage(pm, pm.getWidth()/2, pm.getHeight()/2);
		pm.dispose();

		//box2d
		Box2D.init();
		world = new World(Vector2.Zero, true);
		debugRenderer = new Box2DDebugRenderer();


		//engine
		pooledEngine = new PooledEngine(10, 1000, 10, 5000);
		pooledEngine.addSystem(new PlayerSystem());
		pooledEngine.addSystem(new MovementSystem());
		pooledEngine.addSystem(new RenderingSystem(camera));

		for (int i = 0; i < 50; i ++)
		{
			for (int j = 0; j < 50; j++)
			{
				int r = (int) (Math.random() * 18);
				//System.out.println("random: " + r);
				Entity te = pooledEngine.createEntity();
				PositionComponent pos = new PositionComponent(i, j);
				te.add(pos);
				int x = (r * 32) % 256;
				int y = (r*32)/256 * 32;
				//System.out.println("x: " + x + ", y: " + y);
				TextureRegion t = new TextureRegion(sandTiles, x, y, 32, 32);
				te.add(new VisualComponent(t));
				if (y>= 32)
				{
					/*
					BodyDef bodyDef = new BodyDef();
					bodyDef.type = BodyDef.BodyType.StaticBody;
					bodyDef.position.set(i + .5f, j + .5f);

					Body body = world.createBody(bodyDef);

					FixtureDef fixtureDef = new FixtureDef();
					PolygonShape rectangle = new PolygonShape();
					rectangle.setAsBox(.5f, .5f);

					fixtureDef.shape = rectangle;
					fixtureDef.density = 0f;
					fixtureDef.friction = 0f;
					fixtureDef.restitution = 0f;
					Fixture fixture = body.createFixture(fixtureDef);
					rectangle.dispose();
					*/
					PolygonShape rectangle = new PolygonShape();
					rectangle.setAsBox(.5f, .5f);
					te.add(new CollisionComponent(world, BodyDef.BodyType.StaticBody, rectangle, pos));
				}
				pooledEngine.addEntity(te);

			}

		}



		//create player entity
		player = pooledEngine.createEntity();
		PositionComponent position = new PositionComponent(0, 0);
		player.add(position);
		player.add(new MovementComponent(position, world, 0, 0, 0));

		TextureRegion tx = new TextureRegion(kenny);
		player.add(new VisualComponent(runAnimation));
		player.add(new PlayerComponent(player));
		pooledEngine.addEntity(player);


		//create weapon entity
		TextureRegion weap = new TextureRegion(objects, 3 * 32, 1 * 32, 32, 32);
		weapon = pooledEngine.createEntity();
		weapon.add(new PositionComponent(0, 0));
		weapon.add(new VisualComponent(weap));
		pooledEngine.addEntity(weapon);

		player.getComponent(PlayerComponent.class).addWeapon(weapon);

		Entity e = pooledEngine.createEntity();
		e.add(new PositionComponent(0, 0));
		e.add(new VisualComponent(tx));
		//pooledEngine.addEntity(e);



		//bullet
		Entity bullet = pooledEngine.createEntity();
		PositionComponent positionB = new PositionComponent(0, -5);
		bullet.add(positionB);
		bullet.add(new MovementComponent(position, world, 0, 0, 0));

		PolygonShape rectangle = new PolygonShape();
		rectangle.setAsBox(.3f, .2f);
		bullet.add(new CollisionComponent(world, BodyDef.BodyType.KinematicBody, rectangle, positionB));


		createBox2d();


		//curs = new Cursor();
		//stage.addActor(curs);

		input = new InputHandler(camera, player); //handle input of 1 single player
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

		stage.draw(); //ui

		debugRenderer.render(world, camera.combined);
		world.step(1/60f, 6, 2); //physics
	}

	public void resize(int width, int height)
	{
		viewport.update(width, height);

	}

	//temporary
	private void createBox2d()
	{
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(-1, 0);

		Body body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape rectangle = new PolygonShape();
		rectangle.setAsBox(.5f, .5f);

		fixtureDef.shape = rectangle;
		Fixture fixture = body.createFixture(fixtureDef);
		//body.setLinearVelocity(0, 1/60f);
		body.applyLinearImpulse(0, 1/60f, body.getPosition().x, body.getPosition().y, true);

		rectangle.dispose();
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
