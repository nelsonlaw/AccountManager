package com.acmebank.accountmanager.interfaces.v1.balance

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class BalanceControllerTest(@Autowired val mockMvc: MockMvc, @Autowired val flyway: Flyway) {

    @BeforeEach
    fun setupForEach() {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun `Test getBalance, Given test data loaded in DB, When get balance from valid account, Expect correct balance figure`() {
        mockMvc.get("/v1/balance?accountId=12345678")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.balance") { value(1000000) }
            }
    }

    @Test
    fun `Test getBalance, Given test data loaded in DB, When get balance from invalid account, Expect failing to get balance`() {
        mockMvc.get("/v1/balance/0")
            .andDo { print() }
            .andExpect { status { isNotFound() } }
    }

}