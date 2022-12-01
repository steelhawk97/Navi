package model

data class BalanceRequest (
    val bankName: String,
    val borrowerName: String,
    val numEmiPaid: Long
)