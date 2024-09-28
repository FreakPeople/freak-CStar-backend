package yjh.cstar.common

import org.springframework.http.HttpStatus
import yjh.cstar.member.domain.Validator.Companion.MAX_NICKNAME
import yjh.cstar.member.domain.Validator.Companion.MAX_PASSWORD
import yjh.cstar.member.domain.Validator.Companion.MIN_NICKNAME
import yjh.cstar.member.domain.Validator.Companion.MIN_PASSWORD
import yjh.cstar.room.domain.Validator.Companion.MAX_CAPACITY
import yjh.cstar.room.domain.Validator.Companion.MIN_CAPACITY

/**
 * 커스텀 코드
 * 0 ~ 9 : Room 도메인
 * 10 ~ 19 : Member 도메인
 * 20 ~ 29 : Quiz 도메인
 * 30 ~ 39 : Game 도메인
 * 40 ~ 49 : Play 도메인
 * 50 ~ 59 : Websocket 도메인
 */
enum class BaseErrorCode(
    val httpStatus: HttpStatus,
    val code: Int,
    val message: String,
) {
    SUCCESS(HttpStatus.OK, 200, "요청 성공"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "권한이 없는 접근입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 에러"),

    // Room
    MAX_CAPACITY_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, 4000, "방 최대 수용가능 인원수는 $MIN_CAPACITY 이상 $MAX_CAPACITY 이하 여야 합니다."),
    EMPTY_ANSWER(HttpStatus.BAD_REQUEST, 4001, "답변이 비어 있습니다."),

    NOT_FOUND_ROOM(HttpStatus.NOT_FOUND, 4040, "게임방을 찾을 수 없습니다"),
    INVALID_QUIZ_ID(HttpStatus.NOT_FOUND, 4042, "퀴즈 번호는 양수여야 합니다."),

    NOT_IN_WAITING_STATUS(HttpStatus.CONFLICT, 4090, "게임 방이 현재 대기 상태가 아닙니다."),
    CAPACITY_EXCEEDED(HttpStatus.CONFLICT, 4091, "게임 방의 수용 가능 인원이 초과되었습니다."),
    CAPACITY_CANNOT_BE_NEGATIVE(HttpStatus.CONFLICT, 4092, "게임 방 수용 인원은 음수가 될 수 없습니다."),

    // Member
    PASSWORD_OUT_OF_LENGTH(HttpStatus.BAD_REQUEST, 40010, "비밀번호 길이는 $MIN_PASSWORD 이상 $MAX_PASSWORD 이하여야 합니다."),
    NICKNAME_OUT_OF_LENGTH(HttpStatus.BAD_REQUEST, 40011, "닉네임의 길이는 $MIN_NICKNAME 이상 $MAX_NICKNAME 이하여야 합니다."),
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST, 40012, "비밀번호가 일치하지 않습니다."),

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, 40410, "회원을 찾을 수 없습니다"),

    CONFLICT_MEMBER(HttpStatus.CONFLICT, 40910, "회원이 이미 존재합니다."),

    // Quiz
    QUIZ_CATEGORY_INVALID(HttpStatus.BAD_REQUEST, 40020, "지원하지 않는 퀴즈 카테고리 입니다."),
    QUIZ_FILTER_INVALID(HttpStatus.BAD_REQUEST, 40021, "유효하지 않은 퀴즈 필터입니다."),

    // Game
    // Play
    // Websocket
}
