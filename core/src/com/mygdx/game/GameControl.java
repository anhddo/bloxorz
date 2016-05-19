package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.map.MapGenerator;
import com.mygdx.game.map.Wood;

enum GAMESTATE {
	PLAYING, WIN, LOOSE, RESTART, NEXTLEVEL, FINISH
};

public class GameControl {
	MapTile mapTile;
	Brick brick;

	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;

	public ModelInstance instance;
	private Array<ModelInstance> instances;
	boolean playable = false;
	private float time;
	private SkyBox skybox;

	SpriteBatch spriteBatch;
	BitmapFont bitmapFont;

	boolean renderMap, renderSkybox, renderBrick;
	float fpsLimit;
	float unit = Constant.unit;
	private Sound backGroundSound;
	GAMESTATE gamestate;
	private Vector3 camLookAt;

	static GameControl createGameControl(int level) {
		return new GameControl(null, level);
	}

	static GameControl createGameControl(MapGenerator mapGenerator) {
		return new GameControl(mapGenerator, 0);
	}

	public GameControl(MapGenerator mapGenerator, int level) {
		renderMap = true;
		renderBrick = true;
		renderSkybox = true;
		fpsLimit = 20f;
		bitmapFont = new BitmapFont();
		bitmapFont.setColor(Color.RED);
		spriteBatch = new SpriteBatch();

		backGroundSound = Gdx.audio.newSound(Gdx.files.internal("bgSound.wav"));
		if (((Bloxorz) Gdx.app.getApplicationListener()).isSoundEnabled())
			backGroundSound.loop();

		//
		skybox = new SkyBox();
		//
		if (renderMap == true && mapGenerator == null) {
			mapTile = new MapTile(generateMap(level));
		} else if (mapGenerator != null)
			mapTile = new MapTile(mapGenerator);
		//
		float w = Constant.unit, h = Constant.unit * 2, d = Constant.unit;
		float yPos = 5 * unit;
		Vector2 brickPos = mapTile.brickPos;
		if (((Bloxorz) Gdx.app.getApplicationListener()).isSoundEnabled())
			brick = new Brick(w, h, d, new Vector3(brickPos.y * unit + w / 2,
					yPos, brickPos.x * unit + d / 2), "metal.png", true, 1f);
		else
			brick = new Brick(w, h, d, new Vector3(brickPos.y * unit + w / 2,
					yPos, brickPos.x * unit + d / 2), "metal.png", false, 1f);
		brick.yDestination = h / 2;
		brick.deltaYStep = (yPos - brick.yDestination) / 20;
		brick.direction = DIRECTION.DOWNY;
		brick.setMapTile(mapTile);
		//
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));
		//
		modelBatch = new ModelBatch();
		//
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		// cam.position.set(2*unit, 10*Constant.unit, 4*unit);
		// cam.lookAt(4*unit, 0, 2*unit);
		cam.near = 1f;
		cam.far = 700f;
		updateCamCorrespondToBrick();
	}

	private MapGenerator generateMap(int level) {
		// Random rand = new Random();
		// Random random = new Random();
		int x = 0;
		int y = 0;
		MapGenerator map = new MapGenerator(5, 10);
		// x = rand.nextInt(5);
		// y = rand.nextInt(9);
		Wood game = new Wood(x, y, map);
		game.mapGenerator();
		// map.print();
		return map;
	}

	private void updateCam() {
		float distance = brick.position.dst((new Vector3(camLookAt.x,
				brick.position.y, camLookAt.z)));
		if (distance > 3 * unit) {
			updateCamCorrespondToBrick();
		}
	}

	private void updateCamCorrespondToBrick() {
		Vector3 camPos = new Vector3(brick.position);
		float y = 3 * unit;
		camPos.add(new Vector3(4 * unit / 2, y, 4 * unit));
		camLookAt = new Vector3(camPos);
		camLookAt.add(0.5f * unit, -y, -unit);

		cam.position.set(camPos);
		cam.lookAt(camLookAt);
		cam.update();
	}

	public void createAxes() {
		ModelBuilder modelBuilder = new ModelBuilder();
		ModelInstance xAxes = null;
		ModelInstance yAxes = null;
		ModelInstance zAxes = null;
		int length = 11;
		xAxes = new ModelInstance(modelBuilder.createArrow(
				new Vector3(0, 0, 0), new Vector3(length, 0, 0), new Material(
						ColorAttribute.createDiffuse(Color.RED)), 5));
		yAxes = new ModelInstance(modelBuilder.createArrow(
				new Vector3(0, 0, 0), new Vector3(0, length, 0), new Material(
						ColorAttribute.createDiffuse(Color.GREEN)), 5));
		zAxes = new ModelInstance(modelBuilder.createArrow(
				new Vector3(0, 0, 0), new Vector3(0, 0, length), new Material(
						ColorAttribute.createDiffuse(Color.BLUE)), 5));
		instances.add(xAxes);
		instances.add(yAxes);
		instances.add(zAxes);
	}

	float tttime = 0;

	public void update() {

		tttime += time;
		time = Gdx.graphics.getDeltaTime();
		// use this to limit fps
		if (time < 1f / fpsLimit) {
			try {
				Thread.sleep((long) (1000 * (1f / fpsLimit - time)));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (playable && renderMap) {
			int out = brick.isFallOutOfMap();
			// we must check Map object exist or not because
			// brick.isFallFromHole()
			// use Map to check
			if (brick.isFallIntoHole() && brick.direction == DIRECTION.STOP) {
				gamestate = GAMESTATE.WIN;
				brick.setDirection(DIRECTION.DOWNY);
			} else if (out != 0) {
				if (out == 1)
					brick.setDirection(DIRECTION.DOWNY);
				else if (brick.direction != DIRECTION.DOWNY)
					brick.setDirection(DIRECTION.ROTATEOUT);
				gamestate = GAMESTATE.LOOSE;
			}
		}
		// if brick position.y==unit so it stand on ground, so game is playable
		// brick must update before check playable
		brick.update();
		// if Brick not falling play sound
		// check playable: brick is in required position and mapTile finish
		// loading
		if (!playable && Math.abs(brick.position.y - brick.height / 2) < 0.01f
				&& renderMap && mapTile.doneLoading()) {
			playable = true;
			brick.setDirection(DIRECTION.STOP);
		}
		if (playable) {
			brick.yDestination = -10f;
			brick.deltaYStep = (Constant.unit - brick.yDestination) / 10;
		}
		// If game playable so maptile is fixed position no need to update
		if (renderMap && !playable)
			mapTile.loadMap();
		// camController.update();
		// rendering section
		// updateCam();
		modelBatch.begin(cam);
		if (renderSkybox)
			skybox.render(modelBatch);
		if (renderMap)
			mapTile.draw(modelBatch, environment);
		if (renderBrick) {
			spriteBatch.enableBlending();
			brick.draw(modelBatch);
			spriteBatch.disableBlending();
		}
		modelBatch.end();
//		updateCamCorrespondToBrick();
	}

	public void dispose() {
		modelBatch.dispose();
		backGroundSound.stop();
		backGroundSound.dispose();
	}
}
