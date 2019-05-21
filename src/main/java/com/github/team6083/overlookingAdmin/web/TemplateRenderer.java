package com.github.team6083.overlookingAdmin.web;

import com.github.team6083.overlookingAdmin.util.router.RouteHandler;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.IOUtils;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static fi.iki.elonen.NanoHTTPD.*;

public class TemplateRenderer {
    public static NanoHTTPD.Response handle(String filePath, JtwigModel model) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("web/" + filePath);
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_HTML, template.render(model));
    }
}
