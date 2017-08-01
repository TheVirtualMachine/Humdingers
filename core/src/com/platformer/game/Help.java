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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * This method contains static helper methods.
 * @author Vincent Macri
 */
public class Help {
	
	/**
	 * Calculate the distance between two points using Pythagoras' theorem.
	 * @param point1 The first point.
	 * @param point2 The second point.
	 * @return The distance between point1 and point2.
	 */
	public static float calcDistance(Vector2 point1, Vector2 point2){
		double deltaX = point1.x - point2.x;
		double deltaY = point1.y - point2.y;
		return (float) Math.hypot(deltaX, deltaY);
	}

	/**
	 * Calculate the x distance between two points.
	 * @param point1 The first point.
	 * @param point2 The second point.
	 * @return The x distance between point1 and point2.
	 */
	public static float calcXDistance(Vector2 point1, Vector2 point2){
		return Math.abs(point1.x - point2.x);
	}

	/**
	 * Set the x velocity of a body without modifying the y velocity.
	 * @param body The body to modify the x velocity of.
	 * @param newXVelocity The new x velocity of the body.
	 */
	public static void setBodyXVelocity(Body body, float newXVelocity){
		body.setLinearVelocity(newXVelocity, body.getLinearVelocity().y);
	}

	/**
	 * Set the y velocity of a body without modifying the x velocity.
	 * @param body The body to modify the y velocity of.
	 * @param newYVelocity The new y velocity of the body.
	 */
	public static void setBodyYVelocity(Body body, float newYVelocity){
		body.setLinearVelocity(body.getLinearVelocity().x, newYVelocity);
	}

	/**
	 * Calculate what frame to show.
	 * @param timePassed How long this animation has been running.
	 * @param maxFrames How many frames there are.
	 * @param timePerFrame How long each frame lasts.
	 * @return The frame to show.
	 */
	public static int calcFrameNumber (float timePassed, int maxFrames, float timePerFrame){
		int framesRun = (int) (timePassed / timePerFrame);
		return framesRun % maxFrames;
	}
	
	/**
	 * Check if an animation has passed a specific frame. Thhis method assumes that the animation does not loop.
	 * @param timePassed How much time has passed in the animation.
	 * @param timePerFrame How long each frame lasts.
	 * @param targetFrame Check if if this frame has been passed. The first frame is frame 0.
	 * @return If targetFrame has been passed.
	 */
	public static boolean hasFramePassed (float timePassed, float timePerFrame, int targetFrame) {
		int framesRun = (int) (timePassed / timePerFrame);
		return framesRun > targetFrame;
	}
		
}
