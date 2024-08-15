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
) : Serializable

@EntityListeners(AuditingEntityListener::class)
@Table(name = "game_quiz")
@Entity
class GameQuizEntity(
    @EmbeddedId
    val id: GameQuizId,
)
