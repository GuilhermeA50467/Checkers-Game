package isel.leic.tds.checkers.model

@JvmInline
value class Row(val index: Int) {

    init {
        require(index in 0..<BOARD_DIM) { "Invalid row index: $index" }
    }

    val digit: Char get() = (BOARD_DIM - index).digitToChar()

    companion object {
        val values: List<Row> = (0..<BOARD_DIM).map { Row(it) }
    }
}
fun Char.toRowOrNull(): Row? {
    return if (this in '1'..'8') {
        val index = this - '1'
        Row((BOARD_DIM-1) - index)
    }
    else null
}