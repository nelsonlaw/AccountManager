package com.acmebank.accountmanager.domain.transfer.service

import com.acmebank.accountmanager.domain.transfer.Transfer
import java.math.BigDecimal

interface TransferService {
    fun transfer(initiatorAccountId: Long, counterpartAccountId: Long, amount: BigDecimal): Transfer
}