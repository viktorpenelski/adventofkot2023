import java.io.File
import java.lang.Character.isDigit


fun pt1(inputsPath: String): Int {
     return File(inputsPath).readLines()
         .map { it.filter(::isDigit) }
         .sumOf { "${it.first()}${it.last()}".toInt() }
}

fun mapCalibrationValueToInt(value: String): Int {
    if (value.length == 1 && value[0].isDigit()) return value.toInt()

    return when(value) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        "zero" -> 0
        else -> throw Exception("Invalid calibration value")
    }
}

fun lineToFirstLastDigit(line: String, matcher: (value: String) -> Pair<String, String>): Int {
    val matches = matcher(line)
    val first = mapCalibrationValueToInt(matches.first)
    val last = mapCalibrationValueToInt(matches.second)
    return "$first$last".toInt()
}

fun pt2(inputsPath: String): Int {
    return File(inputsPath).readLines().sumOf {
        lineToFirstLastDigit(it) {
            val matches = JavaUtils.matchCalibrations(it)
            Pair(matches.first(), matches.last())
        }
    }
}

fun main() {
    println(pt1("inputs/day1.txt"))
    println(pt2("inputs/day1.txt"))
}