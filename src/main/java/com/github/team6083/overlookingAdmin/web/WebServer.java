package com.github.team6083.overlookingAdmin.web;

import com.github.team6083.overlookingAdmin.OverAdminServer;
import com.github.team6083.overlookingAdmin.util.router.UriRouter;
import com.github.team6083.overlookingAdmin.web.hook.HookServer;
import com.github.team6083.overlookingAdmin.web.pageHandler.IndexPage;
import com.github.team6083.overlookingAdmin.web.pageHandler.UsersPage;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.*;

public class WebServer extends NanoHTTPD {
    private boolean quiet;
    private boolean detail;
    private static final int authFailTime = 2;
    private Map<String, Integer> authFailCount = new HashMap<>();

    private HookServer hookServer;
    private UriRouter router = new UriRouter();
    private Map<String, String> uriRewriteMap = new HashMap<>();

    private StaticFileServer assetsHandler;

    public WebServer(int port) throws IOException {
        super(port);
        quiet = false;
        detail = false;
        hookServer = new HookServer();
        assetsHandler = new StaticFileServer("/web", OverAdminServer.class);
        init();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    private void init() {
        router.add("/login", new FileHandler("/login.html"), Method.GET, UriRouter.HandlerAccessLevel.PublicAccess);

        router.add("/users", new UsersPage(), Method.GET, UriRouter.HandlerAccessLevel.PublicAccess);
        router.add("/index", new FileHandler("/index.html"), Method.GET, UriRouter.HandlerAccessLevel.PrivateAccess);
        router.add("/apps", new FileHandler("/Apps.html"), Method.GET, UriRouter.HandlerAccessLevel.PublicAccess);


        uriRewriteMap.put("/", "/index");
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

        if (uri.contains("/errHtml/")) {
            // 403
            return Template.getForbiddenResponse();
        }
        // serve special uri

        if (uriRewriteMap.containsKey(uri)) {
            uri = uriRewriteMap.get(uri);
            System.out.println(uri);
        }
        // handle uri rewrite

        if (uri.contains("/assets/")) {
            return assetsHandler.handle(session);
        }
        // handle assets

        UriRouter.Handler routeHandler = router.get(uri);

        // handle 404 not found
        if (routeHandler == null) {
            return Template.getNotFoundResponse();
        }

        String authToken;
        if (auth == null) {
            authToken = "";
        } else {
            authToken = auth.split(" ")[1];
        }
        // handle authToken

        if (routeHandler.method != session.getMethod()) {
            return Template.getMethodNotAllowedResponse();
        }
        // check http method

        if (routeHandler.accessControl == UriRouter.HandlerAccessLevel.PrivateAccess && !authToken.equals(new String(Base64.getEncoder().encode("root:root".getBytes())))) {
            Response r;
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

            return r;
        } else if (routeHandler.accessControl == UriRouter.HandlerAccessLevel.AuthAccess) {
            // handle auth access
        }

        return routeHandler.routeHandler.handle(session);
    }
}
