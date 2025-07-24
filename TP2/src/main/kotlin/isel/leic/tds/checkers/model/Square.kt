package isel.leic.tds.checkers.model

class Square private constructor(val index: Int) {
    init {
        require(index in 0 until BOARD_DIM * BOARD_DIM) { "Invalid Square index: $index" }
    }

    val row get() = Row(index / BOARD_DIM)
    val column get() = Column(index % BOARD_DIM)
    val black get() = (row.index + column.index) % 2 == 1

    companion object {
        val values = List(BOARD_DIM * BOARD_DIM) { Square(it) }
    }

    override fun toString() = "${row.digit}${column.symbol}"
}

fun String.toSquareOrNull(): Square? {
    if (this.length != 2) return null
    val row = this[0].toRowOrNull() ?: return null
    val column = this[1].toColumnOrNull() ?: return null
    return Square(row, column)
}

fun String.toSquare() =
    toSquareOrNull() ?: throw IllegalArgumentException("Invalid square: $this")

fun Square(row: Row, column: Column) =
    Square.values[row.index * BOARD_DIM + column.index]