package xyz.delartigue.vw.controller

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import xyz.delartigue.vw.controller.dto.UserDto
import xyz.delartigue.vw.controller.dto.toDomain
import xyz.delartigue.vw.service.UserService
import xyz.delartigue.vw.service.domain.User
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class UserControllerTest {
    @Mock
    private lateinit var userService: UserService
    private lateinit var userController: UserController
    private val id1 = UUID.randomUUID()
    private val id2 = UUID.randomUUID()

    @BeforeTest
    fun setUp() {
        userService = mock()
        userController = UserController(userService)
    }

    @Test
    fun `getUsers returns all users as DTOs`() {
        val serviceResponse = listOf(
            User(id1, "e@mail.com", emptyList()),
            User(id2, "em@ail.com", emptyList()),
        )
        val expected = listOf(
            UserDto(id1, "e@mail.com", emptyList()),
            UserDto(id2, "em@ail.com", emptyList()),
        )
        given(userService.getUsers()).willReturn(serviceResponse)

        val actual = userController.getUsers()

        assertEquals(HttpStatus.OK, actual.statusCode)
        assertEquals(expected, actual.body)
    }

    @Test
    fun `createUser creates a new user`() {
        val input = UserDto(null, "e@mail.com", emptyList())
        val serviceResponse = User(id1, "e@mail.com", emptyList())
        val expected = UserDto(id1, "e@mail.com", emptyList())
        given(userService.createUser(input.toDomain())).willReturn(serviceResponse)

        val actual = userController.createUser(input)

        assertEquals(HttpStatus.CREATED, actual.statusCode)
        assertEquals(expected, actual.body)
    }

    @Test
    fun `getUserById returns a user by id`() {
        val serviceResponse = User(id1, "e@mail.com", emptyList())
        val expected = UserDto(id1, "e@mail.com", emptyList())
        given(userService.getUserById(id1)).willReturn(serviceResponse)

        val actual = userController.getUserById(id1)

        assertEquals(HttpStatus.OK, actual.statusCode)
        assertEquals(expected, actual.body)
    }

    @Test
    fun `getUserById returns 404 if a user is not found`() {
        given(userService.getUserById(id1)).willReturn(null)

        val actual = userController.getUserById(id1)

        assertEquals(HttpStatus.NOT_FOUND, actual.statusCode)
        assertEquals(null, actual.body)
    }

    @Test
    fun `updateUser updates an existing user`() {
        val input = UserDto(id1, "e@mail.com", emptyList())
        val serviceResponse = User(id1, "e@mail.com", emptyList())
        val expected = UserDto(id1, "e@mail.com", emptyList())
        given(userService.updateUser(input.toDomain())).willReturn(serviceResponse)

        val actual = userController.updateUser(input)

        assertEquals(HttpStatus.OK, actual.statusCode)
        assertEquals(expected, actual.body)
    }

    @Test
    fun `deleteUser deletes an existing user`() {
        val actual = userController.deleteUser(id1)

        assertEquals(HttpStatus.NO_CONTENT, actual.statusCode)
        assertEquals(null, actual.body)
    }

}