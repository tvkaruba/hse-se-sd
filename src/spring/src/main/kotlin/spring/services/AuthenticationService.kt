package spring.services

import spring.models.requests.AuthenticationRequest
import spring.models.responses.AuthenticationResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import spring.configuration.JwtProperties
import spring.repositories.RefreshTokenRepository
import java.util.*

@Service
class AuthenticationService(
  private val authManager: AuthenticationManager,
  private val userDetailsService: CustomUserDetailsService,
  private val tokenService: TokenService,
  private val jwtProperties: JwtProperties,
  private val refreshTokenRepository: RefreshTokenRepository,
) {

  fun authentication(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
    authManager.authenticate(
      UsernamePasswordAuthenticationToken(
        authenticationRequest.email,
        authenticationRequest.password
      )
    )

    val user = userDetailsService.loadUserByUsername(authenticationRequest.email)

    val accessToken = createAccessToken(user)
    val refreshToken = createRefreshToken(user)

    refreshTokenRepository.save(refreshToken, user)

    return AuthenticationResponse(
      accessToken = accessToken,
      refreshToken = refreshToken
    )
  }

  fun refreshAccessToken(refreshToken: String): String? {
    val extractedEmail = tokenService.extractEmail(refreshToken)

    return extractedEmail?.let { email ->
      val currentUserDetails = userDetailsService.loadUserByUsername(email)
      val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(refreshToken)

      if (!tokenService.isExpired(refreshToken) && refreshTokenUserDetails?.username == currentUserDetails.username)
        createAccessToken(currentUserDetails)
      else
        null
    }
  }

  private fun createAccessToken(user: UserDetails) = tokenService.generate(
    userDetails = user,
    expirationDate = getAccessTokenExpiration()
  )

  private fun createRefreshToken(user: UserDetails) = tokenService.generate(
    userDetails = user,
    expirationDate = getRefreshTokenExpiration()
  )

  private fun getAccessTokenExpiration(): Date =
    Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)

  private fun getRefreshTokenExpiration(): Date =
    Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
}