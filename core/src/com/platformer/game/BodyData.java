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

/**
 * This class is the base class of everything in the game that has a Box2D body.
 * @author Vincent Macri
 */
public class BodyData {
	
	/** The BodyDataType of this body. This is used for comparisons in the ContactListener. */
	BodyDataType bdType;
	
	/**
	 * The enum for comparing body data types.
	 * @author Vincent Macri
	 */
	public enum BodyDataType {
		/** A player. */
		PLAYER,
		/** A knight. */
		KNIGHT,
		/** The walls. */
		WALL,
		/** The ground. */
		GROUND,
		/** The platforms. */
		PLATFORM,
		/** Bullets. */
		BULLET,
		/** Clouds. */
		CLOUD
	}
	
	public BodyData(BodyDataType newBDType){
		bdType = newBDType;
	}
	
}
