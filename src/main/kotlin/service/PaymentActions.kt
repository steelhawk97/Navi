package service

import helper.BadRequestException
import helper.Helper
import helper.InvalidRequestException
import model.Payment
import model.PaymentInputEnum

class PaymentActions (private val loanActions: LoanActions) {

    private val payments = mutableListOf<Payment>()

    companion object {
        val numInputParamsForPayment = PaymentInputEnum.values().size + 1
    }

    private fun validatePayment(payment: Payment) {
        loanActions.getLoansByBankAndBorrower(
            bankName = payment.bankName,
            borrowerName = payment.borrowerName
        ).firstOrNull() ?: throw BadRequestException(
            "Loan does not exist for bank/borrower combination for ${payment.bankName}/${payment.borrowerName}"
        )
    }

    private fun getPaymentObjectFromString(inputString: String): Payment {
        val input = Helper.getInputValues(inputString, numInputParamsForPayment)
        return try {
            Payment(
                bankName = input[PaymentInputEnum.BANK_NAME.index],
                borrowerName = input[PaymentInputEnum.BORROWER_NAME.index],
                lumpSumAmount = (input[PaymentInputEnum.LUM_SUM_AMOUNT.index].toLong()),
                numEmi = input[PaymentInputEnum.NUM_EMI.index].toLong()
            )
        } catch (e: Exception) {
            throw InvalidRequestException(e.message)
        }
    }

    fun getTotalAmountForPayments (payments: List<Payment>): Long {
        return payments.sumOf {it.lumpSumAmount }
    }

    fun getPaymentsForBankAndBorrowerTillNumEmi (bankName: String, borrowerName: String, numEmi: Long) : List<Payment> {
        return this.payments.filter { it.bankName ==  bankName && it.borrowerName == borrowerName && it.numEmi <= numEmi}
    }

    fun addPayment(inputString: String) {
        val payment = getPaymentObjectFromString(inputString)
        validatePayment(payment)
        this.payments.add(payment)
    }
}