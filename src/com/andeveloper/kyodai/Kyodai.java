package com.andeveloper.kyodai;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Kyodai extends Game {
	private KyodaiActivity app;
	private AlertDialog dialog;
	private GameOverScreen gameOverScreen;
	private GameScreen gameScreen;
	private MainMenuScreen mainMenuScreen;
	private SelectLevelScreen selectLevelScreen;
	private SettingScreen settingScreen;
	private boolean showing;

	public Kyodai(KyodaiActivity kyodaiActivity) {
		this.app = kyodaiActivity;
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				kyodaiActivity);
		localBuilder.setIcon(R.drawable.ic_launcher);
		localBuilder.setTitle("提示");
		localBuilder.setMessage("确认退出吗？");
		localBuilder.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						if (GameConfig.music)
							Assets.bgMusic.stop();
						GameConfig.save(Kyodai.this.getApp());
						Kyodai.this.app.finish();
					}
				});
		localBuilder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						Kyodai.this.showing = false;
					}
				});
		localBuilder.setCancelable(false);
		this.dialog = localBuilder.create();
	}

	@Override
	public void create() {
		GameConfig.load(this.app);
		Assets.load();
		this.mainMenuScreen = new MainMenuScreen(this);
		this.selectLevelScreen = new SelectLevelScreen(this);
		this.gameScreen = new GameScreen(this);
		this.gameOverScreen = new GameOverScreen(this);
		this.settingScreen = new SettingScreen(this);
		setScreen(this.mainMenuScreen);
		if (GameConfig.music)
			Assets.bgMusic.play();
	}

	public void exit() {
		if (!this.showing) {
			this.showing = true;
			this.app.runOnUiThread(new Runnable() {
				public void run() {
					if (!Kyodai.this.dialog.isShowing())
						Kyodai.this.dialog.show();
				}
			});
		}
	}

	public KyodaiActivity getApp() {
		return this.app;
	}

	public GameOverScreen getGameOverScreen() {
		return this.gameOverScreen;
	}

	public GameScreen getGameScreen() {
		return this.gameScreen;
	}

	public MainMenuScreen getMainMenuScreen() {
		return this.mainMenuScreen;
	}

	public SelectLevelScreen getSelectLevelScreen() {
		return this.selectLevelScreen;
	}

	public void render() {
		super.render();
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (getScreen() == this.mainMenuScreen) {
				exit();
				return;
			} else if (getScreen() == this.gameScreen) {
				if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL)
					setScreen(this.selectLevelScreen);
				else
					setScreen(this.mainMenuScreen);
			} else if (getScreen() == this.selectLevelScreen) {
				setScreen(this.mainMenuScreen);
			} else if (getScreen() == this.gameOverScreen) {
				if (GameConfig.GAME_MODE == GameConfig.MODE_NORMAL)
					setScreen(this.selectLevelScreen);
				else
					setScreen(this.mainMenuScreen);
			} else if (getScreen() == this.settingScreen) {
				setScreen(this.mainMenuScreen);
			}
			if (GameConfig.sound) {
				Assets.sel.play();
			}
		}
	}

	public void setApp(KyodaiActivity paramKyodaiActivity) {
		this.app = paramKyodaiActivity;
	}

	public void setGameOverScreen(GameOverScreen paramGameOverScreen) {
		this.gameOverScreen = paramGameOverScreen;
	}

	public void setGameScreen(GameScreen paramGameScreen) {
		this.gameScreen = paramGameScreen;
	}

	public void setMainMenuScreen(MainMenuScreen paramMainMenuScreen) {
		this.mainMenuScreen = paramMainMenuScreen;
	}

	public void setSelectLevelScreen(SelectLevelScreen paramSelectLevelScreen) {
		this.selectLevelScreen = paramSelectLevelScreen;
	}

	public void setSettingScreen(SettingScreen settingScreen) {
		this.settingScreen = settingScreen;
	}

	public SettingScreen getSettingScreen() {
		return settingScreen;
	}
}
