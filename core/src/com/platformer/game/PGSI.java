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
import com.badlogic.gdx.physics.box2d.Body;

/**
 * PGSI is short for Platformer Game Sprite Information.
 * The PGSI class handles split sprites.
 * @author Vincent Macri
 */
public class PGSI {

	/** The centre of the PGSI. */
	TextureRegion centre;
	/** The left of the PGSI. */
	TextureRegion left;
	/** The right of the PGSI. */
	TextureRegion right;
	/** The top of the PGSI. */
	TextureRegion top;

	/**
	 * The PGSI constructor with no top.
	 * @param newLeft The TextureRegion at the left of the PGSI.
	 * @param newCentre The TextureRegion at the centre of the PGSI.
	 * @param newRight The TextureRegion at the right of the PGSI.
	 */
	public PGSI(TextureRegion newLeft, TextureRegion newCentre, TextureRegion newRight) {
		left = newLeft;
		centre = newCentre;
		right = newRight;
	}

	/**
	 * The PGSI constructor with a top.
	 * @param newLeft The TextureRegion at the left of the PGSI.
	 * @param newCentre The TextureRegion at the centre of the PGSI.
	 * @param newRight The TextureRegion at the right of the PGSI.
	 * @param newTop The TextureRegion at the top of the PGSI.
	 */
	public PGSI(TextureRegion newLeft, TextureRegion newCentre, TextureRegion newRight, TextureRegion newTop) {
		left = newLeft;
		centre = newCentre;
		right = newRight;
		top = newTop;
	}

	/**
	 * Draw the PGSI.
	 * @param batch The SpriteBatch to draw with.
	 * @param body Where the sprite should be drawn.
	 */
	void draw(SpriteBatch batch, Body body) {

		Vector2 dim = (Vector2)(body.getFixtureList().first().getUserData()); // Dimensions of first fixture.

		float centrePosX = body.getPosition().x * StaticVars.PIXELS_PER_METER - dim.x / 2f;
		float centrePosY = body.getPosition().y * StaticVars.PIXELS_PER_METER - dim.y / 2f;

		float leftPosX = centrePosX - left.getRegionWidth();

		int drawCentrePosX, drawCentrePosY, drawLeftPosX, drawRightPosX;

		drawLeftPosX = (int) Math.ceil(leftPosX);
		drawCentrePosX = drawLeftPosX + left.getRegionWidth();
		drawCentrePosY = (int) centrePosY;
		drawRightPosX = drawCentrePosX + centre.getRegionWidth();


		batch.draw(centre, drawCentrePosX, drawCentrePosY);
		batch.draw(left, drawLeftPosX, drawCentrePosY);
		batch.draw(right, drawRightPosX, drawCentrePosY);

		if (top != null){
			float topPosY = (centrePosY + dim.y);
			int drawTopPosY = (int) Math.floor(topPosY);
			batch.draw(top, drawCentrePosX, drawTopPosY);
		}

	}

}
