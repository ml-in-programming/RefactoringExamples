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
package ninja.siden;

import io.undertow.Undertow;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import ninja.siden.def.*;
import ninja.siden.internal.LambdaWebSocketFactory;
import ninja.siden.internal.PathPredicate;
import ninja.siden.jmx.MetricsAppBuilder;
import ninja.siden.util.Publisher;

import org.jboss.logging.Logger;
import org.xnio.OptionMap;

/**
 * @author taichi
 */
public class App {

	static final Logger LOG = Logger.getLogger(App.class);

	protected AppDef def;

	protected Publisher<App> stop = new Publisher<>();

	public App() {
		this(Config.defaults().getMap());
	}

	protected App(OptionMap config) {
		Objects.requireNonNull(config);
		this.def = new AppDef(config);
	}

	public static App configure(
			Function<OptionMap.Builder, OptionMap.Builder> fn) {
		OptionMap.Builder omb = fn.apply(Config.defaults());
		return new App(omb.getMap());
	}

	// TODO move to AppDef
	public static RoutingCustomizer add(AppDef appDef, HttpMethod method, String path, Route route) {
		return add(appDef, path, new PathPredicate(path), method, route);
	}

	// TODO move to AppDef
	public static RoutingCustomizer add(AppDef appDef, HttpMethod method, Pattern p, Route route) {
		return add(appDef, p.pattern(), new PathPredicate(p), method, route);
	}

	// TODO move to AppDef
	public static RoutingCustomizer add(AppDef appDef, String template, PathPredicate path,
										HttpMethod method, Route route) {
		RoutingDef def = new RoutingDef(template, path, method, route);
		appDef.router.add(def);
		return def;
	}

	// TODO move to AppDef
	public static void add(AppDef appDef, String template, Predicate predicate,
						   WebSocketFactory factory) {
		appDef.websockets.add(new WebSocketDef(template, predicate, factory));
	}

	// TODO move to AppDef
	public static void add(AppDef appDef, Predicate predicate, Filter filter) {
		appDef.filters.add(new FilterDef(predicate, filter));
	}

	// request handling
	public RoutingCustomizer get(String path, Route route) {
		return add(this.def, HttpMethod.GET, path, route);
	}

	public RoutingCustomizer head(String path, Route route) {
		return add(this.def, HttpMethod.HEAD, path, route);
	}

	public RoutingCustomizer post(String path, Route route) {
		return add(this.def, HttpMethod.POST, path, route);
	}

	public RoutingCustomizer put(String path, Route route) {
		return add(this.def, HttpMethod.PUT, path, route);
	}

	public RoutingCustomizer delete(String path, Route route) {
		return add(this.def, HttpMethod.DELETE, path, route);
	}

	public RoutingCustomizer trace(String path, Route route) {
		return add(this.def, HttpMethod.TRACE, path, route);
	}

	public RoutingCustomizer options(String path, Route route) {
		return add(this.def, HttpMethod.OPTIONS, path, route);
	}

	public RoutingCustomizer connect(String path, Route route) {
		return add(this.def, HttpMethod.CONNECT, path, route);
	}

	public RoutingCustomizer patch(String path, Route route) {
		return add(this.def, HttpMethod.PATCH, path, route);
	}

	public RoutingCustomizer link(String path, Route route) {
		return add(this.def, HttpMethod.LINK, path, route);
	}

	public RoutingCustomizer unlink(String path, Route route) {
		return add(this.def, HttpMethod.UNLINK, path, route);
	}

	public void websocket(String path, WebSocketFactory factory) {
		add(this.def, path, new PathPredicate(path), factory);
	}

	public WebSocketCustomizer websocket(String path) {
		LambdaWebSocketFactory factory = new LambdaWebSocketFactory();
		websocket(path, factory);
		return factory;
	}

	public RoutingCustomizer get(Pattern p, Route route) {
		return add(this.def, HttpMethod.GET, p, route);
	}

	public RoutingCustomizer head(Pattern p, Route route) {
		return add(this.def, HttpMethod.HEAD, p, route);
	}

	public RoutingCustomizer post(Pattern p, Route route) {
		return add(this.def, HttpMethod.POST, p, route);
	}

	public RoutingCustomizer put(Pattern p, Route route) {
		return add(this.def, HttpMethod.PUT, p, route);
	}

	public RoutingCustomizer delete(Pattern p, Route route) {
		return add(this.def, HttpMethod.DELETE, p, route);
	}

	public RoutingCustomizer trace(Pattern p, Route route) {
		return add(this.def, HttpMethod.TRACE, p, route);
	}

	public RoutingCustomizer options(Pattern p, Route route) {
		return add(this.def, HttpMethod.OPTIONS, p, route);
	}

	public RoutingCustomizer connect(Pattern p, Route route) {
		return add(this.def, HttpMethod.CONNECT, p, route);
	}

	public RoutingCustomizer patch(Pattern p, Route route) {
		return add(this.def, HttpMethod.PATCH, p, route);
	}

	public RoutingCustomizer link(Pattern p, Route route) {
		return add(this.def, HttpMethod.LINK, p, route);
	}

	public RoutingCustomizer unlink(Pattern p, Route route) {
		return add(this.def, HttpMethod.UNLINK, p, route);
	}

	public void websocket(Pattern path, WebSocketFactory factory) {
		add(this.def, path.pattern(), new PathPredicate(path), factory);
	}

	public WebSocketCustomizer websocket(Pattern path) {
		LambdaWebSocketFactory factory = new LambdaWebSocketFactory();
		websocket(path, factory);
		return factory;
	}

	/**
	 * serve static resources on {@code root} from {@code root} directory.
	 * 
	 * @param root
	 * @return
	 */
	public AssetsCustomizer assets(String root) {
		return this.assets("/" + root, root);
	}

	/**
	 * mount static resources on {@code path} from {@code root} directory
	 * 
	 * @param path
	 * @param root
	 * @return
	 */
	public AssetsCustomizer assets(String path, String root) {
		return this.def.add(path, root);
	}

	// Filter requests
	public void use(Filter filter) {
		add(this.def, Predicates.truePredicate(), filter);
	}

	public void use(String path, Filter filter) {
		add(this.def, new PathPredicate(path), filter);
	}

	public <T extends Throwable> RendererCustomizer<?> error(Class<T> type,
			ExceptionalRoute<T> route) {
		return this.def.add(type, route);
	}

	public RendererCustomizer<?> error(int statusCode, Route route) {
		return this.def.add(statusCode, route);
	}

	/**
	 * mount sub application on {@code path}
	 * 
	 * @param path
	 * @param sub
	 */
	public void use(String path, App sub) {
		this.def.add(path, sub.def);
	}

	/**
	 * listening http at localhost:8080
	 */
	public Stoppable listen() {
		return listen(8080);
	}

	/**
	 * listening http at localhost with port
	 * 
	 * @param port
	 */
	public Stoppable listen(int port) {
		return listen("0.0.0.0", port);
	}

	/**
	 * listening http at host and port
	 * 
	 * @param host
	 * @param port
	 */
	public Stoppable listen(String host, int port) {
		return listen((b) -> b.addHttpListener(port, host));
	}

	/**
	 * listening more complex server
	 * 
	 * @param fn
	 */
	public Stoppable listen(Function<Undertow.Builder, Undertow.Builder> fn) {
		Undertow.Builder builder = fn.apply(Undertow.builder());
		Undertow server = builder.setHandler(buildHandlers()).build();
		server.start();
		stop.on(stop -> server.stop());
		return new Stoppable() {

			@Override
			public void stop() {
				stop.post(App.this);
			}

			@Override
			public void addShutdownHook() {
				Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
			}
		};
	}

	protected HttpHandler buildHandlers() {
		AppBuilder ab = newBuilder().apply(this.def.config());
		ab.begin();
		this.def.accept(new AppContext(this), ab);
		return ab.end(this);
	}

	protected Function<OptionMap, AppBuilder> newBuilder() {
		return Config.isInDev(this.def.config()) ? DefaultAppBuilder::new
				: MetricsAppBuilder::new;
	}

	public void stopOn(Consumer<App> fn) {
		stop.on(fn);
	}
}
