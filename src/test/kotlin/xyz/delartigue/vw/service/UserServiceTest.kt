package xyz.delartigue.vw.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.springframework.dao.DataIntegrityViolationException
import xyz.delartigue.vw.exception.DuplicateEmailException
import xyz.delartigue.vw.repository.UserRepository
import xyz.delartigue.vw.repository.entity.UserEntity
import xyz.delartigue.vw.service.domain.User
import java.util.Optional
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private val id1 = UUID.randomUUID()
    private val id2 = UUID.randomUUID()

    @BeforeTest
    fun setUp() {
        userRepository = mock()
        userService = UserService(userRepository)
    }

    @Test
    fun `getUsers returns all users`() {
        val repoResponse = listOf(
            UserEntity(id1, "e@mail.com", emptyList()),
            UserEntity(id2, "em@ail.com", emptyList()),
        )
        val expected = listOf(
            User(id1, "e@mail.com", emptyList()),
            User(id2, "em@ail.com", emptyList()),
        )
        given(userRepository.findAll()).willReturn(repoResponse)

        val actual = userService.getUsers()

        assertEquals(expected, actual)
    }

    @Test
    fun `getUserById returns a single user`() {
        val repoResponse = UserEntity(id1, "e@mail.com", emptyList())
        val expected = User(id1, "e@mail.com", emptyList())
        given(userRepository.findById(id1)).willReturn(Optional.of(repoResponse))

        val actual = userService.getUserById(id1)

        assertEquals(expected, actual)
    }

    @Test
    fun `createUser throws an exception if the email already exists`() {
        val input = User(null, "e@mail.com", emptyList())
        given(userRepository.save(any(UserEntity::class.java))).willThrow(DataIntegrityViolationException("email"))

        val exception = assertThrows<DuplicateEmailException> {
            userService.createUser(input)
        }

        assertEquals("User with email e@mail.com already exists", exception.message)
    }

    @Test
    fun updateUser() {
        val input = User(id1, "e@mail.com", emptyList())
        val repoResponse = UserEntity(id1, "e@mail.com", emptyList())
        val expected = User(id1, "e@mail.com", emptyList())
        given(userRepository.save(any(UserEntity::class.java))).willReturn(repoResponse)

        val actual = userService.createUser(input)

        assertEquals(expected, actual)
    }

}
