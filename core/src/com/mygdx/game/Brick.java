package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

enum DIRECTION {
	LEFT, RIGHT, UPZ, DOWNZ, UPY, DOWNY, STOP, ROTATEOUT
};

public class Brick {
	Vector3 position;
	DIRECTION direction;
	private float time = 0;
	private float updateTime;
	private ModelInstance modelInstance=null;
	float height;
	float width;
	float depth;
	float angle;
	boolean hasMoveSound;
	Sound moveSound;

	private float deltaAngle;
	private float unit;
	private MapTile mapTile;
	float deltaYStep;
	float yDestination;

	int moveCount = 0;

	public void setMapTile(MapTile mapTile) {
		this.mapTile = mapTile;
	}

	public void setDirection(DIRECTION direction) {
		this.direction = direction;
		moveCount++;
	}

	static Brick createBrick(float w, float h, float d, Vector3 pos,
			String texture,float opacity) {
		return new Brick(w, h, d, pos, texture, false, opacity);
	}
	static Brick createBrick(float w, float h, float d, Vector3 pos,
			String texture) {
		return new Brick(w, h, d, pos, texture, false, 1);
	}

	public Brick(float w, float h, float d, Vector3 pos, String texture,
			boolean hasMoveSound, float opacity) {
		this.hasMoveSound = hasMoveSound;
		if (this.hasMoveSound) {
			moveSound = Gdx.audio.newSound(Gdx.files.internal("1138.wav"));
		}
		updateTime = 0.01f;
		unit = Constant.unit;
		deltaYStep = 0.1f;
		Material material = new Material();
		Texture brickTexture = new Texture(texture);
		// brickTexture.setFilter(TextureFilter.Nearest, TextureFilter.Linear);
		BlendingAttribute blendingAttribute = new BlendingAttribute(opacity);
		material.set(TextureAttribute.createDiffuse(brickTexture));
		material.set(blendingAttribute);
		depth = d;
		width = w;
		height = h;

		modelInstance = new ModelInstance(new ModelBuilder().createBox(width,
				height, depth, material, Usage.Position | Usage.Normal
						| Usage.TextureCoordinates));

		// position = getPosition();
		position = pos;

		modelInstance.transform.translate(position);
		deltaAngle = 10f;
		angle = 0;
		direction = DIRECTION.STOP;
	}

	// angleSign is 1 or -1;
	Matrix4 createTransformMatrix(Vector3 offset, Vector3 axe, float angleSign) {
		Matrix4 transform = new Matrix4();
		transform.mulLeft(new Matrix4().translate(-offset.x, -offset.y,
				-offset.z));
		transform.mulLeft(new Matrix4().rotate(axe.x, axe.y, axe.z, angleSign
				* deltaAngle));
		transform
				.mulLeft(new Matrix4().translate(offset.x, offset.y, offset.z));
		return transform;
	}

	void rotateBrick() {
		angle += deltaAngle;
		Vector3 offset = null;
		Vector3 axe = new Vector3();
		float angleSign = 0f;
		Vector3 tempPosition = position;
		// Find the offset from the rotation axes to the unit axis
		if (direction == DIRECTION.RIGHT) {
			offset = new Vector3(tempPosition.x + width / 2, 0, 0);
			angleSign = -1;
			axe.z = 1;
		} else if (direction == DIRECTION.LEFT) {
			offset = new Vector3(tempPosition.x - width / 2, 0, 0);
			angleSign = 1;
			axe.z = 1;
		} else if (direction == DIRECTION.UPZ) {
			offset = new Vector3(0, 0, tempPosition.z - depth / 2);
			angleSign = -1;
			axe.x = 1;
		} else if (direction == DIRECTION.DOWNZ) {
			offset = new Vector3(0, 0, tempPosition.z + depth / 2);
			angleSign = 1;
			axe.x = 1;
		} else if (direction == DIRECTION.ROTATEOUT) {
			offset = new Vector3();
			switch (isFallOutOfMap()) {
			case 2:
				angleSign = +1;
				axe.x = 1;
				offset = new Vector3(0, 0, position.z);
				break;
			case 3:
				angleSign = -1;
				axe.x = 1;
				offset = new Vector3(0, 0, position.z);
				break;
			case 4:
				angleSign = 1;
				axe.z = 1;
				offset = new Vector3(position.x, 0, 0);
				break;
			case 5:
				offset = new Vector3(position.x, 0, 0);
				angleSign = -1;
				axe.z = 1;
				break;
			default:
				break;
			}
		}
		// create transfrom matrix and mullef it with the brick's matrix
		modelInstance.transform.mulLeft(createTransformMatrix(offset, axe,
				angleSign));

	}

	public boolean isFallIntoHole() {
		if (width == unit && depth == unit) {
			int u = (int) Math.floor(position.z / unit);
			int v = (int) Math.floor(position.x / unit);

			try {
				if (mapTile.mapIdx[u][v] == 3) {// 0 is a hole
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println("" + u + " " + v);
				e.printStackTrace();
			}
		}
		return false;
	}

	public int isFallOutOfMap() {
		int z = (int) Math.floor(position.z / unit);
		int x = (int) Math.floor(position.x / unit);

		boolean half1, half2, left, right;
		// half of brick out
		if (height == unit) {
			if (depth == unit * 2) {

				// 0<-left
				// x<-half1
				// x<-half2
				// 0<-right
				left = mapTile.isRound(z - 2, x);
				half1 = mapTile.isRound(z - 1, x);
				half2 = mapTile.isRound(z, x);
				right = mapTile.isRound(z + 1, x);

				if (half1 == true && half2 == false && right == false)
					return 2;
				else if (left == false && half1 == false && half2 == true)
					return 3;
				else if (half1 == false && half2 == false)
					return 1;
			} else if (width == unit * 2) {
				left = mapTile.isRound(z, x - 2);
				half1 = mapTile.isRound(z, x - 1);
				half2 = mapTile.isRound(z, x);
				right = mapTile.isRound(z, x + 1);
				if (left == false && half1 == false && half2 == true)
					return 4;
				else if (half1 == true && half2 == false && right == false)
					return 5;
				else if (half1 == false && half2 == false)
					return 1;
			}
		}
		if (height == 2 * unit && mapTile.isRound(z, x) == false) {
			return 1;
		}

		return 0;
	}

	public void translateYaxe() {
		float stepY = 0;
		if (Math.abs(position.y - yDestination) < 0.01f) {
			direction = DIRECTION.STOP;
			return;
		}
		if (direction == DIRECTION.DOWNY) {
			stepY = -deltaYStep;
		} else if (direction == DIRECTION.UPY) {
			stepY = deltaYStep;
		}
		position.add(0, stepY, 0);
		modelInstance.transform.mulLeft(new Matrix4().translate(0, stepY, 0));
	}

	public void update() {
		time += Gdx.graphics.getDeltaTime();
		if (time > updateTime) {
			time = 0;
			// check Brick fall into a hole
			if (direction == DIRECTION.DOWNY || direction == DIRECTION.UPY) {
				translateYaxe();
			} else if (isMoveState() || direction == DIRECTION.ROTATEOUT) {
				rotateBrick();
				// if angle ==90 degree rotation is finish
				if (angle == 90f) {
					angle = 0;
					if (direction == DIRECTION.RIGHT) {
						position.add(width / 2 + height / 2, -height / 2
								+ width / 2, 0);
						float[] s = swap(height, width);
						height = s[0];
						width = s[1];
					} else if (direction == DIRECTION.LEFT) {
						position.add(-width / 2 - height / 2, -height / 2
								+ width / 2, 0);
						float[] s = swap(height, width);
						height = s[0];
						width = s[1];
					} else if (direction == DIRECTION.UPZ) {
						position.add(0, -height / 2 + depth / 2, -depth / 2
								- height / 2);
						float[] s = swap(height, depth);
						height = s[0];
						depth = s[1];
					} else if (direction == DIRECTION.DOWNZ) {
						position.add(0, -height / 2 + depth / 2, depth / 2
								+ height / 2);
						float[] s = swap(height, depth);
						height = s[0];
						depth = s[1];
					}
					if (direction == DIRECTION.ROTATEOUT) {
						direction = DIRECTION.DOWNY;
					} else if (1 != isFallOutOfMap()) {
						setDirection(DIRECTION.STOP);
						if (hasMoveSound) {
							moveSound.play();
						}
					}
				}
			}
		}
	}

	boolean isMoveState() {
		if (direction == DIRECTION.LEFT || direction == DIRECTION.RIGHT
				|| direction == DIRECTION.UPZ || direction == DIRECTION.DOWNZ)
			return true;
		return false;

	}

	private float[] swap(float a, float b) {
		return new float[] { b, a };
	}

	public void draw(ModelBatch modelBatch) {
		if (modelInstance != null)
			modelBatch.render(modelInstance);
	}

}
