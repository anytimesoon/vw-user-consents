package xyz.delartigue.vw.service

import org.springframework.stereotype.Service
import xyz.delartigue.vw.repository.ConsentRepository
import xyz.delartigue.vw.repository.UserRepository
import xyz.delartigue.vw.service.domain.Consent
import xyz.delartigue.vw.service.domain.ConsentEvent
import xyz.delartigue.vw.service.domain.toDomain
import xyz.delartigue.vw.service.domain.toEntity

@Service
class ConsentService (
    private val consentRepository: ConsentRepository,
    private val userRepository: UserRepository,
    private val consentProducerService: ConsentProducerService
){
    fun createConsent(event: ConsentEvent) {
        val user = userRepository.findById(event.user.id).orElseThrow().toDomain()
        val savedConsents = mutableListOf<Consent>()

        event.consents.forEach { newConsent ->
            val existingConsent = consentRepository.findByUserAndConsent(user.toEntity(), newConsent.consent)
                .map { it.toDomain() }
                .orElse(null)

            val consent = existingConsent?.copy(enabled = newConsent.enabled) ?: newConsent

            val savedConsent = consentRepository.save(consent.toEntity(user.toEntity())).toDomain()
            savedConsents.add(savedConsent)
        }

        consentProducerService.send(
            event.copy(consents = savedConsents)
        )
    }
}
