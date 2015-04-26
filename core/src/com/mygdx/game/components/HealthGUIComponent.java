package com.mygdx.game.components;

import java.security.KeyStore.PrivateKeyEntry;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameScreen;
import com.mygdx.game.utility.Factory;

public class HealthGUIComponent extends Component
{
	Stage stage;
	Image front;
	Image back;
	Image icon;
	int max;
	int current;
	Label label;
	
	private int xpos = 20;
	private int ypos = 670;
	private int xoffset = 40;
	private int yoffset = 5;
	
	public HealthGUIComponent(Stage stage, int max)
	{
		this.stage = stage;
		this.max = max;
		current = max;
		front = new Image(Factory.hfront);
		back = new Image(Factory.hback);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		label = new Label(""+current+"/"+max, uiSkin);
		icon = new Image(Factory.heart);
		
		front.setOrigin(0, front.getHeight()/2);
		front.setScale(4, .5f);
		front.setPosition(xpos + xoffset + 4, ypos + yoffset);
		back.setScale(4.25f, 4);
		back.setOrigin(0, back.getHeight()/2);
		back.setPosition(xpos + xoffset, ypos + yoffset);
		icon.setScale(2);
		icon.setOrigin(icon.getWidth()/2, icon.getHeight()/2);
		icon.setPosition(xpos, ypos);
		label.setOrigin(10, label.getHeight()/2);
		label.setPosition(xpos + xoffset + 10, ypos + yoffset + 3);
		
		stage.addActor(back);
		stage.addActor(front);
		stage.addActor(label);
		stage.addActor(icon);
		
	}
	public void setValue(int current)
	{
		this.current = current;
		label.setText(""+current+"/"+max);
		front.setScale(4f * current/(float)max, .5f);
		
	}
}
