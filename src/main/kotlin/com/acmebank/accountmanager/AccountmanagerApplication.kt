package com.acmebank.accountmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccountmanagerApplication

fun main(args: Array<String>) {
    runApplication<AccountmanagerApplication>(*args)
}
