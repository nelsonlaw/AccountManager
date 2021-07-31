package com.acmebank.accountmanager.infrastructure.rest

import org.springframework.http.HttpStatus

class RestApiError(
    val status: HttpStatus,
    val message: String?
)