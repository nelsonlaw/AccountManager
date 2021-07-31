package com.acmebank.accountmanager.interfaces.v1.balance

import com.acmebank.accountmanager.domain.account.service.AccountService
import com.acmebank.accountmanager.domain.balance.service.BalanceService
import com.acmebank.accountmanager.interfaces.v1.balance.dto.BalanceDto
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/balance")
class BalanceController(
    private val accountService: AccountService,
    private val balanceService: BalanceService
) {

    @GetMapping
    @Operation(summary = "get balance", description = "get balance")
    fun getBalance(@RequestParam accountId: Long): BalanceDto {
        accountService.validateAccountFailFast(accountId)

        val accountBalance = balanceService.getAccountBalance(accountId)
        return BalanceDto(
            id = accountId,
            balance = accountBalance.amount
        )
    }
}