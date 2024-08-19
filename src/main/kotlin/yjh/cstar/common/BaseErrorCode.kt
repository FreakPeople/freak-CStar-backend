package yjh.cstar.common

import org.springframework.http.HttpStatus
import yjh.cstar.room.domain.Validator.Companion.MAX_CAPACITY
import yjh.cstar.room.domain.Validator.Companion.MAX_NICKNAME
import yjh.cstar.room.domain.Validator.Companion.MAX_PASSWORD
import yjh.cstar.room.domain.Validator.Companion.MIN_CAPACITY
import yjh.cstar.room.domain.Validator.Companion.MIN_NICKNAME
import yjh.cstar.room.domain.Validator.Companion.MIN_PASSWORD

enum class BaseErrorCode(
    val httpStatus: HttpStatus,
    val code: Int,
    val message: String,
) {
    // 200
    SUCCESS(HttpStatus.OK, 2000, "요청 성공"),

    // 400 : maxCapacity field
    MAX_CAPACITY_OUT_OF_RANGE(
        HttpStatus.BAD_REQUEST,
        4001,
        "방 최대 수용가능 인원수는 $MIN_CAPACITY 이상 $MAX_CAPACITY 이하 여야 합니다."
    ),
    PASSWORD_OUT_OF_LENGTH(
        HttpStatus.BAD_REQUEST,
        4002,
        "비밀번호 길이는 $MIN_PASSWORD 이상 $MAX_PASSWORD 이하여야 합니다."
    ),
    NICKNAME_OUT_OF_LENGTH(
        HttpStatus.BAD_REQUEST,
        4003,
        "닉네임의 길이는 $MIN_NICKNAME 이상 $MAX_NICKNAME 이하여야 합니다."
    ),

    // 404
    NOT_FOUND_ROOM(HttpStatus.NOT_FOUND, 4041, "게임방을 찾을 수 없습니다"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "서버 내부 에러"),
}
