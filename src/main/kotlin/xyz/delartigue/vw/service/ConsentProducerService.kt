package xyz.delartigue.vw.service

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import xyz.delartigue.vw.service.domain.ConsentEvent

@Service
class ConsentProducerService(
    private val rabbitTemplate: RabbitTemplate,
    @Value("\${rabbitmq.exchange}")
    val exchangeName: String,
    @Value("\${rabbitmq.routingkey}")
    val routingKey: String,
) {
    fun send(event: ConsentEvent) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, event)
        logger.info("Sent consent change event: $event")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ConsentProducerService::class.java)
    }
}
