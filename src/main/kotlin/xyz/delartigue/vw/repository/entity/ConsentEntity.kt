package xyz.delartigue.vw.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import xyz.delartigue.vw.type.ConsentType
import java.time.LocalDateTime

@Entity
@Table(name = "consent")
class ConsentEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0L,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: UserEntity,
    @Enumerated(EnumType.STRING)
    val consent: ConsentType,
    val enabled: Boolean,
    @Column(name = "updated")
    val updated: LocalDateTime
)
