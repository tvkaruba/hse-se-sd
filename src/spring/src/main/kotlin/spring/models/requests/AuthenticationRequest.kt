package spring.models.requests

data class AuthenticationRequest(
  val email: String,
  val password: String,
)