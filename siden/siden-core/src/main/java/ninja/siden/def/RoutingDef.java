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

import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ninja.siden.HttpMethod;
import ninja.siden.Renderer;
import ninja.siden.Request;
import ninja.siden.Route;
import ninja.siden.RoutingCustomizer;
import ninja.siden.internal.Core;

/**
 * @author taichi
 */
public class RoutingDef implements RoutingCustomizer {

	final String template;
	public final Predicate predicate;
	final HttpMethod method;
	public final Route route;
	public Renderer<?> renderer;
	public String type = "";
	public List<String> accepts = new ArrayList<>();
	public Predicate matches = Predicates.truePredicate();

	public RoutingDef(String template, Predicate predicate, HttpMethod method,
			Route route) {
		this.template = template;
		this.predicate = predicate;
		this.method = method;
		this.route = route;
	}

	@Override
	public RoutingCustomizer type(String type) {
		this.type = Objects.requireNonNull(type);
		return this;
	}

	@Override
	public RoutingCustomizer accept(String type) {
		this.accepts.add(Objects.requireNonNull(type));
		return this;
	}

	@Override
	public RoutingCustomizer match(java.util.function.Predicate<Request> fn) {
		this.matches = Predicates.and(this.matches, Core.adapt(fn));
		return this;
	}

	@Override
	public <MODEL> RoutingCustomizer render(Renderer<MODEL> renderer) {
		this.renderer = renderer;
		return this;
	}

	public String template() {
		return this.template;
	}

	public Predicate predicate() {
		return this.predicate;
	}

	public HttpMethod method() {
		return this.method;
	}

	public Route route() {
		return this.route;
	}

	public Renderer<?> renderer() {
		return this.renderer;
	}

	public String type() {
		return this.type;
	}

	public List<String> accepts() {
		return Collections.unmodifiableList(this.accepts);
	}

	public void acceps(List<String> accepts) {
		this.accepts = Objects.requireNonNull(accepts);
	}

	public Predicate matches() {
		return this.matches;
	}

	public void matches(Predicate matches) {
		this.matches = Objects.requireNonNull(matches);
	}
}
