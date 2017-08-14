/*
 * This file is part of Spoutcraft Launcher.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft Launcher is licensed under the Spout License Version 1.
 *
 * Spoutcraft Launcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Spoutcraft Launcher is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.launcher;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.spoutcraft.launcher.api.Event;
import org.spoutcraft.launcher.api.Launcher;
import org.spoutcraft.launcher.entrypoint.SpoutcraftLauncher;
import org.spoutcraft.launcher.exceptions.CorruptedMinecraftJarException;
import org.spoutcraft.launcher.exceptions.MinecraftVerifyException;
import org.spoutcraft.launcher.launch.MinecraftAppletEnglober;
import org.spoutcraft.launcher.launch.MinecraftLauncher;
import org.spoutcraft.launcher.skin.components.LoginFrame;
import org.spoutcraft.launcher.util.Utils;

public class GameLauncher extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;
	MinecraftAppletEnglober minecraft;

	public static final int RETRYING_LAUNCH = -1;
	public static final int ERROR_IN_LAUNCH = 0;
	public static final int SUCCESSFUL_LAUNCH = 1;

	public GameLauncher() {
		super("Spoutcraft");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.addWindowListener(this);
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.spoutcraftIcon));
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		SpoutcraftLauncher.destroyConsole();
		if (this.minecraft != null) {
			this.minecraft.stop();
			this.minecraft.destroy();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) { }
		}
		System.out.println("Exiting Spoutcraft");
		this.dispose();
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
