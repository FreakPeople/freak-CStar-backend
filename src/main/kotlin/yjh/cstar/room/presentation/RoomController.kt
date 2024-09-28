package yjh.cstar.room.presentation

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yjh.cstar.auth.jwt.TokenProvider
import yjh.cstar.common.Response
import yjh.cstar.room.application.RoomService
import yjh.cstar.room.presentation.request.RoomCreateRequest
import yjh.cstar.room.presentation.request.toCommand
import yjh.cstar.room.presentation.response.RoomResponse

@RestController
@RequestMapping("/v1")
class RoomController(
    private val roomService: RoomService,
    private val tokenProvider: TokenProvider,
) {

    @PostMapping("/rooms")
    fun create(
        @RequestBody request: RoomCreateRequest,
    ): ResponseEntity<Response<Long>> {
        val createdRoomId = roomService.create(request.toCommand())
        return ResponseEntity.ok(Response(data = createdRoomId))
    }

    @GetMapping("/rooms/{id}")
    fun retrieve(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Response<RoomResponse>> {
        val response = RoomResponse.from(roomService.retrieve(id))
        return ResponseEntity.ok(Response(data = response))
    }

    @GetMapping("/rooms")
    fun retrieveAll(
        @PageableDefault(size = 10) pageable: Pageable,
    ): ResponseEntity<Response<Page<RoomResponse>>> {
        val responses = roomService.retrieveAll(pageable)
            .map { RoomResponse.from(it) }
        return ResponseEntity.ok(Response(data = responses))
    }

    @PostMapping("/rooms/{id}")
    fun join(
        @PathVariable("id") roomId: Long,
        authentication: Authentication,
    ): ResponseEntity<Response<Long>> {
        synchronized(this) {
            val memberId = tokenProvider.getMemberId(authentication)
            return ResponseEntity.ok(Response(data = roomService.join(roomId, memberId)))
        }
    }
}
