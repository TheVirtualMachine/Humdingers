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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Manages the health and mana bars.
 * @author Vincent Macri
 */
public class BarManager extends Stack {
	
	private Image background;
	private Bar bar;
	
	/**
	 * Create a new {@link BarManager}.
	 * 
	 * @param backTexture
	 * @param barTexture
	 */
	public BarManager(Drawable backTexture, Texture barTexture, float maxValue) {
		this.background = new Image(backTexture);
		bar = new Bar(barTexture, maxValue);
		this.add(background);
		this.add(bar);
	}
	
	/**
	 * Calls {@link Bar#setValue(float)}
	 * @see Bar.setValue(float)
	 * @param value The new value for the bar.
	 */
	public void setBarValue(float value){
		bar.setValue(value);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

}
