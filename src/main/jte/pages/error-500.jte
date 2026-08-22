@import Data.Auth.Result.Value.User

@param User user = null

@template.layout.page(
    user = user,
    content = @`
        Upss, coś poszło nie tak.
    `
)