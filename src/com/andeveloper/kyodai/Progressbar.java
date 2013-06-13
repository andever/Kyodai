package com.andeveloper.kyodai;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Progressbar extends Actor {
	private NinePatch bluebg;
	public float maxlen;
	private NinePatch orangebg;
	public float progress;

	public Progressbar(float x, float y, float width,
			float height, float paramInt) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.maxlen = paramInt;
		this.orangebg = new NinePatch(Assets.orangebg, 10, 21, 21, 10);
		NinePatch localNinePatch = new NinePatch(new TextureRegion(
				Assets.bluebg, 4, 4, 24, 24), 0, 24, 24, 0);
		this.bluebg = localNinePatch;
		this.touchable = false;
	}

	public void draw(SpriteBatch paramSpriteBatch, float paramFloat) {
		this.orangebg.draw(paramSpriteBatch, this.x, this.y, this.width,
				this.height);
		paramSpriteBatch.setColor(this.color.r, this.color.g, this.color.b,
				paramFloat * this.color.a);
		float f = this.progress / this.maxlen * (this.width - 4.0F);
		this.bluebg.draw(paramSpriteBatch, 4.0F + this.x, 4.0F + this.y, f,
				this.height - 8.0F);
		paramSpriteBatch.setColor(this.color.r, this.color.g, this.color.b,
				1.0F);
	}

	public Actor hit(float paramFloat1, float paramFloat2) {
		return null;
	}
}
