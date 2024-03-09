package ktor.sample.services

import User
import ktor.sample.data.UserRepository

class UserService(private val repository: UserRepository) {
    suspend fun create(user: User): User {
        return repository.create(user) ?: throw Exception();
    }

    suspend fun readAll(): List<User> {
        return repository.getAll(1, 10);
    }
}