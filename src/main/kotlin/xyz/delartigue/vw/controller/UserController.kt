package xyz.delartigue.vw.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import xyz.delartigue.vw.controller.dto.UserDto
import xyz.delartigue.vw.controller.dto.toDomain
import xyz.delartigue.vw.controller.dto.toDto
import xyz.delartigue.vw.service.UserService
import java.util.UUID

@RestController
@RequestMapping("/user")
class UserController (
    private val userService: UserService
){
    @GetMapping("")
    fun getUsers(): ResponseEntity<List<UserDto>> =
        ResponseEntity(
            userService.getUsers().map { it.toDto() },
            HttpStatus.OK
        )

    @PostMapping("")
    fun createUser(
        @Valid @RequestBody userDto: UserDto
    ): ResponseEntity<UserDto> =
        ResponseEntity(
            userService.createUser(userDto.toDomain()).toDto(),
            HttpStatus.CREATED
        )

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable("id") id: UUID
    ): ResponseEntity<UserDto> {
        val user = userService.getUserById(id)?.toDto()

        if (user == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PutMapping("")
    fun updateUser(
        @Valid @RequestBody userDto: UserDto
    ): ResponseEntity<UserDto> {
        val user = userService.updateUser(userDto.toDomain()).toDto()

        return ResponseEntity(user, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable("id") id: UUID
    ): ResponseEntity<UserDto> {
        userService.deleteUser(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
