package isel.leic.tds.checkers.model

import kotlin.math.abs

object Diagonal {
    fun middleSquare(from: Square, to: Square): Square {
        val middleRow = (from.row.index + to.row.index) / 2
        val middleColumn = (from.column.index + to.column.index) / 2
        return Square(Row(middleRow), Column(middleColumn))
    }

    tailrec fun middleSquares(from: Square, to: Square, curr: Square = from, sq: List<Square> = emptyList()): List<Square> {
        return if (curr == to) sq else {
            val next = Square(
                Row(curr.row.index + getDirection(from, to).rowStep),
                Column(curr.column.index + getDirection(from, to).colStep)
            )
            middleSquares(from, to, next, sq + next)
        }
    }

    fun isDiagonal(from: Square, to: Square): Boolean =
        abs(from.row.index - to.row.index) == abs(from.column.index - to.column.index)

    fun isCaptureMove(from: Square, to: Square): Boolean =
        abs(from.row.index - to.row.index) == 2 && abs(from.column.index - to.column.index) == 2

    fun opponentsPiece(from: Square, to: Square, board: Board): Int {
        val piece = board.moves[from] ?: throw IllegalStateException("No piece found at $from")
        val middleSquares = middleSquares(from, to)
        val opponentPieces = middleSquares.filter { square -> board.moves[square]?.player == piece.player.other }
        return opponentPieces.size
    }

    fun myPieces(from: Square, to: Square, board: Board): Int {
        val piece = board.moves[from] ?: throw IllegalStateException("No piece found at $from")
        val middleSquares = middleSquares(from, to)
        val myPieces = middleSquares.filter { square -> board.moves[square]?.player == piece.player }
        return myPieces.size
    }

    fun possibleMoves(from: Square, board: Board): List<Square> {
        val possibleCaptures = mutableListOf<Square>()
        val possibleMoves = mutableListOf<Square>()
        val fromPiece = board.moves[from]
        for (to in Square.values) {
            when (fromPiece) {
                is Pawn -> {
                    if (fromPiece.canCapture(from, to, board)) possibleCaptures.add(to)
                    if (fromPiece.canMove(from, to, board)) possibleMoves.add(to)
                }
                is Queen -> {
                    if (fromPiece.canCapture(from, to, board)) possibleCaptures.add(to)
                    if (fromPiece.canMove(from, to, board)) possibleMoves.add(to)
                }
            }
        }
        return possibleCaptures.ifEmpty { possibleMoves }
    }
}
