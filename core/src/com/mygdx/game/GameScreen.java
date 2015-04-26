package com.mygdx.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.PositionComponent;
import com.mygdx.game.components.VisualComponent;
import com.mygdx.game.components.WeaponComponent;
import com.mygdx.game.dungeon.DungeonGenerator;
import com.mygdx.game.networking.NetworkClient;
import com.mygdx.game.networking.NetworkHost;
import com.mygdx.game.networking.NetworkHostConnectHandler;
import com.mygdx.game.systems.*;
import com.mygdx.game.utility.Factory;
import com.mygdx.game.utility.RandomInt;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class GameScreen implements Screen
{

	static final int WORLD_WIDTH = 100;
	static final int WORLD_HEIGHT = 100;

	static final int CAM_WIDTH = 20;
	
	static final int CAM_SIZE = 20;

	//MainGame game;
	OrthographicCamera camera;
	static FitViewport viewport;

	Stage stage;
	Skin uiSkin;
	Label playerUsername;

	public static PooledEngine pooledEngine;
	public static World world;
	InputHandler input;
	public Entity player;

	Box2DDebugRenderer debugRenderer;

	public Entity weapon;

	//box2d
	BodyDef bodyDef;
	
	public int port;
	public String ip;
	boolean host;
	
	ArrayList<Entity> map;

	public NetworkHost networkHost;

	public NetworkClient networkClient;
	
	public static Queue<Entity> toBeDeleted;
	
	public static int networkPlayerNum;
	
	public static CopyOnWriteArraySet<HashMap<String, Object>> myEntities = new CopyOnWriteArraySet<HashMap<String, Object>>();
    public static CopyOnWriteArraySet<HashMap<String, Object>> allEntities = new CopyOnWriteArraySet<HashMap<String, Object>>();
    public static boolean myEntitiesLock = false;
    public static boolean allEntitiesLock = false;


	float deltatimesink;
	static final float physicsTimeStep = 1/60f;

	public boolean initialized = false;
		
	
	public GameScreen(boolean host, String ip, int port)
	{
		this.host = host;
		this.port = port;
		this.ip = ip;
		//this.game = game;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(CAM_WIDTH, CAM_WIDTH* (h/w) );
		viewport = new FitViewport(CAM_SIZE, CAM_SIZE * (h/w), camera);
		camera.position.set(0, 0, 0);
		camera.update();

		//stage for ui
		stage = new Stage();
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		//change mouse cursor to picture
		Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
		Gdx.input.setCursorImage(pm, pm.getWidth()/2, pm.getHeight()/2);
		pm.dispose();
		
		//box2d
		Box2D.init();
		world = new World(Vector2.Zero, true);
		world.setContactListener(new MyContactListener());
		debugRenderer = new Box2DDebugRenderer();

		//engine
		pooledEngine = new PooledEngine(10, 1000, 10, 5000);
		pooledEngine.addSystem(new PlayerSystem());
		pooledEngine.addSystem(new MovementSystem());
		pooledEngine.addSystem(new RenderingSystem(camera));
		pooledEngine.addSystem(new NetworkSystem());
		pooledEngine.addSystem(new WeaponSystem());

		toBeDeleted = new LinkedList<Entity>();
		

		
		
		
		if(host)
		{
			networkHost = new NetworkHost(this);
		}
		else //client
		{
			networkClient = new NetworkClient(this);
		}
		
		while (!initialized) {}
		
		//create player entity
		Vector2 pos = DungeonGenerator.getSpawnPosition();
		player = Factory.createPlayer((int)pos.x, (int) pos.y, networkPlayerNum);
		
		//create weapon entity
		weapon = Factory.createWeapon();
		weapon.add(new WeaponComponent(weapon));
		player.getComponent(PlayerComponent.class).addWeapon(weapon);

		input = new InputHandler(camera, player); //handle input of 1 single player

		playerUsername = new Label(player.getComponent(PlayerComponent.class).name, uiSkin);
		float xx = player.getComponent(PositionComponent.class).x;
		float yy = player.getComponent(PositionComponent.class).y;
		Vector3 v2 = new Vector3(xx , yy , 0);
		System.out.println(v2);
		camera.project(v2);
		System.out.println(v2);
		playerUsername.setPosition(100, 100);
		stage.addActor(playerUsername);

		createBox2d();
		//Entity play = Factory.createPlayer(pos.x - 1, pos.y, 1);
		//Entity weaponz = Factory.createWeapon();
		//weaponz.add(new WeaponComponent(weaponz));
		//play.getComponent(PlayerComponent.class).addWeapon(weaponz);
		deltatimesink = 0.0f;


	}

	Vector2 camPos()
	{
		Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mousePos);
		PositionComponent position = player.getComponent(PositionComponent.class);

		Vector2 cam = new Vector2((position.x + mousePos.x)/2, (position.y + mousePos.y)/2);
		Vector2 cam2 = new Vector2((position.x + cam.x)/2, (position.y + cam.y)/2);
		return cam2;
	}

	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		PositionComponent position = player.getComponent(PositionComponent.class);
		Vector2 pos = camPos();
		float x = MathUtils.lerp(camera.position.x, pos.x, 7f * delta);
		float y = MathUtils.lerp(camera.position.y, pos.y, 7f * delta);
		camera.position.set(x, y, 0);

		camera.update();

		pooledEngine.update(Gdx.graphics.getDeltaTime());

		stage.draw(); //ui

		debugRenderer.render(world, camera.combined);
		world.step(delta, 6, 2);
		//Find number of physics steps to simulate
		/*
		deltatimesink += delta;
		int numStepsToSim = 0;
		while(deltatimesink > physicsTimeStep)
		{
			numStepsToSim++;
			deltatimesink -= physicsTimeStep;
		}
		for(int i = 0; i < numStepsToSim; i++)
		{
			world.step(1/60f, 6, 2); //physics simulation of 1/60th of a second
		}
		*/
		//Temporal Aliasing?
		//Spiral of death?
		
		//remove all components scheduled for removal AFTER physics step
		/*while(!toBeDeleted.isEmpty())
		{
			Entity e = toBeDeleted.remove();
<<<<<<< HEAD
=======
			try
			{
				//e.removeAll();
				//e.getComponent(MovementComponent.class).body.setActive(false);
				//world.destroyBody(e.getComponent(MovementComponent.class).body);
				//e.getComponent(MovementComponent.class).body.setUserData(null);
				//e.getComponent(MovementComponent.class).body = null;
			} catch (Exception ex) {
				System.out.println("deletion exception: " + ex.getMessage());
			} finally {
				//pooledEngine.removeEntity(e);
			}
>>>>>>> 84dfbf646a7b0fa883e0a1739eca9281abe542db

			world.destroyBody(e.getComponent(MovementComponent.class).body);
			pooledEngine.removeEntity(e);
			
		}*/

	}
	public void resize(int width, int height)
	{
		viewport.update(width, height);
	}
	public void show()
	{
		Gdx.input.setInputProcessor(input);
	}
	public void hide()
	{
		
	}
	public void pause()
	{
		
	}
	public void resume()
	{
		
	}
	public void dispose()
	{
		stage.dispose();
	}
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
		fixtureDef.filter.categoryBits = Factory.WALL;
		//fixtureDef.filter.maskBits = Factory.PLAYER_PROJ_COL;
		Fixture fixture = body.createFixture(fixtureDef);
		//body.setLinearVelocity(0, 1/60f);
		body.applyLinearImpulse(0, 1/60f, body.getPosition().x, body.getPosition().y, true);

		rectangle.dispose();
	}

	public static void worldViewSize(int size)
	{
		viewport.setWorldSize(size, size);
	}	
}
