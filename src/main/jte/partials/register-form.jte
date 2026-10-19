@import Helpers.FormErrors

@param String login = null
@param FormErrors errors = Helpers.FormErrors.empty()

<form id="register-form" class="padding" data-on:submit="@post('/auth/register', { contentType: 'form' })">
    <input type="hidden" name="pathname" data-bind:pathname />
    <fieldset class="surface-container-highest">
        <legend><h4>Register</h4></legend>
        <nav class="row">
            @if(errors.has("login"))
                <div class="field invalid border max">
                    <label for="login">Login</label>
                    <input
                        autocomplete="off"
                        type="text"
                        name="login"
                        id="login"
                        value="${login}"
                    />
                    <output class="invalid">${errors.field("login")}</output>
                </div>
            @else
                <div class="field border max">
                    <label for="login">Login</label>
                    <input
                        autocomplete="off"
                        type="text"
                        name="login"
                        id="login"
                        value="${login}"
                    />
                </div>
            @endif
        </nav>

        <nav class="row">
            @if(errors.has("password"))
                <div class="field invalid border max">
                    <label for="password">Password</label>
                    <input
                        autocomplete="new-password"
                        type="password"
                        name="password"
                        id="password"
                    >
                    <output class="invalid">${errors.field("password")}</output>
                </div>
            @else
                <div class="field border max">
                    <label for="password">Password</label>
                    <input
                        autocomplete="new-password"
                        type="password"
                        name="password"
                        id="password"
                    >
                </div>
            @endif
        </nav>
        <nav>
        @if(errors.global() != null)
            <span class="error-text">${errors.global()}</span>
        @endif
        </nav>
        <nav>
            <button>Submit</button>
        </nav>
        <nav>
            <a class="link" href="/login">Login instead</a>
        </nav>
    </fieldset>
</form>
