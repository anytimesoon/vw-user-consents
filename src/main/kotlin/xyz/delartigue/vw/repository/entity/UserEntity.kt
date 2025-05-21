package xyz.delartigue.vw.repository.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity (
    @Id
    val id: UUID,
    @Column(unique = true)
    val email: String,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val consents: List<ConsentEntity>
)
