/*
 * Copyright 2015 SATO taichi
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
package ninja.siden.def;

import io.undertow.server.HttpHandler;
import ninja.siden.App;

/**
 * @author taichi
 */
public interface AppBuilder {

	void begin();

	void apply(AppContext context, AssetDef def);

	void apply(AppContext context, RoutingDef def);

	void apply(AppContext context, ErrorCodeRoutingDef def);

	void apply(AppContext context, ExceptionalRoutingDef<?> def);

	void apply(AppContext context, SubAppDef def);

	void apply(AppContext context, WebSocketDef def);

	void apply(AppContext context, FilterDef def);

	HttpHandler end(App root);
}
