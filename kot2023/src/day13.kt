import java.io.File



fun summarize(island: List<String>, originalSummary: Int? = null): Int {
    // look for horizontal symmetry
    for (row in 0..<island.lastIndex) {
        if (island[row] == island[row+1]) {
            var allEqual = true
            var up = row - 1
            var down = row + 2
            while (up >= 0 && down < island.size) {
                if (island[up] != island[down]) {
                    allEqual = false
                    break
                }
                up--
                down++
            }
            val ans = 100 * (row + 1)
            if (allEqual && (originalSummary == null || ans != originalSummary)) return ans
        }
    }

    // look for vertical symmetry
    for (col in 0..<island[0].lastIndex) {
        var current = island.map { it[col] }
        var next = island.map { it[col+1] }
        if (current == next) {
            var allEqual = true
            var left = col - 1
            var right = col + 2
            while (left >= 0 && right < island[0].length) {
                current = island.map { it[left] }
                next = island.map { it[right] }
                if (current != next) {
                    allEqual = false
                    break
                }
                left--
                right++
            }
            val ans = col + 1
            if (allEqual && (originalSummary == null || ans != originalSummary)) return ans
        }
    }

    throw Exception("No symmetry found")
}

fun smugify(island: List<String>): Int {
    val originalSummary = summarize(island)

    for (row in island.indices) {
        for (col in 0..island[row].lastIndex) {
            val mut = island.toMutableList()
            val mutableRow = island[row].toCharArray()
            mutableRow[col] = if(island[row][col] == '.') '#' else '.'
            mut[row] = mutableRow.joinToString("")

            try {
                val summary = summarize(mut, originalSummary)
                if (summary == originalSummary) {
                    println("continue, as we found the original summary")
                    continue
                }
                println("mutated: r$row c$col ${mut[row]}")
                println("about to return $summary for: \n$mut")
                return summary
            } catch (e: Exception) {
                println("Failed to find symmetry at $row, $col")
                continue
            }
        }
    }
    throw Exception("ERROR: No symmetry found")
}


fun main() {
//    val summaries = File("inputs/day13.txt")
//        .readText()
//        .split("${System.lineSeparator()}${System.lineSeparator()}")
//        .map { it.split(System.lineSeparator()) }
//        .sumOf { summarize(it) }
//    println(summaries)

    val smuggifiedSummaries = File("inputs/day13.txt")
        .readText()
        .split("${System.lineSeparator()}${System.lineSeparator()}")
        .map { it.split(System.lineSeparator()) }
        .sumOf { smugify(it) }
    println(smuggifiedSummaries)


}
