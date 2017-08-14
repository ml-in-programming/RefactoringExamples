/*
 * Copyright 2014 SATO taichi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package example;

import ninja.siden.App;

/**
 * @author taichi
 */
public class UseWebsocket {

	public static void main(String[] args) {
		App app = new App();

		app.get("/", (q, s) -> new java.io.File("assets/chat.html"));

		app.websocket("/ws").onText(
				(con, txt) -> con.peers().forEach(c -> c.send(txt)));

		app.listen(8181).addShutdownHook();
	}
}
