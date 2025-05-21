package xyz.delartigue.vw.controller.dto

import xyz.delartigue.vw.service.domain.ConsentEvent
import xyz.delartigue.vw.service.domain.UserEvent
import java.util.UUID

data class ConsentRequest (
    val user: UserRequest,
    val consents: List<ConsentDto>
)

fun ConsentRequest.toEvent() = ConsentEvent(
    user = UserEvent(user.id),
    consents = consents.map { it.toDomain() }
)

data class UserRequest (
    val id: UUID
)