package com.acmebank.accountmanager.domain.account.service

interface AccountService {
    fun validateAccountFailFast(id: Long)
}