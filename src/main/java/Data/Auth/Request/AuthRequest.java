package Data.Auth.Request;

import Data.Operation.RegisterCommand;
import DataValidator.Data.Rules.MinLength;
import DataValidator.Data.Rules.Required;
import DataValidator.Data.Rules.Rule;
import DataValidator.Data.Core.Data;

import java.util.List;
import java.util.Map;

public record AuthRequest(String login, String password) implements Data<RegisterCommand> {
    @Override
    public Map<String, List<Rule<?>>> rules() {
        return Map.of(
                "login", List.of(Required.of(), MinLength.of(8)),
                "password", List.of(Required.of(), MinLength.of(8))
        );
    }

    @Override
    public RegisterCommand mapToOperation() {
        return RegisterCommand.of(login, password);
    }


    public static AuthRequest of(String login, String password) {
        return new AuthRequest(login, password);
    }
}
