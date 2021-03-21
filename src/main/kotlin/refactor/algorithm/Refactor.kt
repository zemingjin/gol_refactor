package refactor.algorithm

import refactor.algorithm.Cell.Companion.toName

class Refactor(private val livingCellsMap: Map<String, Cell>) {
    fun isLivingCell(x: Int, y: Int) = toName(x, y).isLivingCell
    val livingCells get() = livingCellsMap.values
    fun tick() = Refactor(nextMap)
    val neighbouringDeadCells get() = livingCells.flatMap{ it.neighbours }.filter{ it.isDeadCell }.distinct()

    private val String.isLivingCell get() = livingCellsMap[this] is Cell
    private val nextMap get() = (nextGenerationCells + reproducibleCells).map{(it.name to it)}.toMap()
    private val nextGenerationCells get() = livingCells.getFilteredCells{ it in 2..3 }
    private val reproducibleCells get() = neighbouringDeadCells.getFilteredCells{ it == 3 }
    private fun Collection<Cell>.getFilteredCells(test: (Int) -> Boolean) = filter { test(it.numberOfLivingNeighbours) }
    private val Cell.numberOfLivingNeighbours get() = neighbours.filter { it.name.isLivingCell }.count()
    private val Cell.isDeadCell get() = !name.isLivingCell
}