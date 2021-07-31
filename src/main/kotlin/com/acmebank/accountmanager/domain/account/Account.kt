package com.acmebank.accountmanager.domain.account

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Account(
    @Id val id: Long
)