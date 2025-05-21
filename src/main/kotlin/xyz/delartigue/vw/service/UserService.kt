package xyz.delartigue.vw.service

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import xyz.delartigue.vw.exception.DuplicateEmailException
import xyz.delartigue.vw.exception.MissingUserIdException
import xyz.delartigue.vw.repository.UserRepository
import xyz.delartigue.vw.service.domain.User
import xyz.delartigue.vw.service.domain.toDomain
import xyz.delartigue.vw.service.domain.toEntity
import java.util.UUID

@Service
class UserService (
    private val userRepository: UserRepository
){
    fun getUsers() = userRepository.findAll().map { it.toDomain() }

    fun getUserById(id: UUID): User? = userRepository.findById(id).map { it.toDomain() }.orElse(null)

    fun createUser(user: User): User {
        try {
            return userRepository.save(user.toEntity()).toDomain()
        } catch (e: DataIntegrityViolationException) {
            if (e.message?.contains("email") == true) {
                throw DuplicateEmailException(user.email)
            }
            throw e
        }
    }

    fun updateUser(user: User): User {
        val userId = user.id ?: throw MissingUserIdException()
        val userWithConsents = userRepository.findById(userId).orElseThrow().toDomain()

        try {
            return userRepository.save(userWithConsents.copy(email = user.email).toEntity()).toDomain()
        } catch (e: DataIntegrityViolationException) {
            if (e.message?.contains("email") == true) {
                throw DuplicateEmailException(user.email)
            }
            throw e
        }
    }

    fun deleteUser(id: UUID) {
        return userRepository.deleteById(id)
    }
}
