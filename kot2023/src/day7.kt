import java.io.File

enum class HandStrength(val strength: Int) {
    HIGH_CARD(1),
    ONE_PAIR(2),
    TWO_PAIRS(3),
    THREE_OF_A_KIND(4),
    FULL_HOUSE(5),
    FOUR_OF_A_KIND(6),
    FIVE_OF_A_KIND(7);

    companion object {
        fun fromString(cards: String, wildcards: Boolean): HandStrength {
            if (cards == "JJJJJ") return FIVE_OF_A_KIND

            val distinctCards = if (!wildcards) {
                cards.groupBy { it }.map { it.value.size }.sortedDescending()
            } else {
                val jokers = cards.count { it == 'J' }
                val tmp = cards.filterNot { it == 'J' }.groupBy { it }.map { it.value.size }.sortedDescending()
                    .toMutableList()
                tmp[0] = tmp[0] + jokers
                tmp.toList()
            }

            return when (distinctCards) {
                listOf(1, 1, 1, 1, 1) -> HIGH_CARD
                listOf(2, 1, 1, 1) -> ONE_PAIR
                listOf(2, 2, 1) -> TWO_PAIRS
                listOf(3, 1, 1) -> THREE_OF_A_KIND
                listOf(3, 2) -> FULL_HOUSE
                listOf(4, 1) -> FOUR_OF_A_KIND
                listOf(5) -> FIVE_OF_A_KIND
                else -> throw Exception("Invalid hand")
            }
        }
    }
}

private val CARD_RANKS = mapOf(
    '2' to 2,
    '3' to 3,
    '4' to 4,
    '5' to 5,
    '6' to 6,
    '7' to 7,
    '8' to 8,
    '9' to 9,
    'T' to 10,
    'J' to 11,
    'Q' to 12,
    'K' to 13,
    'A' to 14
)

private val CARD_RANKS_PT_2 = CARD_RANKS.map {
    if (it.key != 'J') {
        it.key to it.value
    } else {
        'J' to 0
    }
}.toMap()

data class CamelHand(val cards: String, val bet: Int, val wildcards: Boolean) {

    val strength: HandStrength = HandStrength.fromString(cards, wildcards)

    companion object {
        fun fromInputLine(line: String, wildcards: Boolean): CamelHand {
            val split = line.split("\\s+".toRegex())
            val cards = split[0]
            val bet = split[1].toInt()
            return CamelHand(cards, bet, wildcards)
        }

        fun comparator(wildcards: Boolean) = object : Comparator<CamelHand> {
            private val cardRanks = if (wildcards) CARD_RANKS_PT_2 else CARD_RANKS
            override fun compare(h1: CamelHand, h2: CamelHand): Int {
                val strengthComparison = h1.strength.strength.compareTo(h2.strength.strength)
                if (strengthComparison != 0) {
                    return strengthComparison
                }
                for (i in 0..h1.cards.lastIndex) {
                    val inOrderCardComparison = cardRanks[h1.cards[i]]!!.compareTo(cardRanks[h2.cards[i]]!!)
                    if (inOrderCardComparison != 0) {
                        return inOrderCardComparison
                    }
                }
                return 0
            }

        }
    }
}


private fun pt1(inputsPath: String): Int {
    return File(inputsPath).readLines()
        .map { CamelHand.fromInputLine(it, false) }
        .sortedWith(CamelHand.comparator(false))
        .mapIndexed { index, camelHand -> camelHand.bet * (index + 1) }
        .sum()
}

private fun pt2(inputsPath: String): Int {
    return File(inputsPath).readLines()
        .map { CamelHand.fromInputLine(it, true) }
        .sortedWith(CamelHand.comparator(true))
        .mapIndexed { index, camelHand -> camelHand.bet * (index + 1) }
        .sum()
}

fun main() {
    println(pt1("inputs/day7.txt"))
    println(pt2("inputs/day7.txt"))
}