package yjh.cstar.quiz.infrastructure.jpa

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable

@Embeddable
class GameQuizId(
    val gameId: Long = 0,
    val quizId: Long = 0,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameQuizId

        if (gameId != other.gameId) return false
        if (quizId != other.quizId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = gameId.hashCode()
        result = 31 * result + quizId.hashCode()
        return result
    }
}

@EntityListeners(AuditingEntityListener::class)
@Table(name = "game_quiz")
@Entity
class GameQuizEntity(
    @EmbeddedId
    val id: GameQuizId,
)
