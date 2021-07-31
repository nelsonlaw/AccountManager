package com.acmebank.accountmanager.interfaces.v1.balance.dto

import java.math.BigDecimal

data class BalanceDto(
    val id: Long,
    val balance: BigDecimal
)