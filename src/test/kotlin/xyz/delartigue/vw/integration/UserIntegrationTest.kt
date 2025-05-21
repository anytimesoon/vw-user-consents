package xyz.delartigue.vw.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import xyz.delartigue.vw.controller.dto.UserDto
import xyz.delartigue.vw.repository.ConsentRepository
import xyz.delartigue.vw.repository.UserRepository
import xyz.delartigue.vw.repository.entity.ConsentEntity
import xyz.delartigue.vw.repository.entity.UserEntity
import xyz.delartigue.vw.type.ConsentType
import xyz.delartigue.vw.utils.invalidEmails
import xyz.delartigue.vw.utils.validEmails
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var consentRepository: ConsentRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val id1 = UUID.randomUUID()
    private val id2 = UUID.randomUUID()

    @BeforeTest
    fun setUp() {
        val user1 = UserEntity(id1, "e@mail.com", emptyList())
        val user2 = UserEntity(id2, "em@ail.com", emptyList())

        userRepository.save(user1)
        userRepository.save(user2)
        consentRepository.save(ConsentEntity(
            id = null,
            user = user2,
            consent = ConsentType.EMAIL,
            enabled = true,
            updated = LocalDateTime.now()
        ))
    }

    @AfterTest
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun `createUser creates a new user`() {
        val input = generateUserDto(email = "user@email.com")
        val result = mockMvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated)
            .andReturn()

        val user = objectMapper.readValue(result.response.contentAsString, UserDto::class.java)

        assertTrue(user.email == input.email)
        assertNotNull(user.id)
        assertTrue(user.consents?.isEmpty() == true)
    }

    @Test
    fun `create user returns 422 when email is duplicate`() {
        val input = generateUserDto(email = "e@mail.com")
        mockMvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isUnprocessableEntity)
    }


    @Test
    fun `update user returns 422 when email is duplicate`() {
        val input = generateUserDto(email = "e@mail.com")
        mockMvc.perform(put("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `createUser allows valid email addresses`() {
        validEmails.forEach { email ->
            val input = generateUserDto(email = email)
            val result = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated)
                .andReturn()

            val user = objectMapper.readValue(result.response.contentAsString, UserDto::class.java)

            assertTrue(user.email == email)
            assertNotNull(user.id)
        }
    }

    @Test
    fun `updateUser allows valid email addresses`() {
        validEmails.forEach { email ->
            val input = generateUserDto(id = id1, email = email)
            val result = mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk)
                .andReturn()

            val user = objectMapper.readValue(result.response.contentAsString, UserDto::class.java)

            assertTrue(user.email == email)
            assertNotNull(user.id)
            assertTrue(user.id == id1)
            assertTrue(user.consents?.isEmpty() == true)
        }
    }

    @Test
    fun `createUser returns 422 if the email is invalid`() {
        invalidEmails.forEach { email ->
            val input = generateUserDto(email = email)
            mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isUnprocessableEntity)
        }
    }


    @Test
    fun `updateUser returns 422 if the email is invalid`() {
        invalidEmails.forEach { email ->
            val input = generateUserDto(id = id1, email = email)
            mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isUnprocessableEntity)
        }
    }

    @Test
    fun `updateUser returns 422 if the id is null`() {
        val input = generateUserDto(email = "email@user.com")
            mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `deleteUser deletes an existing user`() {
        mockMvc.perform(delete("/user/${id1}"))
            .andExpect(status().isNoContent)
            .andReturn()

        mockMvc.perform(get("/user/${id1}"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deleteUser deletes an existing user and existing nested consents`() {
        mockMvc.perform(delete("/user/${id2}"))
            .andExpect(status().isNoContent)
            .andReturn()

        mockMvc.perform(get("/user/${id2}"))
            .andExpect(status().isNotFound)
    }

    private fun generateUserDto(
        id: UUID? = null,
        email: String
    ): UserDto {
        return UserDto(id, email, emptyList())
    }
}