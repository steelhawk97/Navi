package helper

object Helper {
    fun getInputValues(inputString: String, numExpected: Int): List<String> {
        val input = inputString.split(" ")
        val numInputValues = input.size
        if (numInputValues != numExpected) {
            throw InvalidRequestException(
                "request does not have correct number of values. ($numInputValues/${numExpected})"
            )
        }
        return input
    }
}