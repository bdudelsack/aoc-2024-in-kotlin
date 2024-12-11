package day11

import utils.checkResult
import utils.readInput

fun part1(lines: List<String>): Int {
    var stones = lines.first().split(" ").map { it.toLong() }

    repeat(25) {
        println("Iteration #${it + 1} is ${stones.size}")
        println(stones)
        stones = stones.flatMap { stone ->
            when {
                stone == 0L -> listOf(1)
                stone.toString().length % 2 == 0 -> {
                    val middle = stone.toString().length / 2
                    listOf(
                        stone.toString().substring(0 until middle).toLong(),
                        stone.toString().substring(middle).toLong(),
                    )
                }
                else -> listOf(stone * 2024L)
            }
        }
    }

    return stones.size
}

fun MutableMap<Long, Long>.incByOrSet(key: Long, increment: Long) {
    if(containsKey(key)) {
        set(key, get(key)!! + increment)
    } else {
        set(key, increment)
    }
}

fun part2(lines: List<String>): Long {
    var stones = lines.first().split(" ").map { it.toLong() }.associateWith { 1L }

    repeat(75) { iteration ->
        val stonesClone = stones.toMutableMap()
        stones.entries.filter { it.value != 0L }.forEach { (key, count) ->
            stonesClone[key] = stonesClone[key]!! - count

            when {
                key == 0L -> { stonesClone.incByOrSet(1L, count) }
                key.toString().length % 2 == 0 -> {
                    val middle = key.toString().length / 2
                    val left = key.toString().substring(0 until middle).toLong()
                    val right = key.toString().substring(middle).toLong()

                    stonesClone.incByOrSet(left, count)
                    stonesClone.incByOrSet(right, count)
                }
                else -> {
                    stonesClone.incByOrSet(key * 2024L, count)
                }
            }
        }

        stones = stonesClone.filter { it.value != 0L }.toMap()
//        println("Iteration #${iteration + 1} is ${stones}")
    }

    return stones.values.sum()
}

fun main() {
    val testInput = readInput("day11/test")
//    checkResult(part1(testInput), 55312)
//    checkResult(part2(testInput), 55312)

    val input = readInput("day11/input")
//    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
