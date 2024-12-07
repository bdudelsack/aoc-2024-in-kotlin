package day07

import utils.checkResult
import utils.readInput

enum class Operator(val fn: (a: Long, b: Long) -> Long) {
    ADD({ a,b -> a + b }),
    MUL({ a, b -> a * b }),
    CON({ a, b -> (a.toString() + b.toString()).toLong()})
}

data class Equation(val result: Long, val numbers: List<Long>) {
    fun solve(sum: Long = numbers.first(), ops: List<Operator> = emptyList(), operators: List<Operator> = listOf(Operator.ADD, Operator.MUL)): Boolean {
        if(sum > result) return false
        if(ops.size == numbers.size - 1) return result == sum

        return operators.any { op -> solve(sum = op.fn(sum, numbers[ops.size + 1]), ops + op, operators) }
    }
}

fun readEquation(line: String): Equation {
    val (result, operands) = line.split(": ")
    return Equation(result.toLong(), operands.split(" ").map { it.toLong() })
}

fun part1(lines: List<String>): Long {
    val eqs = lines.map { readEquation(it) }
    return eqs.filter { it.solve() }.sumOf { it.result }
}

fun part2(lines: List<String>): Long {
    val eqs = lines.map { readEquation(it) }
    val (simple, hard) = eqs.partition { it.solve() }
    val add = hard.filter { it.solve(operators = Operator.entries)  }

    return (simple + add).sumOf { it.result }
}


fun main() {
    val testInput = readInput("day07/test")
    checkResult(part1(testInput), 3749)
    checkResult(part2(testInput), 11387)

    val input = readInput("day07/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
