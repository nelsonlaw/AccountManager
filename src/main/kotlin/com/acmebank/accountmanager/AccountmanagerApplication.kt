package com.acmebank.accountmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class AccountmanagerApplication

fun main(args: Array<String>) {
    runApplication<AccountmanagerApplication>(*args)
}
