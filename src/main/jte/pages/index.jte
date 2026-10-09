@import Data.Auth.Result.Success.User

@param User user

@template.layout.page(
    user = user,
    content = @`
        <div
            id="home-content"
            class="container relative min-w-screen"
        >
            @template.components.button(text = "+", href = "/lessons/create", kind = "fab")
        </div>
    `
)