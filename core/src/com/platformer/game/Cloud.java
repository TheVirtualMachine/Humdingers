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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * @author Vincent Macri
 */
public class Cloud extends Entity {


	public Cloud(Vector2 loc, TextureRegion cloudImage) {
		super(loc, cloudImage, BodyDataType.CLOUD, 0, StaticVars.CLOUD_MASK, StaticVars.CLOUD_MASK, BodyType.KinematicBody);
		body.setLinearVelocity(1, 0);
	}

	/* (non-Javadoc)
	 * @see com.platformer.game.Entity#update()
	 */
	@Override
	public void update() {
		if (body.getPosition().x * StaticVars.PIXELS_PER_METER > StaticVars.LEVEL_WIDTH + baseShape.getRegionWidth()) {
			body.setTransform(-baseShape.getRegionWidth() / StaticVars.PIXELS_PER_METER, body.getPosition().y, 0);
		}
	}

	

}
