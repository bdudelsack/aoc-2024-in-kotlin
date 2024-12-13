package day13

import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.LUDecomposition
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.util.ArithmeticUtils
import utils.Point
import utils.checkResult
import utils.readInput
import kotlin.math.roundToInt
import kotlin.math.roundToLong

operator fun Pair<Long, Long>.times(i: Long): Pair<Long, Long> {
    return Pair(this.first * i, this.second * i)
}

operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>): Pair<Long, Long> {
    return Pair(this.first + other.first, this.second + other.second)
}

class Game(input: List<String>, add: Long = 0) {
    private val buttonA: Pair<Long, Long>
    private val buttonB: Pair<Long, Long>
    private val prize: Pair<Long, Long>

    init {
        val buttonsRegex = Regex("X\\+([0-9]+), Y\\+([0-9]+)")
        val prizeRegex = Regex("X=([0-9]+), Y=([0-9]+)")

        val (a1,a2) = buttonsRegex.find(input[0])!!.destructured
        val (b1,b2) = buttonsRegex.find(input[1])!!.destructured

        buttonA = a1.toLong() to a2.toLong()
        buttonB = b1.toLong() to b2.toLong()

        val (p1,p2) = prizeRegex.find(input[2])!!.destructured

        prize = p1.toLong() + add to p2.toLong() + add
    }

    fun bruteforce(): Pair<Int, Int>? {
        repeat(100) { a ->
            repeat(100) { b ->
                if(buttonA * a.toLong() + buttonB * b.toLong() == prize) {
                    return a to b
                }
            }
        }

        return null
    }

    fun solve(): Pair<Long, Long>? {
        val matrix = Array2DRowRealMatrix(
            arrayOf(
                arrayOf(buttonA.first.toDouble(),buttonB.first.toDouble()).toDoubleArray(),
                arrayOf(buttonA.second.toDouble(),buttonB.second.toDouble()).toDoubleArray(),
            ),
            false
        )
        val solver = LUDecomposition(matrix).solver
        val constants = ArrayRealVector(arrayOf(
            prize.first.toDouble(),
            prize.second.toDouble()
        ))
        val solution = solver.solve(constants) ?: return null
        val res = solution.getEntry(0).roundToLong() to solution.getEntry(1).roundToLong()

        val verify = buttonA * res.first + buttonB * res.second
        if(verify != prize) {
            // Probably not a natural number solution
            return null
        }

        return res
    }

}

fun part1(lines: List<String>): Long {
    val games = lines.chunked(4)
        .mapNotNull { Game(it).solve() }


    return games.sumOf { 3 * it.first + it.second }
}

fun part2(lines: List<String>): Long {
    val games = lines.chunked(4)
        .mapNotNull { Game(it, 10000000000000).solve() }

    return games.sumOf { 3 * it.first + it.second }
}

fun main() {
    val testInput = readInput("day13/test")
    checkResult(part1(testInput), 480)
    checkResult(part2(testInput), 875318608908)

    val input = readInput("day13/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
