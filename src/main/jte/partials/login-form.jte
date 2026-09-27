@import Helpers.FormErrors

@param String login = null
@param FormErrors errors = Helpers.FormErrors.empty()

<div
    id="login-form"
    class="form-container container flex flex-col p-5 gap-4 w-1/5 mx-auto mt-8"
>
    <form
        class="container flex flex-col"
        data-on:submit="@post('/auth/login', { contentType: 'form' })">
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

            @template.components.button(text = "Submit", variant = "primary")
        </fieldset>
    </form>
    <div class="actions">
        @template.components.button(href= "/register", text = "Create account", kind = "basic", variant = "primary")
    </div>
</div>
