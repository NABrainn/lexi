@import Helpers.FormErrors

@param String login = null
@param FormErrors errors = Helpers.FormErrors.empty()

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
                @if(errors.has("login"))
                    <span class="error">
                        ${errors.field("login")}
                    </span>
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
                @if(errors.has("password"))
                    <span class="error">
                        ${errors.field("password")}
                    </span>
                @elseif(errors.global() != null)
                    <span class="error">
                        ${errors.global()}
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