package com.mygdx.game.networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.mygdx.game.GameScreen;

public class NetworkClient {
	public GameScreen gScreen;
	public NetworkClientInput netCliInput;
	public NetworkClientOutput netCliOutput;
	
	public NetworkClient(GameScreen gScreen) {
		this.gScreen = gScreen;
		
		System.out.println("CLIENT");
		SocketHints socketHints = new SocketHints();
		socketHints.connectTimeout = 10000; //10s?

		Socket socket = Gdx.net.newClientSocket(Protocol.TCP, gScreen.ip, gScreen.port, socketHints);
		
		NetworkClientInput netCliInput = new NetworkClientInput(gScreen, socket);
		while (!gScreen.initialized) {};
		NetworkClientOutput netCliOutput = new NetworkClientOutput(gScreen, socket);
		//NetworkClientPingpong netCliPp = new NetworkClientPingpong(gScreen, socket);
	}
}