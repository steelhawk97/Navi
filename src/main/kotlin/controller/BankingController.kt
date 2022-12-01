package controller

import helper.InvalidRequestException
import service.BalanceActions
import service.LoanActions
import service.PaymentActions


class BankingController {
    private val loanActions = LoanActions()
    private val paymentActions = PaymentActions(loanActions)
    private val balanceActions = BalanceActions(loanActions, paymentActions)
    private val actionsMap = mapOf<String, (input: String) -> Any>(
        "LOAN" to loanActions::addLoan,
        "PAYMENT" to paymentActions::addPayment,
        "BALANCE" to this::performBalanceAction
    )

    fun executeCommand(inputString: String) {
        val commandType = inputString.split(" ").firstOrNull()
            ?: throw InvalidRequestException("Cannot identify command type from input $inputString")
        actionsMap[commandType.uppercase()]?.invoke(inputString)
            ?: throw InvalidRequestException("Cannot identify command type from input $inputString")
    }

    private fun performBalanceAction (inputString: String) {
        val balance = balanceActions.getBalance(inputString)
        println("${balance.bankName} ${balance.borrowerName} " +
                "${balance.totalAmountPaid} ${balance.numEmiLeft}")
    }
}