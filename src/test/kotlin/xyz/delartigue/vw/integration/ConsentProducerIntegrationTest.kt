package xyz.delartigue.vw.integration

import org.junit.jupiter.api.Test
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import xyz.delartigue.vw.service.ConsentProducerService
import xyz.delartigue.vw.service.domain.Consent
import xyz.delartigue.vw.service.domain.ConsentEvent
import xyz.delartigue.vw.service.domain.UserEvent
import xyz.delartigue.vw.type.ConsentType
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull

@SpringBootTest
@Testcontainers
class ConsentProducerServiceIntegrationTest {

    companion object {
        const val EXCHANGE_NAME = "consent-exchange"
        const val ROUTING_KEY = "consent-routing-key"
        const val QUEUE_NAME = "consent-queue"

        @Container
        val rabbitMQContainer: RabbitMQContainer = RabbitMQContainer("rabbitmq:3.9-alpine")
            .withExposedPorts(5672)
    }

    @Autowired
    private lateinit var connectionFactory: ConnectionFactory

    @Autowired
    private lateinit var amqpAdmin: AmqpAdmin

    @Autowired
    private lateinit var consentProducerService: ConsentProducerService

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    @BeforeTest
    fun setUp() {
        rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
        consentProducerService = ConsentProducerService(
            rabbitTemplate = rabbitTemplate,
            exchangeName = EXCHANGE_NAME,
            routingKey = ROUTING_KEY
        )
    }

    @Test
    fun `queue should exist`() {
        val properties = amqpAdmin.getQueueProperties(QUEUE_NAME)
        assertNotNull(properties, "Queue should exist")
    }


    @Test
    fun `send should publish message to RabbitMQ`() {
        val id = UUID.randomUUID()
        val event = ConsentEvent(
            user = UserEvent(
                id = id
            ),
            consents = listOf(
                Consent(
                    consent = ConsentType.EMAIL,
                    enabled = true,
                    updated = LocalDateTime.now(),
                    id = 1
                )
            )
        )

        consentProducerService.send(event)

        val message = rabbitTemplate.receiveAndConvert(QUEUE_NAME, 1000)
        assertNotNull(message)
    }
}
