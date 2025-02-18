package yjh.cstar.member.infrastructure.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import yjh.cstar.member.domain.Friend

@EntityListeners(AuditingEntityListener::class)
@Table(name = "friend")
@Entity
class FriendEntity(
    @Id
    @Column(name = "friend_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "member_one_id", nullable = false)
    private val memberOneId: Long,

    @Column(name = "member_two_id", nullable = false)
    private val memberTwoId: Long,
) {
    companion object {
        fun from(friend: Friend): FriendEntity {
            return FriendEntity(
                id = friend.id,
                memberOneId = friend.memberOneId,
                memberTwoId = friend.memberTwoId
            )
        }
    }

    fun toModel(): Friend {
        return Friend(
            id = this.id,
            memberOneId = this.memberOneId,
            memberTwoId = this.memberTwoId
        )
    }
}
