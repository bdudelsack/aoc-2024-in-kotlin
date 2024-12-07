package day06

import day06.Direction.*
import utils.Point
import utils.checkResult
import utils.map.Map2D
import utils.readInput

sealed class Result {
    data class OK(val map: Map2D<Char>, val guardPos: Point, val guardDirection: Direction): Result()
    data object LoopFound : Result()
}

enum class Direction(val p: Point, val c: Char) {
    UP(Point(0, -1), '^'),
    RIGHT(Point(1, 0), '>'),
    DOWN(Point(0, 1), 'v'),
    LEFT(Point(-1, 0), '<')
}

fun Direction.turnRight(): Direction = when(this) {
    UP -> RIGHT
    RIGHT -> DOWN
    DOWN -> LEFT
    LEFT -> UP
}

fun Map2D<Char>.visualize(visited: Map<Point, Set<Direction>>) {
    val sb = StringBuilder()

    for(y in 0 until height) {
        for(x in 0 until width) {
            val char = get(x,y)

            if(char == 'X') {
                val d = visited[Point(x, y)] ?: emptySet()

                sb.append(when {
                    (d.contains(UP) || d.contains(DOWN)) && !d.contains(RIGHT) && !d.contains(LEFT) -> '|'
                    (d.contains(RIGHT) || d.contains(LEFT)) && !d.contains(UP) && !d.contains(DOWN) -> '-'
                    else -> '+'
                })
            } else {
                sb.append(char)
            }

            sb.append("")
        }
        sb.appendLine()
    }

    println(sb.toString())
}

fun simulateGuard(map: Map2D<Char>): Result {
    var guardPos = map.findPoint { it in listOf('^','>','<','v') }!!
    var guardDirection = Direction.entries.find {  it.c == map[guardPos] }!!
    val visited: MutableMap<Point, MutableSet<Direction>> = mutableMapOf()

    while(true) {
        if(visited[guardPos]?.contains(guardDirection) == true) {
//            map.visualize(visited)
            return Result.LoopFound
        }

        map[guardPos] = 'X'

        if(visited[guardPos] == null) {
            visited[guardPos] = mutableSetOf(guardDirection)
        } else {
            visited[guardPos]!! += guardDirection
        }

        if(map.contains(guardPos + guardDirection.p)) {
            if(map[guardPos + guardDirection.p] in listOf('#', 'O')) {
                guardDirection = guardDirection.turnRight()
            }

            visited[guardPos]!! += guardDirection

            guardPos += guardDirection.p
        } else {
            return Result.OK(map, guardPos, guardDirection)
        }
    }
}

fun part1(lines: List<String>): Int {
    val map = Map2D.readFromLines(lines)
    return when(val res = simulateGuard(map)) {
        is Result.LoopFound -> 0
        is Result.OK -> res.map.count { it in listOf('X') }
    }
}

fun part2(lines: List<String>): Int {
    var count = 0
    var total = 0
    val map = Map2D.readFromLines(lines.filter { it.isNotBlank() })
    val guardPos = map.findPoint { it in listOf('^','>','<','v') }!!

    val res = simulateGuard(map.clone())

    (res as Result.OK).map.findPoints { it == 'X' }.forEach { pt ->
        total++
        if(pt.first != guardPos) {
            val mapCopy = map.clone()
            mapCopy[pt.first] = 'O'

            if(simulateGuard(mapCopy) is Result.LoopFound) {
                count++
            }
        }
    }

    println("Checked $total")

    return count
}

fun main() {
    val testInput = readInput("day06/test")
    checkResult(part1(testInput), 41)
    checkResult(part2(testInput), 6)

    val input = readInput("day06/input")
//    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
