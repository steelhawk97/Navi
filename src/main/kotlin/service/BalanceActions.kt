package service

import helper.BadRequestException
import helper.Helper
import helper.InvalidRequestException
import model.Balance
import model.BalanceInputEnum
import model.BalanceRequest
import kotlin.math.ceil

class BalanceActions(
    private val loanActions: LoanActions,
    private val paymentActions: PaymentActions
) {
    companion object {
        val numInputParamsForBalance = BalanceInputEnum.values().size + 1
    }

    private fun getBalanceRequestObjectFromString(inputString: String): BalanceRequest {
        val input = Helper.getInputValues(inputString, numInputParamsForBalance)
        return try {
            BalanceRequest(
                bankName = input[BalanceInputEnum.BANK_NAME.index],
                borrowerName = input[BalanceInputEnum.BORROWER_NAME.index],
                numEmiPaid = input[BalanceInputEnum.NUM_EMI_PAID.index].toLong()
            )
        } catch (e: Exception) {
            throw InvalidRequestException(e.message)
        }
    }

    private fun calculateTotalAmountPayed(
        monthlyEmi: Long,
        lumSumPayed: Long,
        numEmi: Long,
        totalLoanAmount: Long
    ): Long {
        val calculatedTotalAmountPaid = (monthlyEmi * numEmi) + lumSumPayed
        return if ( calculatedTotalAmountPaid > totalLoanAmount) {
            totalLoanAmount
        } else {
            calculatedTotalAmountPaid
        }
    }

    private fun calculateNumRemainingInstallments(
        totalLoanAmount: Long,
        totalAmountPaid: Long,
        monthlyEmi: Long
    ): Long {
        return ceil((totalLoanAmount - totalAmountPaid) / (monthlyEmi * 1.0)).toLong()
    }

    fun getBalance(inputString: String): Balance {
        val balanceRequest = getBalanceRequestObjectFromString(inputString)
        val validLoan = loanActions.getLoansByBankAndBorrower(
            bankName = balanceRequest.bankName,
            borrowerName = balanceRequest.borrowerName
        ).firstOrNull()
            ?: throw BadRequestException("Loan does not exists for bank/borrower combination. " +
                    "${balanceRequest.bankName}/${balanceRequest.borrowerName}\"")
        val validPayments = paymentActions.getPaymentsForBankAndBorrowerTillNumEmi(
            bankName = balanceRequest.bankName,
            borrowerName = balanceRequest.borrowerName,
            numEmi = balanceRequest.numEmiPaid,
        )
        val totalLoanAmount = loanActions.getTotalLoanAmount(validLoan)
        val calculatedMonthlyEmi = loanActions.getMonthlyEMIAmount(validLoan)
        val lumSumPayed = paymentActions.getTotalAmountForPayments(validPayments)

        val totalAmountPaid = calculateTotalAmountPayed(
            monthlyEmi = calculatedMonthlyEmi,
            lumSumPayed = lumSumPayed,
            numEmi = balanceRequest.numEmiPaid,
            totalLoanAmount = totalLoanAmount
        )
        val numRemainingEmi = calculateNumRemainingInstallments(
            totalLoanAmount =  totalLoanAmount,
            totalAmountPaid = totalAmountPaid,
            monthlyEmi = calculatedMonthlyEmi
        )

        return Balance(
            bankName = balanceRequest.bankName,
            borrowerName = balanceRequest.borrowerName,
            totalAmountPaid = totalAmountPaid,
            numEmiLeft = numRemainingEmi
        )
    }
}