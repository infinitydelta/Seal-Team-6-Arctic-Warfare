package com.mygdx.game.dungeon;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by KS on 4/23/2015.
 */
public class Room {
    public int index;
    public int width, height;
    public int x, y;
    public Vector2 center;
    public int neighbor;
    public boolean connected = false;


    public Room()
    {
        index = 0;
        width = 0;
        height = 0;
        center = new Vector2(0,0);
        neighbor = 0;
        connected = false;
    }


    public void move(int mx, int my)
    {
        x += mx;
        y += my;
        updateCenter();
    }
    public void updateCenter()
    {
        center = new Vector2(x + width/2, y + height/2);
    }
}
