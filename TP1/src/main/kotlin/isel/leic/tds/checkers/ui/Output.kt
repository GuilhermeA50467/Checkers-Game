package isel.leic.tds.checkers.ui
import isel.leic.tds.checkers.model.*

fun ClashRun.show() {
    val board = checkNotNull(this.game.board) { "No board" }
    when (board) {
        is BoardRun -> displayBoard(board, this)
        is BoardWin -> {
            displayBoard(board)
            println("  Winner: ${board.winner}")
        }
    }
}

fun Board.show() {
    when (this) {
        is BoardRun -> displayBoard(this)
        is BoardWin -> {
            displayBoard(this)
            println("  Winner: ${this.winner}")
        }
    }
}

private val sepLine = "  +${"-".repeat(BOARD_DIM * 2 - 1)}+"

private fun displayBoard(board: Board, clash: ClashRun? = null) {
    val turn = if (board is BoardRun) "  Turn: ${board.turn}" else ""
    println("$sepLine$turn")
    for (row in Row.values) {
        print("${row.digit} |")
        for (col in Column.values) {
            val square = Square(row, col)
            val pieceChar = when (val piece = board.moves[square]) {
                is Queen -> if (piece.player == Player.b) 'B' else 'W'
                is Pawn -> if (piece.player == Player.b) 'b' else 'w'
                else -> if (square.black) '-' else ' '
            }
            print(pieceChar)
            if (col != Column.values.last()) print(" ")
        }
        if (clash != null && row == Row.values.first()) println("|  Player = ${clash.sidePlayer}")
        else println("|")
    }
    println(sepLine)
    print("   ")
    Column.values.forEach { col -> print("${col.symbol} ") }
    println()
}