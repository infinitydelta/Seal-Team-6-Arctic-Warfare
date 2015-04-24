package com.mygdx.game.networking;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.GameScreen;
import com.mygdx.game.dungeon.DungeonGenerator;
import com.mygdx.game.utility.RandomInt;

public class NetworkHost {
	GameScreen gScreen;
	
	public final ServerSocketHints serverSocketHint;
	public final ServerSocket serverSocket;
	final ArrayList<Socket> sockets = new ArrayList<Socket>();
	
	final long mapSeed;
	
	public NetworkHost(GameScreen gScreen) {
		this.gScreen = gScreen;
		
		System.out.println("HOST");
		
		Random rand = new Random();
		mapSeed = rand.nextLong();
		RandomInt.setSeed(mapSeed);
		DungeonGenerator.generateDungeon(gScreen);

		serverSocketHint = new ServerSocketHints();
		serverSocketHint.acceptTimeout = 0; //0 = no timeout
		
		serverSocket = Gdx.net.newServerSocket(Protocol.TCP, gScreen.port, serverSocketHint); //Create ServerSocket with TCP protocol on the port specified
		
		NetworkHostConnectHandler networkHostConnectHandler = new NetworkHostConnectHandler(this);
	}
}