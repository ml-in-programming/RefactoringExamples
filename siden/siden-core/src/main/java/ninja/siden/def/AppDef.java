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

import java.util.ArrayList;
import java.util.List;

import ninja.siden.*;

import org.xnio.OptionMap;

/**
 * @author taichi
 */
public class AppDef {

	OptionMap config;

	List<AssetDef> assets = new ArrayList<>();

	public List<RoutingDef> router = new ArrayList<>();

	List<ErrorCodeRoutingDef> errorRouter = new ArrayList<>();

	List<ExceptionalRoutingDef<?>> exceptionRouter = new ArrayList<>();

	List<SubAppDef> subapp = new ArrayList<>();

	public List<WebSocketDef> websockets = new ArrayList<>();

	public List<FilterDef> filters = new ArrayList<>();

	public AppDef(OptionMap config) {
		this.config = config;
	}

	public OptionMap config() {
		return this.config;
	}

	public AssetsCustomizer add(String path, String root) {
		AssetDef def = new AssetDef(path, root);
		this.assets.add(def);
		return def;
	}

	public void add(String prefix, AppDef subapp) {
		this.subapp.add(new SubAppDef(prefix, subapp));
	}

	public <T extends Throwable> RendererCustomizer<?> add(Class<T> type,
			ExceptionalRoute<T> route) {
		ExceptionalRoutingDef<T> def = new ExceptionalRoutingDef<>(type, route);
		this.exceptionRouter.add(def);
		return def;
	}

	public RendererCustomizer<?> add(int errorCode, Route route) {
		ErrorCodeRoutingDef def = new ErrorCodeRoutingDef(errorCode, route);
		this.errorRouter.add(def);
		return def;
	}

	public void accept(AppContext context, AppBuilder ab) {
		this.assets.forEach(d -> ab.apply(context, d));
		this.router.forEach(d -> ab.apply(context, d));
		this.errorRouter.forEach(d -> ab.apply(context, d));
		this.exceptionRouter.forEach(d -> ab.apply(context, d));
		this.subapp.forEach(d -> ab.apply(context, d));
		this.websockets.forEach(d -> ab.apply(context, d));
		this.filters.forEach(d -> ab.apply(context, d));
	}
}
