import controller.BankingController
import java.io.File

fun main(args: Array<String>) {
    val bankingController = BankingController()
    val inputFile = args.firstOrNull() ?:  "./input.txt"
    File(inputFile).forEachLine {
        if (it.isNotBlank()) bankingController.executeCommand(it.trim())
    }
}
