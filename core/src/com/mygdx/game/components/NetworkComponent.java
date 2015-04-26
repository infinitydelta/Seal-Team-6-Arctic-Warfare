package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by KS on 4/25/2015.
 */
public class NetworkComponent extends Component {

    public String owner;
    public Long ownerID;
    public float xPos, yPos;
    public float xVel, yVel;


    public NetworkComponent()
    {

    }
}
