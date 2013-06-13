package com.andeveloper.kyodai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class SettingScreen implements Screen, ClickListener {
	private Image bg;
	private Group btGroup;
	private TextButton btMusic;
	private TextButton btReturn;
	private TextButton btSound;
	private Kyodai game;
	private Stage stage;
	private Label label;
	public SettingScreen(Kyodai paramKyodai) {
		this.game = paramKyodai;
		NinePatch localNinePatch = new NinePatch(Assets.orangebg, 10, 21, 21,
				10);
		BitmapFont localBitmapFont = new BitmapFont(
				Gdx.files.internal("font/kyodai.fnt"),
				Gdx.files.internal("font/kyodai.png"), false);
		localBitmapFont.setScale(1.4F);
		TextButton.TextButtonStyle localTextButtonStyle = new TextButton.TextButtonStyle(
				localNinePatch, localNinePatch, localNinePatch, 0.0F, 0.0F,
				0.0F, 0.0F, localBitmapFont, new Color(0.0F, 1.0F, 1.0F, 1.0F),
				new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(0.0F, 1.0F, 1.0F,
						1.0F));
		this.btSound = new TextButton("关闭音乐", localTextButtonStyle, "btPlay");
		this.btSound.width = 240.0F;
		this.btSound.height = 80.0F;
		this.btSound.x = (GameConfig.SCREEN_WIDTH / 2 - this.btSound.width / 2.0F);
		this.btSound.y = (60 + (GameConfig.SCREEN_HEIGHT / 2 + Assets.next
				.getRegionHeight() / 2));
		this.btMusic = new TextButton("关闭音乐", localTextButtonStyle, "btSetting");
		this.btMusic.width = 240.0F;
		this.btMusic.height = 80.0F;
		this.btMusic.x = (GameConfig.SCREEN_WIDTH / 2 - this.btMusic.width / 2.0F);
		this.btMusic.y = (GameConfig.SCREEN_HEIGHT / 2 - Assets.back
				.getRegionHeight() / 2);
		this.btReturn = new TextButton("返回", localTextButtonStyle, "btExit");
		this.btReturn.width = 240.0F;
		this.btReturn.height = 80.0F;
		this.btReturn.x = (GameConfig.SCREEN_WIDTH / 2 - this.btReturn.width / 2.0F);
		this.btReturn.y = (-60 + (GameConfig.SCREEN_HEIGHT / 2
				- Assets.back.getRegionHeight() - Assets.back.getRegionHeight() / 2));
		this.btSound.setClickListener(this);
		this.btMusic.setClickListener(this);
		this.btReturn.setClickListener(this);
		this.btGroup = new Group("button");
		this.btGroup.addActor(this.btSound);
		this.btGroup.addActor(this.btMusic);
		this.btGroup.addActor(this.btReturn);
		this.bg = new Image(new NinePatch(Assets.bg, 10, 10, 20, 20));
		this.bg.height = GameConfig.SCREEN_HEIGHT;
		this.bg.width = GameConfig.SCREEN_WIDTH;
		BitmapFont localBitmapFont2 = new BitmapFont(
				Gdx.files.internal("font/kyodai.fnt"),
				Gdx.files.internal("font/kyodai.png"), false);
		localBitmapFont2.setScale(4.0F);
		this.label = new Label("设定", new Label.LabelStyle(localBitmapFont2,
				new Color(0.0F, 1.0F, 1.0F, 1.0F)));
		this.label.x = (GameConfig.SCREEN_WIDTH / 2 - this.label.width / 2.0F);
		this.label.y = (50.0F + (this.btSound.y + this.btSound.height));
		this.stage = new Stage(GameConfig.SCREEN_WIDTH,GameConfig.SCREEN_HEIGHT, true);
	}

	public void click(Actor paramActor, float paramFloat1, float paramFloat2) {
		if (paramActor == this.btSound) {
			GameConfig.sound = !GameConfig.sound;
			if (GameConfig.sound) {
				this.btSound.setText("关闭音效");
			} else {
				this.btSound.setText("打开音效");
			}
		} else if (paramActor == this.btMusic) {
			GameConfig.music = !GameConfig.music;
			if (GameConfig.music) {
				this.btMusic.setText("关闭音乐");
				Assets.bgMusic.play();
			}
			else {
				this.btMusic.setText("打开音乐");
				 Assets.bgMusic.pause();
			}
		} else if (paramActor == this.btReturn) {
			this.game.setScreen(this.game.getMainMenuScreen());
		}
		if (GameConfig.sound)
			Assets.sel.play();
	}

	public void dispose() {
		this.stage.dispose();
	}

	public void hide() {
		this.stage.clear();
	}

	public void pause() {
	}

	public void render(float paramFloat) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this.stage.act(Gdx.graphics.getDeltaTime());
		this.stage.draw();
	}

	public void resize(int paramInt1, int paramInt2) {
	}

	public void resume() {
	}

	public void show() {
		game.getApp().setAdViewVisibility(true);
		this.stage.addActor(this.bg);
		if (GameConfig.sound) {
			this.btSound.setText("关闭音效");
		} else {
			this.btSound.setText("打开音效");
		}
		if (GameConfig.music)
			this.btMusic.setText("关闭音乐");
		else {
			this.btMusic.setText("打开音乐");
		}
		this.stage.addActor(this.btGroup);
		this.stage.addActor(this.label);
		Gdx.input.setInputProcessor(this.stage);

	}
}
