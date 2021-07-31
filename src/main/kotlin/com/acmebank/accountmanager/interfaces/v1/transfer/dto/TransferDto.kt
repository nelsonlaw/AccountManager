package com.acmebank.accountmanager.interfaces.v1.transfer.dto

import com.acmebank.accountmanager.domain.transfer.Transfer
import java.math.BigDecimal

class TransferDto(
    val initiatorAccountId: Long,
    val counterpartAccountId: Long,
    val amount: BigDecimal
)

fun Transfer.toDto() = TransferDto(
    initiatorAccountId = initiatorAccountId,
    counterpartAccountId = counterpartAccountId,
    amount = amount
)