@import Helpers.FormErrors

@param String login = null
@param FormErrors errors = Helpers.FormErrors.empty()

<form
    id="login-form"
    hx-post="/auth/login"
>
    <fieldset>
        <legend>
            <h1>Login</h1>
        </legend>

        <div class="field">
            <label for="login">Login</label>
            <input
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
                hx-get="/auth/register-form"
                hx-target="#login-form"
            >Create account</a>
        </div>
    </fieldset>
</form>