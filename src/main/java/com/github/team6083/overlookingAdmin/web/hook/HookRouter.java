package com.github.team6083.overlookingAdmin.web.hook;

import com.github.team6083.overlookingAdmin.web.hook.handler.AppsHandler;
import com.github.team6083.overlookingAdmin.web.hook.handler.FieldConfigHandler;
import com.github.team6083.overlookingAdmin.web.hook.handler.MemberProfileHandler;
import com.github.team6083.overlookingAdmin.web.hook.handler.UsersHandler;

import java.util.HashMap;
import java.util.Map;

public class HookRouter {

    private static Map<String, HookHandler> handlerMap = new HashMap<>();

    public static void init() throws NoSuchMethodException {
        handlerMap.put("/users", new UsersHandler());
        handlerMap.put("/apps", new AppsHandler());
        handlerMap.put("/memberProfiles", new MemberProfileHandler());
        handlerMap.put("/fieldConfig", new FieldConfigHandler());
    }

    public static HookHandler getHandler(String uri) {
        if (handlerMap.containsKey(getAppUri(uri))) {
            return handlerMap.get(getAppUri(uri));
        }
        return null;
    }

    public static String getAppUri(String uri){
        return "/" + uri.split("/")[1];
    }

}
