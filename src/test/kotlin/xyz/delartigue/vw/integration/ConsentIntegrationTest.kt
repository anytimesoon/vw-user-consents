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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import xyz.delartigue.vw.controller.dto.ConsentDto
import xyz.delartigue.vw.controller.dto.ConsentRequest
import xyz.delartigue.vw.controller.dto.UserDto
import xyz.delartigue.vw.controller.dto.UserRequest
import xyz.delartigue.vw.repository.ConsentRepository
import xyz.delartigue.vw.repository.UserRepository
import xyz.delartigue.vw.repository.entity.ConsentEntity
import xyz.delartigue.vw.repository.entity.UserEntity
import xyz.delartigue.vw.type.ConsentType
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ConsentIntegrationTest {

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
        consentRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `createConsent creates a new consent`() {
        val input = generateConsentRequest(emailEnabled = true, smsEnabled = null)

        mockMvc.perform(post("/consent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk)

        val result = mockMvc.perform(get("/user/${id1}"))
            .andExpect(status().isOk)
            .andReturn()

        val user = objectMapper.readValue(result.response.contentAsString, UserDto::class.java)

        assertTrue(user.consents?.size == 1)
        assertTrue(user.consents?.get(0)?.id == ConsentType.EMAIL.value)
        assertTrue(user.consents?.get(0)?.enabled == true)
    }

    @Test
    fun `createConsent returns 422 if the consent is not valid`() {
        val input = ConsentRequest(
            user = UserRequest(id1),
            listOf(ConsentDto(
                id = "invalid",
                enabled = true)
            )
        )

        mockMvc.perform(post("/consent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `updateConsent updates or creates a consent`() {
        val input = generateConsentRequest(userId = id2, emailEnabled = false, smsEnabled = true)

        mockMvc.perform(put("/consent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk)

        val result = mockMvc.perform(get("/user/${id2}"))
            .andExpect(status().isOk)
            .andReturn()

        val user = objectMapper.readValue(result.response.contentAsString, UserDto::class.java)

        assertTrue(user.consents?.size == 2)
        assertTrue(user.consents?.get(0)?.id == ConsentType.EMAIL.value)
        assertTrue(user.consents?.get(1)?.id == ConsentType.SMS.value)
        assertTrue(user.consents?.get(0)?.enabled == false)
        assertTrue(user.consents?.get(1)?.enabled == true)
    }

    @Test
    fun `updateConsent returns 422 if the consent is not valid`() {
        val input = ConsentRequest(
            user = UserRequest(id1),
            listOf(ConsentDto(
                id = "invalid",
                enabled = true)
            )
        )

        mockMvc.perform(put("/consent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isUnprocessableEntity)
    }

    private fun generateConsentRequest(
        userId: UUID = id1,
        emailEnabled: Boolean?,
        smsEnabled: Boolean?,
    ): ConsentRequest {
        val consents = mutableListOf<ConsentDto>()

        if (emailEnabled != null) {
            consents.add(ConsentDto(
                id = ConsentType.EMAIL.value,
                enabled = emailEnabled
            ))
        }

        if (smsEnabled != null) {
            consents.add(ConsentDto(
                id = ConsentType.SMS.value,
                enabled = smsEnabled
            ))
        }

        return ConsentRequest(
            user = UserRequest(userId),
            consents = consents
        )
    }
}
