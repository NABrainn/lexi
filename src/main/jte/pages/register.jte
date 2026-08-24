@import Data.Auth.Result.Value.User
@import Helpers.FormErrors
@import java.util.List

@param User user = null
@param List<String> styles = null
@param FormErrors errors = Helpers.FormErrors.empty()

@template.layout.page(
    user = user,
    styleSheets = styles,
    content = @`
        @template.partials.register-form(
        errors = errors == null
            ? Helpers.FormErrors.empty()
            : errors
        )
    `
)
