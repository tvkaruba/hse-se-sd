package com.api.models.errors

class AuthenticationException: Exception(){
    override val message: String
        get() = "You are not authorized to access this resource."
}