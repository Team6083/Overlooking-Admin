package com.github.team6083.overlookingAdmin.web.hook;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.*;

import java.util.Map;

public abstract class HookHandler {
    protected abstract Response handle(String uri, Map<String, String> header, String body, NanoHTTPD.Method method);

    protected Response sendTextResponse(Response.Status status, String msg) {
        return NanoHTTPD.newFixedLengthResponse(status, NanoHTTPD.MIME_PLAINTEXT, msg);
    }

    protected Response okResponse(String msg){
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
}
