/*******************************************************************************
 * Copyright (C) 2017 Vincent Macri
 * 
 * This file is part of "Bah, Humdinger!".
 *
 * "Bah, Humdinger!" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * "Bah, Humdinger!" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Bah, Humdinger!".  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.platformer.game.desktop;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.platformer.game.Platformer;

/**
 * Sets up the window and starts the game.
 * @author Vincent Macri
 */
public class DesktopLauncher {
	
	public static DisplayMode displayMode;
	
	public static void main (String[] arg) {
		displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	
		config.setFromDisplayMode(displayMode);
		config.title = "Platformer";
		config.vSyncEnabled = true;
		config.foregroundFPS = 0;
		new LwjglApplication(new Platformer(), config);
	}
}