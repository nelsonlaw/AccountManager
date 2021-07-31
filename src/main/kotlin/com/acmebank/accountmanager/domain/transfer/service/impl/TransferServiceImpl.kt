package com.acmebank.accountmanager.domain.transfer.service.impl

import com.acmebank.accountmanager.domain.balance.exception.BalanceUpdateException
import com.acmebank.accountmanager.domain.balance.service.BalanceService
import com.acmebank.accountmanager.domain.transfer.Transfer
import com.acmebank.accountmanager.domain.transfer.exception.TransferAbortException
import com.acmebank.accountmanager.domain.transfer.service.TransferService
import org.slf4j.LoggerFactory
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class TransferServiceImpl(
    private val balanceService: BalanceService
) : TransferService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    @Retryable(BalanceUpdateException::class, maxAttempts = 5)
    override fun transfer(initiatorAccountId: Long, counterpartAccountId: Long, amount: BigDecimal): Transfer {

        logger.info("Start transfer from $initiatorAccountId to $counterpartAccountId")

        validateTransfer(
            initiatorAccountId = initiatorAccountId,
            counterpartAccountId = counterpartAccountId,
            amount = amount
        )

        balanceService.updateAccountBalance(initiatorAccountId, amount.negate())
        balanceService.updateAccountBalance(counterpartAccountId, amount)

        logger.info("Completed transfer from $initiatorAccountId to $counterpartAccountId")

        return Transfer(
            initiatorAccountId = initiatorAccountId,
            counterpartAccountId = counterpartAccountId,
            amount = amount
        )
    }

    private fun validateTransfer(
        initiatorAccountId: Long,
        counterpartAccountId: Long,
        amount: BigDecimal
    ) {
        if (getNumberOfDecimalPlaces(amount) > 2) {
            throw TransferAbortException("Transfer amount shouldn't have more than two decimal place").also {
                logger.info(it.message)
            }
        }

        if (amount <= BigDecimal("0.01")) {
            throw TransferAbortException("Transfer amount should be larger than 0.01").also {
                logger.info(it.message)
            }
        }

        if (initiatorAccountId == counterpartAccountId) {
            throw TransferAbortException("Initiator and counterpart can't be same account").also {
                logger.info(it.message)
            }
        }

        val initiatorAccountBalance = balanceService.getAccountBalance(initiatorAccountId).amount
        if (initiatorAccountBalance.minus(amount) < BigDecimal.ZERO) {
            throw TransferAbortException("No enough balance to complete transfer").also {
                logger.info(it.message)
            }
        }
    }

    fun getNumberOfDecimalPlaces(bigDecimal: BigDecimal): Int {
        val string = bigDecimal.stripTrailingZeros().toPlainString()
        val index = string.indexOf(".")
        return if (index < 0) 0 else string.length - index - 1
    }
}