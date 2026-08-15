@import Data.Validation.ValidationErrors

@param String login = null
@param ValidationErrors errors = null
@param String serviceError = null
<form
    hx-post="/auth/register"
    id="register-form"
>
    <fieldset>
        <legend>
            <h1>Register</h1>
        </legend>
        <div class="field">
            <label for="login">Login</label>
            <input
                autocomplete="off"
                type="text"
                name="login"
                id="login"
                class="input primary"
                value="${login}"
            >
            <div class="error-box">
                @if(errors != null)
                    !{var messages = errors.getMessages("login");}
                    @if(!messages.isEmpty())
                        <span class="error">
                        ${messages.getLast()}
                    </span>
                    @endif
                @endif
            </div>
        </div>
        <div class="field">
            <label for="password">Password</label>
            <input
                autocomplete="new-password"
                type="password"
                name="password"
                id="password"
                class="input primary"
            >
            <div class="error-box">
                @if(errors != null)
                    !{var messages = errors.getMessages("password");}
                    @if(!messages.isEmpty())
                        <span class="error">
                            ${messages.getLast()}
                        </span>
                    @endif
                @elseif(serviceError != null)
                    <span class="error">
                        ${serviceError}
                    </span>
                @endif
            </div>
        </div>
        <button
            class="btn primary"
            type="submit"
        >Submit</button>
        <div class="links">
            <a
                class="link"
                hx-get="/auth/login-form"
                hx-target="#register-form"
            >Login instead</a>
        </div>
    </fieldset>
</form>
