package com.acmebank.accountmanager.domain.account

data class AccountValidationResult(
    val validity: Boolean,
    val reason: String = ""
)