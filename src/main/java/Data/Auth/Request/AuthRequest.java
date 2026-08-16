package Data.Auth.Request;

public record AuthRequest(String login, String password) {

    public static AuthRequest of(String login, String password) {
        return new AuthRequest(login, password);
    }
}
