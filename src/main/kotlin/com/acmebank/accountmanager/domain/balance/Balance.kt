package com.acmebank.accountmanager.domain.balance

import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
data class Balance(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
    @Column val accountId: Long,
    @Column val amount: BigDecimal,
    @Column val lastUpdateTime: ZonedDateTime
)