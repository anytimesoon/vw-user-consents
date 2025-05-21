package xyz.delartigue.vw.repository

import org.springframework.data.repository.CrudRepository
import xyz.delartigue.vw.repository.entity.ConsentEntity
import xyz.delartigue.vw.repository.entity.UserEntity
import xyz.delartigue.vw.type.ConsentType
import java.lang.Long
import java.util.Optional

interface ConsentRepository : CrudRepository<ConsentEntity, Long> {
    fun findByUserAndConsent(user: UserEntity, consent: ConsentType): Optional<ConsentEntity>
}
