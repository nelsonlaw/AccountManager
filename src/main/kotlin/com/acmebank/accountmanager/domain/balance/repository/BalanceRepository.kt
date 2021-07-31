package com.acmebank.accountmanager.domain.balance.repository

import com.acmebank.accountmanager.domain.balance.Balance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal
import java.time.ZonedDateTime

interface BalanceRepository : JpaRepository<Balance, Long> {

    fun getOneByAccountId(accountId: Long): Balance

    @Modifying
    @Query(
        "update Balance balance set balance.amount = :newBalance, balance.lastUpdateTime = :updateTime " +
                "where balance.accountId = :accountId and balance.amount = :oldBalance and balance.lastUpdateTime = :lastUpdateTime"
    )
    fun updateBalance(
        @Param("accountId") accountId: Long,
        @Param("oldBalance") oldBalance: BigDecimal,
        @Param("newBalance") newBalance: BigDecimal,
        @Param("updateTime") updateTime: ZonedDateTime,
        @Param("lastUpdateTime") lastUpdateTime: ZonedDateTime
    ): Int
}