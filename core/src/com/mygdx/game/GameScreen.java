package com.mygdx.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.server.SocketSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.PositionComponent;
import com.mygdx.game.components.VisualComponent;
import com.mygdx.game.dungeon.DungeonGenerator;
import com.mygdx.game.networking.NetworkHost;
import com.mygdx.game.networking.NetworkHostConnectHandler;
import com.mygdx.game.systems.InputHandler;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.utility.Factory;
import com.mygdx.game.utility.RandomInt;

public class GameScreen implements Screen
{

	static final int WORLD_WIDTH = 100;
	static final int WORLD_HEIGHT = 100;

	static final int CAM_WIDTH = 20;
	
	static final int CAM_SIZE = 100;

	//MainGame game;
	OrthographicCamera camera;
	FitViewport viewport;

	public static PooledEngine pooledEngine;
	Stage stage;
	public static World world;
	InputHandler input;
	Entity player;

	Box2DDebugRenderer debugRenderer;

	Entity weapon;

	//box2d
	BodyDef bodyDef;
	
	public int port;
	public String ip;
	boolean host;
	
	ArrayList<Entity> map;

	NetworkHost networkHost;
	
	
	
	public GameScreen(boolean host, String ip, int port)
	{
		this.host = host;
		this.port = port;
		this.ip = ip;
		//this.game = game;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(CAM_WIDTH, CAM_WIDTH* (h/w) );
		viewport = new FitViewport(20, 20 * (h/w), camera); // 20 world units wide
		camera.position.set(0, 0, 0);
		camera.update();

		//stage for ui
		stage = new Stage();
		
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
		
		
		
		
		if(host)
		{
			networkHost = new NetworkHost(this);
			
			
			
			
			
			//create player entity
			player = Factory.createPlayer(0, 0);

			
			Long newEntityID = player.getId();
			HashMap<String, Object> newEntityData = new HashMap<String, Object>();
			newEntityData.put("Type", "Player");
			newEntityData.put("Owner", "host");
			newEntityData.put("X", 0);
			newEntityData.put("Y", 0);
			newEntityData.put("Z", 0);
			networkHost.networkHostUpdateHandler.entities.put(newEntityID, newEntityData);
			
			
			//create weapon entity
			weapon = Factory.createWeapon();

			player.getComponent(PlayerComponent.class).addWeapon(weapon);


			//bullet
			/*
			Entity bullet = pooledEngine.createEntity();
			PositionComponent positionB = new PositionComponent(0, -5);
			bullet.add(positionB);
			//bullet.add(new MovementComponent(position, world, 0, 0, 0));

			PolygonShape rectangle = new PolygonShape();
			rectangle.setAsBox(.3f, .2f);
			bullet.add(new CollisionComponent(world, BodyDef.BodyType.KinematicBody, rectangle, positionB));
			*/
		}
		else //client
		{
			System.out.println("CLIENT");
			SocketHints socketHints = new SocketHints();
			socketHints.connectTimeout = 10000; //10s?
			
			Socket socket = Gdx.net.newClientSocket(Protocol.TCP, ip, port, socketHints);
			System.out.println("CONNECTED");
			try 
			{
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				System.out.println("Retrieving map...");
				Object o = ois.readObject();
				while(o == null)
				{
					o = ois.readObject();
				}
				if (o.getClass() == Long.class) {
					System.out.println("Receiving Long");
					long mapSeed = (Long) o;
					RandomInt.setSeed(mapSeed);
					DungeonGenerator.generateDungeon(this);
				}
				else if (o.getClass() == HashMap.class) {
					System.out.println("Receiving hashmap");
					for (Map.Entry<Long, Map<String, Object>> entry : ((ConcurrentHashMap<Long, Map<String, Object>>) o).entrySet()) {
					}
				}
				else {
					System.out.println("Receiving other datatype");
				}
				
			} 
			catch (Exception e) 
			{
				System.out.println("Exception in client code:" + e.getMessage());
				e.printStackTrace();
			}
			
			
			//create player entity
			player = Factory.createPlayer(0, 0);
			
			
			//create weapon entity
			weapon = Factory.createWeapon();

			player.getComponent(PlayerComponent.class).addWeapon(weapon);

			Entity e = pooledEngine.createEntity();
			//pooledEngine.addEntity(e);
			
			//bullet
			/*Entity bullet = pooledEngine.createEntity();
			PositionComponent positionB = new PositionComponent(0, -5);
			bullet.add(positionB);
			bullet.add(new MovementComponent(position, world, 0, 0, 0));

			PolygonShape rectangle = new PolygonShape();
			rectangle.setAsBox(.3f, .2f);
			bullet.add(new CollisionComponent(world, BodyDef.BodyType.KinematicBody, rectangle, positionB));*/
		}
		

		createBox2d();


		//curs = new Cursor();
		//stage.addActor(curs);

		input = new InputHandler(camera, player); //handle input of 1 single player
		
	}
	public void render(float delta)
	{
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
		Fixture fixture = body.createFixture(fixtureDef);
		//body.setLinearVelocity(0, 1/60f);
		body.applyLinearImpulse(0, 1/60f, body.getPosition().x, body.getPosition().y, true);

		rectangle.dispose();
	}

	
}
