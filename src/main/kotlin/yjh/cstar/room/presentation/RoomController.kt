package yjh.cstar.room.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import yjh.cstar.common.Response
import yjh.cstar.room.application.RoomService
import yjh.cstar.room.presentation.request.RoomCreateRequest
import yjh.cstar.room.presentation.request.toCommand

@RestController
class RoomController(
    private val roomService: RoomService,
) {

    @PostMapping("/posts")
    fun create(
        @RequestBody request: RoomCreateRequest,
    ): ResponseEntity<Response<Long>> {
        return ResponseEntity.ok(Response(data = roomService.create(request.toCommand())))
    }
}
