package day01

import utils.checkResult
import utils.readInput
import kotlin.math.abs

fun part1(lines: List<String>): Int {
    val pairs = lines.map { it.split("   ") }

    val left = pairs.map { it.first().toInt() }.sorted()
    val right = pairs.map { it.last().toInt() }.sorted()

    val scores = left.mapIndexed { index, i ->
        abs(i - right[index])
    }

    return scores.sum()
}

fun part2(lines: List<String>): Int {

    val pairs = lines.map { it.split("   ") }
    val right = pairs.map { it.last().toInt() }.groupBy { it }.mapValues { it.value.size }

    return pairs.map { it.first().toInt() }.map {
        val score = right.getOrDefault(it, 0)
        it * score
    }.sum()
}

fun main() {
    val testInput = readInput("day01/test")
    checkResult(part1(testInput), 11)
    checkResult(part2(testInput), 31)

    val input = readInput("day01/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
