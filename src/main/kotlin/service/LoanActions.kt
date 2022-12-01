package service

import helper.BadRequestException
import helper.Helper
import helper.InvalidRequestException
import model.Loan
import model.LoanInputEnum
import kotlin.math.ceil

class LoanActions {

    private val loans = mutableListOf<Loan>()

    companion object {
        val numInputParamsForLoan = LoanInputEnum.values().size + 1
        const val denominatorForInterestCalculation = 10_000.0
        const val numMonthsInAYear = 12.0
    }

    private fun verifyLoan (loan: Loan) {
        val existingLoans = getLoansByBankAndBorrower(bankName = loan.bankName, borrowerName = loan.borrowerName)
        if (existingLoans.isNotEmpty()) {
            throw BadRequestException(
                "Loan exists for bank/borrower combination for ${loan.bankName}/${loan.borrowerName}"
            )
        }
    }

    private fun verifyAndAddLoan(loan: Loan) {
        verifyLoan(loan)
        this.loans.add(loan)
    }

    private fun getLoanObjectFromString(inputString: String): Loan {
        val input = Helper.getInputValues(inputString, numInputParamsForLoan)
        return try {
            Loan(
                bankName = input[LoanInputEnum.BANK_NAME.index],
                borrowerName = input[LoanInputEnum.BORROWER_NAME.index],
                principal = (input[LoanInputEnum.PRINCIPAL.index].toLong()),
                numYears = (input[LoanInputEnum.NUM_YEARS.index].toLong()),
                rateOfInterest = (input[LoanInputEnum.RATE_OF_INTEREST.index].toLong() * 100)
            )
        } catch (e: Exception) {
            throw InvalidRequestException(e.message)
        }
    }

    private fun getInterestForLoan(loan: Loan): Long {
        return ceil((loan.principal * loan.numYears * loan.rateOfInterest) / denominatorForInterestCalculation).toLong()
    }

    fun getTotalLoanAmount(loan: Loan): Long {
        val interest = getInterestForLoan(loan)
        return loan.principal + interest
    }

    fun getMonthlyEMIAmount(loan: Loan): Long {
        val totalLoanAmount = getTotalLoanAmount(loan)
        val numMonths = loan.numYears * numMonthsInAYear
        return ceil(totalLoanAmount / numMonths).toLong()
    }

    fun getLoansByBankAndBorrower(bankName: String, borrowerName: String) : List<Loan> {
        return this.loans.filter {
            it.bankName.trim().lowercase() == bankName.trim().lowercase()
                    && it.borrowerName.trim().lowercase() == borrowerName.trim().lowercase()
        }
    }

    fun addLoan(inputString: String) {
        val loan = getLoanObjectFromString(inputString)
        verifyAndAddLoan(loan)
    }
}