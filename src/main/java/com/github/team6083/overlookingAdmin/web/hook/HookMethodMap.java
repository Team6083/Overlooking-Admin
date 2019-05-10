package com.github.team6083.overlookingAdmin.web.hook;

import com.github.team6083.overlookingAdmin.util.UserPermission;
import fi.iki.elonen.NanoHTTPD;

import java.lang.reflect.Method;
import java.util.HashMap;

public class HookMethodMap {

    private HashMap<String, MethodOption> map = new HashMap<>();

    public class MethodOption{
        public Method method;
        public UserPermission requiredPermission;
        public NanoHTTPD.Method httpMethod;

        public MethodOption(Method method, UserPermission requiredPermission, NanoHTTPD.Method httpMethod) {
            this.method = method;
            this.requiredPermission = requiredPermission;
            this.httpMethod = httpMethod;
        }
    }


    public boolean containsKey(String uri) {
        return map.containsKey(uri);
    }


    public MethodOption get(String uri) {
        if (containsKey(uri)) {
            return map.get(uri);
        }
        else {
            return null;
        }
    }


    public void put(String uri, Method method, UserPermission userPermission, NanoHTTPD.Method httpMethod) {
        MethodOption methodOption = new MethodOption(method, userPermission, httpMethod);
        map.put(uri, methodOption);
    }

    public void put(String uri, MethodOption option){
        map.put(uri, option);
    }


    public void remove(String uri) {
        map.remove(uri);
    }

    public void clear() {
        map.clear();
    }
}
