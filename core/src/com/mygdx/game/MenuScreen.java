package com.mygdx.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen
{
	MainGame game;
	private Stage stage;
	Skin uiSkin;
	TextButton hostButton;
	TextButton joinButton;

	
	Label hostiplabel;
	Label joiniplabel;
	Label hostportlabel;
	Label joinportlabel;
	Label hostipfield;

	
	TextField joinportfield;
	TextField hostportfield;
	TextField joinipfield;

	Table table;

	public static String ipaddress;

	public MenuScreen(MainGame game)
	{
		this.game = game;
		stage = new Stage();
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		hostButton = new TextButton("HOST", uiSkin);
		joinButton = new TextButton("JOIN", uiSkin);
	
		hostiplabel = new Label("ADDRESS:", uiSkin);
		hostipfield = new Label("UNKNOWN", uiSkin);

		try
		{
			ipaddress = Inet4Address.getLocalHost().getHostAddress();
			hostipfield.setText(ipaddress);
		}
		catch(Exception e)
		{
			
		}
		hostportlabel = new Label("PORT:", uiSkin);
		joiniplabel = new Label("ADDRESS:", uiSkin);
		joinportlabel = new Label("PORT:", uiSkin);
		hostportfield = new TextField("7777", uiSkin);
		joinipfield = new TextField("localhost", uiSkin);
		joinportfield = new TextField("7777", uiSkin);

		table = new Table();
		
	}
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}
	public void resize(int width, int height)
	{
		
	}
	public void show()
	{
		hostButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y)
			{
				game.setScreen(new GameScreen(true, hostipfield.getText().toString(), Integer.parseInt(hostportfield.getText())));
			}
		});
		joinButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y)
			{
				game.setScreen(new GameScreen(false, joinipfield.getText().toString(), Integer.parseInt(joinportfield.getText())));
			}
		});
		table.add(hostiplabel);
		table.add(hostipfield).row();
		table.add(hostportlabel);
		table.add(hostportfield).row();
		table.add(hostButton).size(150, 60).colspan(2).padBottom(40).row();
		table.add(joiniplabel);
		table.add(joinipfield).row();
		table.add(joinportlabel);
		table.add(joinportfield).row();
		table.add(joinButton).size(150,60).colspan(2).padBottom(40).row();

		table.setFillParent(true);
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
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
		uiSkin.dispose();
	}

}
