@import Data.Auth.Result.Success.User

@param User user

@template.layout.page(
    user = user,
    content = @`
        <div class="grid padding">
            <article class="round surface-container-highest">
                <div class="padding">
                    <h5>Title</h5>
                    <p>
                        Some description
                    </p>
                    <nav>
                        <button>Read</button>
                    </nav>
                </div>
            </article>
        </div>
    `
)