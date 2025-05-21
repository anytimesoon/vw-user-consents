package xyz.delartigue.vw.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.delartigue.vw.controller.dto.ConsentRequest
import xyz.delartigue.vw.controller.dto.toEvent
import xyz.delartigue.vw.service.ConsentService

@RestController
@RequestMapping("/consent")
class ConsentController (
    private val consentService: ConsentService
){
    @PostMapping("")
    fun createConsent(
        @RequestBody request: ConsentRequest
    ) = consentService.createConsent(request.toEvent())
}
