package com.github.team6083.overlookingAdmin.web;

import com.github.team6083.overlookingAdmin.OverAdminServer;
import com.github.team6083.overlookingAdmin.util.router.RouteHandler;
import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class FileHandler implements RouteHandler {

    String fileName;
    public FileHandler(String fileName){
        this.fileName = fileName;
    }

    @Override
    public NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session) {
        return StaticFileServer.getResponseOfUri(OverAdminServer.class, "/web", fileName);
    }
}
