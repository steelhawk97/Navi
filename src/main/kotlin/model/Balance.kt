package model

data class Balance (
    val bankName: String,
    val borrowerName: String,
    val totalAmountPaid: Long,
    val numEmiLeft: Long
)