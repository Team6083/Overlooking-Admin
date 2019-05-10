package com.github.team6083.overlookingAdmin.web.hook;

import com.github.team6083.overlookingAdmin.web.hook.handler.AppsHandler;
import com.github.team6083.overlookingAdmin.web.hook.handler.FieldConfigHandler;
import com.github.team6083.overlookingAdmin.web.hook.handler.MemberProfileHandler;
import com.github.team6083.overlookingAdmin.web.hook.handler.UsersHandler;

import java.util.HashMap;
import java.util.Map;

public class HookRouter {

    private static Map<String, HookHandler> handlerMap = new HashMap<>();

    public static void init() {
        handlerMap.put("users", new UsersHandler());
        handlerMap.put("apps", new AppsHandler());
        handlerMap.put("memberProfile", new MemberProfileHandler());
        handlerMap.put("fieldConfig", new FieldConfigHandler());
    }

    public static HookHandler getHandler(String uri) {
        if (handlerMap.containsKey(uri.split("/")[1])) {
            return handlerMap.get(uri.split("/")[1]);
        }
        return null;
    }

}
