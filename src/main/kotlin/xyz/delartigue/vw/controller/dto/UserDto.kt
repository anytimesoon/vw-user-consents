package xyz.delartigue.vw.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import xyz.delartigue.vw.service.domain.User
import java.util.UUID

data class UserDto (
    val id: UUID?,
    @field:Email(message = "Invalid email address", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @field:NotEmpty(message = "Email must not be empty")
    val email: String,
    val consents: List<ConsentDto>?
)

fun UserDto.toDomain() = User(
    id,
    email,
    consents?.map { it.toDomain() } ?: emptyList()
)

fun User.toDto() = UserDto(
    id,
    email,
    consents.map { it.toDto() }
)