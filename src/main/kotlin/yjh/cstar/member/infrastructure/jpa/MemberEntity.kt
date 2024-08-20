package yjh.cstar.member.infrastructure.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import yjh.cstar.member.domain.Member
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Table(name = "member")
@Entity
class MemberEntity(
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "email", nullable = false)
    private val email: String,

    @Column(name = "password", nullable = false)
    private val password: String,

    @Column(name = "nickname", nullable = false)
    private val nickname: String,

    @CreatedDate
    @Column(name = "created_at")
    private var createdAt: LocalDateTime?,

    @LastModifiedDate
    @Column(name = "updated_at")
    private var updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    private val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun from(member: Member): MemberEntity {
            return MemberEntity(
                id = member.id,
                email = member.email,
                password = member.password,
                nickname = member.nickname,
                createdAt = member.createdAt,
                updatedAt = member.updatedAt,
                deletedAt = member.deletedAt
            )
        }
    }

    fun toModel(): Member {
        return Member(
            id = this.id,
            email = this.email,
            password = this.password,
            nickname = this.nickname,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }
}
