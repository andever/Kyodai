package com.andeveloper.kyodai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.Repeat;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameScreen implements Screen, ClickListener {

	private Image bg;
	private Button btBack;
	private Button btReplay;
	private Button btResort;
	private Chessboard chessboard;
	private Kyodai game;
	private int level;
	private Label levelLabel;
	private Image musicImage;
	private TextureRegion musicOff;
	private TextureRegion musicOn;
	private Progressbar progressbar;
	private Label recordLabel;
	private boolean resortAble;
	private Stage stage;
	private long startTime;
	private Label timeLabel;
	private boolean timeNotified;
	private long totalTime;
	private long useTime;

	public GameScreen(Kyodai game) {
		this.game = game;
		this.bg = new Image(new NinePatch(Assets.bg, 10, 10, 20, 20));
		this.bg.height = GameConfig.SCREEN_HEIGHT;
		this.bg.width = GameConfig.SCREEN_WIDTH;
		BitmapFont localBitmapFont = new BitmapFont(
				Gdx.files.internal("font/kyodai.fnt"),
				Gdx.files.internal("font/kyodai.png"), false);
		this.levelLabel = new Label("关卡: " + this.level, new Label.LabelStyle(
				localBitmapFont, new Color(0.0F, 1.0F, 1.0F, 1.0F)));
		this.levelLabel.x = 20.0F;
		this.levelLabel.y = (GameConfig.SCREEN_HEIGHT - this.levelLabel.height - 10.0F);
		this.timeLabel = new Label("时间: 99秒", new Label.LabelStyle(
				localBitmapFont, new Color(0.0F, 1.0F, 1.0F, 1.0F)));
		this.timeLabel.x = (20.0F + (this.levelLabel.x + this.levelLabel.width));
		this.timeLabel.y = (GameConfig.SCREEN_HEIGHT - this.levelLabel.height - 10.0F);
		String str = " ";
		if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL) {
			if (GameConfig.record[this.level] > 0) {
				str = "最快记录: " + GameConfig.record[this.level] + "秒";
			}
		} else {
			if (GameConfig.crazyModeRecord > 0) {
				str = "总用时记录: " + GameConfig.crazyModeRecord + "秒";
			}
		}
		this.recordLabel = new Label(str, new Label.LabelStyle(localBitmapFont,
				new Color(0.0F, 1.0F, 1.0F, 1.0F)));
		this.recordLabel.x = (20.0F + (this.timeLabel.x + this.timeLabel.width));
		this.recordLabel.y = (GameConfig.SCREEN_HEIGHT
				- this.recordLabel.height - 10.0F);
		this.progressbar = new Progressbar(-20 + GameConfig.SCREEN_WIDTH / 6,
				this.levelLabel.y - 32.0F - 10.0F,
				2 * GameConfig.SCREEN_WIDTH / 3, 32.0F, 100f);
		this.musicOn = new TextureRegion(Assets.soundOn);
		this.musicOff = new TextureRegion(Assets.soundOff);
		this.musicImage = new Image(this.musicOn);
		this.musicImage.x = (8 + 5 * GameConfig.SCREEN_WIDTH / 6);
		this.musicImage.y = (this.progressbar.y + this.progressbar.height
				/ 2.0F - this.musicImage.height / 2.0F);
		this.musicImage.setClickListener(this);
		this.chessboard = new Chessboard();
		this.btBack = new Button(Assets.back);
		this.btBack.x = (-80 + (GameConfig.SCREEN_WIDTH / 2
				- Assets.back.getRegionWidth() - Assets.back.getRegionWidth() / 2));
		this.btBack.y = 20.0F;
		this.btReplay = new Button(Assets.replay);
		this.btReplay.x = (GameConfig.SCREEN_WIDTH / 2 - Assets.replay
				.getRegionWidth() / 2);
		this.btReplay.y = 20.0F;
		this.btResort = new Button(Assets.resort);
		this.btResort.x = (80 + (GameConfig.SCREEN_WIDTH / 2 + Assets.next
				.getRegionWidth() / 2));
		this.btResort.y = 20.0F;
		this.btBack.setClickListener(this);
		this.btReplay.setClickListener(this);
		this.btResort.setClickListener(this);

		this.stage = new Stage(GameConfig.SCREEN_WIDTH,
				GameConfig.SCREEN_HEIGHT, true);
	}

	public void click(Actor paramActor, float paramFloat1, float paramFloat2) {
		if (paramActor == this.btBack) {
			if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL) {
				this.game.setScreen(this.game.getSelectLevelScreen());
			} else {
				this.game.setScreen(this.game.getMainMenuScreen());
			}
			if (GameConfig.sound)
				Assets.sel.play();
		}
		if (paramActor == this.btReplay) {
			resetGame(this.level);
			if (GameConfig.sound)
				Assets.sel.play();
		} else if (paramActor == this.btResort) {
			if (this.resortAble) {
				this.chessboard.resort();
				this.resortAble = false;
				Action[] arrayOfAction = new Action[2];
				arrayOfAction[0] = FadeTo.$(0.4F, 0.1F);
				arrayOfAction[1] = Delay.$(FadeTo.$(1.0F, 0.1F),
						10.0F + 2.0F * this.level);
				Sequence localSequence = Sequence.$(arrayOfAction);
				localSequence.setCompletionListener(new OnActionCompleted() {
					public void completed(Action paramAnonymousAction) {
						GameScreen.this.btResort.color.a = 1.0F;
						GameScreen.this.resortAble = true;
					}
				});
				this.btResort.action(localSequence);
				if (GameConfig.sound)
					Assets.sel.play();
			}
		} else if (paramActor == this.musicImage) {
			GameConfig.music = !GameConfig.music;
			if (GameConfig.music) {
				this.musicImage.setRegion(this.musicOn);
				this.musicImage.pack();
				Assets.bgMusic.play();
			} else {
				this.musicImage.setRegion(this.musicOff);
				this.musicImage.pack();
				Assets.bgMusic.pause();
			}
			if (!GameConfig.sound)
				Assets.sel.play();
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void gameOver(boolean paramBoolean) {
		GameConfig.save(game.getApp());
		GameOverScreen localGameOverScreen = this.game.getGameOverScreen();
		localGameOverScreen.setWin(paramBoolean);
		localGameOverScreen.setTime(useTime);
		this.game.setScreen(localGameOverScreen);
	}

	public Kyodai getGame() {
		return this.game;
	}

	public int getLevel() {
		return this.level;
	}

	public void hide() {
		this.stage.clear();
	}

	@Override
	public void pause() {

	}

	public void render(float paramFloat) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		long time = System.currentTimeMillis() - this.startTime;
		if (time > this.totalTime)
			time = this.totalTime;
		long remainTime = this.totalTime - time;
		this.timeLabel.setText("时间: " + remainTime / 1000L + "秒");
		this.progressbar.progress = this.progressbar.maxlen
				* (1.0f - Float.valueOf(time) / Float.valueOf(this.totalTime));
		if ((remainTime < 10999L) && (!this.timeNotified)) {
			Action[] arrayOfAction = new Action[2];
			arrayOfAction[0] = FadeTo.$(0.4F, 0.1F);
			arrayOfAction[1] = FadeTo.$(1.0F, 0.1F);
			Repeat localRepeat = Repeat.$(Sequence.$(arrayOfAction), 18);
			this.progressbar.action(localRepeat);
			if (GameConfig.sound)
				Assets.timenotify.play();
			this.timeNotified = true;
		}
		if (this.progressbar.progress < 0)
			this.progressbar.progress = 0;
		this.stage.act(Gdx.graphics.getDeltaTime());
		this.stage.draw();
		useTime = time / 1000L;
		if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL) {
			if (this.chessboard.getRemaining() <= 0) {
				int lv = 1 + this.level;
				if (lv < GameConfig.MAX_LEVEL)
					GameConfig.levelFlag[lv] = true;
				if ((GameConfig.record[this.level] == 0)
						|| (useTime < GameConfig.record[this.level]))
					GameConfig.record[this.level] = (int) useTime;
				gameOver(true);
			} else if ((remainTime <= 0L)
					&& (this.chessboard.getRemaining() > 0)) {
				gameOver(false);
			}
		} else {
			if (this.chessboard.getRemaining() <= 0) {
				GameConfig.crazyModeTime += useTime;
				int lv = 1 + this.level;
				if (lv >= GameConfig.CRAZY_MODE_LEVEL) {
					if (GameConfig.crazyModeRecord == 0
							|| GameConfig.crazyModeTime < GameConfig.crazyModeRecord) {
						GameConfig.crazyModeRecord = GameConfig.crazyModeTime;
					}
					gameOver(true);
				} else {
					if (GameConfig.sound)
						Assets.zhangsheng.play();
					resetGame(lv);
				}
			} else if ((remainTime <= 0L)
					&& (this.chessboard.getRemaining() > 0)) {
				gameOver(false);
			}
		}

	}

	public void resetGame(int paramInt) {
		int i = Math.min(Math.max(0, paramInt), -1 + GameConfig.MAX_LEVEL);
		this.level = i;
		this.timeNotified = false;
		this.resortAble = true;
		this.levelLabel.setText("关卡: " + (i + 1));
		Label localLabel = this.recordLabel;
		int row = 0;
		int col = 0;
		row = GameConfig.LEVEL[i][0];
		col = GameConfig.LEVEL[i][1];
		String str = " ";
		if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL) {
			if (GameConfig.record[this.level] > 0) {
				str = "最快记录: " + GameConfig.record[this.level] + "秒";
			}
		} else {
			if (GameConfig.crazyModeRecord > 0) {
				str = "总用时记录: " + GameConfig.crazyModeRecord + "秒";
			}
		}
		localLabel.setText(str);
		localLabel.pack();
		this.progressbar.clearActions();
		this.progressbar.color.a = 1.0F;
		this.btResort.clearActions();
		this.btResort.color.a = 1.0F;
		this.chessboard.initChessboard(row, col);
		this.totalTime = (1000 * (row * col) + i * 10);
		this.startTime = System.currentTimeMillis();
		this.useTime = 0;
		if (GameConfig.sound)
			Assets.start.play();
	}

	@Override
	public void resize(int arg0, int arg1) {

	}

	@Override
	public void resume() {

	}

	public void show() {
		game.getApp().setAdViewVisibility(false);
		this.stage.addActor(this.bg);
		if (GameConfig.music) {
			this.musicImage.setRegion(this.musicOn);
			this.musicImage.pack();
		} else {
			this.musicImage.setRegion(this.musicOff);
			this.musicImage.pack();
		}
		this.stage.addActor(this.levelLabel);
		this.stage.addActor(this.timeLabel);
		this.stage.addActor(this.recordLabel);
		this.stage.addActor(this.musicImage);
		this.stage.addActor(this.progressbar);
		this.stage.addActor(this.btBack);
		if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL)
			this.stage.addActor(this.btReplay);
		this.stage.addActor(this.btResort);
		this.stage.addActor(this.chessboard);
		Gdx.input.setInputProcessor(this.stage);

	}

}
