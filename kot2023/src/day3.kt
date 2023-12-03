import java.io.File

enum class LocType {
    NUMBER, SYMBOL
}

data class Loc(
    val startX: Int,
    val endX: Int,
    val y: Int,
    val value: String,
    val type: LocType
)

fun locFromLine(line: String, y: Int): List<Loc> {
    val locs = mutableListOf<Loc>()
    var i = 0
    while (i < line.length) {
        val ch = line[i]
        if (ch == '.') {
            i++
            continue
        }
        if (ch.isDigit()) {
            val startX = i
            var next = i + 1
            while (next < line.length && line[next].isDigit()) {
                next++
            }
            locs.add(Loc(startX, next - 1, y, line.substring(startX, next), LocType.NUMBER))
            i = next
        } else {
            locs.add(Loc(i, i, y, ch.toString(), LocType.SYMBOL))
            i++
        }
    }
    return locs
}

private fun adjacentSymbols(symbolMap: Map<Int, List<Loc>>, loc: Loc): List<Loc> {
    val adjacentSymbols = mutableListOf<Loc>()
    for (y in loc.y-1..loc.y+1) {
        if (symbolMap[y] == null) continue
        adjacentSymbols.addAll(symbolMap[y]!!.filter { it.startX >= loc.startX - 1 && it.endX <= loc.endX + 1 })
    }
    return adjacentSymbols
}

private fun pt1(inputsPath: String): Int {
    val locs = File(inputsPath).readLines().mapIndexed { i, line -> locFromLine(line, i) }.flatten()

    val symbolMap = locs.filter { it.type == LocType.SYMBOL }.groupBy { it.y }

    return locs
        .filter { it.type == LocType.NUMBER }
        .filter { adjacentSymbols(symbolMap, it).isNotEmpty() }
        .sumOf { it.value.toInt() }
}


private fun potentialGears(valuesMap: Map<Int, List<Loc>>, loc: Loc): List<Loc> {
    val potentialGears = mutableListOf<Loc>()
    for (y in loc.y-1..loc.y+1) {
        if (valuesMap[y] == null) continue
        potentialGears.addAll(
            valuesMap[y]!!.filter { loc.startX in (it.startX-1)..(it.endX+1)}
        )
    }
    return potentialGears
}

private fun pt2(inputsPath: String): Int {
    val locs = File(inputsPath).readLines().mapIndexed { i, line -> locFromLine(line, i) }.flatten()

    val valuesMap = locs.filter { it.type == LocType.NUMBER }.groupBy { it.y }
    val symbolsByType = locs.filter { it.type == LocType.SYMBOL }.groupBy { it.value }

    return symbolsByType["*"]!!
        .map { potentialGears(valuesMap, it) }
        .filter { it.size == 2 }
        .sumOf { it[0].value.toInt() * it[1].value.toInt() }
}

fun main() {
    println(pt1("inputs/day3.txt"))
    println(pt2("inputs/day3.txt"))
}