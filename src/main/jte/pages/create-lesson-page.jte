@param Data.Auth.Result.Value.User user = null

@template.layout.page(
    user = user,
    content = @`
        @template.partials.create-lesson-form()
    `
)