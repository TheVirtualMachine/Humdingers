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

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.platformer.game.BodyData.BodyDataType;

/**
 * This class creates levels by reading the data from a file.
 * @author Vincent Macri
 */
public class Level {

	/** The width of the level. */
	private final int LEVEL_WIDTH;

	/** The background textures. */
	Texture[] background;

	/** This body holds all of the vertical ground fixtures. */
	Body walls;

	/** This body holds all of the horizontal ground fixtures. */
	Body ground;

	/** This body holds all of the floating platforms. */
	Body platforms;

	/**
	 * @author Vincent Macri
	 * @param backgroundImages The textures of the background.
	 * @param fileName The file name to get position data from.
	 */
	public Level(Texture[] backgroundImages, String fileName){
		background = backgroundImages;

		setupBodies();
		readLevel(fileName);
		for (int i = 0; i < background.length; i++) {
			background[i].setFilter(TextureFilter.Linear, TextureFilter.Linear); // Prevent background tearing.			
		}
		
		int width = 0;
		for (int i = 0; i < background.length; i++) {
			width += background[i].getWidth();
		}
		LEVEL_WIDTH = width;
	}

	/**
	 * Setup all of the level's bodies.
	 */
	private void setupBodies() {
		BodyDef groundDef = new BodyDef(); // Create a body definition.
		groundDef.type = BodyType.StaticBody; // Set its type to static.
		groundDef.position.set(0, 0);

		walls = StaticVars.world.createBody(groundDef);
		walls.setUserData(new BodyData(BodyDataType.WALL));

		ground = StaticVars.world.createBody(groundDef);
		ground.setUserData(new BodyData(BodyDataType.GROUND));

		platforms = StaticVars.world.createBody(groundDef);
		platforms.setUserData(new BodyData(BodyDataType.PLATFORM));
	}

	private void createHorizontalBody(int xStart, int xEnd, int yPos){

		EdgeShape edge = new EdgeShape(); // Create an edge shape.
		// Set its shape.
		edge.set(xStart / StaticVars.PIXELS_PER_METER, yPos / StaticVars.PIXELS_PER_METER,
				xEnd / StaticVars.PIXELS_PER_METER, yPos / StaticVars.PIXELS_PER_METER);

		// Create a fixture for the walls body.
		ground.createFixture(createLevelFixture(edge));

		// Dispose of the edge so there is no memory leak.
		edge.dispose();
	}

	/**
	 * Create a new vertical edge shape for the walls. This method creates a line segment AB A(xPos, yPos), B(xPos, lastYPos).
	 * Neither yPos or lastYPos has to be less than, greater than, or equal to the other.
	 * All that happens is a line segment is created from A to B.
	 * The line segment is an edge shape that is attached to a static Box2D body.
	 * @param xPos The x position of this wall.
	 * @param yPos The first y position of the wall.
	 * @param lastYPos The second y position of the wall.
	 */
	private void createVerticalLine(int xPos, int yPos, int lastYPos) {
		EdgeShape edge = new EdgeShape(); // Create an edge shape.
		// Set its shape.
		edge.set(xPos / StaticVars.PIXELS_PER_METER, yPos / StaticVars.PIXELS_PER_METER,
				xPos / StaticVars.PIXELS_PER_METER, lastYPos / StaticVars.PIXELS_PER_METER);

		// Create a fixture for the walls body.
		walls.createFixture(createLevelFixture(edge));

		// Dispose of the edge so there is no memory leak.
		edge.dispose();
	}

	/** 
	 * Create a new platform from (xStart, yPos) to (xEnd, yPos).
	 * @param xStart The starting x of this platform.
	 * @param xEnd The ending x of this platform.
	 * @param yPos The y position of this platform.
	 */
	private void createPlatform(int xStart, int xEnd, int yPos) {
		EdgeShape edge = new EdgeShape(); // Create an edge shape.
		// Set its shape.
		edge.set(xStart / StaticVars.PIXELS_PER_METER, yPos / StaticVars.PIXELS_PER_METER,
				xEnd / StaticVars.PIXELS_PER_METER, yPos / StaticVars.PIXELS_PER_METER);

		// Create a fixture for the walls body.
		FixtureDef fixture = createLevelFixture(edge);
		fixture.filter.categoryBits = StaticVars.PLATFORM_MASK; // I am a platform.
		platforms.createFixture(fixture);

		// Dispose of the edge so there is no memory leak.
		edge.dispose();
	}

	/**
	 * Create a Fixture from the given EdgeShape. Sets friction and density. Sets bitmask to WORLD_MASK.
	 * @param edge The EdgeShape to create the fixture from.
	 * @return The fixture.
	 */
	private FixtureDef createLevelFixture(EdgeShape edge){
		FixtureDef fixture = new FixtureDef();
		fixture.friction = 0;
		fixture.density = 0;
		fixture.shape = edge;
		fixture.filter.categoryBits = StaticVars.WORLD_MASK; // I am a world entity.
		fixture.filter.maskBits = StaticVars.ENEMY_MASK | StaticVars.PLAYER_MASK | StaticVars.BULLET_MASK; // I collide with enemies, players, or bullets.
		return fixture;
	}

	private void createGroundBody(int xStart, int xEnd, int yPos, int lastYPos) {

		createHorizontalBody(xStart, xEnd, yPos);
		createVerticalLine(xStart, yPos, lastYPos);

	}

	/** Read from the input file to figure out where the platforms are. */
	void readLevel (String fileName){
		Scanner s = null;
		String inputType;
		int xStart, xEnd, yPos, lastYPos;

		try {
			s = new Scanner(Gdx.files.internal(fileName).read());
		} catch (GdxRuntimeException e) {
			e.printStackTrace();
			s.close();
			return;
		}catch(NullPointerException f){
			f.printStackTrace();
			s.close();
			return;
		} catch (Exception t){
			t.printStackTrace();
			s.close();
			return;
		}

		inputType = s.next();

		if (inputType.equals("[SOG]")){

			xStart = 0;
			xEnd = 0;
			yPos = 0;
			lastYPos = 0;

			while (s.hasNextInt()){

				try{
					xStart = s.nextInt();
					xEnd = s.nextInt();
					yPos = s.nextInt();

					createGroundBody(xStart, xEnd, yPos, lastYPos);

					lastYPos = yPos;

				}catch(InputMismatchException e){
					System.out.println("Error occured while reading ground input.");
					System.out.println("InputMismatchException - the next token does not match the Integer regular expression, or is out of range.");
					e.printStackTrace();
					s.close();
					return;
				}catch(NoSuchElementException f){
					System.out.println("Error occured while reading ground input.");
					System.out.println("NoSuchElementException - input is exhausted.");
					f.printStackTrace();
					s.close();
					return;
				}catch(IllegalStateException g){
					System.out.println("Error occured while reading ground input.");
					System.out.println("IllegalStateException - this scanner is closed.");
					g.printStackTrace();
					s.close();
					return;
				}catch(Exception h){
					System.out.println("Error occured while reading ground input.");
					h.printStackTrace();
					s.close();
					return;
				}

			}

			inputType = s.next();
			if (!inputType.equals("[EOG]")){
				System.out.println("Bad PGLI format. " + inputType + " is not [EOG].");
				s.close();
				return;
			}

		}else{
			System.out.println("Bad PGLI format. Improper [SOG].");
			s.close();
			return;
		}

		if (!s.hasNext()){
			s.close();
			return;
		}

		inputType = s.next();
		if (!inputType.equals("[SOP]")){
			System.out.println("Bad PGLI format. Improper [SOP].");
			s.close();
			return;
		}

		while (s.hasNextInt()){
			try{
				xStart = s.nextInt();
				xEnd = s.nextInt();
				yPos = s.nextInt();

				createPlatform(xStart, xEnd, yPos);

			}catch(InputMismatchException e){
				System.out.println("Error occured while reading air input.");
				System.out.println("InputMismatchException - the next token does not match the Integer regular expression, or is out of range.");
				e.printStackTrace();
				s.close();
				return;
			}catch(NoSuchElementException f){
				System.out.println("Error occured while reading air input.");
				System.out.println("NoSuchElementException - input is exhausted.");
				f.printStackTrace();
				s.close();
				return;
			}catch(IllegalStateException g){
				System.out.println("Error occured while reading air input.");
				System.out.println("IllegalStateException - this scanner is closed.");
				g.printStackTrace();
				s.close();
				return;
			}catch(Exception h){
				System.out.println("Error occured while reading air input.");
				h.printStackTrace();
				s.close();
				return;
			}
		}

		inputType = s.next();
		if (!inputType.equals("[EOP]")){
			System.out.println("Bad PGLI format. " + inputType + " is not [EOP].");
			s.close();
			return;
		}

		inputType = s.next();

		if (!inputType.equals("[SOK]")){
			System.out.println("Bad PGLI format. " + inputType + " is not [SOK].");
			s.close();
			return;
		}

		while (s.hasNextInt()){
			try{
				xStart = s.nextInt();

				StaticVars.enemyList.add(new Knight(new Vector2(xStart, 1024)));

			}catch(InputMismatchException e){
				System.out.println("Error occured while reading enemy input.");
				System.out.println("InputMismatchException - the next token does not match the Integer regular expression, or is out of range.");
				e.printStackTrace();
				s.close();
				return;
			}catch(NoSuchElementException f){
				System.out.println("Error occured while reading enemy input.");
				System.out.println("NoSuchElementException - input is exhausted.");
				f.printStackTrace();
				s.close();
				return;
			}catch(IllegalStateException g){
				System.out.println("Error occured while reading enemy input.");
				System.out.println("IllegalStateException - this scanner is closed.");
				g.printStackTrace();
				s.close();
				return;
			}catch(Exception h){
				System.out.println("Error occured while reading enemy input.");
				h.printStackTrace();
				s.close();
				return;
			}
		}

		s.close();

	}

	/**
	 * Draw the level.
	 * @param batch The SpriteBatch to draw with.
	 */
	public void draw(SpriteBatch batch) {
		int xPos = 0;
		for (int i = 0; i < background.length; i++) {
			batch.draw(background[i], xPos, 0);
			xPos += background[i].getWidth();
		}
	}

	/**
	 * Get the total width of the level.
	 * @return
	 */
	public int getWidth() {
		return LEVEL_WIDTH;
	}

}
