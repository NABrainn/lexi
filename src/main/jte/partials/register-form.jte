@import Helpers.FormErrors

@param String login = null
@param FormErrors errors = Helpers.FormErrors.empty()

<form
    class="container flex flex-col w-full max-w-content-2 mx-auto my-8 p-5"
    hx-post="/auth/register"
    id="register-form"
>
    <fieldset class="container flex flex-col gap-4">
        <legend class="size-3">
            <h1>Register</h1>
        </legend>

        <div class="container flex flex-col gap-2">
            <label for="login">Login</label>
            <input
                autocomplete="off"
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
                autocomplete="new-password"
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
                hx-get="/auth/login-form"
                hx-target="#register-form"
            >Login instead</a>
        </div>
    </fieldset>
</form>
