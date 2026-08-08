package Data.Auth;

import Data.Core.Data;
import Data.Rules.Required;
import Data.Rules.Rule;

import java.util.Map;

public record AuthRequest(String login, String password) implements Data {
    @Override
    public Map<String, Rule<?>> rules() {
        return Map.of(
                "login", Required.of(),
                "password", Required.of()
        );
    }

    public static AuthRequest of(String login, String password) {
        return new AuthRequest(login, password);
    }
}
