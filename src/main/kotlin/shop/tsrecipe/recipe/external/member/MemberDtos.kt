package shop.tsrecipe.recipe.external.member

data class MemberResponse(
    val id: String,
    val oauthProvider: OAuthProvider,
    val oauthId: String,
    val nickname: String,
    val isVerified: Boolean
) {
    enum class OAuthProvider {
        GOOGLE,
        APPLE
    }
}