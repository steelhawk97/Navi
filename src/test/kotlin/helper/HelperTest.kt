package helper

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import service.BalanceActions
import service.LoanActions
import service.PaymentActions

internal class HelperTest {

    @Test
    fun `get input values test`() {
        assertEquals(Helper.getInputValues("BALANCE UON Shelly 12", BalanceActions.numInputParamsForBalance).size, 4)
        assertEquals(Helper.getInputValues("LOAN MBI Harry 10000 3 7", LoanActions.numInputParamsForLoan).size, 6)
        assertEquals(Helper.getInputValues("PAYMENT MBI Harry 5000 10", PaymentActions.numInputParamsForPayment).size, 5)
        Assertions.assertThrows(InvalidRequestException::class.java) {
            Helper.getInputValues("BALANCE UON Shelly", BalanceActions.numInputParamsForBalance)
        }
        Assertions.assertThrows(InvalidRequestException::class.java) {
            Helper.getInputValues("LOAN MBI Harry 10000 3 7 3", LoanActions.numInputParamsForLoan)
        }
        Assertions.assertThrows(InvalidRequestException::class.java) {
            Helper.getInputValues("PAYMENT MBI", PaymentActions.numInputParamsForPayment)
        }
    }
}