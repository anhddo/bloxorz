package com.mygdx.game.map;

public class Cell {
	public static final int VOID = 0;
	public static final int BRICK = 1;
	public static final int HOLE = 3;
	public static final int START_POINT = 2;

	private int status;

	public Cell(int status) {
		this.status = status;
	}

	public Cell() {
		status = Cell.VOID;
	}

	public String value() {
		return String.valueOf(status);
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
