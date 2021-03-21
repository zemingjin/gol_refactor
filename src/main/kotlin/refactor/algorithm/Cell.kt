package refactor.algorithm

open class Cell(val x: Int, val y: Int) : Comparable<Cell> {
    @JvmField
    val name = toName(x, y)
    val neighbours: List<Cell> get() = (y - 1..y + 1).flatMap { it.rowNeighbours }

    private val Int.rowNeighbours get() = (x - 1..x + 1).filter { it != x || this != y }.map { Cell(it, this) }

    override fun equals(other: Any?) = other is Cell && x == other.x && y == other.y
    override fun hashCode() = name.hashCode()
    override fun toString() = name
    override fun compareTo(other: Cell) = name.compareTo(other.name)

    companion object {
        @JvmStatic
        fun toName(x: Int, y: Int) = "$x|$y"
    }
}