package Helpers;

import io.javalin.http.Context;

public class Requests {
    public static void requireHx(Context ctx) {
        if(!Headers.isHxRequest(ctx)) {
            ctx.status(400);
        }
    }
}
