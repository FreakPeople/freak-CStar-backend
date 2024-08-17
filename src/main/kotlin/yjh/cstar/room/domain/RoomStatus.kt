package yjh.cstar.room.domain

enum class RoomStatus(
    val description: String,
) {
    WAITING("대기중"),
    IN_PROGRESS("진행중"),
}
