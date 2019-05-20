package com.github.team6083.overlookingAdmin.web;

import com.github.team6083.overlookingAdmin.OverAdminServer;
import com.github.team6083.overlookingAdmin.util.router.RouteHandler;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static fi.iki.elonen.NanoHTTPD.getMimeTypeForFile;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class StaticFileServer implements RouteHandler {

    private String basePath;
    private Class mainClass;

    public StaticFileServer(String basePath, Class mainClass) {
        this.basePath = basePath;
        this.mainClass = mainClass;
    }

    @Override
    public NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();

        return getResponseOfUri(mainClass, basePath, uri);
    }

    public static NanoHTTPD.Response getResponseOfUri(Class mainClass, String basePath, String uri) {
        InputStream in = OverAdminServer.class.getResourceAsStream(basePath + uri);
        NanoHTTPD.Response r;

        if (in == null) {
            return Template.getNotFoundResponse();
        }

        byte[] fileReadIn = new byte[0];
        try {
            fileReadIn = IOUtils.toByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        r = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, getMimeTypeForFile(uri), new String(fileReadIn, StandardCharsets.UTF_8));

        return r;
    }
}
