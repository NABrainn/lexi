@import Data.Auth.Result.Value.User
@import Helpers.FormErrors
@import Helpers.Session
@import java.util.List

@param User user = null
@param List<String> styles = null
@param FormErrors errors = Helpers.FormErrors.empty()

@template.layout.page(
    user = user,
    styleSheets = styles,
    content = @`
        @template.partials.login-form(
            errors = errors == null
                ? Helpers.FormErrors.empty()
                : errors
        )
    `
)
