package com.acmebank.accountmanager.domain.balance.service.impl

import com.acmebank.accountmanager.domain.balance.Balance
import com.acmebank.accountmanager.domain.balance.exception.BalanceUpdateException
import com.acmebank.accountmanager.domain.balance.repository.BalanceRepository
import com.acmebank.accountmanager.domain.balance.service.BalanceService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.ZonedDateTime

@Service
class BalanceServiceImpl(
    private val balanceRepository: BalanceRepository,
    private val nowGenerator: () -> ZonedDateTime = { ZonedDateTime.now() }
) : BalanceService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getAccountBalance(accountId: Long): Balance {
        return balanceRepository.getOneByAccountId(accountId)
    }

    override fun updateAccountBalance(accountId: Long, amount: BigDecimal) {
        logger.info("Updating balance for account $accountId")

        val balance = balanceRepository.getOneByAccountId(accountId)
        val newBalance = balance.amount.plus(amount)
        val numOfRecordUpdated = balanceRepository.updateBalance(
            accountId = accountId,
            oldBalance = balance.amount,
            newBalance = newBalance,
            updateTime = nowGenerator(),
            lastUpdateTime = balance.lastUpdateTime
        )
        if (numOfRecordUpdated < 1) {
            throw BalanceUpdateException("Balance for account $accountId has been modified before, fails to update balance").also {
                logger.info(it.message)
            }
        }

        logger.info("Finish updating balance for account $accountId")
    }
}