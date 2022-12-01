package service

import helper.BadRequestException
import helper.InvalidRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class BalanceActionsTest {

    @Test
    fun `incorrect input for balance should fail`() {
        val loanActions = LoanActions()
        val paymentActions = PaymentActions(loanActions)
        val balanceActions = BalanceActions(loanActions, paymentActions)
        assertThrows(InvalidRequestException::class.java) {
            balanceActions.getBalance("BALANCE IDIDI Dale")
        }
        assertThrows(InvalidRequestException::class.java) {
            balanceActions.getBalance("BALANCE IDIDI Dale text")
        }
    }

    @Test
    fun `loan not exist shoukd fail`() {
        val loanActions = LoanActions()
        val paymentActions = PaymentActions(loanActions)
        val balanceActions = BalanceActions(loanActions, paymentActions)
        assertThrows(BadRequestException::class.java) {
            balanceActions.getBalance("BALANCE IDIDI Dale 3")
        }
    }

    @Test
    fun `correct input for balance`() {
        val loanActions = LoanActions()
        val paymentActions = PaymentActions(loanActions)
        val balanceActions = BalanceActions(loanActions, paymentActions)
        loanActions.addLoan("LOAN IDIDI Dale 5000 1 6")
        paymentActions.addPayment("PAYMENT IDIDI Dale 1000 5")
        var balance = balanceActions.getBalance("BALANCE IDIDI Dale 3")
        assertEquals(balance.totalAmountPaid, 1326)
        assertEquals(balance.numEmiLeft, 9)
        balance = balanceActions.getBalance("BALANCE IDIDI Dale 6")
        assertEquals(balance.totalAmountPaid, 3652)
        assertEquals(balance.numEmiLeft, 4)
        balance = balanceActions.getBalance("BALANCE IDIDI Dale 25")
        assertEquals(balance.totalAmountPaid, 5300)
        assertEquals(balance.numEmiLeft, 0)
    }
}