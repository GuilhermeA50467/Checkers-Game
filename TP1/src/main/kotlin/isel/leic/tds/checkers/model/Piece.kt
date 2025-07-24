package isel.leic.tds.checkers.model

abstract class Piece(val player: Player, val square: Square) {
    abstract fun canMove(from: Square, to: Square, board: Board): Boolean
    abstract fun canCapture(from:Square, to: Square, board: Board): Boolean

    override fun toString(): String = "$player:${if (this is Queen) "Queen" else "Pawn"}"

    fun promoteIfNeeded(to: Square): Piece =
        if ((player == Player.b && to.row.index == BOARD_DIM-1) || (player == Player.w && to.row.index == 0)) Queen(player, to)
        else this
}