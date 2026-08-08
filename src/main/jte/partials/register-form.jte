<form
    fx-method="POST"
    fx-action="/auth/register"
>
    <fieldset>
        <label for="login">Login</label>
        <input type="text" name="login" id="login">
        <label for="password">Password</label>
        <input type="password" name="password" id="password">
        <button type="submit">Register</button>
    </fieldset>
</form>