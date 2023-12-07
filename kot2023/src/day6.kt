import java.io.File

fun allRaces(time: Int): List<Long> {
    val races: MutableList<Long> = mutableListOf()
    var timeLeft = time
    var speed: Long = 0
    while (timeLeft > 0) {
        timeLeft -= 1
        speed += 1
        races.add(timeLeft * speed)
    }
    return races
}

private fun pt1(inputsPath: String): Int {
    val inputs = File(inputsPath).readLines()
    val times = inputs[0].substringAfter("Time:")
        .split("\\s+".toRegex())
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
    val distances =
        inputs[1].substringAfter("Distance:")
            .split("\\s+".toRegex())
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.toInt() }

    return times.zip(distances)
        .map { (time, distance) ->
            allRaces(time).filter { it > distance }.size
        }.reduce { acc, i ->
            acc * i
        }

}

private fun pt2(): Int {
    return allRaces(44826981)
        .filter { it > 202107611381458 }.size
}

fun main() {
    println(pt1("inputs/day6.txt"))
    timed {
        println(pt2())
    }
}