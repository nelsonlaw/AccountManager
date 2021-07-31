package com.acmebank.accountmanager.interfaces.v1.transfer

import com.acmebank.accountmanager.interfaces.v1.transfer.dto.TransferDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class TransferControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val flyway: Flyway
) {

    @BeforeEach
    fun setupForEach() {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    internal fun `Test createTransfer, Given Account is valid and has sufficient balance, When create transfer more than 0_01, expect transfer successful`() {
        mockMvc.post("/v1/transfer") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                TransferDto(
                    initiatorAccountId = 12345678,
                    counterpartAccountId = 88888888,
                    amount = BigDecimal("100")
                )
            )
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    internal fun `Test createTransfer, Given Account is valid and doesn't has sufficient balance, When multiple transfers at the same time, expect balance never drop below zero`() {

        val testTheadPoolExecutorService = Executors.newFixedThreadPool(2)
        val countDownLatch = CountDownLatch(2)

        for (i in 1..2) {
            testTheadPoolExecutorService.execute {
                mockMvc.post("/v1/transfer") {
                    contentType = MediaType.APPLICATION_JSON
                    accept = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(
                        TransferDto(
                            initiatorAccountId = 12345678,
                            counterpartAccountId = 88888888,
                            amount = BigDecimal("1000000")
                        )
                    )
                }.andDo { print() }
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()

        mockMvc.get("/v1/balance?accountId=12345678")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.balance") { value(0) }
            }
        mockMvc.get("/v1/balance?accountId=88888888")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.balance") { value(2_000_000) }
            }
    }

    @Test
    internal fun `Test createTransfer, Given Account is valid and has sufficient balance, When multiple transfers at the same time, expect all go through`() {

        val testTheadPoolExecutorService = Executors.newFixedThreadPool(5)
        val countDownLatch = CountDownLatch(5)

        for (i in 1..5) {
            testTheadPoolExecutorService.execute {
                mockMvc.post("/v1/transfer") {
                    contentType = MediaType.APPLICATION_JSON
                    accept = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(
                        TransferDto(
                            initiatorAccountId = 12345678,
                            counterpartAccountId = 88888888,
                            amount = BigDecimal("100000")
                        )
                    )
                }.andDo { print() }
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()

        mockMvc.get("/v1/balance?accountId=12345678")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.balance") { value(500_000) }
            }
        mockMvc.get("/v1/balance?accountId=88888888")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.balance") { value(1_500_000) }
            }
    }

    @Test
    internal fun `Test createTransfer, Given Account is valid and has sufficient balance, When transfer amount less than 0_01, expect transfer fails`() {
        mockMvc.post("/v1/transfer") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                TransferDto(
                    initiatorAccountId = 12345678,
                    counterpartAccountId = 88888888,
                    amount = BigDecimal("0.001")
                )
            )
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    internal fun `Test createTransfer, Given Account is valid and has sufficient balance, When initiator is same as counterpart, expect transfer fails`() {
        mockMvc.post("/v1/transfer") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                TransferDto(
                    initiatorAccountId = 12345678,
                    counterpartAccountId = 12345678,
                    amount = BigDecimal("100")
                )
            )
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    internal fun `Test createTransfer, Given Account is valid and has sufficient balance, When transfer amount has more than two decimal place, expect transfer fails`() {
        mockMvc.post("/v1/transfer") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                TransferDto(
                    initiatorAccountId = 12345678,
                    counterpartAccountId = 12345678,
                    amount = BigDecimal("100.001")
                )
            )
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    internal fun `Test createTransfer, Given Account is valid, When transfer amount larger than balance, expect transfer fails`() {
        mockMvc.post("/v1/transfer") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                TransferDto(
                    initiatorAccountId = 12345678,
                    counterpartAccountId = 88888888,
                    amount = BigDecimal("1000000.1")
                )
            )
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
            }
    }
}