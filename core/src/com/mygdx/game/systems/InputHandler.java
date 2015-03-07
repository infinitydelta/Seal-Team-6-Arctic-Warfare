package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.PositionComponent;
import com.mygdx.game.components.VisualComponent;

/**
 * Created by KS on 3/5/2015.
 * acutally input is handled by each computer alone
 * computer sends data of position, fired projectiles... to server
 */
public class InputHandler implements InputProcessor {


    Entity player, cursor;
    MovementComponent movement;
    PlayerComponent playerComponent;

    PositionComponent cursorPosition;
    VisualComponent cursorVisual;

    float speed = 100;
    boolean up = false, down = false, left = false, right = false;

    public InputHandler(Entity player, Entity cursor)
    {
        this.player = player;
        this.cursor = cursor;
        movement = player.getComponent(MovementComponent.class);
        playerComponent = player.getComponent(PlayerComponent.class);

        cursorPosition = cursor.getComponent(PositionComponent.class);
        cursorVisual = cursor.getComponent(VisualComponent.class);

    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A)
        {
            playerComponent.left = true;
        }
        if (keycode == Input.Keys.D)
        {
            playerComponent.right = true;
        }
        if (keycode == Input.Keys.W)
        {
            playerComponent.up = true;
        }
        if (keycode == Input.Keys.S)
        {
            playerComponent.down = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A)
        {
            playerComponent.left = false;

        }
        if (keycode == Input.Keys.D)
        {
            playerComponent.right = false;
        }
        if (keycode == Input.Keys.W)
        {
            playerComponent.up = false;
        }
        if (keycode == Input.Keys.S)
        {
            playerComponent.down = false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
