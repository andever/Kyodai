package com.andeveloper.kyodai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameOverScreen implements Screen, ClickListener {
	private Button back;
	private Image bg;
	private Image flag;
	private Kyodai game;
	private Image loseFlag;
	private Button next;
	private Button replay;
	private Stage stage;
	private boolean win;
	private Image winFlag;
	private Label label;
	private long time;

	public GameOverScreen(Kyodai paramKyodai) {
		this.game = paramKyodai;
		this.winFlag = new Image(Assets.win);
		this.loseFlag = new Image(Assets.lose);
		this.back = new Button(Assets.back);
		this.replay = new Button(Assets.replay);
		this.next = new Button(Assets.next);
		this.back.setClickListener(this);
		this.replay.setClickListener(this);
		this.next.setClickListener(this);
		this.bg = new Image(new NinePatch(Assets.bg, 10, 10, 20, 20));
		this.bg.height = GameConfig.SCREEN_HEIGHT;
		this.bg.width = GameConfig.SCREEN_WIDTH;
		BitmapFont localBitmapFont2 = new BitmapFont(
				Gdx.files.internal("font/kyodai.fnt"),
				Gdx.files.internal("font/kyodai.png"), false);
		localBitmapFont2.setScale(1.4F);
		this.label = new Label("999秒! 恭喜闯关成功!", new Label.LabelStyle(
				localBitmapFont2, new Color(0.0F, 1.0F, 1.0F, 1.0F)));
		this.stage = new Stage(GameConfig.SCREEN_WIDTH,
				GameConfig.SCREEN_HEIGHT, true);
	}

	public void click(Actor paramActor, float paramFloat1, float paramFloat2) {
		if (paramActor == this.back) {
			if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL) {
				this.game.setScreen(this.game.getSelectLevelScreen());
			} else {
				this.game.setScreen(this.game.getMainMenuScreen());
			}
		} else if (paramActor == this.replay) {
			if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL) {
				GameScreen localGameScreen2 = this.game.getGameScreen();
				localGameScreen2.resetGame(localGameScreen2.getLevel());
				this.game.setScreen(localGameScreen2);
			} else {
				GameConfig.crazyModeTime = 0;
				GameScreen localGameScreen2 = this.game.getGameScreen();
				localGameScreen2.resetGame(0);
				this.game.setScreen(localGameScreen2);
			}
		} else if (paramActor == this.next) {
			GameScreen localGameScreen1 = this.game.getGameScreen();
			localGameScreen1.resetGame(1 + localGameScreen1.getLevel());
			this.game.setScreen(localGameScreen1);
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

	public boolean isWin() {
		return this.win;
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

	public void setWin(boolean paramBoolean) {
		this.win = paramBoolean;
	}

	public void show() {
		game.getApp().setAdViewVisibility(true);
		this.back.x = (-80 + (GameConfig.SCREEN_WIDTH / 2
				- Assets.back.getRegionWidth() - Assets.back.getRegionWidth() / 2));
		this.back.y = 20.0F;
		int i = 1 + this.game.getGameScreen().getLevel();
		if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL) {
			if ((i >= GameConfig.MAX_LEVEL) || (!GameConfig.levelFlag[i])) {
				this.replay.x = (80 + (GameConfig.SCREEN_WIDTH / 2 + Assets.replay
						.getRegionWidth() / 2));
				this.replay.y = 20.0F;
				// 隐藏下一关按钮
				this.next.x = (GameConfig.SCREEN_WIDTH / 2 - Assets.next
						.getRegionWidth() / 2);
				this.next.y = 20.0F;
				this.next.visible = false;
			} else {
				this.replay.x = (GameConfig.SCREEN_WIDTH / 2 - Assets.replay
						.getRegionWidth() / 2);
				this.replay.y = 20.0F;
				this.next.x = (80 + (GameConfig.SCREEN_WIDTH / 2 + Assets.next
						.getRegionWidth() / 2));
				this.next.y = 20.0F;
				this.next.visible = true;
			}
		} else {
			this.replay.x = (80 + (GameConfig.SCREEN_WIDTH / 2 + Assets.replay
					.getRegionWidth() / 2));
			this.replay.y = 20.0F;
			// 隐藏下一关按钮
			this.next.x = (GameConfig.SCREEN_WIDTH / 2 - Assets.next
					.getRegionWidth() / 2);
			this.next.y = 20.0F;
			this.next.visible = false;
		}

		if (this.win) {
			if (GameConfig.sound)
				Assets.zhangsheng.play();
			this.flag = this.winFlag;
			if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL)
				this.label.setText(time + "秒! 恭喜闯关成功!");
			else
				this.label.setText(GameConfig.crazyModeTime + "秒! 恭喜挑战成功!");
		} else {
			if (GameConfig.sound)
				Assets.end.play();
			this.flag = this.loseFlag;
			if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL)
				this.label.setText("很遗憾,闯关失败!");
			else
				this.label.setText("很遗憾,挑战失败!");
		}
		this.flag.x = (GameConfig.SCREEN_WIDTH / 2 - this.flag.getRegion()
				.getRegionWidth() / 2);
		this.flag.y = (100 + (GameConfig.SCREEN_HEIGHT / 2 - this.flag
				.getRegion().getRegionHeight() / 2));
		this.label.x = (GameConfig.SCREEN_WIDTH / 2 - this.label.width / 2.0F);
		this.label.y = this.flag.y - this.label.height - 40.0F;
		this.label.pack();
		this.stage.addActor(this.bg);
		this.stage.addActor(this.flag);
		this.stage.addActor(this.next);
		this.stage.addActor(this.back);
		this.stage.addActor(this.replay);
		this.stage.addActor(this.label);
		Gdx.input.setInputProcessor(this.stage);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
