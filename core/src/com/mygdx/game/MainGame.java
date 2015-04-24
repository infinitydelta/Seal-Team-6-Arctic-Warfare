package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainGame extends Game {

	MenuScreen menuScreen;
	//GameScreen gameScreen;
	
	static Texture kenny;
	static Texture bg_tile;
	public static Texture whiteball;
	static Texture objects;
	public static Texture sandTiles;

	public static Animation runAnimation;
	public static Animation idleAnmation;

	@Override
	public void create () 
	{
		loadAssets();
		menuScreen = new MenuScreen(this);
		//gameScreen = new GameScreen(this);
		setScreen(menuScreen);

	}

	@Override
	public void render () 
	{
		super.render();
	}

	public void resize(int width, int height)
	{
		

	}
	private void loadAssets()
	{
		//textures
		kenny = new Texture("p1_stand.png");
		bg_tile = new Texture("blacktile.png");
		whiteball = new Texture("white ball.png");
		objects = new Texture("objects.png");
		sandTiles = new Texture("map.png");

		//animation
		Texture walk = new Texture("minimalObjects_32x32Tiles.png");
		TextureRegion[][] temp = TextureRegion.split(walk, 32, 32); //rows = 4; num cols = 3
		TextureRegion[] walkFrames = new TextureRegion[6];
		TextureRegion[] idleFrames = new TextureRegion[6];

		int index = 0;
		for (int i = 0; i < 1; i++) // column length, number of rows
		{
			for (int j = 0; j < 6; j++) //row length, number of columns
			{
				if (index < 6)
				{
					//System.out.println("index: " + index);
					//System.out.println("i: " + i + "; j: " + j);
					walkFrames[index] = temp[i][j];
					idleFrames[index] = temp[1][j];
					idleFrames[index].flip(true, false);
					index++;
				}
			}
		}
		runAnimation = new Animation(1/6f, walkFrames);

		idleAnmation = new Animation(1/2f, idleFrames);

	}
}
