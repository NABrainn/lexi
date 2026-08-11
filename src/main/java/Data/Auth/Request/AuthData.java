package Data.Auth.Request;

import Data.Operation.AuthCommand;
import DataValidator.Data.Core.Operation;
import DataValidator.Data.Rules.MinLength;
import DataValidator.Data.Rules.Required;
import DataValidator.Data.Rules.Rule;
import DataValidator.Data.Core.Data;

import java.util.List;
import java.util.Map;

public record AuthData(String login, String password) implements Data {
    @Override
    public Map<String, List<Rule<?>>> rules() {
        return Map.of(
                "login", List.of(Required.of(), MinLength.of(8)),
                "password", List.of(Required.of(), MinLength.of(8))
        );
    }

    @Override
    public Operation mapToOperation() {
        return AuthCommand.of(login, password);
    }

    public static AuthData of(String login, String password) {
        return new AuthData(login, password);
    }
}
