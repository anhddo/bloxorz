package com.mygdx.game;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.map.MapGenerator;
import com.sun.org.apache.regexp.internal.recompile;

public class MapTile {
	int[][] mapIdx;
	Brick[][] bricks;
	Model model;
	Vector2 brickPos;
	Vector2 holePos;
	MapGenerator map;
	private float widthTile;
	private float heightTile;
	private float depthTile;
	private float marginTile;

	Vector2 hole;

	public MapTile(MapGenerator map) {
		this.map = map;
		int[][] arr = map.convertTo2DArray();
		mapIdx = new int[arr.length][];
		for (int i = 0; i < mapIdx.length; i++) {
			mapIdx[i] = new int[arr[i].length];
			System.arraycopy(arr[i], 0, mapIdx[i], 0, arr[i].length);
		}
		brickPos = map.initPos;
		holePos = map.targetPos;
		mapIdx[(int) brickPos.x][(int) brickPos.y] = 1;
		int sizeX = arr[0].length, sizeZ = arr.length;

		marginTile = 0.05f * Constant.unit;

		widthTile = Constant.unit;
		heightTile = 0.2f * Constant.unit;
		depthTile = widthTile;
		//
		Random random = new Random();

		bricks = new Brick[sizeZ][sizeX];
		float yDes = -10f * Constant.unit;
		for (int i = 0; i < mapIdx.length; i++) {
			for (int j = 0; j < mapIdx[i].length; j++) {
				if (mapIdx[i][j] == 1) {
					Vector3 pos = new Vector3(widthTile * j + widthTile / 2,
							yDes, i * depthTile + depthTile / 2);
					Brick brick = Brick
							.createBrick(widthTile - marginTile, heightTile,
									depthTile - marginTile, pos, "box.jpeg");
					brick.direction = DIRECTION.UPY;
					brick.yDestination = -heightTile / 2;
					brick.deltaYStep = Math.abs(yDes - brick.yDestination)
							/ (Constant.unit * 5 + random
									.nextInt((int) (5 * Constant.unit)));
					bricks[i][j] = brick;
				}
				if (mapIdx[i][j] == 3) {
					hole = new Vector2(i, j);
				}
			}
		}

	}

	// check pos(z,x) is space or not
	boolean isRound(int z, int x) {
		if (z >= 0 && z < mapIdx.length && x >= 0 && x < mapIdx[0].length
				&& mapIdx[z][x] == 1)
			return true;
		return false;
	}

	// all tiles go up with different velocity.
	public void loadMap() {
		for (int i = 0; i < mapIdx.length; i++) {
			for (int j = 0; j < mapIdx[i].length; j++) {
				if (mapIdx[i][j] == 1) {
					bricks[i][j].update();
				}
			}
		}
	}

	// return true if all tile is stop so loading Maptile is finish
	public boolean doneLoading() {
		for (int i = 0; i < mapIdx.length; i++) {
			for (int j = 0; j < mapIdx[i].length; j++) {
				if (mapIdx[i][j] == 1
						&& bricks[i][j].direction != DIRECTION.STOP) {
					return false;
				}
			}
		}

		Vector3 pos = new Vector3(widthTile * hole.y + widthTile / 2, 0, hole.x
				* depthTile + depthTile / 2);
		Brick brick = Brick.createBrick(widthTile - marginTile, heightTile,
				depthTile - marginTile, pos, "box.jpeg",0.2f);
		bricks[(int) hole.x][(int) hole.y] = brick;
		return true;
	}

	public void draw(ModelBatch modelBatch, Environment environment) {
		for (int i = 0; i < mapIdx.length; i++) {
			for (int j = 0; j < mapIdx[i].length; j++) {
				if (mapIdx[i][j] == 1 || mapIdx[i][j] == 3) {
					if (bricks[i][j] != null)
						bricks[i][j].draw(modelBatch);
				}
			}
		}
	}
}
