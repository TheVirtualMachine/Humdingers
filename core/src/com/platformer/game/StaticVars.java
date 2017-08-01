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

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/** 
 * This class is for all of the static variables that need to be easily accessed.
 * @author Vincent Macri
 */
public class StaticVars {
	
	/** The attack type currently selected by the player. */
	public enum AttackType {
		/** Melee attacks. */
		MELEE,
		
		/** Magic attacks. */
		MAGIC
	}
	
	/** The number of pixels per meter. This is used for Box2D, which operates in meters. */
	public static final float PIXELS_PER_METER = 50f;
	
	/** The gravity rate. */
	public static final float GRAVITY_RATE = -PIXELS_PER_METER;

	/** The Box2D world. */
	protected static World world;
	
	/** The player. */
	public static Player player;

	/** The current level. */
	public static Level currentLevel;
	
	/** The collision bitmask for world objects. It has the value -1, so it collides with everything. */
	public static final short WORLD_MASK = -1;
	
	/** The collision bitmask for clouds. It has the value 0, so it will not collide with anything. */
	public static final short CLOUD_MASK = 0;
	
	/** The collision bitmask for enemies. */
	public static final short ENEMY_MASK = 0x1;
	
	/** The collision bitmask for the player. */
	public static final short PLAYER_MASK = 0x2;
		
	/** The collision bitmask for platforms. */
	public static final short PLATFORM_MASK = 0x4;
	
	/** The collision bitmask for bullets. */
	public static final short BULLET_MASK = 0x8;

	/** A value very close to 0 to be used in velocity checking so that we can deal with floating point errors. */
	public static final float BASICALLY_ZERO = 0.0000001f;

	/** An unordered list to store all of the enemies. */
	public static Array<BaseEnemy> enemyList = new Array<BaseEnemy>(false, 16);
	
	/** An unordered list to store all of the bullets. */
	public static Array<BaseBullet> bulletList = new Array<BaseBullet>(false, 16);
	
	/** An unordered list to store all of the clouds. */
	public static Array<Cloud> cloudList = new Array<Cloud>(false, 16);
	
	/** The current type of attack selected. Defaults to melee. */
	public static AttackType attackType = AttackType.MELEE;
	
	/** The width of the entire level. */
	public static final int LEVEL_WIDTH = 10240;

	/** The filename of the settings file. */
	public static final String SETTINGS_FILE_NAME = "Settings.json";
	
}
