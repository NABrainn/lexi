package Helpers;

import Data.Auth.Result.Value.User;
import Data.Session.TokenKind;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;

import java.time.Instant;
import java.util.Optional;

public class Session {

    private static final long SESSION_EXPIRED_DURATION = 1000 * 60 * 60 * 24;
    private static final long REFRESH_TOKEN_EXPIRED_DURATION = (1000 * 60 * 60) / 2;

    public static void authenticate(Context ctx, User user) {
        var now = Instant.now();
        ctx.sessionAttribute("userId", user.id());
        ctx.sessionAttribute("username", user.username());
        ctx.sessionAttribute("sessionExpiredAt", now.plusMillis(SESSION_EXPIRED_DURATION));
        ctx.sessionAttribute("refreshTokenExpiredAt", now.plusMillis(REFRESH_TOKEN_EXPIRED_DURATION));
    }

    public static boolean isAuthenticated(Context ctx) {
        var session = getSession(ctx);
        return session != null && validDuration(ctx, TokenKind.SESSION).isPresent() && validDuration(ctx, TokenKind.REFRESH).isPresent();
    }

    public static void logout(Context ctx) {
        var session = getSession(ctx);

        if (session != null) {
            session.invalidate();
        }
    }

    public static boolean tryToRefresh(Context ctx) {
        var session = getSession(ctx);

        if(session != null) {
            var optionalSessionTokenDuration = validDuration(ctx, TokenKind.SESSION);
            var optionalRefreshTokenDuration = validDuration(ctx, TokenKind.REFRESH);

            if(optionalSessionTokenDuration.isEmpty()) {
                session.invalidate();
                return false;
            }

            if(optionalRefreshTokenDuration.isEmpty()) {
                session.invalidate();
                return false;
            }

            var refreshTokenExpiredAt = optionalRefreshTokenDuration.get();
            ctx.sessionAttribute("refreshTokenExpiredAt", refreshTokenExpiredAt.plusMillis(REFRESH_TOKEN_EXPIRED_DURATION));
            return true;
        }

        return false;
    }

    private static Optional<Instant> validDuration(Context ctx, TokenKind kind) {
        var session = getSession(ctx);

        if(session != null) {
            var key = kind == TokenKind.SESSION
                    ? "sessionExpiredAt"
                    : "refreshTokenExpiredAt";
            var optionalExpiredAt = ctx.sessionAttribute(key);

            if(optionalExpiredAt == null) {
                return Optional.empty();
            }

            var expiredAt = (Instant) optionalExpiredAt;

            if(Instant.now().isAfter(expiredAt)) {
                return Optional.empty();
            }

            return Optional.of(expiredAt);
        }

        return Optional.empty();
    }

    private static HttpSession getSession(Context ctx) {
        return ctx.req().getSession(false);
    }
}
