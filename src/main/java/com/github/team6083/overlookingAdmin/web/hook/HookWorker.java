package com.github.team6083.overlookingAdmin.web.hook;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

public interface HookWorker {
    NanoHTTPD.Response serve(String uri, Map<String, String> header, String body, NanoHTTPD.Method method);
}
