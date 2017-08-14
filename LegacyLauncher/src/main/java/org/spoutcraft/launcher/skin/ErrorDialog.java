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
package org.spoutcraft.launcher.skin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.swing.*;

import org.spoutcraft.launcher.Settings;
import org.spoutcraft.launcher.util.Compatibility;

public class ErrorDialog extends JDialog implements ActionListener {
	private static final URL spoutcraftIcon = ErrorDialog.class.getResource("/org/spoutcraft/launcher/resources/icon.png");
	private static final String CLOSE_ACTION = "close";
	private static final String REPORT_ACTION = "report";
	private static final String PASTEBIN_URL = "http://pastebin.com";
	private static final long serialVersionUID = 1L;
	private final Throwable cause;
	JLabel titleLabel;
	JLabel exceptionLabel;
	JScrollPane scrollPane1;
	JTextArea errorArea;
	JButton reportButton;
	JButton closeButton;
	public ErrorDialog(Frame owner, Throwable t) {
		super(owner);
		this.cause = t;
		ConsoleFrame.initComponents(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Compatibility.setIconImage(this, Toolkit.getDefaultToolkit().getImage(spoutcraftIcon));
		populateException(this.cause);
		reportButton.addActionListener(this);
		reportButton.setActionCommand(REPORT_ACTION);
		closeButton.addActionListener(this);
		closeButton.setActionCommand(CLOSE_ACTION);
	}

	private void populateException(Throwable e) {
		StringBuilder builder = new StringBuilder();
		builder.append("Stack Trace:").append("\n");
		builder.append("    Exception: ").append(e.getClass().getSimpleName()).append("\n");
		builder.append("    Message: ").append(e.getMessage()).append("\n");
		logTrace(builder, e);
		errorArea.setText(builder.toString());
	}

	private void logTrace(StringBuilder builder, Throwable e) {
		Throwable parent = e;
		String indent = "    ";
		while (parent != null) {
			if (parent == e) {
				builder.append(indent).append("Trace:").append("\n");
			} else {
				builder.append(indent).append("Caused By: (").append(parent.getClass().getSimpleName()).append(")").append("\n");
				builder.append(indent).append("    ").append("[").append(parent.getMessage()).append("]").append("\n");
			}
			for (StackTraceElement ele : e.getStackTrace()) {
				builder.append(indent).append("    ").append(ele.toString()).append("\n");
			}
			indent += "    ";
			parent = parent.getCause();
		}
	}

	private String generateExceptionReport() {
		StringBuilder builder = new StringBuilder("Spoutcraft Launcher Error Report:\n");
		builder.append("( Please submit this report to http://spout.in/issues )\n");
		builder.append("    Launcher Build: ").append(Settings.getLauncherBuild()).append("\n");
		builder.append("----------------------------------------------------------------------").append("\n");
		builder.append("Stack Trace:").append("\n");
		builder.append("    Exception: ").append(cause.getClass().getSimpleName()).append("\n");
		builder.append("    Message: ").append(cause.getMessage()).append("\n");
		logTrace(builder, cause);
		builder.append("----------------------------------------------------------------------").append("\n");
		builder.append("System Information:\n");
		builder.append("    Operating System: ").append(System.getProperty("os.name")).append("\n");
		builder.append("    Operating System Version: ").append(System.getProperty("os.version")).append("\n");
		builder.append("    Operating System Architecture: ").append(System.getProperty("os.arch")).append("\n");
		builder.append("    Java version: ").append(System.getProperty("java.version")).append(" ").append(System.getProperty("sun.arch.data.model", "32")).append(" bit").append("\n");
		builder.append("    Total Memory: ").append(Runtime.getRuntime().totalMemory() / 1024L / 1024L).append(" MB\n");
		builder.append("    Max Memory: ").append(Runtime.getRuntime().maxMemory() / 1024L / 1024L).append(" MB\n");
		builder.append("    Memory Free: ").append(Runtime.getRuntime().freeMemory() / 1024L / 1024L).append(" MB\n");
		builder.append("    CPU Cores: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
		return builder.toString();
	}

	public void actionPerformed(ActionEvent e) {
		action(e.getActionCommand());
	}

	private void action(String command) {
		if (command.equals(CLOSE_ACTION)) {
			closeForm();
		} else if (command.equals(REPORT_ACTION)) {
			PasteBinAPI pastebin = new PasteBinAPI("963f01dd506cb3f607a487bc34b60d16");
			String response = "";
			try {
				response = pastebin.makePaste(generateExceptionReport(), "ser_" + System.currentTimeMillis(), "text");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (response.startsWith(PASTEBIN_URL)) {
				try {
					Compatibility.browse((new URL(response)).toURI());
				} catch (Exception e) {
					System.err.println("Unable to generate error report. Response: " + response);
					e.printStackTrace();
				}
			} else {
				System.err.println("Unable to generate error report. Response: " + response);
			}
			closeForm();
		}
	}

	private void closeForm() {
		this.dispose();
		System.exit(0);
	}
}
