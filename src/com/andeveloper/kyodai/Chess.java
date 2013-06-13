package com.andeveloper.kyodai;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Chess extends Image {
	public static int REGION_HEIGHT = 60;
	public static int REGION_WIDTH = 60;
	/**
	 * 行下标
	 */
	public int row;
	/**
	 * 列下标
	 */
	public int col;

	/**
	 * 正在消除的
	 */
	public boolean disappearing;
}
