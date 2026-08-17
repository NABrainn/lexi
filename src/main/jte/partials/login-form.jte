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

        <div>
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
                    <span>
                        ${errors.field("login")}
                    </span>
                @endif
            </div>
        </div>

        <div>
            <label for="password">Password</label>
            <input
                type="password"
                name="password"
                id="password"
                class="input primary"
            >

            <div class="error-box">
                @if(errors.has("password"))
                    <span>
                        ${errors.field("password")}
                    </span>
                @elseif(errors.global() != null)
                    <span>
                        ${errors.global()}
                    </span>
                @endif
            </div>
        </div>

        <button
            class="btn primary"
            type="submit"
        >Submit</button>

        <div>
            <a
                class="link"
                hx-get="/auth/register-form"
                hx-target="#login-form"
            >Create account</a>
        </div>
    </fieldset>
</form>
