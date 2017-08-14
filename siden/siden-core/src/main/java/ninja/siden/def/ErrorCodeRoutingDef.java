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

import ninja.siden.Renderer;
import ninja.siden.RendererCustomizer;
import ninja.siden.Route;
import ninja.siden.internal.RoutingHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author taichi
 */
public class ErrorCodeRoutingDef implements
		RendererCustomizer<ErrorCodeRoutingDef> {

	final int code;
	final Route route;
	Renderer<?> renderer;

	public ErrorCodeRoutingDef(int code, Route route) {
		super();
		this.code = code;
		this.route = route;
	}

	@Override
	public <MODEL> ErrorCodeRoutingDef render(Renderer<MODEL> renderer) {
		this.renderer = renderer;
		return this;
	}

	public int code() {
		return this.code;
	}

	public Route route() {
		return this.route;
	}

	public Renderer<?> renderer() {
		return this.renderer;
	}

    public void add(RoutingHandler routingHandler) {
        List<ErrorCodeRoutingDef> list = routingHandler.errorCodeMappings.get(
                code());
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(this);
        routingHandler.errorCodeMappings.put(code(), list);
    }
}
