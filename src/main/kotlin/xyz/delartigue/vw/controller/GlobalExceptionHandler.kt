package xyz.delartigue.vw.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import xyz.delartigue.vw.exception.DuplicateEmailException
import xyz.delartigue.vw.exception.InvalidConsentException
import xyz.delartigue.vw.exception.MissingUserIdException


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException::class)
    fun handleDuplicateEmailException(e: DuplicateEmailException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(InvalidConsentException::class)
    fun handleInvalidConsentException(e: InvalidConsentException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(MissingUserIdException::class)
    fun handleMissingUserIdException(e: MissingUserIdException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(e: MethodArgumentNotValidException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.UNPROCESSABLE_ENTITY)
    }

}