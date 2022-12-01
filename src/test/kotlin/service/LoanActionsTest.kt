package service

import helper.BadRequestException
import helper.InvalidRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class LoanActionsTest {

    @Test
    fun `incorrect input for loan should fail` () {
        val loanActions = LoanActions()
        assertThrows(InvalidRequestException::class.java) {
            loanActions.addLoan("LOAN IDIDI Dale 5000 1")
        }
        assertThrows(InvalidRequestException::class.java) {
            loanActions.addLoan("LOAN IDIDI Dale text 1 6")
        }
    }

    @Test
    fun `duplicate input for loan should fail`() {
        val loanActions = LoanActions()
        loanActions.addLoan("LOAN IDIDI Dale 5000 1 6")
        assertThrows(BadRequestException::class.java) {
            loanActions.addLoan("LOAN IDIDI Dale 5000 1 6")
        }
    }

    @Test
    fun `correct input for loan` () {
        val loanActions = LoanActions()
        assertEquals(loanActions.getLoansByBankAndBorrower("IDIDI", "Dale").size, 0)
        loanActions.addLoan("LOAN IDIDI Dale 5000 1 6")
        val loan = loanActions.getLoansByBankAndBorrower("IDIDI", "Dale").firstOrNull()
        assertNotNull(loan)
        assertEquals(loanActions.getTotalLoanAmount(loan!!), 5300)
        assertEquals(loanActions.getMonthlyEMIAmount(loan), 442)
    }
}