import java.io.File
import kotlin.math.abs

data class Location(val row: Int, val col: Int)

private fun expandUniverse(input: List<String>): List<List<Char>> {
    val expandedVertically = mutableListOf<String>()
    input.forEachIndexed { idx, line ->
        expandedVertically.add(line)
        if (line.toCharArray().all { it == '.' }) {
            expandedVertically.add(line)
        }
    }

    val expandedHorizontally = MutableList<MutableList<Char>>(expandedVertically.size) { mutableListOf() }
    for (col in 0..expandedVertically[0].lastIndex) {
        var allEmpty = true
        for (row in 0..expandedVertically.lastIndex) {
            val ch = expandedVertically[row][col]
            if (ch != '.') {
                allEmpty = false
            }
            expandedHorizontally[row].add(ch)
        }
        if (allEmpty) {
            for (row in 0..expandedVertically.lastIndex) {
                expandedHorizontally[row].add('.')
            }
        }
    }

    return expandedHorizontally
}

fun distance(a: Location, b: Location) = abs(b.row - a.row) + abs(b.col - a.col)

private fun pt1(universe: List<List<Char>>): Int {
    val galaxies = universe.mapIndexed { row, line ->
        line.mapIndexedNotNull { col, ch ->
            when (ch) {
                '.' -> null
                else -> Location(row, col)
            }
        }
    }.flatten()

    val galaxyPairs = mutableSetOf<Pair<Location, Location>>()
    for (i in 0..<galaxies.lastIndex) {
        for (j in i + 1..galaxies.lastIndex) {
            galaxyPairs.add(Pair(galaxies[i], galaxies[j]))
        }
    }
    return galaxyPairs.sumOf { (a, b) -> distance(a, b) }
}

private fun pt2(lines: List<String>, gap: Int): Long {
    val emptyRowIds = lines.mapIndexedNotNull { idx, line ->
        if (line.all { it == '.' }) {
            idx
        } else {
            null
        }
    }

    val emptyColIds = mutableListOf<Int>()
    for (col in 0..lines[0].lastIndex) {
        var allEmpty = true
        for (row in 0..lines.lastIndex) {
            val ch = lines[row][col]
            if (ch != '.') {
                allEmpty = false
            }
        }
        if (allEmpty) {
            emptyColIds.add(col)
        }
    }
    val galaxyPairs = mutableSetOf<Pair<Location, Location>>()
    val galaxies = lines.mapIndexed { row, line ->
        line.mapIndexedNotNull { col, ch ->
            when (ch) {
                '.' -> null
                else -> Location(row, col)
            }
        }
    }.flatten()
    for (i in 0..<galaxies.lastIndex) {
        for (j in i + 1..galaxies.lastIndex) {
            galaxyPairs.add(Pair(galaxies[i], galaxies[j]))
        }
    }

    return galaxyPairs.sumOf {
        val minRow = minOf(it.first.row, it.second.row)
        val maxRow = maxOf(it.first.row, it.second.row)
        val minCol = minOf(it.first.col, it.second.col)
        val maxCol = maxOf(it.first.col, it.second.col)

        val rowGap = (gap-1) * emptyRowIds.filter { row -> row in minRow..maxRow }.size
        val colGap = (gap-1) * emptyColIds.filter { col -> col in minCol..maxCol }.size

        distance(it.first, it.second) + rowGap + colGap.toLong()
    }
}

fun main() {
    val lines = File("inputs/day11.txt").readLines()
    val expandedUniverse = expandUniverse(lines)

    println(pt1(expandedUniverse))
    println(pt2(lines, 2))
    println(pt2(lines, 1000000))
}