package com.mygdx.game;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.utility.Factory;

public class LoginScreen implements Screen{

	MainGame game;
	private Stage stage;
	Skin uiSkin;
	TextButton loginButton;
	TextButton guestButton;
	TextButton newUserButton;
	Label usernameLabel;
	Label passwordLabel;
	TextField usernameField;
	TextField passwordField;
	public static boolean guest=false;
	Socket s;
	public static String username;

	Image bg;
	
	Table table;
	
	LoginScreen(MainGame game){

		this.game = game;
		stage = new Stage();
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		loginButton = new TextButton("LOGIN", uiSkin);
		guestButton = new TextButton("LOGIN AS GUEST", uiSkin);
		newUserButton = new TextButton("CREATE NEW USER", uiSkin);
		usernameLabel = new Label("USERNAME:", uiSkin);
		passwordLabel = new Label("PASSWORD:",uiSkin);
		usernameField = new TextField("",uiSkin);
		passwordField = new TextField("",uiSkin);
		passwordField.setPasswordMode(true);
		passwordField.setPasswordCharacter('*');
		table=new Table();
		bg = new Image(Factory.menuBG);
	}
	
	public void show() {
		bg.setPosition(0, 0);
		stage.addActor(bg);
		loginButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if(usernameField.getText()!=""&& passwordField.getText()!=""){
					try {
						s=new Socket("localhost",6789);
						ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
						oos.writeObject("Login");
						username=usernameField.getText();
						oos.writeObject(username);
						oos.writeObject(passwordField.getText());
						oos.flush();
						ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
						boolean flag= (Boolean) ois.readObject();
						System.out.println(flag);
						if(flag){
							game.setScreen(new MenuScreen(game));
						}else{
							
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		guestButton.addListener(new ClickListener(){
			public void clicked(InputEvent event,float x, float y){
				guest=true;
				username = "Guest";
				game.setScreen(new MenuScreen(game));
			}
		});
		newUserButton.addListener(new ClickListener(){
			public void clicked(InputEvent event,float x,float y){
				try {
					s=new Socket("localhost",6789);
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					oos.writeObject("New User");
					oos.writeObject(usernameField.getText());
					oos.writeObject(passwordField.getText());
					oos.flush();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		table.add(usernameLabel);
		table.add(usernameField).row();
		table.add(passwordLabel);
		table.add(passwordField).row();
		table.add(loginButton).size(150,60).colspan(2).padBottom(40);
		table.add(newUserButton).size(150,60).colspan(2).padBottom(40);
		table.add(guestButton).size(150,60).colspan(2).padBottom(40).row();

		table.setFillParent(true);
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
