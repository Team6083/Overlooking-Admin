package com.github.team6083.overlookingAdmin.util.router;

import java.util.HashMap;
import java.util.Map;

public class StringRouter {

    private Map<String, RouteHandler> routeMap;

    public StringRouter() {
        routeMap = new HashMap<>();
    }

    public void add(String uri, RouteHandler handler) {
        routeMap.put(uri, handler);
    }

    public RouteHandler getHandler(String uri) {
        if (checkUri(uri)) {
            return routeMap.get(uri);
        } else{
            return new NotFoundHandler();
        }
    }

    public boolean checkUri(String uri){
        return routeMap.containsKey(uri);
    }
}
