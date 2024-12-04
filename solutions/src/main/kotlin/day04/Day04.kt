package day04

import utils.checkResult
import utils.map.Map2D
import utils.readInput

enum class Directions(val dx: Int, val dy: Int) {
    S(0, 1),
    N(0, -1),
    E(1, 0),
    W(-1, 0),

    NE(1, -1),
    SE(1, 1),
    SW(-1, 1),
    NW(-1, -1),
}

fun checkDirection(map: Map2D<Char>, x: Int, y: Int, direction: Directions, needle: String = "XMAS"): Boolean {
    if(needle.isEmpty()) return true
    if(x !in 0 until map.width || y !in 0 until map.height) return false

    if(map[x, y] != needle.first()) return false

    return checkDirection(map, x + direction.dx, y + direction.dy, direction, needle.substring(1))
}

fun checkXMas(map: Map2D<Char>, x: Int, y: Int): Boolean {
    if(x !in 1 until map.width - 1 || y !in 1 until map.height -1) return false

    val check0 = map[x -1, y - 1] == 'M' && map[x + 1, y + 1] == 'S' || map[x - 1, y - 1] == 'S' && map[x + 1, y + 1] == 'M'
    val check1 = map[x + 1, y - 1] == 'M' && map[x - 1, y + 1] == 'S' || map[x + 1, y - 1] == 'S' && map[x - 1, y + 1] == 'M'

    return check0 && check1
}

fun part1(lines: List<String>): Int {
    var count = 0
    val map = Map2D.readFromLines(lines)

    map.forEach { c, x, y ->
        Directions.entries.forEach { direction ->
            if(checkDirection(map, x, y, direction)) {
                count++
            }
        }
    }

    return count
}

fun part2(lines: List<String>): Int {
    var count = 0
    val map = Map2D.readFromLines(lines)

    map.forEach { c, x, y ->
        if(c == 'A' && checkXMas(map, x, y)) {
            count++
        }
    }

    return count
}


fun main() {
    val testInput = readInput("day04/test")
    checkResult(part1(testInput), 18)
    checkResult(part2(testInput), 9)

    val input = readInput("day04/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
