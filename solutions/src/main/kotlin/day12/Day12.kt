package day12

import utils.Line
import utils.Point
import utils.checkResult
import utils.map.Map2D
import utils.readInput

data class Tile(val pt: Point, val c: Char)
typealias GardenMap = Map2D<Tile>

fun readMap(lines: List<String>): GardenMap {
    return GardenMap.readFromLines(lines) { c, x, y -> Tile(Point(x,y), c)}
}

fun GardenMap.buildRegions(): List<Pair<Char, Set<Point>>> {
    val visited = mutableSetOf<Point>()
    val regions: MutableList<Pair<Char, Set<Point>>> = mutableListOf()

    forEach { tile ->
        if(!visited.contains(tile.pt)) {
            val region = buildRegion(tile.c, tile.pt, mutableSetOf())

            regions.add(Pair(tile.c, region))
            visited.addAll(region)
        }
    }

    return regions.toList()
}

fun GardenMap.buildRegion(c: Char, pt: Point, region: MutableSet<Point>): Set<Point> {
    region.add(pt)

    neighborsOrtho(pt).filter { get(it).c == c && !region.contains(it) }.forEach {
        region.addAll(buildRegion(c, it, region))
    }

    return region.toSet()
}

fun Set<Point>.calculatePrice(c: Char, map: GardenMap): Triple<Char, Int, List<Line>> {
    var area = 0
    val fences = mutableListOf<Line>()

    forEach { pt ->
        area++
        Map2D.OrthoDirection.entries.forEach { dir ->
            if(!map.isValid(pt + dir.pt) || map[pt + dir.pt].c != c) {
                fences.add(when(dir) {
                    Map2D.OrthoDirection.UP -> { Line(pt, pt + Point(1,0)) }
                    Map2D.OrthoDirection.LEFT -> { Line(pt + Point(0,1), pt) }
                    Map2D.OrthoDirection.RIGHT -> { Line(pt + Point(1,0), pt + Point(1,1)) }
                    Map2D.OrthoDirection.DOWN -> { Line(pt + Point(1,1), pt + Point(0,1)) }
                })
            }
        }
    }

    return Triple(c, area, fences)
}

fun List<Line>.connect(): List<Line> {
    val res = mutableListOf<Line>()
    val root = first()
    var current = root

    do {
        res.add(current)
        val next = this.find { it.start == current.end }
        current = next!!
    } while(current != root)

    return res.toList()
}

fun Line.isVertical(): Boolean {
    return start.x == end.x
}

fun List<Line>.corners(): Int {
    var res = 1
    val copy = toMutableList()
    var current = copy.minBy { it.start }

    while(copy.isNotEmpty()) {
        copy.remove(current)
        val next = copy.find { it.start == current.end }

        if(next == null) {
            if(copy.isNotEmpty()) {
                res += copy.corners()
            }
            break
        }

        if(current.isVertical() != next.isVertical()) {
            res++
        }

        current = next
    }

    return res
}


fun part1(lines: List<String>): Int {
    val map = readMap(lines)
    val regions = map.buildRegions()

    val res = regions.map { it.second.calculatePrice(it.first, map) }.sumOf { it.second * it.third.size }

    return res
}

fun part2(lines: List<String>): Int {
    val map = readMap(lines)
    val regions = map.buildRegions()

    val res = regions.map { it.second.calculatePrice(it.first, map) }.map {
        Triple(it.first, it.second, it.third.corners())
    }

    return res.sumOf { it.second * it.third }
}

fun main() {
    val testInput = readInput("day12/test")
    val testInput2 = readInput("day12/test2")
    val testInput3 = readInput("day12/test3")

    checkResult(part1(testInput), 140)
    checkResult(part1(testInput2), 772)
    checkResult(part1(testInput3), 1930)

    val testInput4 = readInput("day12/test4")
    val testInput5 = readInput("day12/test5")
    val testInput6 = readInput("day12/test6")

    checkResult(part2(testInput4), 80)
    checkResult(part2(testInput5), 236)
    checkResult(part2(testInput6), 368)
    checkResult(part2(testInput3), 1206)

    val input = readInput("day12/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
