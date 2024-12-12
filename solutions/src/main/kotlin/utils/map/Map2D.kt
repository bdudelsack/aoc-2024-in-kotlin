package utils.map

import utils.Point

/**
 * Generic 2D Map Class
 */
open class Map2D<T>(initData: List<List<T>>): Iterable<T> {
    private val data = initData.map { it.toMutableList() }.toMutableList()
    val height: Int get() = data.size
    val width: Int get() = data.first().size

    val rows get() = RowsAccess(this)
    val cols get() = ColsAccess(this)

    operator fun get(pt: Point) = this[pt.x, pt.y]
    operator fun get(x: Int, y: Int) = data[y][x]
    operator fun set(pt: Point, value: T) = value.also { this[pt.x,pt.y] = it }
    operator fun set(x: Int, y: Int, value: T) = value.also { data[y][x] = it }

    fun print() {
        println(toString())
    }

    override fun toString(): String {
        val sb = StringBuilder()

        for(y in 0 until height) {
            for(x in 0 until width) {
                sb.append(get(x,y))
                sb.append(" ")
            }
            sb.appendLine()
        }

        return sb.toString()
    }

    fun contains(pt: Point) = pt.x in 0 until  width && pt.y in 0 until  height

    fun<R> map(transform: (T, Int, Int) -> R): Map2D<R> {
        val data = data.mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                transform(c,x,y)
            }
        }

        return Map2D(data)
    }

    fun forEach(block: (c: T, x: Int, y: Int) -> Unit) {
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                block(c,x,y)
            }
        }
    }

    fun findPoint(predicate: (T) -> Boolean): Point? {
        return indexedIterator().asSequence().firstOrNull { predicate(it.second) }?.first
    }

    fun findPoints(predicate: (T) -> Boolean): Sequence<Pair<Point, T>> {
        return indexedIterator().asSequence().filter { predicate(it.second) }
    }

    fun isValid(pt: Point) = pt.x >= 0 && pt.y >= 0 && pt.x < width && pt.y < height
    fun clone(): Map2D<T> = Map2D(data)

    fun neighbors(pt: Point) = Direction.entries.map { pt + it.pt }.filter { isValid(it) }
    fun neighborsOrtho(pt: Point) = OrthoDirection.entries.map { pt + it.pt }.filter { isValid(it) }

    fun rows() = RowsAccess(this)
    fun cols() = ColsAccess(this)

    companion object {
        fun<T> readFromLines(lines: List<String>, transform: (Char, Int, Int) -> T): Map2D<T> {
            val data = lines.mapIndexed { y,line -> line.mapIndexed { x, char -> transform(char,x,y) } }
            return Map2D(data)
        }

        fun readFromLines(lines: List<String>): Map2D<Char> {
            val data = lines.mapIndexed { y,line -> line.mapIndexed { x, char -> char } }
            return Map2D(data)
        }

        fun<T> create(width: Int, height: Int, init: (x: Int, y: Int) -> T): Map2D<T> {
            val data = List(height) { y -> List(width) { x -> init(x,y) } }
            return Map2D(data)
        }
    }

    override fun iterator(): Iterator<T> = MapIterator(this)
    fun indexedIterator() = MapIndexedIterator(this)

    enum class OrthoDirection(val pt: Point) {
        UP(Point(0,-1)),
        DOWN(Point(0,1)),
        LEFT(Point(-1,0)),
        RIGHT(Point(1,0))
    }

    enum class Direction(val pt: Point) {
        S(Point(0, 1)),
        N(Point(0, -1)),
        E(Point(1, 0)),
        W(Point(-1, 0)),

        NE(Point(1, -1)),
        SE(Point(1, 1)),
        SW(Point(-1, 1)),
        NW(Point(-1, -1))
    }
}
