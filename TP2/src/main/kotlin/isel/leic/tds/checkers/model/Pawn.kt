package isel.leic.tds.checkers.model
import isel.leic.tds.checkers.model.Diagonal.isCaptureMove
import isel.leic.tds.checkers.model.Diagonal.isDiagonal
import kotlin.math.abs

class Pawn(player: Player, square: Square) : Piece(player, square) {
    override fun canMove(from: Square, to: Square, board: Board): Boolean =
        isDiagonal(from,to) && board.moves[to] == null && isMovingForward(from, to) && abs(from.row.index - to.row.index) == 1
                && !canCapture(from, to, board)

    override fun canCapture(from: Square, to: Square, board: Board): Boolean {
        if (!isMovingForward(from, to)) return false
        val midPiece = board.moves[Diagonal.middleSquare(from,to)] ?: return false
        return isDiagonal(from,to) && board.moves[to] == null && midPiece.player == player.other && isCaptureMove(from,to)
    }

    fun isMovingForward(from: Square, to: Square): Boolean =
        (player == Player.b && to.row.index > from.row.index) || (player == Player.w && to.row.index < from.row.index)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pawn) return false
        return player == other.player && square == other.square
    }

    override fun hashCode() = player.hashCode() * 31 + square.hashCode()
}


