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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class SelectLevelScreen implements Screen, ClickListener {
	private Button back;
	private Image bg;
	private TextButton[] buttons;
	private Kyodai game;
	private Group levelButtons;
	private TextButton.TextButtonStyle loseFlagStyle;
	private Stage stage;
	private TextButton.TextButtonStyle winFlagStyle;
	private Label label;
	public SelectLevelScreen(Kyodai paramKyodai) {
		this.game = paramKyodai;
		this.levelButtons = new Group("levelButtons");
		NinePatch localNinePatch1 = new NinePatch(Assets.blueBall);
		NinePatch localNinePatch2 = new NinePatch(Assets.orangeBall);
		BitmapFont localBitmapFont = new BitmapFont(
				Gdx.files.internal("font/kyodai.fnt"),
				Gdx.files.internal("font/kyodai.png"), false);
		this.winFlagStyle = new TextButton.TextButtonStyle(localNinePatch2,
				localNinePatch2, localNinePatch2, 0.0F, 0.0F, 0.0F, 0.0F,
				localBitmapFont, new Color(0.0F, 1.0F, 1.0F, 1.0F), new Color(
						1.0F, 1.0F, 1.0F, 1.0F), new Color(0.0F, 1.0F, 1.0F,
						1.0F));
		Color localColor1 = new Color(0.0F, 1.0F, 1.0F, 1.0F);
		Color localColor2 = new Color(1.0F, 1.0F, 1.0F, 1.0F);
		Color localColor3 = new Color(0.0F, 1.0F, 1.0F, 1.0F);
		this.loseFlagStyle = new TextButton.TextButtonStyle(localNinePatch1,
				localNinePatch1, localNinePatch1, 0.0F, 0.0F, 0.0F, 0.0F,
				localBitmapFont, localColor1, localColor2, localColor3);
		this.buttons = new TextButton[GameConfig.levelFlag.length];
		this.bg = new Image(new NinePatch(Assets.bg, 10, 10, 20, 20));
		this.bg.height = GameConfig.SCREEN_HEIGHT;
		this.bg.width = GameConfig.SCREEN_WIDTH;
		this.back = new Button(Assets.back);
		this.back.x = (GameConfig.SCREEN_WIDTH / 2 - Assets.back
				.getRegionWidth() / 2);
		this.back.y = 20.0F;
		this.back.setClickListener(this);
		
		BitmapFont localBitmapFont2 = new BitmapFont(
				Gdx.files.internal("font/kyodai.fnt"),
				Gdx.files.internal("font/kyodai.png"), false);
		localBitmapFont2.setScale(4.0F);
		this.label = new Label("¹Ø¿¨", new Label.LabelStyle(localBitmapFont2,
				new Color(0.0F, 1.0F, 1.0F, 1.0F)));
		
		this.stage = new Stage(GameConfig.SCREEN_WIDTH,GameConfig.SCREEN_HEIGHT, true);
		
	}

	public void click(Actor paramActor, float paramFloat1, float paramFloat2) {
		if (paramActor == this.back) {
			this.game.setScreen(this.game.getMainMenuScreen());
		} else {
			TextButton localTextButton = (TextButton) paramActor;
			int i = -1 + Integer.parseInt(localTextButton.getText().toString());
			if (GameConfig.levelFlag[i]) {
				GameScreen localGameScreen = this.game.getGameScreen();
				localGameScreen.resetGame(i);
				this.game.setScreen(localGameScreen);
			}
		}
		if (GameConfig.sound) {
			Assets.sel.play();
		}
	}

	public void dispose() {
		this.stage.dispose();
	}

	public void hide() {
		this.levelButtons.clear();
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
		this.stage.addActor(this.back);
		int i = (1 + GameConfig.levelFlag.length / 4) / 2;
		int j = GameConfig.SCREEN_HEIGHT / 2 + i * 104;
		int k = -188 + GameConfig.SCREEN_WIDTH / 2;
		for (int m = 0; m < GameConfig.levelFlag.length; ++m) {
			if (GameConfig.levelFlag[m]) {
				this.buttons[m] = new TextButton((m + 1) + "",
						this.winFlagStyle);
			} else {
				this.buttons[m] = new TextButton((m + 1) + "",
						this.loseFlagStyle);
			}
			this.buttons[m].x = (k + 104 * (m % 4));
			this.buttons[m].y = (j - 104 * (m / 4));
			this.buttons[m].setClickListener(this);
			this.levelButtons.addActor(this.buttons[m]);
		}
		this.stage.addActor(this.levelButtons);
		this.label.x = (GameConfig.SCREEN_WIDTH / 2 - this.label.width / 2.0F);
		this.label.y = (50.0F + (this.buttons[0].y + this.buttons[0].height));
		this.stage.addActor(this.label);
		Gdx.input.setInputProcessor(this.stage);
	}

}
