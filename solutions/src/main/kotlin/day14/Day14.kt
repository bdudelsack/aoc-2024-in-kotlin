package day14

import utils.Point
import utils.checkResult
import utils.readInput



class RobotMap(val width: Int, val height: Int, lines: List<String>) {
    val robots: List<Robot> = buildList {
        lines.forEach { line ->
            val (p0,p1,v0,v1) = Regex("p=([0-9]+),([0-9]+) v=(-?[0-9]+),(-?[0-9]+)").find(line)!!.destructured
            add(Robot(Point(p0.toInt(),p1.toInt()), Point(v0.toInt(),v1.toInt())))
        }
    }

    fun positions(): List<Point> {
        return robots.map { it.position }
    }

    fun solve(rounds: Int) {
        repeat(rounds) {
            with(this) {
                robots.forEach { it.move() }
            }
        }
    }

    fun solve2(): Int {
        var count = 1
        while(true) {
            with(this) {
                robots.forEach { it.move() }
            }

            if(positions().groupBy { it }.size == robots.size) {
                break
            }

            count++
        }

        return count
    }

    fun safetyFactor(): Int {
        val cw = width / 2
        val ch = height / 2

        var c0: Int = 0
        var c1: Int = 0
        var c2: Int = 0
        var c3: Int = 0

        val counts = positions().groupingBy { it }.eachCount()

        counts.forEach { (pt, count) ->
            when {
                pt.x < cw && pt.y < ch -> { c0 += count }
                pt.x > cw && pt.y < ch -> { c1 += count }
                pt.x < cw && pt.y > ch -> { c2 += count }
                pt.x > cw && pt.y > ch -> { c3 += count }
            }
        }

        return c0 * c1 * c2 * c3
    }

    override fun toString(): String {
        val counts = positions().groupingBy { it }.eachCount()
        return buildString {
            repeat(height) { y ->
                repeat(width) { x ->
                    append(counts[Point(x, y)] ?: "." )
//                    append(" ")
                }
                appendLine()
            }
        }
    }
}

data class Robot(var position: Point, val velocity: Point) {

    context(RobotMap)
    fun move() {
        position += velocity

        if(position.x >= width) {
            position -= Point(width, 0)
        }

        if(position.y >= height) {
            position -= Point(0, height)
        }

        if(position.x < 0) {
            position += Point(width, 0)
        }

        if(position.y < 0) {
            position += Point(0, height)
        }
    }
}

fun part1(lines: List<String>, width: Int, height: Int): Int {
    val map = RobotMap(width,height, lines)
    map.solve(100)

    return map.safetyFactor()
}



fun part2(lines: List<String>, width: Int, height: Int): Int {
    val map = RobotMap(width,height, lines)

    return map.solve2()
}

fun main() {
    val testInput = readInput("day14/test")
    checkResult(part1(testInput, 11, 7), 12)

    val input = readInput("day14/input")
    println("Part 1: ${part1(input, 101, 103)}")
    println("Part 2: ${part2(input, 101, 103)}")
}
