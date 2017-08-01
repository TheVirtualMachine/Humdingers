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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * This class is for storing bullets
 * @author Vincent Macri
 */

public abstract class BaseBullet extends GameEntity {

	/** Amount of damage the bullet does. */
	float damage;
	
	/** This bullet's image. */
	TextureRegion image;
	
	/** How long the bullet lasts, in seconds. */
	float lifespan;
	
	/** How much time has passed since the bullet was created. */
	float timePassed;
	
	public BaseBullet(Vector2 newPos, Vector2 newVelocity, TextureRegion newImage, float newDamage, float newLifespan) {
		super (newPos, 10, Platformer.fireballRight, BodyDataType.BULLET, 1, StaticVars.BULLET_MASK, StaticVars.ENEMY_MASK);
		image = newImage;
		body.setLinearVelocity(newVelocity);
		damage = newDamage;
		lifespan = newLifespan;
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(image,
				body.getPosition().x * StaticVars.PIXELS_PER_METER - image.getRegionWidth() / 2,
				body.getPosition().y * StaticVars.PIXELS_PER_METER - image.getRegionHeight() / 2);
	}

	@Override
	public void update() {
		timePassed += GameScreen.deltaTime;
		if (timePassed >= lifespan) {
			active = false;
		}
	}

	@Override
	void getFrame() {
	}

}
