package com.mygdx.game.networking;

import java.io.ObjectOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.GameScreen;

public class NetworkHostOutput extends Thread {
	NetworkHost networkHost;
	Socket socket;
	ObjectOutputStream oos;
	
	public NetworkHostOutput(NetworkHost networkHost, Socket socket, ObjectOutputStream oos) {
		this.networkHost = networkHost;
		this.socket = socket;
		this.oos = oos;
		
		start();
	}
	
	public void run()
	{	
		//FOREVER
		while(true)
		{
			try
			{
				GameScreen.networkSystem.update(Gdx.graphics.getDeltaTime());
				
				//System.out.println(GameScreen.allEntities);
				//oos.writeObject(GameScreen.allEntities);
				oos.flush();
				oos.reset();
			}
			catch(Exception e)
			{
				System.out.println("Exception in NetworkHostUpdateHandler reading ois: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
