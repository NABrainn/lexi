package Helpers;

import io.javalin.http.Context;

public class Headers {
    public static boolean isHxRequest(Context ctx) {
        var value = ctx.header("Hx-Request");

        if(value == null) {
            return false;
        }

        return value.equals("true");
    }

    public static void hxRedirect(Context ctx, String location) {
        ctx.header("HX-Redirect", location);
    }
}
