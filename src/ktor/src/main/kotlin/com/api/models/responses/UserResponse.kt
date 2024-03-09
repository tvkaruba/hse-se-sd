package com.api.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val id: String, val name: String, val email: String)