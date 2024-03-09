package com.api.services

import com.api.models.domain.User
import com.api.persistece.UserRepository
import java.util.*

class UserService(private val userRepository: UserRepository) {
    fun createNewUser(name: String, email: String) : User {
        val user = User(name = name, email = email)
        val newUser = userRepository.create(user)
        return newUser
    }

    fun getAllUsers() : List<User> {
        val users = userRepository.getAll()
        return users
    }

    fun getUserById(id: UUID) : User {
        val user = userRepository.getById(id)
        return user
    }

    fun deleteUserById(id: UUID) {
        userRepository.deleteById(id)
    }
}