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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * An extension of the Entity class. This subclass implements features such as health and PGSIs.
 * @author Vincent Macri
 */
public abstract class GameEntity extends Entity {

	/** How much health the entity currently has. */
	float health;
	/** The entity's health regeneration rate. Defaults to 0. */
	float healRate = 0;
	/** Maximum health. */
	final int MAX_HEALTH;
	/** Jump power. */
	final float jumpPower = 20;
	/** How far away the entity needs to be to hit something. */
	float attackRange;
	/** How much damage the entity does. */
	float attackDamage;
	/** PGSI that will be used this frame. */
	PGSI currentPGSI;

	/**
	 * Create a new character.
	 * @param loc
	 * @param newMaxHealth
	 * @param newBaseShape
	 * @param newBDType
	 * @param newDensity
	 * @param newCategoryBits
	 * @param newMaskBits
	 */
	public GameEntity(Vector2 loc, int newMaxHealth, TextureRegion newBaseShape, BodyDataType newBDType,
			float newDensity, short newCategoryBits, short newMaskBits) {
		super(loc, newBaseShape, newBDType, newDensity, newCategoryBits, newMaskBits, BodyType.DynamicBody);

		MAX_HEALTH = newMaxHealth;
		health = MAX_HEALTH;
		getFrame();

	}

	/* (non-Javadoc)
	 * @see com.platformer.game.Entity#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void draw(SpriteBatch batch) {
	}

	/* (non-Javadoc)
	 * @see com.platformer.game.Entity#update()
	 */
	@Override
	public void update() {

	}


	/**
	 * Find out which frame to use.
	 */
	abstract void getFrame();

	/**
	 * Set up, such as frame stuff.
	 */
	public void initFrames(){
		getFrame();
	}

	public void takeDamage(float damage){
		if(health - damage > 0){ //If the health won't go below 0.
			health -= damage;
		}else{
			health = 0;
			kill();
		}
	}

	public void healDamage(float heal){
		if(health + heal <= MAX_HEALTH){ //If the health won't exceed max health.
			health += heal;
		}else{
			health = MAX_HEALTH;
		}
	}

	/**
	 * Everything that needs to happen when the entity dies.
	 */
	public void kill() {
		active = false;
	}

}
