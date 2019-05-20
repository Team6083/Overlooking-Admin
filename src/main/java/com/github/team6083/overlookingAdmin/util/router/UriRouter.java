package com.github.team6083.overlookingAdmin.util.router;

import com.github.team6083.overlookingAdmin.web.Template;
import fi.iki.elonen.NanoHTTPD;

import java.util.HashMap;
import java.util.Map;

public class UriRouter {

    private Map<String, Handler> routeTable;

    public enum HandlerAccessLevel {
        PublicAccess,
        AuthAccess,
        PrivateAccess
    }

    public class Handler {
        public RouteHandler routeHandler;
        public NanoHTTPD.Method method;
        public HandlerAccessLevel accessControl;

        Handler(RouteHandler routeHandler, NanoHTTPD.Method method, HandlerAccessLevel accessControl) {
            this.routeHandler = routeHandler;
            this.method = method;
            this.accessControl = accessControl;
        }
    }

    public UriRouter() {
        routeTable = new HashMap<>();
    }

    public void add(String uri, RouteHandler handler, NanoHTTPD.Method method, HandlerAccessLevel accessLevel) {
        routeTable.put(uri, new Handler(handler, method, accessLevel));
    }

    public Handler get(String uri) {
        if (checkUri(uri)) {
            return routeTable.get(uri);
        } else {
            return null;
        }
    }

    public boolean checkUri(String uri) {
        return routeTable.containsKey(uri);
    }
}
