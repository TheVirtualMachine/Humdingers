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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * @author Vincent Macri
 */
public class CreditsScreen implements Screen {

	/** The Platformer instance to be used. */
	private final Platformer PLATFORMER;
	/** The Stage to hold the contents of the credits. */
	private final Stage credits;
	/** The Table to hold the credits scene. */
	private Table table;

	/** The header that says who coded the game. */
	private Label codeHeader;
	/** The header that says who did the art for the game. */
	private Label artHeader;
	/** The header that says who tested the game. */
	private Label testHeader;
	/** The header that says who made the music for the game. */
	private Label musicHeader;
	/** The header for the special thanks section. */
	private Label thanksHeader;
	/** The Label that says who coded the game. */
	private Label coder;
	/** The Label that says who did the art for the game. */
	private Label artist;
	/** The Label that says who tested the game. */
	private Label tester;
	/** The Label that says who did the music for the game. */
	private Label musician;
	/** The Labels that says who we want to give a special thanks to. */
	private Label[] thanks;

	/** The company logo. */
	private Image logo;

	/** If we are currently showing the text. This becomes false when we show the company logo. */
	private boolean showText;


	/**
	 * The constructor for the credits screen.
	 * @param PLATFORMER The Platformer instance to be used.
	 */
	public CreditsScreen(final Platformer PLATFORMER) {
		this.PLATFORMER = PLATFORMER;
		credits = new Stage(new ScreenViewport(), this.PLATFORMER.batch);

		table = new Table();

		table.top();
		table.setFillParent(true);

		credits.addActor(table);
		
		// Get the skin.
		Skin skin = this.PLATFORMER.getSkin();
		
		LabelStyle header = skin.get("bold-big", LabelStyle.class);
		LabelStyle contents = skin.get("normal-regular", LabelStyle.class);

		codeHeader = new Label("Coding", header);

		artHeader = new Label("Art & Design", header);

		testHeader = new Label("Story & Testing", header);

		musicHeader = new Label("Music", header);

		thanksHeader = new Label("Special Thanks", header);

		coder = new Label("Vincent Macri", contents);

		artist = new Label("Kayton Yan", contents);

		tester = new Label("Alex Chen", contents);

		musician = new Label("\"Adventure Meme\", \"Blip Stream\", \"Overworld\"\n"
				+ "Kevin MacLeod (incompetech.com)\n"
				+ "Licensed under Creative Commons: By Attribution 3.0\n"
				+ "http://creativecommons.org/licenses/by/3.0/", contents);

		thanks = new Label[] {
				new Label ("The libGDX Team", contents),
				new Label ("Mrs. Krasteva", contents),
				new Label ("Ms. Dyke", contents)
		};

		logo = new Image(this.PLATFORMER.assets.get("assets/Other/Logo.png", Texture.class));
	}

	/**
	 * Layout the credits screen.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	private void layoutCredits(int width, int height) {
		table.clearChildren(); // Clear the table.

		if (showText) {
			table.add(codeHeader).padBottom(3);
			table.row();
			table.add(coder).padBottom(15);
			table.row();

			table.add(artHeader).padBottom(3);
			table.row();
			table.add(artist).padBottom(15);
			table.row();

			table.add(testHeader).padBottom(3);
			table.row();
			table.add(tester).padBottom(15);
			table.row();

			table.add(musicHeader).padBottom(3);
			table.row();
			table.add(musician).padBottom(15);
			table.row();

			table.add(thanksHeader).padBottom(3);

			for (int i = 0; i < thanks.length; i++) {
				table.row();
				table.add(thanks[i]);
			}
		} else {
			table.center();
			table.add(logo);
		}
	}

	@Override
	public void show() {
		PLATFORMER.music.playSong(MusicManager.Song.CREDITS_SONG);
		table.setDebug(PLATFORMER.settings.isDebug()); // Set debug lines for tables.
		showText = true;
		table.clear();
		table.setPosition(0, -Gdx.graphics.getHeight());
		table.addAction(Actions.moveTo(0, Gdx.graphics.getHeight(), 30f));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (showText) {
			if (!table.hasActions()) {
				showText = false;
				table.setPosition(0, 0); // Adjust the table position.
				layoutCredits(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				logo.setColor(logo.getColor().r, logo.getColor().g, logo.getColor().b, 0);
				logo.addAction(Actions.sequence(Actions.fadeIn(3f), Actions.delay(3f), Actions.fadeOut(3f), Actions.delay(1f)));
			}
		} else if (!logo.hasActions()) {
			PLATFORMER.setScreen(PLATFORMER.menuScreen); // Switch to the menu screen.
		}

		credits.act(Gdx.graphics.getDeltaTime());
		credits.draw();
	}

	@Override
	public void resize(int width, int height) {
		credits.getViewport().update(width, height, true);
		layoutCredits(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}
