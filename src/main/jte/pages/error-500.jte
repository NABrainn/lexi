@import Data.Auth.Result.Value.User

@param User user = null

@template.layout.page(
    user = user,
    content = @`
        <div class="container flex flex-col items-center justify-center min-h-screen">
            <section class="card container flex flex-col gap-4 text-center">
                <h1 class="title text-error">500</h1>
                <span>Internal Server Error</span>
            </section>
        </div>
    `
)
