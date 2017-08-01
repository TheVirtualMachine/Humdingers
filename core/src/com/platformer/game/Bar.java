/*******************************************************************************************
 * Humdingers is a simple fighting platformer game created in Java with the libGDX library.
 * Copyright (C) 2017 Vincent Macri
 * 
 * This file is part of Humdingers.
 *
 * Humdingers is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Humdingers is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Humdingers.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************************/

package com.platformer.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * A single bar. The bar that the user sees is composed of two of these, one for the background, and one for the foreground.
 * @author Vincent Macri
 */
public class Bar extends Image {

	/** How full this bar currently is. */
	private float value;
	/** The maximum this bar can be filled to. */
	private final float MAX_VALUE;
	/** The texture of the filled bar. */
	private final Texture BAR_TEXTURE;
	
	/**
	 * @param texture
	 */
	public Bar(Texture texture, float maxValue) {
		super(texture);
		BAR_TEXTURE = texture;
		this.value = maxValue;
		this.MAX_VALUE = maxValue;
	}

	/**
	 * Setter for {@link #value}
	 * @param value The new value for this bar.
	 */
	public void setValue(float value) {
		this.value = MathUtils.clamp(value, 0, this.MAX_VALUE);
	}

	public void draw (Batch batch, float parentAlpha) {
		validate();

		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float x = getX();
		float y = getY();

		batch.draw(BAR_TEXTURE, x + getImageX(), y + getImageY(), 0, 0, (int) (BAR_TEXTURE.getWidth() * value / MAX_VALUE), BAR_TEXTURE.getHeight());
	}
	
}
