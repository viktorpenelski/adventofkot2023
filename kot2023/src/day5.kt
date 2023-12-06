import java.io.File
import kotlin.streams.asStream


data class RecordRange(val destination: Long, val source: Long, val length: Long) {

    fun isInRange(targetSource: Long): Boolean {
        return targetSource >= source && targetSource < source + length
    }

    fun map(source: Long): Long {
        if (!isInRange(source)) {
            throw IllegalArgumentException("Source $source is not in range $this")
        }
        val sourceDiff = source - this.source
        return destination + sourceDiff
    }

    companion object {
        fun fromString(line: String): RecordRange {
            val split = line.split("\\s+".toRegex())
            return RecordRange(split[0].trim().toLong(), split[1].trim().toLong(), split[2].trim().toLong())
        }
    }
}

data class Mapper(val ranges: List<RecordRange>) {
    fun map(destination: Long): Long {
        val range = ranges.firstOrNull { it.isInRange(destination) }
        if (range == null) {
            return destination
        }
        return range.map(destination)
    }
}

fun mapSeeds(seeds: List<Long>, mappers: List<Mapper>): List<Long> {
    val seedLocations = mutableListOf<Long>()
    for (seed in seeds) {
        var seedState = seed
        for (mapper in mappers) {
            seedState = mapper.map(seedState)
        }
        seedLocations.add(seedState)
    }
    return seedLocations
}

fun createMappers(inputs: List<String>): List<Mapper> {
    val mappers = mutableListOf<Mapper>()
    for (i in 1..< inputs.size) {
        val sectionLines = inputs[i].lines()
        val mapper = Mapper(sectionLines.drop(1).map { RecordRange.fromString(it) })
        mappers.add(mapper)
    }
    return mappers
}

private fun pt1(inputsPath: String): Long {
    val inputs = File(inputsPath).readText().split("${System.lineSeparator()}${System.lineSeparator()}")
    val seeds = inputs[0].substringAfter("seeds: ").split("\\s+".toRegex()).map { it.toLong() }
    val mappers = createMappers(inputs)
    val seedLocations = mapSeeds(seeds, mappers)

    println(seedLocations)
    return seedLocations.min()
}

private fun pt2(inputsPath: String): Long {
    val inputs = File(inputsPath).readText().split("${System.lineSeparator()}${System.lineSeparator()}")
    val mappers = createMappers(inputs)
    val seeds = inputs[0].substringAfter("seeds: ").split("\\s+".toRegex()).map { it.toLong() }
    val seedRanges = seeds.chunked(2).map { it[0] to it[1] }
    println(seedRanges)
    return seedRanges
        .onEach { println("starting for $it") }
        .map {
        val seedRange = it.first..<it.first+it.second
        seedRange.asSequence().asStream().parallel().map { seed ->
            var seedState = seed
            for (mapper in mappers) {
                seedState = mapper.map(seedState)
            }
            seedState
        }.min { a, b -> a.compareTo(b) }.get()
    }.onEach { println("finished for $it") }
        .min()

}

fun main() {
    println(pt1("inputs/day5.txt"))
    timed {
        println(pt2("inputs/day5.txt"))
    }
}