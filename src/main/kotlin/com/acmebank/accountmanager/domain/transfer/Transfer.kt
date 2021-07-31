package com.acmebank.accountmanager.domain.transfer

import java.math.BigDecimal

data class Transfer(
    val initiatorAccountId: Long,
    val counterpartAccountId: Long,
    val amount: BigDecimal
)