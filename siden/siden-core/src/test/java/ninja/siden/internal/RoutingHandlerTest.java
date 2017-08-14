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
package ninja.siden.internal;

import static org.junit.Assert.assertEquals;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;

import java.io.IOException;
import java.util.Optional;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import ninja.siden.Config;
import ninja.siden.Renderer;
import ninja.siden.Request;
import ninja.siden.Response;
import ninja.siden.Route;
import ninja.siden.def.ErrorCodeRoutingDef;
import ninja.siden.def.ExceptionalRoutingDef;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author taichi
 */
@RunWith(JMockit.class)
public class RoutingHandlerTest {

	HttpServerExchange exchange;

	@Before
	public void setUp() {
		this.exchange = new MockUp<HttpServerExchange>() {
			@Mock
			public boolean isInIoThread() {
				return false;
			}
		}.getMockInstance();
	}

	@Test
	public void testNotMatch() throws Exception {
		RoutingHandler target = new RoutingHandler(Testing.mustCall());
		target.add(Predicates.falsePredicate(), (q, s) -> "", null);
		target.handleRequest(this.exchange);
	}

	@Test
	public void testSimpleMatch() throws Exception {
		RoutingHandler target = new RoutingHandler(Testing.empty());
		target.add(Predicates.truePredicate(), (q, s) -> "Hello",
				new MockUp<Renderer<Object>>() {
					@Mock(invocations = 1)
					void render(Object model, HttpServerExchange sink)
							throws IOException {
						assertEquals("Hello", model);
					}
				}.getMockInstance());
		target.handleRequest(this.exchange);
	}

	@Test
	public void testReturnOptinal() throws Exception {
		RoutingHandler target = new RoutingHandler(Testing.empty());
		target.add(Predicates.truePredicate(), (q, s) -> Optional.of("Hello"),
				new MockUp<Renderer<Object>>() {
					@Mock(invocations = 1)
					void render(Object model, HttpServerExchange sink)
							throws IOException {
						assertEquals("Hello", model);
					}
				}.getMockInstance());
		target.handleRequest(this.exchange);
	}

	@Test
	public void testReturnStatusCode() throws Exception {
		RoutingHandler target = new RoutingHandler(Testing.empty());
		new ErrorCodeRoutingDef(400, new MockUp<Route>() {
			@Mock(invocations = 1)
			Object handle(Request request, Response response) throws Exception {
				return "Hey";
			}
		}.getMockInstance()).render(new MockUp<Renderer<Object>>() {
			@Mock(invocations = 1)
			void render(Object model, HttpServerExchange sink)
					throws IOException {
				assertEquals("Hey", model);
			}
		}.getMockInstance()).add(target);

		target.add(Predicates.truePredicate(), (q, s) -> 400,
				new MockUp<Renderer<Object>>() {
					@Mock(invocations = 0)
					void render(Object model, HttpServerExchange sink)
							throws IOException {
					}
				}.getMockInstance());

		target.handleRequest(this.exchange);
	}

	@Test
	public void testSetStatusCode() throws Exception {
		RoutingHandler target = new RoutingHandler(Testing.empty());
		new ErrorCodeRoutingDef(402, new MockUp<Route>() {
			@Mock(invocations = 1)
			Object handle(Request request, Response response) throws Exception {
				return "Hey";
			}
		}.getMockInstance()).render(new MockUp<Renderer<Object>>() {
			@Mock(invocations = 1)
			void render(Object model, HttpServerExchange sink)
					throws IOException {
				assertEquals("Hey", model);
			}
		}.getMockInstance()).add(target);

		this.exchange.putAttachment(Core.CONFIG, Config.defaults().getMap());
		this.exchange.putAttachment(Core.RESPONSE, new SidenResponse(
				this.exchange));

		target.add(Predicates.truePredicate(), (q, s) -> s.status(402),
				new MockUp<Renderer<Object>>() {
					@Mock(invocations = 0)
					void render(Object model, HttpServerExchange sink)
							throws IOException {
					}
				}.getMockInstance());

		target.handleRequest(this.exchange);
		assertEquals(402, this.exchange.getResponseCode());
	}

	@Test
	public void testEspeciallyPkgsAreNotRender() throws Exception {
		this.exchange.putAttachment(Core.CONFIG, Config.defaults().getMap());
		this.exchange.putAttachment(Core.RESPONSE, new SidenResponse(
				this.exchange));

		RoutingHandler target = new RoutingHandler(Testing.empty());
		target.add(Predicates.truePredicate(),
				(q, s) -> s.cookie("hoge", "fuga"),
				new MockUp<Renderer<Object>>() {
					@Mock(invocations = 0)
					void render(Object model, HttpServerExchange sink)
							throws IOException {
					}
				}.getMockInstance());

		target.handleRequest(this.exchange);
	}

	@Test
	public void testResponseCodeSettigIsOnce() throws Exception {
		this.exchange = new MockUp<HttpServerExchange>() {
			@Mock
			public boolean isInIoThread() {
				return false;
			}

			@Mock
			public HeaderMap getResponseHeaders() {
				return new HeaderMap();
			}

			@Mock(invocations = 1)
			public HttpServerExchange setResponseCode(final int responseCode) {
				return getMockInstance();
			}

			@Mock(invocations = 1)
			public HttpServerExchange endExchange() {
				return getMockInstance();
			}
		}.getMockInstance();

		this.exchange.putAttachment(Core.CONFIG, Config.defaults().getMap());
		this.exchange.putAttachment(Core.RESPONSE, new SidenResponse(
				this.exchange));

		RoutingHandler target = new RoutingHandler(Testing.empty());
		target.add(Predicates.truePredicate(),
				(q, s) -> s.redirect("/hoge/fuga/moge"), null);

		target.handleRequest(this.exchange);
	}

	static class MyIoException extends IOException {
		private static final long serialVersionUID = 1L;
	}

	@Test
	public void testExceptionalRouting() throws Exception {
		this.exchange = new MockUp<HttpServerExchange>() {
			@Mock
			public boolean isInIoThread() {
				return false;
			}

			@Mock
			public boolean isRequestChannelAvailable() {
				return true;
			}
		}.getMockInstance();
		RoutingHandler target = new RoutingHandler(Testing.empty());

		target.add(new ExceptionalRoutingDef<>(IOException.class,
				(ex, res, req) -> {
					assertEquals(MyIoException.class, ex.getClass());
					return "Hey";
				}).render(new MockUp<Renderer<Object>>() {
			@Mock(invocations = 1)
			void render(Object model, HttpServerExchange sink)
					throws IOException {
				assertEquals("Hey", model);
			}
		}.getMockInstance()));

		target.add(Predicates.truePredicate(), (q, s) -> {
			throw new MyIoException();
		}, new MockUp<Renderer<Object>>() {
			@Mock(invocations = 0)
			void render(Object model, HttpServerExchange sink)
					throws IOException {
			}
		}.getMockInstance());

		target.handleRequest(this.exchange);

		assertEquals(500, this.exchange.getResponseCode());
	}
}
