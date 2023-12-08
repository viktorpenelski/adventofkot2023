import java.io.File

data class Node(val value: String, val left: String, val right: String)

private fun constructNodeMap(inputLines: List<String>): Map<String, Node> {
    val nodeMap: MutableMap<String, Node> = mutableMapOf()
    inputLines.forEach {
        val nodeId = it.substringBefore(" =")
        if (!nodeMap.containsKey(nodeId)) {
            val left = it.substringAfter("= (").substringBefore(",")
            val right = it.substringAfter(", ").substringBefore((")"))
            nodeMap[nodeId] = Node(nodeId, left, right)
        }
    }
    return nodeMap.toMap()
}


private fun pt1(instructions: String, nodeMap: Map<String, Node>): Int {
    var node = nodeMap["AAA"]!!
    val targetNode = nodeMap["ZZZ"]!!
    var idx = 0
    while (node != targetNode) {
        val instruction = instructions[idx % instructions.length]
        node = when (instruction) {
            'L' -> nodeMap[node.left]!!
            'R' -> nodeMap[node.right]!!
            else -> throw Exception("invalid instruction $instruction")
        }
        idx += 1
    }

    return idx
}

private fun findLeastCommonMultiple(first: Long, second: Long): Long {
    val larger = if (first > second) first else second
    val maxLcm = first * second
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % first == 0L && lcm % second == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

private fun pt2(instructions: String, nodeMap: Map<String, Node>): Long {
    val nodes = nodeMap.filter { it.key.endsWith('A') }.values.toMutableList()
    val nodesSize = nodes.size
    var idx = 0
    val firstZ = mutableListOf<Long>()
    while (firstZ.size < nodesSize) {
        val instruction = instructions[idx % instructions.length]
        val nodesToRemove = mutableListOf<Node>()
        for (i in 0..nodes.lastIndex) {
            if (nodes[i].value.endsWith('Z')) {
                firstZ.add(idx.toLong())
                nodesToRemove.add(nodes[i])
            } else {
                nodes[i] = when (instruction) {
                    'L' -> nodeMap[nodes[i].left]!!
                    'R' -> nodeMap[nodes[i].right]!!
                    else -> throw Exception("invalid instruction $instruction")
                }
            }
        }
        nodes.removeAll(nodesToRemove)
        idx += 1
    }
    var res = firstZ[0]
    for (i in 1 until firstZ.size) {
        res = findLeastCommonMultiple(res, firstZ[i])
    }
    return res
}

fun main() {
    val inputs = File("kot2023/inputs/day8.txt").readLines()
    val instructions = inputs[0]
    val nodeMap = constructNodeMap(inputs.drop(2))
    println(pt1(instructions, nodeMap))
    println(pt2(instructions, nodeMap))
}