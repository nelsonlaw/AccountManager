package com.acmebank.accountmanager.interfaces.v1.transfer

import com.acmebank.accountmanager.domain.account.service.AccountService
import com.acmebank.accountmanager.domain.transfer.service.TransferService
import com.acmebank.accountmanager.interfaces.v1.transfer.dto.TransferDto
import com.acmebank.accountmanager.interfaces.v1.transfer.dto.toDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/transfer")
class TransferController(private val transferService: TransferService, private val accountService: AccountService) {

    @PostMapping
    fun createTransfer(@RequestBody transfer: TransferDto): TransferDto {
        accountService.validateAccountFailFast(transfer.initiatorAccountId)
        accountService.validateAccountFailFast(transfer.counterpartAccountId)
        return transferService.transfer(
            initiatorAccountId = transfer.initiatorAccountId,
            counterpartAccountId = transfer.counterpartAccountId,
            amount = transfer.amount
        ).toDto()
    }
}