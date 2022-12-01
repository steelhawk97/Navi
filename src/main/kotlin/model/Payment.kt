package model

data class Payment (
    val bankName: String,
    val borrowerName: String,
    val lumpSumAmount: Long,
    val numEmi: Long
)