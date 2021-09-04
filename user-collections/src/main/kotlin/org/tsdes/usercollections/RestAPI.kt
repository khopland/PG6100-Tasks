package org.tsdes.usercollections

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse
import org.tsdes.usercollections.db.UserService
import org.tsdes.usercollections.dto.Command.*
import org.tsdes.usercollections.dto.PatchResultDto
import org.tsdes.usercollections.dto.PatchUserDto
import org.tsdes.usercollections.dto.UserDto

@Api(value = "/api/user-collections", description = "Operations on card collections owned by users")
@RequestMapping(
    "/api/user-collections",
    produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class RestAPI(private val userService: UserService) {

    @ApiOperation("get a user by an id")
    @GetMapping("/{userId}")
    fun getUserInfo(@PathVariable("userId") userId: String): ResponseEntity<WrappedResponse<UserDto>> {
        val user = userService.findByIdEager(userId) ?: return RestResponseFactory.notFound("User $userId not found")
        return RestResponseFactory.payload(200, DtoConverter.transform(user))
    }

    @ApiOperation("creat a user on a id")
    @PutMapping("/{userId}")
    fun createUser(@PathVariable("userId") userId: String): ResponseEntity<WrappedResponse<Void>> {
        return if (userService.registerNewUser(userId)) RestResponseFactory.noPayload(201)
        else RestResponseFactory.userFailure("User $userId already exist")
    }

    @ApiOperation("Execute a command on a user's collection, like for example buying/milling cards")
    @PatchMapping("/{userId}", consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun patchUser(
        @PathVariable("userId") userId: String,
        @RequestBody dto: PatchUserDto
    ): ResponseEntity<WrappedResponse<PatchResultDto>> {
        return when (dto.command) {
            BUY_CARD -> {
                val cardId = dto.cardId ?: return RestResponseFactory.userFailure("Missing card id")
                try {
                    userService.buyCard(userId, cardId)
                } catch (e: IllegalArgumentException) {
                    return RestResponseFactory.userFailure(e.message ?: "Failed to buy card ${dto.cardId}")
                }
                RestResponseFactory.payload(200, PatchResultDto())
            }
            MILL_CARD -> {
                val cardId = dto.cardId ?: return RestResponseFactory.userFailure("Missing card id")
                try {
                    userService.millCard(userId, cardId)
                } catch (e: java.lang.IllegalArgumentException) {
                    return RestResponseFactory.userFailure(e.message ?: "Failed to mill card $cardId")
                }
                RestResponseFactory.payload(200, PatchResultDto())
            }
            OPEN_PACK -> {
                val ids = try {
                    userService.openPack(userId)
                } catch (e: IllegalArgumentException) {
                    return RestResponseFactory.userFailure(e.message ?: "Failed to open pack")
                }
                RestResponseFactory.payload(200, PatchResultDto().apply { cardIdsInOpenedPack.addAll(ids) })
            }
            else -> RestResponseFactory.userFailure("Unrecognized command: ${dto.command}")
        }
    }
}