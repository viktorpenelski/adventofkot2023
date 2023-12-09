import java.io.File
import kotlin.math.abs

fun extrapolate(input: List<Int>): Int {
    val diffs = mutableListOf<MutableList<Int>>()
    diffs.add(input.toMutableList())
    while (!diffs.last().all { it == 0 }) {
        if (diffs.last().size == 1) {
            diffs.add(mutableListOf(0))
            break
        }
        val nextLevel = diffs.last()
            .windowed(2)
            .map{it[1] - it[0]}
        diffs.add(nextLevel.toMutableList())
    }

    diffs.last().add(0)

    for (i in diffs.lastIndex downTo 1) {
        val current = diffs[i].last()
        val previous = diffs[i - 1].last()
        diffs[i - 1].add(current + previous)
    }

    return diffs[0].last()
}


fun main() {
    val lines = File("inputs/day9.txt").readLines()
        .map { line ->
            line.split("\\s+".toRegex())
                .map { it.trim() }
                .map { it.toInt() }
        }

    println(lines.sumOf { extrapolate(it) })

}