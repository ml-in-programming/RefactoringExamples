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
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

import org.spoutcraft.launcher.api.*;
import org.spoutcraft.launcher.entrypoint.SpoutcraftLauncher;
import org.spoutcraft.launcher.exceptions.CorruptedMinecraftJarException;
import org.spoutcraft.launcher.exceptions.MinecraftVerifyException;
import org.spoutcraft.launcher.launch.MinecraftAppletEnglober;
import org.spoutcraft.launcher.launch.MinecraftLauncher;
import org.spoutcraft.launcher.util.OperatingSystem;
import org.spoutcraft.launcher.util.Utils;

import javax.swing.*;

public final class StartupParameters {
	@SuppressWarnings("unused")
	private final String[] args;
	public StartupParameters(String[] args) {
		this.args = args;
	}
	@Parameter
	private List<String> parameters = Lists.newArrayList();

	@Parameter(names = {"-username", "-user", "-u"}, description = "Minecraft username")
	private String user = null;

	@Parameter(names = {"-password", "-pass", "-p"}, description = "Minecraft password")
	private String pass = null;

	@Parameter(names = {"-server", "-host", "-join", "-j", "-h", "-s"}, description = "Minecraft server to join")
	private String server = null;

	@Parameter(names = {"-portable", "--portable", "-pmode", "-portable_mode", "-pm"}, description = "Portable mode")
	private boolean portable = false;

	@Parameter(names = {"-portable_path", "--portable_path", "-pmode_path", "-portable_mode_path", "-pm_path"}, description = "Portable mode path (optional)")
	private String portablePath = "./";

	@Parameter(names = {"-debug", "--debug", "-verbose", "-v", "-d"}, description = "Debug mode")
	private boolean debug = false;

	@Parameter(names = {"-proxy_host"}, description = "HTTP proxy host")
	String proxyHost = null;

	@Parameter(names = {"-proxy_port"}, description = "HTTP proxy port")
	String proxyPort = null;

	@Parameter(names = {"-proxy_user"}, description = "HTTP proxy username")
	String proxyUser = null;

	@Parameter(names = {"-proxy_password"}, description = "HTTP proxy password")
	String proxyPassword = null;

	@Parameter(names = {"-nomd5", "-ignoremd5"}, description = "Disables file integrity checking with MD5")
	boolean ignoreMD5 = false;

	@Parameter(names = {"-build"}, description = "Uses a specific Spoutcraft build")
	private int build = -1;

	@Parameter(names = {"-relaunched"}, description = "Used to indicate the process has been relaunched for property memory arguments")
	private boolean relaunched = false;

	@Parameter(names = {"-console"}, description = "Shows the Spoutcraft debug console window")
	private boolean console = false;

	// TODO move to GameLauncher
	public static void runGame(GameLauncher gameLauncher, String user, String session, String downloadTicket) {
		Dimension size = WindowMode.getModeById(Settings.getWindowModeId()).getDimension(gameLauncher);
		Point centeredLoc = WindowMode.getModeById(Settings.getWindowModeId()).getCenteredLocation(Launcher.getLoginFrame());

		gameLauncher.setLocation(centeredLoc);
		gameLauncher.setSize(size);

		Launcher.getGameUpdater().setWaiting(true);
		while (!Launcher.getGameUpdater().isFinished()) {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ignore) { }
		}

		Applet applet = null;
		try {
			applet = MinecraftLauncher.getMinecraftApplet(Launcher.getGameUpdater().getBuild().getLibraries());
		} catch (CorruptedMinecraftJarException corruption) {
			corruption.printStackTrace();
		} catch (MinecraftVerifyException verify) {
			Launcher.clearCache();
			JOptionPane.showMessageDialog(gameLauncher.getParent(), "Your installation is corrupt, and has been cleaned. \nTry to login again.\n\n If that fails, close and restart this launcher.");
			gameLauncher.setVisible(false);
			gameLauncher.dispose();
			Launcher.getLoginFrame().enableForm();
			return;
		}
		if (applet == null) {
			String message = "Failed to launch Spoutcraft!";
			gameLauncher.setVisible(false);
			JOptionPane.showMessageDialog(gameLauncher.getParent(), message);
			gameLauncher.dispose();
			Launcher.getLoginFrame().enableForm();
			return;
		}

		StartupParameters params = Utils.getStartupParameters();

		gameLauncher.minecraft = new MinecraftAppletEnglober(applet);
		gameLauncher.minecraft.addParameter("username", user);
		gameLauncher.minecraft.addParameter("sessionid", session);
		gameLauncher.minecraft.addParameter("downloadticket", downloadTicket);
		gameLauncher.minecraft.addParameter("spoutcraftlauncher", "true");
		gameLauncher.minecraft.addParameter("portable", params.isPortable() + "");
		gameLauncher.minecraft.addParameter("working_directory", Utils.getWorkingDirectory().getAbsolutePath());
		if (params.getServer() != null) {
			gameLauncher.minecraft.addParameter("server", params.getServer());
			if (params.getPort() != null) {
				gameLauncher.minecraft.addParameter("port", params.getPort());
			} else {
				gameLauncher.minecraft.addParameter("port", "25565");
			}
		} else if (Settings.getDirectJoin() != null && Settings.getDirectJoin().length() > 0) {
			String address = Settings.getDirectJoin();
			String port = "25565";
			if (address.contains(":")) {
				String[] s = address.split(":");
				address = s[0];
				port = s[1];
			}
			gameLauncher.minecraft.addParameter("server", address);
			gameLauncher.minecraft.addParameter("port", port);
		}
		if (params.getProxyHost() != null) {
			gameLauncher.minecraft.addParameter("proxy_host", params.getProxyHost());
		}
		if (params.getProxyPort() != null) {
			gameLauncher.minecraft.addParameter("proxy_port", params.getProxyPort());
		}
		if (params.getProxyUser() != null) {
			gameLauncher.minecraft.addParameter("proxy_user", params.getProxyUser());
		}
		if (params.getProxyPassword() != null) {
			gameLauncher.minecraft.addParameter("proxy_pass", params.getProxyPassword());
		}
		//minecraft.addParameter("fullscreen", WindowMode.getModeById(Settings.getWindowModeId()) == WindowMode.FULL_SCREEN ? "true" : "false");

		applet.setStub(gameLauncher.minecraft);
		gameLauncher.add(gameLauncher.minecraft);

		gameLauncher.validate();
		gameLauncher.setVisible(true);
		new File(Utils.getWorkingDirectory(), "resources").mkdirs();
		try {
			gameLauncher.minecraft.init();
			gameLauncher.minecraft.setSize(gameLauncher.getWidth(), gameLauncher.getHeight());
			gameLauncher.minecraft.start();
			Launcher.getLoginFrame().onEvent(org.spoutcraft.launcher.api.Event.GAME_LAUNCH);
		} catch (Throwable t) {
			Launcher.getLogger().log(Level.SEVERE, "Unable to launch Spoutcraft", t);
			SpoutcraftLauncher.flush();
		}
		return;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void logParameters(Logger log) {
		log.info("------------- Startup Parameters ------------");
		if (user != null) {
			log.info("Minecraft username: " + user);
		}
		if (pass != null) {
			log.info("Minecraft password exists");
		}
		if (server != null) {
			log.info("Minecraft server: " + server);
		}
		if (portable) {
			log.info("Portable mode activated");
		}
		if (debug) {
			log.info("Debug mode activated");
		}
		if (proxyHost != null) {
			log.info("Proxy host: " + proxyHost);
		}
		if (proxyPort != null) {
			log.info("Proxy port: " + proxyPort);
		}
		if (proxyUser != null) {
			log.info("Proxy user exists");
		}
		if (proxyPassword != null) {
			log.info("Porxy password exists");
		}
		if (ignoreMD5) {
			log.info("No MD5 mode activated");
		}
		if (build != -1) {
			log.info("Spoutcraft build selected: " + build);
		}
		if (relaunched) {
			log.info("Relaunched with correct memory");
		}
		if (console) {
			log.info("Debug console enabled");
		}
		log.info("--------- End of Startup Parameters ---------");
	}

	public boolean hasAccount() {
		return user != null && user.length() > 0 && pass != null && pass.length() > 0;
	}

	private boolean shouldRelaunch() {
		if (relaunched) {
			return false;
		}
		int mb = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
		int min = Memory.getMemoryFromId(Settings.getMemory()).getMemoryMB();
		return mb < min;
	}

	public boolean relaunch(Logger log) {
		if (shouldRelaunch()) {
			String pathToJar;
			File jar = new File(SpoutcraftLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile());
			try {
				pathToJar = jar.getCanonicalPath();
			} catch (IOException e1) {
				pathToJar = jar.getAbsolutePath();
			}
			try {
				pathToJar = URLDecoder.decode(pathToJar, "UTF-8");
			} catch (java.io.UnsupportedEncodingException ignore) { }

			final int memory = Memory.getMemoryFromId(Settings.getMemory()).getMemoryMB();
			log.info("Attempting relaunch with " + memory + "MB of RAM");
			log.info("Path to launcher jar: " + pathToJar);

			ProcessBuilder processBuilder = new ProcessBuilder();
			ArrayList<String> commands = new ArrayList<String>();
			if (OperatingSystem.getOS().isWindows()) {
				commands.add("javaw");
			} else if (OperatingSystem.getOS().isMac()) {
				commands.add("java");
				commands.add("-Xdock:name=Spoutcraft");
			} else {
				commands.add("java");
			}
			commands.add("-Xmx" + memory + "m");
			commands.add("-cp");
			commands.add(pathToJar);
			commands.add(SpoutcraftLauncher.class.getName());
			commands.addAll(getRelaunchParameters());
			commands.add("-relaunched");
			processBuilder.command(commands);

			try {
				processBuilder.start();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private List<String> getRelaunchParameters() {
		List<String> params = new ArrayList<String>();
		if (user != null) {
			params.add("-username");
			params.add(user);
		}
		if (pass != null) {
			params.add("-password");
			params.add(pass);
		}
		if (server != null) {
			params.add("-server");
			params.add(server);
		}
		if (portable) {
			params.add("-portable");
			params.add("-portable_path");
			params.add(portablePath);
		}
		if (debug) {
			params.add("-debug");
		}
		if (proxyHost != null) {
			params.add("-proxy_host");
			params.add(proxyHost);
		}
		if (proxyPort != null) {
			params.add("-proxy_port");
			params.add(proxyPort);
		}
		if (proxyUser != null) {
			params.add("-proxy_user");
			params.add(proxyUser);
		}
		if (proxyPassword != null) {
			params.add("-proxy_password");
			params.add(proxyPassword);
		}
		if (ignoreMD5) {
			params.add("-nomd5");
		}
		if (build != -1) {
			params.add("-build");
			params.add(Integer.toString(build));
		}
		if (console) {
			params.add("-console");
		}
		return params;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getServer() {
		if (server == null) {
			return null;
		}
		if (server.contains(":")) {
			return server.substring(0, server.indexOf(":"));
		}
		return server;
	}

	public String getPort() {
		if (server == null) {
			return null;
		}
		if (server.contains(":")) {
			return server.substring(server.indexOf(":") + 1);
		}
		return null;
	}

	public boolean isPortable() {
		return portable;
	}

	public boolean isDebugMode() {
		return debug;
	}

	public int getSpoutcraftBuild() {
		return build;
	}

	public boolean isConsole() {
		return console;
	}

	public void setSpoutcraftBuild(int build) {
		this.build = build;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public String getPortablePath() {
		return portablePath;
	}
}
