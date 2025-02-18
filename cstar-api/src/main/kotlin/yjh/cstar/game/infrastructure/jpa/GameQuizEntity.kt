package yjh.cstar.game.infrastructure.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@EntityListeners(AuditingEntityListener::class)
@Table(name = "game_quiz_mapping")
@Entity
class GameQuizEntity(
    @Id
    @Column(name = "game_quiz_mapping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "game_id", nullable = false)
    private val gameId: Long,

    @Column(name = "quiz_id", nullable = false)
    private val quizId: Long,
)
