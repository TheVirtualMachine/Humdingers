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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.platformer.game.BodyData.BodyDataType;
/**
 * This class handles things that need to happen when a collision occurs, such as making enemies jump when they hit a wall.
 * @author Vincent Macri
 */
public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		if (contact.isTouching()){ // If it's a proper collision and not just an AABB collision.
			Fixture fixA = contact.getFixtureA();
			Fixture fixB = contact.getFixtureB();
			Body bodyA = fixA.getBody();
			Body bodyB = fixB.getBody();
			BodyData bodyDataA = (BodyData)bodyA.getUserData();
			BodyData bodyDataB = (BodyData)bodyB.getUserData();

			if (bodyDataA.bdType == BodyDataType.KNIGHT || bodyDataB.bdType == BodyDataType.KNIGHT){ // If a knight is in the collision.
				if (bodyDataA.bdType == BodyDataType.WALL || bodyDataB.bdType == BodyDataType.WALL){ // If a knight is colliding with a wall.
					Knight knight = bodyDataA.bdType == BodyDataType.KNIGHT ? (Knight)bodyDataA : (Knight)bodyDataB;
					Fixture wall = bodyDataA.bdType == BodyDataType.WALL ? fixA : fixB;

					// Temporarily store points in wall.
					Vector2 vec1 = new Vector2();
					Vector2 vec2 = new Vector2();

					EdgeShape edge = (EdgeShape)wall.getShape();

					edge.getVertex1(vec1);
					edge.getVertex2(vec2);

					float yPos = Math.max(vec1.y, vec2.y);

					knight.jump(yPos - (knight.body.getPosition().y - knight.baseShape.getRegionHeight() / 2f / StaticVars.PIXELS_PER_METER));
					knight.thinking = true; // Allow the enemy to think again.

				} else if (bodyDataA.bdType == BodyDataType.BULLET || bodyDataB.bdType == BodyDataType.BULLET) { // If a knight is colliding with a bullet.
					Knight knight = bodyDataA.bdType == BodyDataType.KNIGHT ? (Knight)bodyDataA : (Knight)bodyDataB;
					BaseBullet bullet = bodyDataA.bdType == BodyDataType.BULLET ? (BaseBullet)bodyDataA : (BaseBullet)bodyDataB;

					if (bullet.active) { // If a fireball collides with multiple enemies at once, only damage one. We want knockback to applied to all enemies however.
						knight.takeDamage(bullet.damage);
					}

					bullet.kill();

					knight.body.setLinearVelocity((bullet.body.getLinearVelocity().x * (0.5f + (float) Math.random())), 10 + 20 * (float) Math.random());
					knight.seenPlayer = true;
					knight.thinking = false; // Stop the knight from thinking.
				}
			} else if (bodyDataA.bdType == BodyDataType.BULLET || bodyDataB.bdType == BodyDataType.BULLET){ // If a bullet is in the collision.
				if (bodyDataA.bdType == BodyDataType.WALL || bodyDataB.bdType == BodyDataType.WALL || bodyDataA.bdType == BodyDataType.GROUND || bodyDataB.bdType == BodyDataType.GROUND){ // If a bullet is colliding with a wall or the ground.
					BaseBullet bullet = bodyDataA.bdType == BodyDataType.BULLET ? (BaseBullet)bodyDataA : (BaseBullet)bodyDataB;
					bullet.kill();
				}
			}
		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		if (contact.isTouching()){ // If it's a proper collision and not just an AABB collision.

			Fixture fixA = contact.getFixtureA();
			Fixture fixB = contact.getFixtureB();
			Body bodyA = fixA.getBody();
			Body bodyB = fixB.getBody();
			BodyData bodyDataA = (BodyData)bodyA.getUserData();
			BodyData bodyDataB = (BodyData)bodyB.getUserData();

			if (bodyDataA.bdType == BodyDataType.PLAYER || bodyDataB.bdType == BodyDataType.PLAYER){ // If a player is in the collision.
				if (bodyDataA.bdType == BodyDataType.PLATFORM || bodyDataB.bdType == BodyDataType.PLATFORM){ // If a player is colliding with a platform.
					Player player = bodyDataA.bdType == BodyDataType.BULLET ? (Player)bodyDataA : (Player)bodyDataB;
					if (player.body.getLinearVelocity().y > StaticVars.BASICALLY_ZERO) {
						contact.setEnabled(false);
					}
				}
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
