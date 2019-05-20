package com.github.team6083.overlookingAdmin.util.router;

import fi.iki.elonen.NanoHTTPD;

public interface RouteHandler {
    NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session);
}
