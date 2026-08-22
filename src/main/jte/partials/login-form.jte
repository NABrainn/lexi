@import Helpers.FormErrors

@param String login = null
@param FormErrors errors = Helpers.FormErrors.empty()

<form
    class="container flex flex-col"
    id="login-form"
    hx-post="/auth/login"
>
    <fieldset class="container flex flex-col gap-4">
        <legend class="size-3">
            <h1>Login</h1>
        </legend>
        <div class="container flex flex-col gap-2">
            <label for="login">Login</label>
            <input
                type="text"
                name="login"
                id="login"
                class="input primary"
                value="${login}"
            >
            <div class="error-container">
                @if(errors.has("login"))
                    <span class="error-message">
                        ${errors.field("login")}
                    </span>
                @endif
            </div>
        </div>

        <div class="container flex flex-col gap-2">
            <label for="password">Password</label>
            <input
                type="password"
                name="password"
                id="password"
                class="input primary"
            >

            <div class="error-container">
                @if(errors.has("password"))
                    <span class="error-message">
                        ${errors.field("password")}
                    </span>
                @elseif(errors.global() != null)
                    <span class="error-message">
                        ${errors.global()}
                    </span>
                @endif
            </div>
        </div>

        <button
            class="btn primary"
            type="submit"
        >Submit</button>
        <div class="actions">
            <a
                class="link"
                hx-get="/auth/register-form"
                hx-target="#login-form"
            >Create account</a>
        </div>
    </fieldset>
</form>
