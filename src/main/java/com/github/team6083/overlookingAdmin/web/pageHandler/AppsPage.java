package com.github.team6083.overlookingAdmin.web.pageHandler;

import com.github.team6083.overlookingAdmin.util.router.RouteHandler;
import com.github.team6083.overlookingAdmin.web.TemplateRenderer;
import fi.iki.elonen.NanoHTTPD;
import org.jtwig.JtwigModel;

public class AppsPage implements RouteHandler {
    @Override
    public NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session) {
        return TemplateRenderer.handle("apps.html", new JtwigModel());
    }
}
