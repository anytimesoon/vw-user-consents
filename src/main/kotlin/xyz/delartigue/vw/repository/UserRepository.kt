package xyz.delartigue.vw.repository

import org.springframework.data.repository.CrudRepository
import xyz.delartigue.vw.repository.entity.UserEntity
import java.util.UUID

interface UserRepository : CrudRepository<UserEntity, UUID>
