package com.acmebank.accountmanager.domain.balance.service

import com.acmebank.accountmanager.domain.balance.Balance
import java.math.BigDecimal

interface BalanceService {
    fun getAccountBalance(accountId: Long): Balance
    fun updateAccountBalance(accountId: Long, amount: BigDecimal)
}