package isel.leic.tds.checkers.model

private val ColumnArray = arrayOf('a','b','c','d','e','f','g','h')

@JvmInline
value class Column (val index:Int){
    init {
        require(index in 0..<BOARD_DIM) { "Invalid column index: $index" }
    }

    val symbol get() = ColumnArray[index]

    companion object {
        val values = Array(BOARD_DIM) { Column(it) }
    }
}

fun Char.toColumnOrNull(): Column? =
    if(ColumnArray.indexOf(this.lowercaseChar()) != -1) Column(ColumnArray.indexOf(this.lowercaseChar())) else null