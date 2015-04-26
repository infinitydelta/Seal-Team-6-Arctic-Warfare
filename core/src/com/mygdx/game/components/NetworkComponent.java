package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.MenuScreen;

import java.net.Inet4Address;

/**
 * Created by KS on 4/25/2015.
 */
public class NetworkComponent extends Component {

    public String owner;
    public Long ownerID;
    public float xPos, yPos;
    public float xVel, yVel;


    public NetworkComponent(long id, PositionComponent pos, MovementComponent move)
    {

        owner = MenuScreen.ipaddress;
        this.ownerID = id;
        xPos = pos.x;
        yPos = pos.y;
        xVel = move.xVel;
        yVel = move.yVel;
    }
}
