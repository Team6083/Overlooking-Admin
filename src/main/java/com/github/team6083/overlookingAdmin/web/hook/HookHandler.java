package com.github.team6083.overlookingAdmin.web.hook;

import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.google.firebase.auth.FirebaseAuthException;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.*;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class HookHandler {
    public HookMethodMap hookMethodMap = new HookMethodMap();

    public HookHandler() throws NoSuchMethodException {
        setHookMethodMap();
    }

    protected abstract void setHookMethodMap() throws NoSuchMethodException;

    protected Response handle(String uri, Map<String, String> header, String body, NanoHTTPD.Method httpMethod) {
        Response r = null;

        HookMethodMap.MethodOption methodOption = hookMethodMap.get(uri);

        if (methodOption == null) {
            return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "404 not found");
        }

        // http method check
        if (methodOption.httpMethod != httpMethod) {
            return methodNotAllowed("405 method not allowed");
        }

        // auth check
        if (methodOption.requiredPermission != UserPermission.NONE) {
            String idToken = HookServer.getIdToken(header);
            if (idToken.equals("")) {
                return unauthorized("401 unauthorized");
            }

            try {
                if (!HookServer.checkPermission(idToken, methodOption.requiredPermission)) {
                    return forbidden("403 forbidden");
                }
            } catch (InterruptedException | ExecutionException | ParseException | FirebaseAuthException e) {
                e.printStackTrace();
            }
        }

        //invoke method
        java.lang.reflect.Method method = methodOption.method;

        try {
            r = (Response) method.invoke(this, body);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (r == null) {
            r = internalError("response empty");
        }

        return r;
    }

    protected Response sendTextResponse(Response.Status status, String msg) {
        return NanoHTTPD.newFixedLengthResponse(status, NanoHTTPD.MIME_PLAINTEXT, msg);
    }

    protected Response okResponse(String msg) {
        return sendTextResponse(Response.Status.OK, msg);
    }

    protected Response badRequest(String msg) {
        return sendTextResponse(Response.Status.BAD_REQUEST, msg);
    }

    protected Response unauthorized(String msg) {
        return sendTextResponse(Response.Status.UNAUTHORIZED, msg);
    }

    protected Response forbidden(String msg) {
        return sendTextResponse(Response.Status.FORBIDDEN, msg);
    }

    protected Response methodNotAllowed(String msg) {
        return sendTextResponse(Response.Status.METHOD_NOT_ALLOWED, msg);
    }

    protected Response internalError(String msg) {
        return sendTextResponse(Response.Status.INTERNAL_ERROR, "internal error: " + msg);
    }
}
