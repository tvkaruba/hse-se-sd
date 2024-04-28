package spring.models.responses

data class AuthenticationResponse(
  val accessToken: String,
  val refreshToken: String,
)