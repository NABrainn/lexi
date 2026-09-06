@param Data.Auth.Result.Success.User user = null

@template.layout.page(
    user = user,
    content = @`
        @template.partials.create-lesson-form()
    `
)