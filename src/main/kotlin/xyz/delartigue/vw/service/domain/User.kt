package xyz.delartigue.vw.service.domain

import xyz.delartigue.vw.repository.entity.UserEntity
import java.util.UUID

data class User (
    val id: UUID?,
    val email: String,
    val consents: List<Consent>
)

fun User.toEntity(): UserEntity {
    val userEntity = UserEntity(
        id ?: UUID.randomUUID(),
        email,
        emptyList()
    )

    val consentEntities = consents.map { it.toEntity(userEntity) }

    return UserEntity(
        userEntity.id,
        userEntity.email,
        consentEntities
    )
}

fun UserEntity.toDomain() = User(
    id,
    email,
    consents.map { it.toDomain() }
)
