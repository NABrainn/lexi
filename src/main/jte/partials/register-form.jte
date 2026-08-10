@import DataValidator.Data.Validation.ValidationErrors

@param ValidationErrors errors

<form
    fx-method="POST"
    fx-action="/auth/register"
>
    <fieldset>
        <legend>Register</legend>
        <label for="login">Login</label>
        <input type="text" name="login" id="login">
        <span>
            @if(errors != null)
                !{var messages = errors.getMessages("login");}
                @if(!messages.isEmpty())
                    ${messages.getLast()}
                @endif
            @endif
        </span>
        <label for="password">Password</label>
        <input type="password" name="password" id="password">
        <span>
            @if(errors != null)
                !{var messages = errors.getMessages("password");}
                @if(!messages.isEmpty())
                    ${messages.getLast()}
                @endif
            @endif
        </span>
        <button type="submit">Submit</button>
    </fieldset>
</form>