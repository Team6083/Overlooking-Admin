package com.github.team6083.overlookingAdmin.util.router;

import fi.iki.elonen.NanoHTTPD;

public class NotFoundHandler implements RouteHandler {
    @Override
    public NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session) {
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "404 not found");
    }
}
