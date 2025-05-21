package xyz.delartigue.vw.service.domain

import java.util.UUID

data class ConsentEvent (
    val user: UserEvent,
    val consents: List<Consent>
)

data class UserEvent (
    val id: UUID
)