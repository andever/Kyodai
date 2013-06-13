package com.andeveloper.kyodai;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static Texture[] icons;
	public static Texture line;
	public static Texture win;
	public static Texture lose;
	public static Sound elec;
	public static Sound sel;
	public static Sound start;
	public static Sound zhangsheng;
	public static Sound end;
	public static Sound timenotify;
	public static Music bgMusic;
	public static Texture orangebg;
	public static Texture bluebg;
	public static Texture soundOn;
	public static Texture soundOff;
	public static TextureRegion bg;
	public static TextureRegion resort;
	public static TextureRegion replay;
	public static TextureRegion next;
	public static TextureRegion back;
	public static Texture blueBall;
	public static Texture orangeBall;
	public static List<AtlasRegion> iconRegions;

	public static void load() {
		FileHandle[] arrayOfFileHandle = Gdx.files.internal("icons")
				.list();
		icons = new Texture[arrayOfFileHandle.length];
		for (int i = 0; i < arrayOfFileHandle.length; i++) {
			icons[i] = new Texture(arrayOfFileHandle[i]);
		}
		line = new Texture(Gdx.files.internal("images/line.png"));
		win = new Texture(Gdx.files.internal("images/win.png"));
		lose = new Texture(Gdx.files.internal("images/lose.png"));

		orangebg = new Texture(Gdx.files.internal("images/orangebg.png"));
		bluebg = new Texture(Gdx.files.internal("images/bluebg.png"));
		soundOn = new Texture(Gdx.files.internal("images/sound_on.png"));
		soundOff = new Texture(Gdx.files.internal("images/sound_off.png"));
		orangeBall = new Texture(Gdx.files.internal("images/orangeball.png"));
		blueBall = new Texture(Gdx.files.internal("images/blueball.png"));
		Texture localTexture = new Texture(
				Gdx.files.internal("images/buttons.png"));
		back = new TextureRegion(localTexture, 2, 2, 72, 72);
		next = new TextureRegion(localTexture, 76, 2, 72, 72);
		replay = new TextureRegion(localTexture, 150, 2, 72, 72);
		resort = new TextureRegion(localTexture, 224, 2, 72, 72);
		bg = new TextureRegion(
				new Texture(Gdx.files.internal("images/bg.png")), 309, 537);

		elec = Gdx.audio.newSound(Gdx.files.internal("sound/elec.wav"));
		sel = Gdx.audio.newSound(Gdx.files.internal("sound/sel.wav"));
		start = Gdx.audio.newSound(Gdx.files.internal("sound/start.wav"));
		zhangsheng = Gdx.audio.newSound(Gdx.files
				.internal("sound/zhangsheng.wav"));
		end = Gdx.audio.newSound(Gdx.files.internal("sound/end.wav"));
		timenotify = Gdx.audio.newSound(Gdx.files
				.internal("sound/timenotify.wav"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/bg.mid"));
		bgMusic.setLooping(true);
	}

}
