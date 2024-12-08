package day08

import utils.Point
import utils.checkResult
import utils.map.Map2D
import utils.readInput
import kotlin.math.abs

data class Cell(val c: Char, val pt: Point, val antinode: Boolean = false) {
    override fun toString() = if(antinode) "#" else c.toString()
}

fun Map2D<Cell>.antennas() = filter { it.c != '.' }.groupBy { cell -> cell.c }

fun Map2D<Cell>.simulate() {
    antennas().forEach { (f, cells) ->
        cells.forEach { c0 ->
            cells.forEach { c1 ->
                if(c0 != c1) {
                    val diff = c1.pt - c0.pt
                    val target = c1.pt + diff
                    if(contains(target)) {
                        set(target, get(target).copy(antinode = true))
                    }
                }
            }
        }
    }
}

fun Map2D<Cell>.simulate2() {
    antennas().forEach { (f, cells) ->
        cells.forEach { c0 ->
            cells.forEach { c1 ->
                if(c0 != c1) {
                    val diff = c1.pt - c0.pt
                    var target = c1.pt

                    while(contains(target)) {
                        set(target, get(target).copy(antinode = true))
                        target += diff
                    }
                }
            }
        }
    }
}

fun part1(lines: List<String>): Int {
    val map = Map2D.readFromLines(lines) { c, x, y -> Cell(c, Point(x,y))}

    map.simulate()

    return map.count { it.antinode }
}

fun part2(lines: List<String>): Int {
    val map = Map2D.readFromLines(lines) { c, x, y -> Cell(c, Point(x,y))}

    map.simulate2()
    map.print()

    return map.count { it.antinode }
}

fun main() {
    val testInput = readInput("day08/test")
    checkResult(part1(testInput), 14)
    checkResult(part2(testInput), 34)

    val input = readInput("day08/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
