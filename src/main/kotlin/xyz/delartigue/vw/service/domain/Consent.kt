package xyz.delartigue.vw.service.domain

import xyz.delartigue.vw.repository.entity.ConsentEntity
import xyz.delartigue.vw.repository.entity.UserEntity
import xyz.delartigue.vw.type.ConsentType
import java.time.LocalDateTime

data class Consent (
    val consent: ConsentType,
    val enabled: Boolean,
    val updated: LocalDateTime? = null,
    val id: Long? = null
)

fun Consent.toEntity(userEntity: UserEntity) = ConsentEntity(
    id = id,
    user = userEntity,
    consent = consent,
    enabled = enabled,
    updated = LocalDateTime.now()
)

fun ConsentEntity.toDomain() = Consent(
    consent = consent,
    enabled = enabled,
    updated = updated,
    id = id
)
