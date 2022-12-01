package controller

import helper.InvalidRequestException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class BankingControllerTest {

    @Test
    fun executeCommand() {
        val bankingController = BankingController()
        Assertions.assertThrows(InvalidRequestException::class.java) {
            bankingController.executeCommand("text IDIDI Dale")
        }
    }
}