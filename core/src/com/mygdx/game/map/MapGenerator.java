package com.mygdx.game.map;

import com.badlogic.gdx.math.Vector2;

public class MapGenerator {
	private int width;
	private int length;
	private Cell[][] cells;
	public Vector2 initPos;
	public Vector2 targetPos;

	public MapGenerator(int width, int length) {
		this.width = width;
		this.length = length;
		cells = new Cell[width][length];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.length; j++) {
				cells[i][j] = new Cell();
			}
		}
	}

	public void print() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				System.out.print("[" + cells[i][j].value() + "] ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public int[][] convertTo2DArray() {
		int[][] arr=new int[width][length];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				arr[i][j]=Integer.parseInt(cells[i][j].value());
				if(arr[i][j]==2){
					initPos=new Vector2(i, j);
				}
				if(arr[i][j]==3){
					targetPos=new Vector2(i, j);
				}
			}
		}
		return arr;
	}

	public void setFloor(int x, int y, int status) {
		cells[x][y].setStatus(status);
	}

	public int getWidth() {
		return width;
	}

	public int getLength() {
		return length;
	}
}
