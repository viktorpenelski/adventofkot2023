import java.io.File

data class GameRun(val green: Int?, val red: Int?, val blue: Int?) {

    fun validate(): Boolean {
        if (red != null && red > 12) return false
        if (green != null && green > 13) return false
        if (blue != null && blue > 14) return false

        return true
    }

    fun pow(): Int {
        return (green ?: 0) * (red ?: 0) * (blue ?: 0)
    }

    companion object {
        fun fromString(line: String): GameRun {
            val cubes = line.split(", ")
            var green: Int? = null
            var red: Int? = null
            var blue: Int? = null
            for (cube in cubes) {
                if (cube.contains("green")) {
                    green = cube.substringBefore(" green").toInt()
                } else if (cube.contains("red")) {
                    red = cube.substringBefore(" red").toInt()
                } else if (cube.contains("blue")) {
                    blue = cube.substringBefore(" blue").toInt()
                }
            }
            return GameRun(green, red, blue)
        }
    }
}

fun List<GameRun>.min(): GameRun {
    var minRed = 0
    var minGreen = 0
    var minBlue = 0
    for (gameRun in this) {
        if (gameRun.green != null && gameRun.green > minGreen) {
            minGreen = gameRun.green
        }
        if (gameRun.red != null && gameRun.red > minRed) {
            minRed = gameRun.red
        }
        if (gameRun.blue != null && gameRun.blue > minBlue) {
            minBlue = gameRun.blue
        }
    }
    return GameRun(minGreen, minRed, minBlue)
}

fun parseGameLine(line: String): Pair<Int, List<GameRun>> {
    val id = line.substringBefore(": ").substringAfter("Game ").toInt()
    val gameRuns = line.substringAfter(": ")
        .split("; ")
        .map { GameRun.fromString(it) }
    return Pair(id, gameRuns)
}

private fun pt1(inputsPath: String): Int {
    return File(inputsPath).readLines().map { parseGameLine(it) }
        .filter { pair -> pair.second.all { it.validate() } }
        .sumOf { it.first }
}

private fun pt2(inputsPath: String): Int {
    return File(inputsPath).readLines().map { parseGameLine(it) }
        .map { it.second.min() }
        .sumOf { it.pow() }
}

fun main() {
    println(pt1("inputs/day2.txt"))
    println(pt2("inputs/day2.txt"))
}