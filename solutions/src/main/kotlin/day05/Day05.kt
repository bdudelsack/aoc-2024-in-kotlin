package day05

import utils.checkResult
import utils.readInput

fun checkUpdate(rules: Map<Int, Set<Int>>, update: List<Int>): Boolean {
    update.forEachIndexed { index, i ->
        val currentRules = rules.getOrDefault(i, emptyList())
        currentRules.forEach { successor ->
            val predecessors = update.subList(0, index)
            if(predecessors.contains(successor)) {
                return false
            }
        }
    }

    return true
}

fun readInput(lines: List<String>): Pair<Map<Int, Set<Int>>, List<List<Int>>> {
    val split = lines.indexOfFirst { it.isBlank() }
    val rules = lines.subList(0, split).map { it.split("|").map(String::toInt) }.groupBy { it.first() }.mapValues {
        it.value.map { it[1] }.toSet()
    }

    val updates = lines.subList(split + 1, lines.size).map { it.split(",").map(String::toInt)  }

    return Pair(rules, updates)
}


fun part1(lines: List<String>): Int {
    val (rules, updates) = readInput(lines)
    return updates.filter { checkUpdate(rules, it) }.sumOf { it[it.size / 2] }
}

fun part2(lines: List<String>): Int {
    val (rules, updates) = readInput(lines)
    val wrong = updates.filter { !checkUpdate(rules, it) }

    val comparator: Comparator<Int> = Comparator { a, b -> when {
        rules[a]?.contains(b) == true -> -1
        else -> 0
    }}

    return wrong.map {
        it.sortedWith(comparator)
    }.map { it[it.size / 2] }.sum()
}


fun main() {
    val testInput = readInput("day05/test")
    checkResult(part1(testInput), 143)
    checkResult(part2(testInput), 123)

    val input = readInput("day05/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
