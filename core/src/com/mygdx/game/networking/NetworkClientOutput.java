package com.mygdx.game.networking;

import java.io.ObjectOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.GameScreen;

public class NetworkClientOutput extends Thread {
	public GameScreen gScreen;
	
	Socket socket;
	ObjectOutputStream oos;
	
	public NetworkClientOutput(GameScreen gScreen, Socket socket) {
		this.gScreen = gScreen;
		this.socket = socket;
		
		
		try 
		{
			oos = new ObjectOutputStream(socket.getOutputStream());
		} 
		catch (Exception e) 
		{
			System.out.println("Exception in client output code preinitialization:" + e.getMessage());
			e.printStackTrace();
		}
		
		start();
	}
	
	public void run() {
		while (true) {
			try {
				//Run NetworkSystem here to update myEntities
				//GameScreen.networkSystem.update(Gdx.graphics.getDeltaTime());
				
				//if (!GameScreen.myEntities.isEmpty()) {
					oos.writeObject(GameScreen.myEntities);
					oos.flush();
					oos.reset();
				//}
			}
			catch (Exception e) {
				System.out.println("Exception in client output code run:" + e.getMessage());
				e.printStackTrace();
			};
		}
	}
}
