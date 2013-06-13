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

public class MainMenuScreen implements Screen, ClickListener {
	private Image bg;
	private TextButton btExit;
	private Group btGroup;
	private TextButton btNormalPlay;
	private TextButton btCrazyPlay;
	private TextButton btSetting;
	private Kyodai game;
	private Label label;
	private Stage stage;

	public MainMenuScreen(Kyodai paramKyodai) {
		this.game = paramKyodai;
		NinePatch localNinePatch = new NinePatch(Assets.orangebg, 10, 21, 21,
				10);
		BitmapFont localBitmapFont1 = new BitmapFont(
				Gdx.files.internal("font/kyodai.fnt"),
				Gdx.files.internal("font/kyodai.png"), false);
		localBitmapFont1.setScale(1.4F);
		TextButton.TextButtonStyle localTextButtonStyle = new TextButton.TextButtonStyle(
				localNinePatch, localNinePatch, localNinePatch, 0.0F, 0.0F,
				0.0F, 0.0F, localBitmapFont1,
				new Color(0.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F,
						1.0F), new Color(0.0F, 1.0F, 1.0F, 1.0F));
		this.btNormalPlay = new TextButton("普通模式", localTextButtonStyle,
				"btNormalPlay");
		this.btNormalPlay.width = 240.0F;
		this.btNormalPlay.height = 80.0F;
		this.btNormalPlay.x = (GameConfig.SCREEN_WIDTH / 2 - this.btNormalPlay.width / 2.0F);
		this.btNormalPlay.y = 780;

		this.btCrazyPlay = new TextButton("挑战模式", localTextButtonStyle,
				"btCrazyPlay");
		this.btCrazyPlay.width = 240.0F;
		this.btCrazyPlay.height = 80.0F;
		this.btCrazyPlay.x = (GameConfig.SCREEN_WIDTH / 2 - this.btNormalPlay.width / 2.0F);
		this.btCrazyPlay.y = 660;

		this.btSetting = new TextButton("设定", localTextButtonStyle, "btSetting");
		this.btSetting.width = 240.0F;
		this.btSetting.height = 80.0F;
		this.btSetting.x = (GameConfig.SCREEN_WIDTH / 2 - this.btSetting.width / 2.0F);
		this.btSetting.y = 540;
		this.btExit = new TextButton("退出", localTextButtonStyle, "btExit");
		this.btExit.width = 240.0F;
		this.btExit.height = 80.0F;
		this.btExit.x = (GameConfig.SCREEN_WIDTH / 2 - this.btExit.width / 2.0F);
		this.btExit.y = 420;
		this.btNormalPlay.setClickListener(this);
		this.btCrazyPlay.setClickListener(this);
		this.btSetting.setClickListener(this);
		this.btExit.setClickListener(this);
		this.btGroup = new Group("button");
		this.btGroup.addActor(this.btNormalPlay);
		this.btGroup.addActor(this.btCrazyPlay);
		this.btGroup.addActor(this.btSetting);
		this.btGroup.addActor(this.btExit);
		this.bg = new Image(new NinePatch(Assets.bg, 10, 10, 20, 20));
		this.bg.height = GameConfig.SCREEN_HEIGHT;
		this.bg.width = GameConfig.SCREEN_WIDTH;
		BitmapFont localBitmapFont2 = new BitmapFont(
				Gdx.files.internal("font/kyodai.fnt"),
				Gdx.files.internal("font/kyodai.png"), false);
		localBitmapFont2.setScale(4.0F);
		this.label = new Label("连连看", new Label.LabelStyle(localBitmapFont2,
				new Color(0.0F, 1.0F, 1.0F, 1.0F)));
		this.label.x = (GameConfig.SCREEN_WIDTH / 2 - this.label.width / 2.0F);
		this.label.y = (50.0F + (this.btNormalPlay.y + this.btNormalPlay.height));
		this.stage = new Stage(GameConfig.SCREEN_WIDTH,
				GameConfig.SCREEN_HEIGHT, true);
	}

	public void click(Actor paramActor, float paramFloat1, float paramFloat2) {
		if (paramActor == this.btNormalPlay) {
			GameConfig.GAME_MODE = GameConfig.MODE_NORMAL;
			this.game.setScreen(this.game.getSelectLevelScreen());
		} else if (paramActor == this.btCrazyPlay) {
			GameConfig.GAME_MODE = GameConfig.MODE_CRAZY;
			GameConfig.crazyModeTime = 0;
			GameScreen localGameScreen = this.game.getGameScreen();
			localGameScreen.resetGame(0);
			this.game.setScreen(localGameScreen);
		} else if (paramActor == this.btSetting) {
			this.game.setScreen(game.getSettingScreen());
		} else if (paramActor == this.btExit) {
			this.game.exit();
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
		this.stage.addActor(this.label);
		this.stage.addActor(this.btGroup);
		Gdx.input.setInputProcessor(this.stage);
	}
}
