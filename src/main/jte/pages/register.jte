@import DataValidator.Data.Validation.ValidationErrors
@import java.util.List

@param List<String> styles = null
@param ValidationErrors errors = null

@template.layout.page(
    styleSheets = styles,
    content = @`
        @template.partials.register-form(
            errors = errors
        )
    `
)
