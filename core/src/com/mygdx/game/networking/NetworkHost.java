package com.mygdx.game.networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.GameScreen;
import com.mygdx.game.dungeon.DungeonGenerator;
import com.mygdx.game.systems.InputHandler;
import com.mygdx.game.utility.RandomInt;

public class NetworkHost {
	GameScreen gScreen;
	
	public final ServerSocketHints serverSocketHint;
	public final ServerSocket serverSocket;
	
	public final long mapSeed;
	
	public NetworkHostConnectHandler networkHostConnectHandler;
	public NetworkHostUpdateHandler networkHostUpdateHandler;
	
	public static int numPlayers = 1;
	public static ConcurrentHashMap<Socket, Integer> playersConnected = new ConcurrentHashMap<Socket, Integer>();
	
	public NetworkHost(GameScreen gScreen) {
		this.gScreen = gScreen;
				
		System.out.println("HOST");
		gScreen.networkPlayerNum = 0;
		
		Random rand = new Random();
		mapSeed = rand.nextLong();
		RandomInt.setSeed(mapSeed);
		
		//Initialize screen host-side
		DungeonGenerator.generateDungeon(gScreen);
		//

		serverSocketHint = new ServerSocketHints();
		serverSocketHint.acceptTimeout = 0; //0 = no timeout
		
		serverSocket = Gdx.net.newServerSocket(Protocol.TCP, gScreen.port, serverSocketHint); //Create ServerSocket with TCP protocol on the port specified
				
		networkHostConnectHandler = new NetworkHostConnectHandler(this);
		
		gScreen.initialized = true;
	}
	
	public int addPlayer(Socket newSocket) {
		playersConnected.put(newSocket, numPlayers++);
		return numPlayers-1;
	}
	
	public void removePlayer(Socket newSocket) {
		playersConnected.remove(newSocket);
	}
}