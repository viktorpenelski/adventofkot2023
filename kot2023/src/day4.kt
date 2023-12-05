import java.io.File
import kotlin.math.pow

data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val myNumbers: List<Int>
) {

    fun calculateScore(): Int {
        val winningSet = winningNumbers.toSet()
        val scoringNumbers = myNumbers.filter { it in winningSet }
        if (scoringNumbers.isEmpty()) {
            return 0
        }
        return 2.0.pow(scoringNumbers.size.toDouble() - 1).toInt()
    }

    companion object {
        fun fromString(line: String): Card {
            val id = line.substringBefore(": ").substringAfter("Card ").trim().toInt()
            val winningNumbers = line.substringAfter(": ").substringBefore(" |")
                .split("\\s+".toRegex())
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
            val numbers = line.substringAfter("| ")
                .split("\\s+".toRegex())
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
            return Card(id, winningNumbers, numbers)
        }

    }
}

private fun pt1(inputsPath: String): Int {
    return File(inputsPath)
        .readLines()
        .map { line -> Card.fromString(line) }
        .sumOf { it.calculateScore() }
}

private fun pt2(inputsPath: String): Int {
    val mappedCards = File(inputsPath)
        .readLines()
        .map { line -> Card.fromString(line) }
        .associateBy { it.id }

    val cardsStack: ArrayDeque<Card> = ArrayDeque(mappedCards.values)
    var score = 0
    while (cardsStack.isNotEmpty()) {
        val card = cardsStack.removeFirst()
        score += 1
        val winningSet = card.winningNumbers.toSet()
        val matchingNumbers = card.myNumbers.count { it in winningSet }
        for (i in 1..matchingNumbers) {
            val cardToAdd = mappedCards[card.id + i]!!
            cardsStack.add(cardToAdd)
        }
    }
    return score
}

fun main() {
    println(pt1("inputs/day4.txt"))
    println(pt2("inputs/day4.txt"))
}