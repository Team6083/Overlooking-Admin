package com.github.team6083.overlookingAdmin.web;

import com.github.team6083.overlookingAdmin.OverAdminServer;
import com.github.team6083.overlookingAdmin.util.router.StringRouter;
import com.github.team6083.overlookingAdmin.web.hook.HookServer;
import com.github.team6083.overlookingAdmin.web.pageHandler.UsersPage;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WebServer extends NanoHTTPD {
    private boolean quiet;
    private boolean detail;
    private static final int authFailTime = 2;
    private Map<String, Integer> authFailCount = new HashMap<String, Integer>();
    private HookServer hookServer;
    private StringRouter router = new StringRouter();

    public WebServer(int port) throws IOException {
        super(port);
        quiet = false;
        detail = false;
        hookServer = new HookServer();
        setRoute();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    private void setRoute() {
        router.add("/users", new UsersPage());
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> header = session.getHeaders();
        Map<String, List<String>> parms = session.getParameters();
        String uri = session.getUri();
        String auth = header.get("authorization");

        Iterator e;
        if (!this.quiet) {
            System.out.println(session.getMethod() + " '" + uri + "'  " + "authorization: " + auth);
            if (this.detail) {
                e = header.keySet().iterator();

                String value;
                while (e.hasNext()) {
                    value = (String) e.next();
                    System.out.println("  HDR: '" + value + "' = '" + (String) header.get(value) + "'");
                }

                e = parms.keySet().iterator();

                while (e.hasNext()) {
                    value = (String) e.next();
                    System.out.println("  PRM: '" + value + "' = '" + (String) parms.get(value).get(0) + "'");
                }
            }
        }

        if (uri.contains("/hook/")) {
            try {
                return hookServer.handle(session);
            } catch (IOException ioe) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (NoSuchMethodException nsme) {
                nsme.printStackTrace();
            } catch (ResponseException re) {
                return newFixedLengthResponse(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
            }
        }

        if (uri.contains("/auth/")) {
            //TODO add auth things
        }

        if (uri.equals("/")) {
            // index
            uri = "/index.html";
        } else if (uri.contains("/errHtml/")) {
            // 403
            return Template.getForbiddenResponse();
        }
        // serve special uri

        if (!session.getMethod().equals(Method.GET)) {
            return Template.getMethodNotAllowedResponse();
        }
        // 405 Method Not Allowed

        InputStream in = OverAdminServer.class.getResourceAsStream("/web" + uri);
        Response r = null;

        String authToken;
        if (auth == null) {
            authToken = "";
        } else {
            authToken = auth.split(" ")[1];
        }
        // handle authToken

        byte[] fileReadIn;
        if (in == null) {
            // File not found, check router
            if(router.checkUri(uri)){
                r = router.getHandler(uri).handle(session);
            } else{
                r = Template.getNotFoundResponse();
            }
        } else if (uri.contains("/assets/")) {
            // Serve resource files
            try {
                fileReadIn = IOUtils.toByteArray(in);
                r = newFixedLengthResponse(Response.Status.OK, getMimeTypeForFile(uri), new String(fileReadIn, StandardCharsets.UTF_8));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (!uri.contains("/server/") || authToken.equals(new String(Base64.getEncoder().encode("root:root".getBytes())))) {
            // Auth passed or not required
            try {
                fileReadIn = IOUtils.toByteArray(in);
                r = newFixedLengthResponse(Response.Status.OK, MIME_HTML, new String(fileReadIn, StandardCharsets.UTF_8));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            // No auth return 401 error
            String remoteAddress = header.get("remote-addr");
            r = Template.getUnauthorizedResponse();

            if (!authFailCount.containsKey(remoteAddress)) {
                authFailCount.put(remoteAddress, 0);
            }

            if (authFailCount.get(remoteAddress) < authFailTime) {
                authFailCount.put(remoteAddress, authFailCount.get(remoteAddress) + 1);
                r.addHeader("WWW-Authenticate", "Basic");
            } else {
                authFailCount.put(remoteAddress, -authFailTime);
            }
        }

        return r;
    }
}
