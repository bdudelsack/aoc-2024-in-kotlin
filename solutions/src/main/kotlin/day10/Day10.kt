package day10

import utils.Point
import utils.checkResult
import utils.map.Map2D
import utils.readInput

sealed class Cell

data object Block : Cell() {
    override fun toString() = "."
}
data class Path(val height: Int) : Cell() {
    override fun toString() = height.toString()
}

enum class Direction(val pt: Point) {
    UP(Point(0,-1)),
    DOWN(Point(0,1)),
    LEFT(Point(-1,0)),
    RIGHT(Point(1,0))
}

fun readMap(lines: List<String>): Map2D<Cell> {
    return Map2D.readFromLines(lines) { it, _, _ ->
        when(it) {
            '.' -> Block
            else -> Path(it.digitToInt())
        }
    }
}

fun Map2D<Cell>.solve(distinct: Boolean = true): Int {
    val trailheads = findPoints { it is Path && it.height == 0 }.toList()

    return trailheads.sumOf {
        solve(it.first, distinct)
    }
}

fun Map2D<Cell>.solve(point: Point, distinct: Boolean = true, ends: MutableSet<Point> = mutableSetOf()): Int {
    val cell = get(point) as Path

    if(cell.height == 9 && (!distinct || !ends.contains(point))) {
        ends.add(point)
        return 1
    }

    val next = Direction.entries.filter { contains(point + it.pt) }
        .map { Pair(point + it.pt, get(point + it.pt)) }
        .filter { it.second is Path && (it.second as Path).height == cell.height + 1 }
        .toList()

    return next.sumOf { solve(it.first, distinct, ends) }
}


fun part1(lines: List<String>): Int {
    val map = readMap(lines)
    return map.solve()
}

fun part2(lines: List<String>): Int {
    val map = readMap(lines)
    return map.solve(false)
}

fun main() {
    val testInput = readInput("day10/test")
    checkResult(part1(testInput), 36)
    checkResult(part2(testInput), 81)

    val input = readInput("day10/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
