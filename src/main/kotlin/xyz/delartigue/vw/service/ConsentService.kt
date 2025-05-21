package xyz.delartigue.vw.service

import org.springframework.stereotype.Service
import xyz.delartigue.vw.repository.ConsentRepository
import xyz.delartigue.vw.repository.UserRepository
import xyz.delartigue.vw.service.domain.ConsentEvent
import xyz.delartigue.vw.service.domain.toDomain
import xyz.delartigue.vw.service.domain.toEntity

@Service
class ConsentService (
    private val consentRepository: ConsentRepository,
    private val userRepository: UserRepository
){
    fun createConsent(event: ConsentEvent) {
        handleConsent(event)
    }

    fun updateConsent(event: ConsentEvent) {
        handleConsent(event)
    }

    private fun handleConsent(event: ConsentEvent) {
        val user = userRepository.findById(event.user.id).orElseThrow().toDomain()

        event.consents.forEach { newConsent ->
            val existingConsent = consentRepository.findByUserAndConsent(user.toEntity(), newConsent.consent)
                .map { it.toDomain() }
                .orElse(null)

            val consent = existingConsent?.copy(enabled = newConsent.enabled) ?: newConsent

            consentRepository.save(consent.toEntity(user.toEntity()))
        }
    }
}
