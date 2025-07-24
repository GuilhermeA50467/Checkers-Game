package isel.leic.tds.checkers.model
import isel.leic.tds.checkers.model.Diagonal.isDiagonal
import isel.leic.tds.checkers.model.Diagonal.opponentsPiece
import isel.leic.tds.checkers.model.Diagonal.myPieces

class Queen(player: Player, square: Square) : Piece(player, square) {
    override fun canMove(from: Square, to: Square, board: Board) =
        isDiagonal(from,to) && myPieces(from, to, board) == 0 && opponentsPiece(from, to, board) == 0

    override fun canCapture(from: Square, to: Square, board: Board): Boolean {
        if (!isDiagonal(from,to) || board.moves[to] != null) return false
        return opponentsPiece(from, to, board) == 1 && myPieces(from, to, board) == 0
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Queen) return false
        return player == other.player && square == other.square
    }

    override fun hashCode() = player.hashCode() * 31 + square.hashCode()
}



