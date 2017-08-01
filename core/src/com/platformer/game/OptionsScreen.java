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
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * The OptionsScreen allows the player to set the game settings.
 * @author Vincent Macri
 */
public class OptionsScreen implements Screen, InputProcessor {

	/** The Platformer instance to be used. */
	private final Platformer PLATFORMER;
	/** The Stage to hold the contents of the credits. */
	private final Stage options;
	/** The layout Table to hold all of the widgets for all of the options. */
	private Table table;

	/** The Label that says what screen we're on. */
	private Label screenHeader;
	/** The Label for the music volume slider. */
	private Label musicSliderLabel;
	/** The Label for the controls table. */
	private Label controlsHeader;

	/** The Slider to control the volume of the music. */
	private Slider musicSlider;

	/** A button to send the user back to the main menu. */
	private TextButton backToMenuButton;

	/** A check box to let the user set fullscreen. */
	private CheckBox fullscreenCheckBox;	
	/** A check box to let the user set debug mode. */
	private CheckBox debugCheckBox;

	/** A Table to hold the settings for the game controls. */
	private Table controlsTable;

	/** Array of Labels to store all of the possible game actions. */
	private Label[] actions;

	/** Array of TextButtons to store the current control mappings and allow them to be changed. */
	private TextButton[] actionControls;

	/** TextButton to reset all of the options. */
	private TextButton resetOptionsButton;

	/** The action currently being remapped. Set to -1 when no action is being remapped. */
	private int actionBeingSet = -1;

	/**
	 * Allows chaining of InputProcessors.
	 * This allows the UI to process events first, and if the UI doesn't process an event, the regular InputProcessor will take over.
	 */
	private InputMultiplexer multiplexer;

	/**
	 * Create a new OptionsScreen.
	 * @param PLATFORMER The Platformer instance to be used.
	 */
	public OptionsScreen(final Platformer PLATFORMER) {
		this.PLATFORMER = PLATFORMER;
		options = new Stage(new ScreenViewport(), this.PLATFORMER.batch);

		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(options);
		multiplexer.addProcessor(this);


		table = new Table();
		table.top();
		table.setFillParent(true);
		controlsTable = new Table(); // Create the controls Table.

		options.addActor(table);

		// The styles for the UI.

		// Get the skin.
		Skin skin = this.PLATFORMER.getSkin();

		// The Label styles.
		LabelStyle header = skin.get("bold-big", LabelStyle.class);
		LabelStyle contents = skin.get("normal-regular", LabelStyle.class);

		// The Slider style.
		SliderStyle sliderStyle = new SliderStyle(skin.get(SliderStyle.class));

		CheckBoxStyle checkBoxStyle = new CheckBoxStyle(skin.get(CheckBoxStyle.class));

		// Create the widgets.
		screenHeader = new Label("Options", header);


		// Setup the music volume slider.
		musicSlider = new Slider(0, 1, 0.1f, false, sliderStyle);
		musicSlider.setValue(this.PLATFORMER.settings.getMusicLevel());
		musicSlider.setAnimateDuration(0.25f);

		musicSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				OptionsScreen.this.PLATFORMER.music.setVolume(musicSlider.getValue());
			}
		});
		musicSliderLabel = new Label ("Music Volume", contents);

		fullscreenCheckBox = new CheckBox("Fullscreen", checkBoxStyle);
		fullscreenCheckBox.setChecked(Gdx.graphics.isFullscreen());
		fullscreenCheckBox.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(fullscreenCheckBox.isChecked()){
					Monitor currentMonitor = Gdx.graphics.getMonitor();
					DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode(currentMonitor);
					if (!Gdx.graphics.setFullscreenMode(currentDisplayMode)){ // Change modes, and check if it worked.
						System.out.println("Failed to change to fullscreen mode."); // Print error message if it didn't work.
					} else {
						PLATFORMER.settings.setFullscreen(true); // If it worked, update the fullscreen option.
					}
				}else{
					if(!Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height)){ // Change modes, and check if it worked.
						System.out.println("Failed to change to windowed mode."); // Print error message if it didn't work.
					} else {
						PLATFORMER.settings.setFullscreen(false); // If it worked, update the fullscreen option.
					}
				}
			}
		});


		debugCheckBox = new CheckBox("Debug mode", checkBoxStyle);
		debugCheckBox.setChecked(PLATFORMER.settings.isDebug());
		debugCheckBox.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				PLATFORMER.settings.setDebug(debugCheckBox.isChecked());
				table.setDebug(PLATFORMER.settings.isDebug());
				controlsTable.setDebug(PLATFORMER.settings.isDebug());
			}
		});



		// Create the controls settings.
		controlsHeader = new Label("Controls", header); // Create the controls header.

		actions = new Label[]{
				new Label ("Jump", contents),
				new Label ("Move right", contents),
				new Label ("Move left", contents),
				new Label ("Switch to magic", contents),
				new Label ("Switch to melee", contents),
				new Label ("Attack forwards", contents),
				new Label ("Attack right", contents),
				new Label ("Attack left", contents),	
		};

		actionControls = new TextButton[]{
				new TextButton(Keys.toString(PLATFORMER.settings.getJumpButton()), skin),
				new TextButton(Keys.toString(PLATFORMER.settings.getMoveRightButton()), skin),
				new TextButton(Keys.toString(PLATFORMER.settings.getMoveLeftButton()), skin),
				new TextButton(Keys.toString(PLATFORMER.settings.getMagicButton()), skin),
				new TextButton(Keys.toString(PLATFORMER.settings.getMeleeButton()), skin),
				new TextButton(Keys.toString(PLATFORMER.settings.getAttackButton()), skin),
				new TextButton(Keys.toString(PLATFORMER.settings.getAttackRightButton()), skin),
				new TextButton(Keys.toString(PLATFORMER.settings.getAttackLeftButton()), skin),
		};

		actionControls[0].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actionControls[0].isPressed()) {
					updateControls(0);
				}
			}

		});
		
		actionControls[1].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actionControls[1].isPressed()) {
					updateControls(1);
				}
			}

		});

		actionControls[2].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actionControls[2].isPressed()) {
					updateControls(2);
				}
			}

		});

		actionControls[3].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actionControls[3].isPressed()) {
					updateControls(3);
				}
			}

		});

		actionControls[4].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actionControls[4].isPressed()) {
					updateControls(4);
				}
			}

		});

		actionControls[5].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actionControls[5].isPressed()) {
					updateControls(5);
				}
			}

		});

		actionControls[6].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actionControls[6].isPressed()) {
					updateControls(6);
				}
			}

		});

		actionControls[7].addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actionControls[7].isPressed()) {
					updateControls(7);
				}
			}

		});

		// Create the reset settings button.
		resetOptionsButton = new TextButton("Reset Options", skin);
		resetOptionsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) { // The changed event for the reset settings button.
				if (resetOptionsButton.isPressed()) {
					PLATFORMER.settings.resetOptions(); // Reset the settings.
					musicSlider.setValue(PLATFORMER.settings.getMusicLevel()); // Update the music slider.
					fullscreenCheckBox.setChecked(PLATFORMER.settings.isFullscreen()); // Update the fullscreenCheckBox;
					debugCheckBox.setChecked(PLATFORMER.settings.isDebug()); // Update the debugCheckBox;
					resetActionControlsLabels();
				}
			}
		});

		// Create the button to send the user back to the main menu.
		backToMenuButton = new TextButton("Main Menu", skin);
		
	}

	/**
	 * Reset the Labels for all of the elements in {@link #actionControls}.
	 */
	protected void resetActionControlsLabels() {
		actionControls[0].setText(Keys.toString(PLATFORMER.settings.getJumpButton()));
		actionControls[1].setText(Keys.toString(PLATFORMER.settings.getMoveRightButton()));
		actionControls[2].setText(Keys.toString(PLATFORMER.settings.getMoveLeftButton()));
		actionControls[3].setText(Keys.toString(PLATFORMER.settings.getMagicButton()));
		actionControls[4].setText(Keys.toString(PLATFORMER.settings.getMeleeButton()));
		actionControls[5].setText(Keys.toString(PLATFORMER.settings.getAttackButton()));
		actionControls[6].setText(Keys.toString(PLATFORMER.settings.getAttackRightButton()));
		actionControls[7].setText(Keys.toString(PLATFORMER.settings.getAttackLeftButton()));
	}

	/**
	 * Update the key associated with an action.
	 * @param The index of the control to update in the {@link #actionControls} array.
	 */
	protected void updateControls(int i) {

		// First, reset all of the buttons. This is done in case the user tries to change a binding, then decides they want to change a different binding instead.
		resetActionControlsLabels();

		actionControls[i].setText("Press a key");
		actionBeingSet = i;
	}

	/**
	 * Layout the options menu.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	private void layoutOptions(int width, int height) {
		table.clear(); // Clear the table.

		table.add(screenHeader).pad(10f);
		table.row();

		table.add(musicSliderLabel);
		table.row();

		table.add(musicSlider).pad(10f);
		table.row();

		table.add(fullscreenCheckBox).pad(10f);
		table.row();

		table.add(debugCheckBox).pad(10f);
		table.row();

		table.add(controlsHeader).pad(10f);
		table.row();

		table.add(controlsTable);

		controlsTable.clear(); // Clear the controls table.
		for (int i = 0; i < actions.length; i++) {
			controlsTable.add(actionControls[i]).pad(5f).fill().width(actionControls[i].getHeight() * 4);
			controlsTable.add(actions[i]).left().pad(5f);

			controlsTable.row();
		}
		table.row();

		table.add(resetOptionsButton).prefSize(width / 4, height / 20).pad(30f);
		table.row();

		table.add(backToMenuButton).expandY().bottom().minSize(width / 4, height / 20).maxSize(width, height / 5).prefSize(width / 3, height / 15).pad(10f);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		Gdx.input.setInputProcessor(multiplexer);
		table.setDebug(PLATFORMER.settings.isDebug()); // Enable debug lines for tables.
		controlsTable.setDebug(PLATFORMER.settings.isDebug());
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		options.act(Gdx.graphics.getDeltaTime());
		options.draw();

		if (backToMenuButton.isPressed()) { // If play is pressed.
			PLATFORMER.settings.writeOptions();
			PLATFORMER.setScreen(PLATFORMER.menuScreen); // Switch to the main menu screen.
		}

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		options.getViewport().update(width, height, true);
		layoutOptions(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		actionBeingSet = -1;
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(int keycode) {
		if (actionBeingSet >= 0 && actionBeingSet < actionControls.length) {

			switch(actionBeingSet) {

			case 0: // Jumping.
				PLATFORMER.settings.setJumpButton(keycode);
				break;

			case 1: // Move right.
				PLATFORMER.settings.setMoveRightButton(keycode);
				break;

			case 2: // Move left.
				PLATFORMER.settings.setMoveLeftButton(keycode);
				break;

			case 3: // Magic.
				PLATFORMER.settings.setMagicButton(keycode);
				break;

			case 4: // Melee.
				PLATFORMER.settings.setMeleeButton(keycode);
				break;

			case 5: // Attack.
				PLATFORMER.settings.setAttackButton(keycode);
				break;

			case 6: // Attack left.
				PLATFORMER.settings.setAttackRightButton(keycode);
				break;

			case 7: // Attack right.
				PLATFORMER.settings.setAttackLeftButton(keycode);
				break;

			default:
				break;

			}
			actionControls[actionBeingSet].setText(Keys.toString(keycode));
			actionBeingSet = -1;
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
