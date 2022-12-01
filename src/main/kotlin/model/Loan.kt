package model

data class Loan (
    val bankName: String,
    val borrowerName: String,
    val principal: Long,
    val numYears: Long,
    val rateOfInterest: Long
)