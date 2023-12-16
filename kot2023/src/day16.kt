import java.io.File


data class Beam(val position: Pair<Int, Int>, val direction: Pair<Int, Int>) {
    fun isInBounds(board: List<String>): Boolean {
        return position.first >= 0
                && position.first < board.size
                && position.second >= 0
                && position.second < board[0].length
    }
}

fun simulate(board: List<String>, startingBeam: Beam): Int {

    var beams = mutableListOf(startingBeam)
    val visited = mutableSetOf<Beam>()

    while (beams.size > 0) {
        val nextBeams = mutableListOf<Beam>()
        for (beam in beams) {
            visited.add(beam)
            val currentTile = board[beam.position.first][beam.position.second]
            val nextDirection: List<Pair<Int, Int>> = when (currentTile) {
                '.' -> listOf(beam.direction)
                '/' -> when (beam.direction) {
                    Pair(0, 1) -> listOf(Pair(-1, 0))
                    Pair(0, -1) -> listOf(Pair(1, 0))
                    Pair(1, 0) -> listOf(Pair(0, -1))
                    Pair(-1, 0) -> listOf(Pair(0, 1))
                    else -> throw Exception("invalid direction")
                }

                '\\' -> when (beam.direction) {
                    Pair(0, 1) -> listOf(Pair(1, 0))
                    Pair(0, -1) -> listOf(Pair(-1, 0))
                    Pair(1, 0) -> listOf(Pair(0, 1))
                    Pair(-1, 0) -> listOf(Pair(0, -1))
                    else -> throw Exception("invalid direction")
                }

                '-' -> when (beam.direction) {
                    Pair(0, 1) -> listOf(beam.direction) // right
                    Pair(0, -1) -> listOf(beam.direction) // left
                    Pair(1, 0) -> listOf(Pair(0, 1), Pair(0, -1)) //down
                    Pair(-1, 0) -> listOf(Pair(0, 1), Pair(0, -1)) // up
                    else -> throw Exception("invalid direction")
                }

                '|' -> when (beam.direction) {
                    Pair(0, 1) -> listOf(Pair(1, 0), Pair(-1, 0)) // right
                    Pair(0, -1) -> listOf(Pair(1, 0), Pair(-1, 0)) // left
                    Pair(1, 0) -> listOf(beam.direction) //down
                    Pair(-1, 0) -> listOf(beam.direction) //up
                    else -> throw Exception("invalid direction")
                }

                else -> throw Exception("invalid character")
            }
            for (direction in nextDirection) {
                nextBeams.add(
                    Beam(
                        Pair(beam.position.first + direction.first, beam.position.second + direction.second),
                        direction
                    )
                )
            }
        }
        beams = nextBeams.filter { it.isInBounds(board) }.filter { !visited.contains(it) }.toMutableList()
    }
    val visitedPositions = visited.map { it.position }.distinct()
    return visitedPositions.size

}

fun main() {
    val lines = File("inputs/day16.txt").readLines()
    println(simulate(lines, Beam(Pair(0, 0), Pair(0, 1))))
    val possibleStartingBeams = mutableListOf<Beam>()
    for (row in 0..lines.lastIndex) {
        possibleStartingBeams.add(Beam(Pair(row, 0), Pair(0, 1)))
        possibleStartingBeams.add(Beam(Pair(row, lines[row].lastIndex), Pair(0, -1)))
    }
    for (col in 0..lines.lastIndex) {
        possibleStartingBeams.add(Beam(Pair(0, col), Pair(1, 0)))
        possibleStartingBeams.add(Beam(Pair(lines.lastIndex, col), Pair(-1, 0)))
    }

    println(possibleStartingBeams.maxOfOrNull { simulate(lines, it) })
}