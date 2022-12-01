package model

enum class LoanInputEnum (val index: Int) {
    BANK_NAME(1),
    BORROWER_NAME(2),
    PRINCIPAL(3),
    NUM_YEARS(4),
    RATE_OF_INTEREST(5)
}