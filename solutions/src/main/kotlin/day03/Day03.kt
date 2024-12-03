package dayXX

import utils.checkResult
import utils.readInput

fun part1(lines: List<String>) = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
    .findAll(lines.joinToString()).sumOf {
        Integer.parseInt(it.groups[1]!!.value) * Integer.parseInt(it.groups[2]!!.value)
    }

fun part2(lines: List<String>) = Regex("""mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""")
    .findAll(lines.joinToString()).fold(Pair(0, true)) { acc, match ->
        when {
            match.groups[0]!!.value.startsWith("don't") -> Pair(acc.first, false)
            match.groups[0]!!.value.startsWith("do") -> Pair(acc.first, true)
            acc.second -> {
                Pair(
                    acc.first + Integer.parseInt(match.groups[1]!!.value) * Integer.parseInt(match.groups[2]!!.value),
                    true
                )
            }
            else -> acc
        }
    }.first

fun main() {
    val testInput = readInput("day03/test")
    checkResult(part1(testInput), 161)

    val testInput2 = readInput("day03/test2")
    checkResult(part2(testInput2), 48)

    val input = readInput("day03/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
