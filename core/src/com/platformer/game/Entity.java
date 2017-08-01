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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * An abstract class that all entities inherit from. Entities are anything that moves and can be interacted with.
 * @author Vincent Macri
 */
public abstract class Entity extends BodyData{

	/** The speed at which this entity can move left and right. */
	float speed;

	/** If the entity is active. */
	boolean active;

	/** The base TextureRegion of this Entity. */
	TextureRegion baseShape;

	/** Box2D physics body for this Entity. */
	Body body;

	/**
	 * The Entity constructor sets up all of the necessary properties of an Entity, such as its Box2D body. It provides control over the physics properties.
	 * @param loc The entity's location.
	 * @param newMaxHealth The entity's maximum health.
	 * @param newBaseShape The base shape for collisions.
	 * @param newBDType The BodyDataType of the entity. This is used so casting can be used in the Box2D CollisionListener.
	 * @param bodyType The Box2D BodyType of the Entity.
	 */
	public Entity(Vector2 loc, TextureRegion newBaseShape, BodyDataType newBDType, float newDensity, short newCategoryBits, short newMaskBits, BodyType bodyType){

		super(newBDType);
		baseShape = newBaseShape;

		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// Set the body type.
		bodyDef.type = bodyType;

		// Set our body's starting position in the world
		bodyDef.position.set((loc.x + baseShape.getRegionWidth() / 2) / StaticVars.PIXELS_PER_METER, (loc.y + baseShape.getRegionHeight() / 2) / StaticVars.PIXELS_PER_METER);

		// Create our body in the world using our body definition
		body = StaticVars.world.createBody(bodyDef);

		// Now define the dimensions of the physics shape
		PolygonShape shape = new PolygonShape();

		// Set the physics polygon to a box with the same dimensions as sprite.
		shape.setAsBox(baseShape.getRegionWidth() / 2 / StaticVars.PIXELS_PER_METER, baseShape.getRegionHeight() / 2  / StaticVars.PIXELS_PER_METER);


		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();

		fixtureDef.shape = shape;
		fixtureDef.density = newDensity;
		fixtureDef.friction = 0f;
		fixtureDef.filter.categoryBits = newCategoryBits; // What I am.
		fixtureDef.filter.maskBits = newMaskBits; // What I collide with.

		// Create fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData(new Vector2(baseShape.getRegionWidth(), baseShape.getRegionHeight())); //Set fixture UserData to width and height.
		body.setFixedRotation(true);

		// Don't memory leak!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		shape.dispose();

		active = true;
	}

	/**
	 * Draw the entity.
	 * @param batch The SpriteBatch to draw with.
	 */
	public void draw(SpriteBatch batch) {
		batch.draw(baseShape, body.getPosition().x * StaticVars.PIXELS_PER_METER - baseShape.getRegionWidth() / 2, body.getPosition().y * StaticVars.PIXELS_PER_METER - baseShape.getRegionHeight() / 2);
	} 

	public abstract void update();

}
