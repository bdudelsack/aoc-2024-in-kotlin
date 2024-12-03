package day02

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import utils.checkResult
import utils.readInput
import kotlin.math.abs
import kotlin.math.sign

val t = Terminal()

data class Step(val current: Int, val next: Int) {
    val diff = next - current

    fun isSafe(direction: Int) = (diff.sign == direction) && abs(diff) in 1 .. 3
    fun canBeExcluded(direction: Int, prevStep: Step, nextStep: Step) = Step(prevStep.next, nextStep.next).isSafe(direction)
}

class StepSequence(private val layers: List<Int>) {
    private val direction = layers.windowed(2, 1)
        .map { (current, target) -> (target - current).sign }
        .groupBy { it }
        .mapValues { it.value.size }
        .toList()
        .sortedBy { it.second }.last().first

    private val steps: List<Step> = List(layers.size - 1) { i ->
        when(i) {
            0 -> Step(layers[i], layers[1])
            else -> Step(layers[i], layers[i + 1])
        }
    }

    fun visualize() {
        steps.forEachIndexed { index, step ->
            val char = directionToArrow(step.diff.sign)

            val firstOrLast = (index == 0 || index == layers.size - 2)
            val canBeExcluded = firstOrLast || step.canBeExcluded(direction, steps[index - 1], steps[index + 1])

            val color = when {
                step.isSafe(direction) -> TextColors.green
                canBeExcluded -> TextColors.yellow
                else -> TextColors.red
            }

            t.print("[${step.current} ${color(char)} ${step.next}] ")
        }

        t.print("${directionToArrow(direction)} ${isSafeWithDampener()} ${isSafeWithDampenerNotWorking()}")

        t.println()
    }

    fun isSafe() = steps.all { it.isSafe(direction) }

    fun isSafeWithDampener(): Boolean {
        if(isSafe()) return true

        val seqs = List(layers.size) { n ->
            val newLayers = layers.filterIndexed { index, _ ->  index != n}
            StepSequence(newLayers)
        }

        return seqs.any { it.isSafe() }
    }

    fun isSafeWithDampenerNotWorking(): Boolean  {
        var hasYellow = false

        steps.forEachIndexed { index, step ->
            val firstOrLast = (index == 0 || index == layers.size - 2)
            val canBeExcluded = firstOrLast || step.canBeExcluded(direction, steps[index - 1], steps[index + 1])

            if(!step.isSafe(direction)) {
                if((firstOrLast || canBeExcluded) && !hasYellow) {
                    hasYellow = true
                } else {
                    return false
                }
            }
        }

        return true
    }

    private fun directionToArrow(direction: Int) = when(direction) {
        1 -> "↑"
        -1 -> "↓"
        else -> "→"
    }
}

fun part1(lines: List<String>): Int {
    val steps = lines.map { StepSequence(it.split(" ").map { it.toInt() }) }

    return steps.count { it.isSafe() }
}


fun part2(lines: List<String>): Int {
    val steps = lines.map { StepSequence(it.split(" ").map { it.toInt() }) }

    steps.filter { it.isSafeWithDampener() != it.isSafeWithDampenerNotWorking() }.forEach {
        it.visualize()
    }



    return steps.count { it.isSafeWithDampener() }
}

fun main() {

    val testInput = readInput("day02/test")
    checkResult(part1(testInput), 2)
    checkResult(part2(testInput), 4)

    val input = readInput("day02/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
