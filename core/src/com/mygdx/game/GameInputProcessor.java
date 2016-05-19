package com.mygdx.game;

import sun.rmi.runtime.Log;
import sun.util.logging.resources.logging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class GameInputProcessor implements InputProcessor, GestureListener {

	GameScreen gameScreen;
	Brick brick;

	public GameInputProcessor(GameScreen gameScreen) {
		super();
		this.gameScreen = gameScreen;
		brick = gameScreen.gameControl.brick;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (gameScreen.gameControl.playable
				&& brick.direction == DIRECTION.STOP) {
			if (keycode == Keys.RIGHT) {
				brick.setDirection(DIRECTION.RIGHT);
			} else if (keycode == Keys.LEFT) {
				brick.setDirection(DIRECTION.LEFT);
			} else if (keycode == Keys.UP) {
				brick.setDirection(DIRECTION.UPZ);
			} else if (keycode == Keys.DOWN) {
				brick.setDirection(DIRECTION.DOWNZ);
			}
		}
		return true;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (gameScreen.gameControl.playable
				&& brick.direction == DIRECTION.STOP) {
			if (deltaY > 0) {
				if (deltaX > 0) {
					brick.setDirection(DIRECTION.DOWNZ);
				} else {
					brick.setDirection(DIRECTION.LEFT);
				}
			} else {
				if (deltaX <= 0){
					brick.setDirection(DIRECTION.UPZ);
				}
				else {
					brick.setDirection(DIRECTION.RIGHT);
				}

			}
		}
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
