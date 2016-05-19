package com.mygdx.game.map;

import java.util.Random;

public class Wood {
	final static int PERPENDICULAR = 0;
	final static int HORIZONTAL = 1;
	final static int VERTICLE = 2;

	final static int LEFT = 3;
	final static int RIGHT = 4;
	final static int DOWN = 5;
	final static int UP = 6;

	private int x1pos;
	private int y1pos;
	private int x2pos;
	private int y2pos;
	private int status;

	private int x1Start;
	private int y1Start;

	private MapGenerator map;

	public Wood(int x1pos, int y1pos, MapGenerator map) {
		super();
		this.x1pos = x1pos;
		this.y1pos = y1pos;
		this.x2pos = x1pos;
		this.y2pos = y1pos;
		this.x1Start = x1pos;
		this.y1Start = y1pos;
		status = Wood.PERPENDICULAR;
		this.map = map;
		this.map.setFloor(x1pos, y1pos, Cell.START_POINT);
	}

	public boolean satify(int direction) {
		switch (direction) {
		case Wood.RIGHT:
			if (status == Wood.PERPENDICULAR) {
				if (y2pos + 2 >= map.getLength()) {
					return false;
				} else
					return true;
			} else if (status == Wood.HORIZONTAL) {
				if (y2pos + 1 >= map.getLength()) {
					return false;
				} else
					return true;
			} else {
				if (y2pos + 1 >= map.getLength())
					return false;
				else
					return true;
			}
		case Wood.LEFT:
			if (status == Wood.PERPENDICULAR) {
				if (y1pos - 2 < 0) {
					return false;
				} else
					return true;
			} else if (status == Wood.HORIZONTAL) {
				if (y1pos - 1 < 0) {
					return false;
				} else
					return true;
			} else {
				if (y1pos - 1 < 0)
					return false;
				else
					return true;
			}
		case Wood.UP:
			if (status == Wood.PERPENDICULAR) {
				if (x1pos - 2 < 0) {
					return false;
				} else
					return true;
			} else if (status == Wood.HORIZONTAL) {
				if (x1pos - 1 < 0) {
					return false;
				} else
					return true;
			} else {
				if (x1pos - 1 < 0)
					return false;
				else
					return true;
			}
		case Wood.DOWN:
			if (status == Wood.PERPENDICULAR) {
				if (x2pos + 2 >= map.getWidth()) {
					return false;
				} else
					return true;
			} else if (status == Wood.HORIZONTAL) {
				if (x2pos + 1 >= map.getWidth()) {
					return false;
				} else
					return true;
			} else {
				if (x2pos + 1 >= map.getWidth())
					return false;
				else
					return true;
			}
		default:
			break;
		}
		return false;
	}

	public void move(int direction) {
		switch (direction) {
		case Wood.RIGHT:
			if (status == Wood.PERPENDICULAR) {
				y1pos++;
				y2pos += 2;
				status = Wood.HORIZONTAL;
			} else if (status == Wood.HORIZONTAL) {
				y1pos += 2;
				y2pos++;
				status = Wood.PERPENDICULAR;
			} else {
				y1pos++;
				y2pos++;
			}
			break;
		case Wood.LEFT:
			if (status == Wood.PERPENDICULAR) {
				y1pos -= 2;
				y2pos--;
				status = Wood.HORIZONTAL;
			} else if (status == Wood.HORIZONTAL) {
				y1pos--;
				y2pos -= 2;
				status = Wood.PERPENDICULAR;
			} else {
				y1pos--;
				y2pos--;
			}
			break;
		case Wood.UP:
			if (status == Wood.PERPENDICULAR) {
				x1pos -= 2;
				x2pos--;
				status = Wood.VERTICLE;
			} else if (status == Wood.HORIZONTAL) {
				x1pos--;
				x2pos--;
			} else {
				x1pos--;
				x2pos -= 2;
				status = Wood.PERPENDICULAR;
			}
			break;
		case Wood.DOWN:
			if (status == Wood.PERPENDICULAR) {
				x1pos++;
				x2pos += 2;
				status = Wood.VERTICLE;
			} else if (status == Wood.HORIZONTAL) {
				x1pos++;
				x2pos++;
			} else {
				x1pos += 2;
				x2pos++;
				status = Wood.PERPENDICULAR;
			}
			break;
		default:
			break;
		}
		map.setFloor(x1pos, y1pos, Cell.BRICK);
		map.setFloor(x2pos, y2pos, Cell.BRICK);
	}

	/*
	 * public void normalize() { if (x1pos > x2pos) { int xTemp = x1pos; int
	 * yTemp = y1pos; x1pos = x2pos; y1pos = y2pos; x2pos = xTemp; y2pos =
	 * yTemp; } if (y1pos > y2pos) { int xTemp = x1pos; int yTemp = y1pos; x1pos
	 * = x2pos; y1pos = y2pos; x2pos = xTemp; y2pos = yTemp; } }
	 */
	public void createCircle(int x, int y) {
		map.setFloor(x - 1, y, Cell.BRICK);
		map.setFloor(x + 1, y, Cell.BRICK);
		map.setFloor(x, y - 1, Cell.BRICK);
		map.setFloor(x, y + 1, Cell.BRICK);
	}
	public void levelGenerator(int levelNumber, MapGenerator map, Wood game ) {
		for (int i = 0; i < levelNumber; i++) {
			game.mapGenerator();
			
		}
	}
	public void mapGenerator() {
		int count = 0;
		int raw1 = 0;
		int raw2 = 0;
		Random rand = new Random();
		Random random = new Random();
		while (count < 15) {
			raw1 = rand.nextInt(100);
			raw2 = random.nextInt(100);
			if (raw1 < 21) {
				if (satify(Wood.UP)) {
					move(Wood.UP);
					count++;
				}
			} else if (raw1 >= 21 && raw1 < 42) {
				if (satify(Wood.DOWN)) {
					move(Wood.DOWN);
					count++;
				}
			} else if (raw1 >= 42 && raw1 < 65) {
				if (satify(Wood.LEFT)) {
					move(Wood.LEFT);
					count++;
				}
			} else {
				if (satify(Wood.RIGHT)) {
					move(Wood.RIGHT);
					count++;
				}
			}
		}
		if (status == Wood.HORIZONTAL) {
			// x1 is the same x2
			// nam phia duoi
			if (this.x1pos + 1 == map.getWidth()) {
				move(Wood.UP);
				if (this.y1pos -2 >= 0) {
					move(Wood.LEFT);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				} else {
					move(Wood.RIGHT);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				}
			} else if (this.x1pos - 1 < 0) {
				// nam phia tren
				move(Wood.DOWN);
				if (this.y1pos -2 >= 0) {
					move(Wood.LEFT);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				} else {
					move(Wood.RIGHT);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				}
			} else {
				// khong nam o bien theo width -- nam ben trong bien width
				if (this.y1pos -2 >= 0) {
					move(Wood.LEFT);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				} else {
					move(Wood.RIGHT);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				}
			}
		} else if (status == Wood.VERTICLE) {
			// nam can ben trai
			if (this.y1pos - 1 < 0) {
				move(Wood.RIGHT);
				if (this.x1pos - 2 >= 0) {
					move(Wood.UP);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				} else {
					move(Wood.DOWN);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				}
			} else if (this.y1pos + 1 == map.getLength()) {
				// nam can ben phai
				move(Wood.LEFT);
				if (this.x1pos - 2 >= 0) {
					move(Wood.UP);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				} else {
					move(Wood.DOWN);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				}
			} else {
				// thoa man nam ben trong 2 can cua length
				if (this.x1pos - 2 >= 0) {
					move(Wood.UP);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				} else {
					move(Wood.DOWN);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				}
			}
		} else {
			// truong hop no dung o bien cua map - xet 4 truong hop
			if (this.x1pos - 1 < 0) {
				move(Wood.DOWN);
				if (this.y1pos -2 >= 0) {
					move(Wood.LEFT);
					move(Wood.DOWN);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				} else {
					move(Wood.RIGHT);
					move(Wood.DOWN);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				}
			} else if (this.x1pos + 1 == map.getWidth()) {
				move(Wood.UP);
				if (this.y1pos -2 >= 0) {
					move(Wood.LEFT);
					move(Wood.UP);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				} else {
					move(Wood.RIGHT);
					move(Wood.UP);
					map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
					createCircle(this.x1pos, this.y1pos);
				}
			} else if (this.y1pos - 1 < 0) {
				move(Wood.RIGHT);
				move(Wood.RIGHT);
				map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
				createCircle(this.x1pos, this.y1pos);

			} else if (this.y1pos + 1 == map.getLength()) {
				move(Wood.LEFT);
				move(Wood.LEFT);
				map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
				createCircle(this.x1pos, this.y1pos);
			} else {
				map.setFloor(this.x1pos, this.y1pos, Cell.HOLE);
				createCircle(this.x1pos, this.y1pos);
			}

		}
		// set lai vi tri ban dau de de nhin
		map.setFloor(x1Start, y1Start, Cell.START_POINT);
	}

}
