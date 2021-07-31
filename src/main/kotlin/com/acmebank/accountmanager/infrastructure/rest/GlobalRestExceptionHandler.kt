package com.acmebank.accountmanager.infrastructure.rest

import com.acmebank.accountmanager.domain.account.exception.InvalidAccountException
import com.acmebank.accountmanager.domain.balance.exception.BalanceUpdateException
import com.acmebank.accountmanager.domain.transfer.exception.TransferAbortException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalRestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(InvalidAccountException::class)
    fun handleInvalidAccountException(e: InvalidAccountException): ResponseEntity<RestApiError> {
        val restApiError = RestApiError(HttpStatus.NOT_FOUND, e.message)
        return ResponseEntity(restApiError, HttpHeaders(), restApiError.status)
    }

    @ExceptionHandler(TransferAbortException::class)
    fun handleTransferAbortException(e: TransferAbortException): ResponseEntity<RestApiError> {
        val restApiError = RestApiError(HttpStatus.BAD_REQUEST, e.message)
        return ResponseEntity(restApiError, HttpHeaders(), restApiError.status)
    }

    @ExceptionHandler(BalanceUpdateException::class)
    fun handleBalanceUpdateException(e: BalanceUpdateException): ResponseEntity<RestApiError> {
        val restApiError = RestApiError(HttpStatus.BAD_REQUEST, e.message)
        return ResponseEntity(restApiError, HttpHeaders(), restApiError.status)
    }
}