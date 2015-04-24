package com.mygdx.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.server.SocketSecurityException;
import java.util.ArrayList;

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
import com.mygdx.game.dungeon.DungeonGenerator;
import com.mygdx.game.systems.InputHandler;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerSystem;
import com.mygdx.game.systems.RenderingSystem;

public class GameScreen implements Screen
{

	static final int WORLD_WIDTH = 100;
	static final int WORLD_HEIGHT = 100;

	static final int CAM_WIDTH = 20;
	
	static final int CAM_SIZE = 100;

	//MainGame game;
	OrthographicCamera camera;
	FitViewport viewport;

	public PooledEngine pooledEngine;
	Stage stage;
	World world;
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
			System.out.println("HOST");
			//generate map
			
			map = new ArrayList<Entity>();
			
			DungeonGenerator.generateDungeon(this);

			//accept connections
			new Thread(new Runnable(){
				public void run()
				{
					ServerSocketHints serverSocketHint = new ServerSocketHints();
					serverSocketHint.acceptTimeout = 0; //0 = no timeout
					
					ServerSocket serverSocket = Gdx.net.newServerSocket(Protocol.TCP, GameScreen.this.port, serverSocketHint); //Create ServerSocket with TCP protocol on the port specified
					
					//FOREVER
					while(true)
					{
						System.out.println("Waiting...");
						Socket socket = serverSocket.accept(null);
						//BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); //dont need anything from the other player yet
						System.out.println("connected");
						//Send the current state of the game as a starting point
						System.out.println("Sending initial game state");
						try
						{
							ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
							oos.writeObject(map);
							oos.flush();
						}
						catch(Exception e)
						{
							System.out.println(e.getMessage());
							e.printStackTrace();
						}
						
					}
				}
			}).start();
			
			
	
	
			
	
			
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
				map = (ArrayList<Entity>) o;
				for(Entity e: map)
				{
					pooledEngine.addEntity(e);
				}
			} 
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		
		
		
		//create player entity
				player = pooledEngine.createEntity();
				PositionComponent position = new PositionComponent(0, 0);
				player.add(position);
				player.add(new MovementComponent(position, world, 0, 0, 0));

				TextureRegion tx = new TextureRegion(MainGame.kenny);
				player.add(new VisualComponent(MainGame.runAnimation));
				player.add(new PlayerComponent(player));
				pooledEngine.addEntity(player);


				//create weapon entity
				TextureRegion weap = new TextureRegion(MainGame.objects, 3 * 32, 1 * 32, 32, 32);
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
