package com.mygdx.game.systems;

import jdk.nashorn.internal.runtime.regexp.joni.MatcherFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.actors.Cursor;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.PositionComponent;
import com.mygdx.game.components.VisualComponent;
import com.mygdx.game.utility.Factory;

/**
 * Created by KS on 3/5/2015.
 * acutally input is handled by each computer alone
 * computer sends data of position, fired projectiles... to server
 */
public class InputHandler implements InputProcessor {

    Camera camera;


    Entity player;
    //Cursor cursor;
    //player components
    MovementComponent movement;
    PlayerComponent playerComponent;
    PositionComponent playerPosition;

    //weapon sprite
    VisualComponent weaponSprite;


    public InputHandler(Camera camera, Entity player)
    {

        this.camera = camera;
        this.player = player;
        //this.cursor = cursor;
        movement = player.getComponent(MovementComponent.class);
        playerComponent = player.getComponent(PlayerComponent.class);
        playerPosition = player.getComponent(PositionComponent.class);


        weaponSprite = player.getComponent(PlayerComponent.class).weapon.getComponent(VisualComponent.class);

    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A)
        {
            playerComponent.left = true;
            //movement.body.setLinearVelocity(-1, movement.body.getLinearVelocity().y);
            //movement.body.applyLinearImpulse(-1, 0, movement.body.getPosition().x + .5f, movement.body.getPosition().y + .5f, true);
        }
        if (keycode == Input.Keys.D)
        {
            playerComponent.right = true;
            //movement.body.setLinearVelocity(1, movement.body.getLinearVelocity().y);

            //movement.body.applyLinearImpulse(1, 0, movement.body.getPosition().x, movement.body.getPosition().y, true);

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

        Vector3 clickPos= new Vector3(screenX, screenY, 0);
        camera.unproject(clickPos);

        //float angle = (float) Math.atan2(clickPos.y - (playerPosition.y + .5f), clickPos.x - (playerPosition.x + .5f));
        //Factory.createBullet(playerPosition.x  , playerPosition.y, angle, 30f);

        float angle = (float) (Math.atan2(clickPos.y - (playerPosition.y + .5f), clickPos.x - (playerPosition.x + .5f)));

        playerComponent.weaponComponent.fire(angle);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //cursor.setPostion(screenX, Gdx.graphics.getHeight() - screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        //cursor.setPostion(screenX, Gdx.graphics.getHeight() - screenY);

        //converts screen coords to world coords
        //needed for weapon rotation
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoordinates);
        //angle between mouse and player (weapon) pos
        float rotation = (float) Math.toDegrees(Math.atan2(worldCoordinates.y - (playerPosition.y + .5f), worldCoordinates.x - (playerPosition.x + .5f)));
        weaponSprite.sprite.setRotation(rotation);
        if(worldCoordinates.x > playerPosition.x)
        {
        	if(playerComponent.visual.sprite.isFlipX())
        	{
        		playerComponent.visual.sprite.flip(true, false);
        		weaponSprite.sprite.flip(false, true);
        	}
        }
        else 
        {
			if(!playerComponent.visual.sprite.isFlipX())
			{
				playerComponent.visual.sprite.flip(true, false);
				weaponSprite.sprite.flip(false, true);
			}
		}
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        System.out.println(amount);
        //scroll out

        return false;
    }
}
