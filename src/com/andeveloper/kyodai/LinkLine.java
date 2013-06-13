package com.andeveloper.kyodai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LinkLine extends Actor {

	private TextureRegion textureRegion;

	public LinkLine(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(textureRegion, x, y, originX, originY,
				textureRegion.getRegionWidth(),
				textureRegion.getRegionHeight(), scaleX, scaleY, rotation);
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}
}
