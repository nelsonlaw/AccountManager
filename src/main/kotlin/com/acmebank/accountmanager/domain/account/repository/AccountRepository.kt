package com.acmebank.accountmanager.domain.account.repository

import com.acmebank.accountmanager.domain.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long>