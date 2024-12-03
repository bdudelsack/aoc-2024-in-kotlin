package dayXX

import utils.checkResult
import utils.readInput

val pattern = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
val patternWithInstructions = Regex("""mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""")

fun part1(lines: List<String>): Int {
    val matches = pattern.findAll(lines.joinToString()).toList()

    val res = matches.sumOf {
        Integer.parseInt(it.groups[1]!!.value) * Integer.parseInt(it.groups[2]!!.value)
    }

    return res
}

fun part2(lines: List<String>): Int {
    val matches = patternWithInstructions.findAll(lines.joinToString()).toList()
    var enabled = true
    var sum = 0

    matches.forEach { match ->
        when {
            match.groups[0]!!.value.startsWith("don't") -> { enabled = false }
            match.groups[0]!!.value.startsWith("do") -> { enabled = true }
            enabled -> {
                sum += Integer.parseInt(match.groups[1]!!.value) * Integer.parseInt(match.groups[2]!!.value)
            }
        }
    }

    return sum
}

fun main() {
    val testInput = readInput("day03/test")
    checkResult(part1(testInput), 161)

    val testInput2 = readInput("day03/test2")
    checkResult(part2(testInput2), 48)

    val input = readInput("day03/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
