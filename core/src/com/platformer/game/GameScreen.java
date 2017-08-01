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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.platformer.game.StaticVars.AttackType;

public class GameScreen implements Screen, InputProcessor {

	/** How much time has passed between the last frame and the current frame, in seconds. */
	public static float deltaTime;

	/** For debugging the Box2D simulation. */
	private Box2DDebugRenderer debugRenderer;

	/** The collision listener. Collision handling is done in the CollisionListener class. */
	private CollisionListener collisionListener;

	/** How long to wait before switching the game window after the game ends. */
	private final float CLOSE_DELAY = 3f;

	/** How much time has passed since the game ended. */
	private float timePassedSinceEnd;

	/** The stage for the UI. */
	private Stage stage;
	/** The table for the UI. */
	private Table table;
	/** The icon that shows the selected attack type. */
	private Image attackIcon;
	/** The world camera. */
	private static OrthographicCamera camera;
	/** The game's viewport. */
	private static ExtendViewport viewport;

	/**
	 * Allows chaining of InputProcessors.
	 * This allows the UI to process events first, and if the UI doesn't process an event, the regular InputProcessor will take over.
	 */
	private InputMultiplexer multiplexer;

	/** The health bar. */
	private BarManager healthBar;

	/** The mana bar. */
	private BarManager manaBar;

	/** The Platformer instance to be used. */
	private final Platformer PLATFORMER;

	/** The label that contains debug information. */
	private Label debugLabel;

	/**
	 * Creates the game.
	 * @param PLATFORMER The Platformer instance to be used.
	 */
	public GameScreen (final Platformer PLATFORMER) {
		this.PLATFORMER = PLATFORMER;

		Box2D.init(); // Load Box2D library. This is also done by creating a new World, but using the init method is good practice.
		StaticVars.world = new World(new Vector2(0, StaticVars.GRAVITY_RATE), true);
		collisionListener = new CollisionListener();

		StaticVars.world.setContactListener(collisionListener);

		if(PLATFORMER.settings.isDebug()){ //If we are in debug mode.
			debugRenderer = new Box2DDebugRenderer();
		}

		camera = new OrthographicCamera();

		if (PLATFORMER.settings.isDebug()){
			camera.setToOrtho(false, 2000, 1024);

		}else{
			camera.setToOrtho(false, StaticVars.LEVEL_WIDTH, 1024);
		}


		viewport = new ExtendViewport(1024, 1024, camera);

		StaticVars.attackType = AttackType.MELEE; // Reset the attack type to melee, in case this isn't the first game, and the previous game ended in magic mode.
		setupStage2DUI();

		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);

		Gdx.input.setInputProcessor(multiplexer);
		
		Texture[] backgroundTextures = new Texture[this.PLATFORMER.NUMBER_OF_BACKGROUND_TEXTURES];
		for (int i = 0; i < this.PLATFORMER.NUMBER_OF_BACKGROUND_TEXTURES; i++) {
			backgroundTextures[i] = PLATFORMER.assets.get("assets/Environment/Level1/Level1_" + i + ".png", Texture.class);
		}
		
		
		StaticVars.currentLevel = new Level(backgroundTextures, "assets/Environment/Level1/Level1.pgli");

		// Create the clouds.
		for (int i = 0; i < 16; i++) {
			StaticVars.cloudList.add(new Cloud(new Vector2((StaticVars.LEVEL_WIDTH / 16) * i, MathUtils.random(500, 1000)), PLATFORMER.assets.get("assets/Environment/Clouds/Clouds.atlas", TextureAtlas.class).findRegion("Cloud"+MathUtils.random(3))));
		}

		StaticVars.player = new Player(new Vector2(120, 500));

	}

	/** Setup the UI for Stage2D. */
	private void setupStage2DUI() {
		stage = new Stage(new ScreenViewport());

		table = new Table();
		table.setFillParent(true);
		table.top();
		stage.addActor(table);

		if (PLATFORMER.settings.isDebug()){
			table.setDebug(true); // Enable debug lines for tables.
		}

		// Add widgets to the table here.

		// Get the skin.
		Skin skin = this.PLATFORMER.getSkin();
		
		Table bars = new Table();
		table.add(bars).top().left();
		// Add the health bar.
		healthBar = new BarManager(skin.getDrawable("BarBack"), this.PLATFORMER.assets.get("assets/UI/Bars/HealthBar.png", Texture.class), 100){
			@Override
			public void act(float delta) {
				setBarValue(StaticVars.player.health); // Make the health bar update its value when act is called.
			}
		};

		bars.add(healthBar).left().pad(5).prefWidth(skin.getDrawable("BarBack").getMinWidth());

		bars.row();

		// Add the mana bar.
		manaBar =  new BarManager(skin.getDrawable("BarBack"), this.PLATFORMER.assets.get("assets/UI/Bars/ManaBar.png", Texture.class), 100){
			@Override
			public void act(float delta) {
				setBarValue(StaticVars.player.mana); // Make the mana bar update its value when act is called.
			}
		};

		bars.add(manaBar).left().padLeft(5).prefWidth(skin.getDrawable("BarBack").getMinWidth());

		// Add the attack icon.
		attackIcon = new Image(skin.getDrawable("Melee"));
		table.add(attackIcon).top().right().pad(5).expandX();

		// The debug label.
		if (PLATFORMER.settings.isDebug()) {
			LabelStyle labelStyle = skin.get("normal-regular", LabelStyle.class);
			debugLabel = new Label("Debug", labelStyle){
				@Override
				public void act(float delta) {
					this.setText("FPS: " + Integer.toString(Gdx.graphics.getFramesPerSecond()) + "\n"
							+ "Position: " + StaticVars.player.body.getPosition().x * StaticVars.PIXELS_PER_METER + ", " + StaticVars.player.body.getPosition().y * StaticVars.PIXELS_PER_METER + "\n"
							+ "Velocity: " + StaticVars.player.body.getLinearVelocity().toString());
					super.act(delta);
				}
			};
			table.row();
			table.add(debugLabel);
		}
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(200/255f, 225/255f, 1, 1); // Old colour was [205, 223, 255].
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		deltaTime = Gdx.graphics.getDeltaTime(); //Get the new delta time.

		//Lerp the camera into position.
		if (StaticVars.player.body.getPosition().x * StaticVars.PIXELS_PER_METER < camera.viewportWidth / 2){ // On left half of screen.
			camera.position.lerp(new Vector3((int)Math.max(StaticVars.player.body.getPosition().x * StaticVars.PIXELS_PER_METER, camera.viewportWidth / 2), viewport.getWorldHeight() / 2, 0), 0.01f);
		}else{ // On right half of screen.
			camera.position.lerp(new Vector3((int)Math.min(StaticVars.player.body.getPosition().x * StaticVars.PIXELS_PER_METER, StaticVars.currentLevel.getWidth() - camera.viewportWidth / 2), viewport.getWorldHeight() / 2, 0), 0.01f);
		}

		camera.position.set((int)Math.round(camera.position.x), (int)camera.position.y, 0); // Round position to a whole number, otherwise graphical glitches can occur.

		camera.update();

		PLATFORMER.batch.setProjectionMatrix(camera.combined);

		PLATFORMER.batch.begin();

		for (int i = 0; i < StaticVars.cloudList.size; i++){
			StaticVars.cloudList.get(i).draw(PLATFORMER.batch);
		}

		StaticVars.currentLevel.draw(PLATFORMER.batch);

		StaticVars.player.draw(PLATFORMER.batch);

		for (int i = 0; i < StaticVars.enemyList.size; i++) {
			StaticVars.enemyList.get(i).draw(PLATFORMER.batch);
		}

		for (int i = 0; i < StaticVars.bulletList.size; i++) {
			StaticVars.bulletList.get(i).draw(PLATFORMER.batch);
		}

		PLATFORMER.batch.end();

		stage.act();
		stage.draw();

		if (PLATFORMER.settings.isDebug()){
			debugRenderer.render(StaticVars.world, camera.combined.cpy().scale(StaticVars.PIXELS_PER_METER, StaticVars.PIXELS_PER_METER, 0));
		}

		if (StaticVars.player.active && StaticVars.enemyList.size > 0){
			update();
		}else{
			timePassedSinceEnd += Gdx.graphics.getDeltaTime();
		}

		if (timePassedSinceEnd >= CLOSE_DELAY){ // If it's time to switch screens.

			if (StaticVars.player.active){ // And the player is still alive.
				PLATFORMER.setScreen(PLATFORMER.winScreen); // Switch to the game screen.
			}else{
				PLATFORMER.setScreen(PLATFORMER.loseScreen); // Switch to the main menu screen.
			}

		}

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void update(){
		StaticVars.world.step(Gdx.graphics.getDeltaTime(), 6, 2);

		StaticVars.player.update(); // Do processing for the player.
		for (int i = 0; i < StaticVars.enemyList.size; i++) { // Do AI for the enemies.
			StaticVars.enemyList.get(i).update();
		}
		for (int i = 0; i < StaticVars.bulletList.size; i++) { // Update the bullets.
			StaticVars.bulletList.get(i).update();
		}
		for (int i = 0; i < StaticVars.cloudList.size; i++) { // Update the clouds.
			StaticVars.cloudList.get(i).update();
		}


		// Update the UI.
		stage.act();

		removeInactive();
	}

	/** Remove inactive stuff. */
	private void removeInactive() {
		for (int i = 0; i < StaticVars.enemyList.size; i++){
			if (!StaticVars.enemyList.get(i).active){
				StaticVars.world.destroyBody(StaticVars.enemyList.get(i).body);
				StaticVars.enemyList.removeIndex(i);
			}
		}

		for (int i = 0; i < StaticVars.bulletList.size; i++){
			if (!StaticVars.bulletList.get(i).active){
				StaticVars.world.destroyBody(StaticVars.bulletList.get(i).body);
				StaticVars.bulletList.removeIndex(i);
			}
		}
	}

	@Override
	public boolean keyDown(int keycode) {

		if (Gdx.input.isKeyJustPressed(PLATFORMER.settings.getMoveRightButton())){
			StaticVars.player.moveRight();
		}

		if (Gdx.input.isKeyJustPressed(PLATFORMER.settings.getMoveLeftButton())){
			StaticVars.player.moveLeft();
		}

		if (Gdx.input.isKeyJustPressed(PLATFORMER.settings.getJumpButton())){
			StaticVars.player.jump();
		}

		// If we want to attack to the left.
		if (Gdx.input.isKeyJustPressed(PLATFORMER.settings.getAttackLeftButton()) // Left attack button.
				|| (Gdx.input.isKeyJustPressed(PLATFORMER.settings.getAttackButton()) && StaticVars.player.getDirection() == 0)){ // Or attack button and facing left.

			if (StaticVars.attackType == AttackType.MAGIC) { // If we are using magic.
				StaticVars.player.startFireLeft(); // Shoot a fireball to the left.
			}else { // Else we must be using melee.
				StaticVars.player.startAttackLeft(); // Do a melee attack to the left.
			}
		}

		// If we want to attack to the right.
		if (Gdx.input.isKeyJustPressed(PLATFORMER.settings.getAttackRightButton()) // Right attack button.
				|| (Gdx.input.isKeyJustPressed(PLATFORMER.settings.getAttackButton()) && StaticVars.player.getDirection() == 1)){ // Or attack button and facing right.
			if (StaticVars.attackType == AttackType.MAGIC) { // If we are using magic.
				StaticVars.player.startFireRight(); // Shoot a fireball to the right.
			}else { // Else we must be using melee.
				StaticVars.player.startAttackRight(); // Do a melee attack to the right.
			}	
		}

		if (Gdx.input.isKeyJustPressed(PLATFORMER.settings.getMeleeButton())) { // If we pressed the switch to melee key.
			attackIcon.setDrawable(this.PLATFORMER.getSkin().getDrawable("Melee")); // Make the UI show that we are using melee.
			StaticVars.attackType = AttackType.MELEE; // Record the attack type in a place where any class can access it.
		}

		if (Gdx.input.isKeyJustPressed(PLATFORMER.settings.getMagicButton())) { // If we pressed the switch to magic key.
			attackIcon.setDrawable(this.PLATFORMER.getSkin().getDrawable("Magic")); // Make the UI show that we are using melee.
			StaticVars.attackType = AttackType.MAGIC; // Record the attack type in a place where any class can access it.
		}

		return true;

	}

	@Override
	public boolean keyUp(int keycode) {

		if(keycode == PLATFORMER.settings.getMoveLeftButton()) { // If we need to stop moving left.
			if(Gdx.input.isKeyPressed(PLATFORMER.settings.getMoveRightButton())){ // If the player wants to move right.
				StaticVars.player.moveRight(); // Start moving right.
			}else{
				StaticVars.player.stopMoving(); // Stop moving.
			}
		}

		if(keycode == PLATFORMER.settings.getMoveRightButton()) { // If we need to stop moving right.
			if(Gdx.input.isKeyPressed(PLATFORMER.settings.getMoveLeftButton())){ // If the player wants to move left.
				StaticVars.player.moveLeft(); // Start moving left
			}else{
				StaticVars.player.stopMoving(); // Stop moving.
			}
		}

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

	@Override
	public void show() {
		Gdx.input.setCursorCatched(true); // Hide the cursor on the game screen.
		PLATFORMER.music.playSong(MusicManager.Song.GAME_SONG);
	}

	@Override
	public void hide() {
		Gdx.input.setCursorCatched(false); // Make sure the cursor isn't locked when we leave the game screen.
		stage.dispose();
		StaticVars.bulletList.clear();
		StaticVars.cloudList.clear();
		StaticVars.enemyList.clear();
	}

}
