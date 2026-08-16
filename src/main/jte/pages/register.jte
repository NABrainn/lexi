@import Helpers.FormErrors
@import java.util.List

@param List<String> styles = null
@param FormErrors errors = Helpers.FormErrors.empty()

@template.layout.page(
    styleSheets = styles,
    content = @`
        @template.partials.register-form(
        errors = errors == null
            ? Helpers.FormErrors.empty()
            : errors
        )
    `
)
