package com.andeveloper.kyodai;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Chess extends Image {
	public static int REGION_HEIGHT = 60;
	public static int REGION_WIDTH = 60;
	/**
	 * ���±�
	 */
	public int row;
	/**
	 * ���±�
	 */
	public int col;

	/**
	 * ����������
	 */
	public boolean disappearing;
}
