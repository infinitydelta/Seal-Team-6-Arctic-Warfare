package com.mygdx.game.components;

import java.util.ArrayList;

import javafx.scene.Scene;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.utility.Factory;

public class WeaponGUIComponent extends Component
{
	Stage stage;
	ArrayList<Image> ammoElements;
	int maxAmmo;
	int currentAmmo;
	private int xloc = 54;
	private int yloc = 650;
	private int spacing = 14;
	
	public WeaponGUIComponent(Stage stage, int maxAmmo)
	{
		this.stage = stage;
		this.maxAmmo = maxAmmo;
		currentAmmo = maxAmmo;
		ammoElements = new ArrayList<Image>();
		for(int x = 0; x < maxAmmo; x++)
		{
			Image ae = new Image(Factory.ammoElement);
			ae.setX(xloc + x*spacing);
			ae.setY(yloc);
			ae.setScale(3f);
			ammoElements.add(ae);
			stage.addActor(ae);
		}
	}
	public void fire()
	{
		if(currentAmmo > 0)
		{
			currentAmmo--;
			ammoElements.get(currentAmmo).setVisible(false);
		}
	}
	public void reload()
	{
		for(Image i: ammoElements)
		{
			i.setVisible(true);
		}
		currentAmmo = maxAmmo;
	}
	public void empty()
	{
		for(int i = 0; i < currentAmmo; i++)
		{
			ammoElements.get(i).setVisible(false);
		}
		currentAmmo = 0;
	}

}
