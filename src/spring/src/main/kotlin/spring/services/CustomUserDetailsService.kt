package spring.services

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import spring.repositories.UserRepository

typealias ApplicationUser = spring.models.domain.User

@Service
class CustomUserDetailsService(
  private val userRepository: UserRepository
) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails =
    userRepository.findByEmail(username)
      ?.mapToUserDetails()
      ?: throw UsernameNotFoundException("Not found!")

  private fun ApplicationUser.mapToUserDetails(): UserDetails =
    User.builder()
      .username(this.email)
      .password(this.password)
      .roles(this.role.name)
      .build()
}