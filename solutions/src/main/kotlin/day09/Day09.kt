package day09

import utils.checkResult
import utils.readInput

sealed class Node {
    abstract var size: Int

    data class Empty(override var size: Int): Node()
    data class File(val id: Int, override var size: Int): Node()

    fun expand(): List<Node> {
        return when(this) {
            is Empty -> List(size) { Empty(1) }
            is File -> List(size) { File(id, 1) }
        }
    }
}

fun List<Node>.visualize() {
    val sb = StringBuilder()

    forEach { node ->
        repeat(node.size) {
            sb.append(if(node is Node.File) node.id else ".")
        }
    }

    println(sb.toString())
}

fun List<Node>.expand(): List<Node> {
    return buildList {
        this@expand.forEach { node -> addAll(node.expand())}
    }
}

fun List<Node>.check(): Boolean {
    var spaceFound = false

    forEach { node ->
        if (node is Node.Empty) {
            spaceFound = true
        } else if(spaceFound) {
            return false
        }
    }

    return true
}

fun MutableList<Node>.swap(i1: Int, i2: Int) {
    val t = get(i1)
    set(i1, get(i2))
    set(i2, t)
}

fun MutableList<Node>.compress() {
    val space = indexOfFirst { it is Node.Empty }
    val file = indexOfLast { it is Node.File }

    swap(space, file)
}

fun MutableList<Node>.compress2(id: Int) {
    val file = findLast { it is Node.File && it.id == id }!!
    val spaceIndex = indexOfFirst { it is Node.Empty && it.size >= file.size }

    if(spaceIndex > 0 && spaceIndex < indexOf(file)) {
        set(indexOf(file), Node.Empty(file.size))
        get(spaceIndex).size -= file.size
        remove(file)
        add(spaceIndex, file)
    }
}

fun List<Node>.checksum(): Long {
    return filter { it.size != 0 }.mapIndexed { index, t -> when(t) {
        is Node.File -> index * t.id.toLong()
        else -> 0
    }
    }.sum()
}

fun parseLine(line: String): List<Node> {
    return List(line.length) {
        val size = line[it].digitToInt()
        if(it % 2 == 0) Node.File(it / 2, size) else Node.Empty(size)
    } + Node.Empty(0)
}

fun part1(lines: List<String>): Long {
    val nodes = parseLine(lines.first())

    val expanded = nodes.expand().toMutableList()

    while(!expanded.check()) {
        expanded.compress()
    }

    expanded.visualize()

    return expanded.checksum()
}

fun part2(lines: List<String>): Long {
    val nodes = parseLine(lines.first()).toMutableList()

    var id = nodes.filterIsInstance<Node.File>().maxOf { it.id }
    while(id > 0) {
        nodes.compress2(id)
        id--
    }

    nodes.visualize()

    return nodes.expand().checksum()
}

fun main() {
    val testInput = readInput("day09/test")
    checkResult(part1(testInput), 1928)
    checkResult(part2(testInput), 2858)

    val input = readInput("day09/input")
//    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
