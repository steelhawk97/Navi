package service

import helper.BadRequestException
import helper.InvalidRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class PaymentActionsTest {

    @Test
    fun `incorrect input for payment should fail` () {
        val paymentActions = PaymentActions(LoanActions())
        assertThrows(InvalidRequestException::class.java) {
            paymentActions.addPayment("PAYMENT MBI Harry 5000")
        }
        assertThrows(InvalidRequestException::class.java) {
            paymentActions.addPayment("PAYMENT MBI Harry text 10")
        }
        assertEquals(paymentActions.getPaymentsForBankAndBorrowerTillNumEmi("MBI", "Harry", 12).size, 0)
    }

    @Test
    fun `payment where loan does not exist should fail`() {
        val paymentActions = PaymentActions(LoanActions())
        assertThrows(BadRequestException::class.java) {
            paymentActions.addPayment("PAYMENT MBI Harry 5000 10")
        }
    }

    @Test
    fun `correct input for payment` () {
        val loanActions = LoanActions()
        val paymentActions = PaymentActions(loanActions)
        loanActions.addLoan("LOAN MBI Harry 10000 3 7")
        paymentActions.addPayment("PAYMENT MBI Harry 5000 10")
        val payments = paymentActions.getPaymentsForBankAndBorrowerTillNumEmi("MBI", "Harry", 12)
        assertEquals(payments.size, 1)
        assertEquals(paymentActions.getTotalAmountForPayments(payments), 5000)
        assertEquals(paymentActions.getTotalAmountForPayments(listOf()), 0)
    }
}