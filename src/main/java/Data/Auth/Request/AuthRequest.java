package Data.Auth.Request;

import DataValidator.Data.Rules.Required;
import DataValidator.Data.Rules.Rule;
import DataValidator.Data.Core.Data;

import java.util.List;
import java.util.Map;

public record AuthRequest(String login, String password) implements Data {
    @Override
    public Map<String, List<Rule<?>>> rules() {
        return Map.of(
                "login", List.of(Required.of()),
                "password", List.of(Required.of())
        );
    }

    public static AuthRequest of(String login, String password) {
        return new AuthRequest(login, password);
    }
}
