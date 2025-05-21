package xyz.delartigue.vw.controller.dto

import xyz.delartigue.vw.exception.InvalidConsentException
import xyz.delartigue.vw.service.domain.Consent
import xyz.delartigue.vw.type.ConsentType

data class ConsentDto (
    val id: String,
    val enabled: Boolean
)

fun ConsentDto.toDomain(): Consent {
    val consent = ConsentType.from(id)
    if (consent == null) {
        throw InvalidConsentException(id)
    }

    return Consent(
        consent,
        enabled
    )
}
fun Consent.toDto() = ConsentDto(
    consent.value,
    enabled
)