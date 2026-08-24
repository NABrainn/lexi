package Helpers;

import Data.Auth.Result.Value.User;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;

import java.time.Instant;

public class Session {

    private static final long SESSION_DURATION = 1000L * 60 * 60 * 24;

    public static void authenticate(Context ctx, User user) {
        var session = ctx.req().getSession(true);

        ctx.req().changeSessionId();

        session.setAttribute("user", user);
        session.setAttribute("expiredAt", Instant.now().plusMillis(SESSION_DURATION));
    }

    public static User getUser(Context ctx) {
        var session = getSession(ctx);

        if(session == null) {
            return null;
        }

        return (User) session.getAttribute("user");
    }

    public static boolean isAuthenticated(Context ctx) {
        var session = getSession(ctx);

        if (session == null) {
            return false;
        }

        var expiredAt = (Instant) session.getAttribute("expiredAt");

        if (expiredAt == null || Instant.now().isAfter(expiredAt)) {
            session.invalidate();
            return false;
        }

        return true;
    }

    public static void refresh(Context ctx) {
        var session = getSession(ctx);

        if (session != null) {
            session.setAttribute("expiredAt", Instant.now().plusMillis(SESSION_DURATION));
        }
    }

    public static void logout(Context ctx) {
        var session = getSession(ctx);

        if (session != null) {
            session.invalidate();
        }
    }

    private static HttpSession getSession(Context ctx) {
        return ctx.req().getSession(false);
    }
}
