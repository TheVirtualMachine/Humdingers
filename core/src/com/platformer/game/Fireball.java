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

/**
 * 
 */
package com.platformer.game;

import com.badlogic.gdx.math.Vector2;

/**
 * The fireball class.
 * @author Vincent Macri
 */
public class Fireball extends BaseBullet {
	
	/** How long the fireball lasts, in seconds. */
	private final static float FIREBALL_LIFESPAN = 10f;
	
	/** How much damage the fireball does. */
	private final static float FIREBALL_DAMAGE = 1f;
	
	/**
	 * @param newPos
	 * @param newVelocity
	 */
	public Fireball(Vector2 newPos, Vector2 newVelocity) {
		super(newPos, newVelocity, newVelocity.x <= 0 ? Platformer.fireballLeft : Platformer.fireballRight, FIREBALL_DAMAGE, FIREBALL_LIFESPAN);
		body.setUserData(this);
	}

}
