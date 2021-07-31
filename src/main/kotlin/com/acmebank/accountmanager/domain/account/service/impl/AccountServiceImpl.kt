package com.acmebank.accountmanager.domain.account.service.impl

import com.acmebank.accountmanager.domain.account.AccountValidationResult
import com.acmebank.accountmanager.domain.account.exception.InvalidAccountException
import com.acmebank.accountmanager.domain.account.repository.AccountRepository
import com.acmebank.accountmanager.domain.account.service.AccountService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(
    val accountRepository: AccountRepository
) : AccountService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun validateAccountFailFast(id: Long) {
        val accountValidationResult = validateAccount(id)
        if (!accountValidationResult.validity) {
            throw InvalidAccountException(accountValidationResult.reason)
        }
    }

    private fun validateAccount(id: Long): AccountValidationResult {
        logger.info("Validating Account $id")
        val accountExists = accountRepository.existsById(id)
        val accountValidationResult = if (accountExists) {
            AccountValidationResult(
                validity = true
            )
        } else {
            AccountValidationResult(
                validity = false,
                reason = "Account $id doesn't exist"
            )
        }
        logger.info("ValidationResult for account $id is $accountValidationResult")
        return accountValidationResult
    }
}