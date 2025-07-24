package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.model.Diagonal.middleSquares
import isel.leic.tds.checkers.model.Diagonal.isDiagonal
import isel.leic.tds.checkers.model.Diagonal.opponentsPiece
import isel.leic.tds.checkers.model.Diagonal.isCaptureMove

const val BOARD_DIM = 8
typealias Moves = Map<Square,Piece>

val defaultBoard = Square.values.mapNotNull { square ->
    when (square.row.index) {
        in 0 until (BOARD_DIM / 2 - 1) -> if (square.black) square to Pawn(Player.b, square) else null
        in (BOARD_DIM / 2 + 1) until BOARD_DIM -> if (square.black) square to Pawn(Player.w, square) else null
        else -> null
    }
}.toMap()

sealed class Board(val moves: Moves){
    override fun equals(other: Any?): Boolean = other is Board && moves == other.moves
    override fun hashCode() = moves.hashCode()
}
class BoardRun(val turn: Player, moves: Moves = defaultBoard) : Board(moves)
class BoardWin(moves: Moves, val winner: Player) : Board(moves)

fun Board.play(from: Square, to: Square): Board {
    when (this) {
        is BoardRun -> {
            val fromPiece = moves[from]
            check(fromPiece != null) { "No piece found at $from" }
            check(moves[to] == null) { "Position already used at $to" }
            check(fromPiece.player == turn) { "Invalid move, can only move your own pieces" }
            check(isDiagonal(from,to)) { "Invalid move, can only move to diagonals from $from to $to" }

            val canCapture = fromPiece.canCapture(from, to, this)
            if (hasCaptureAvailable())
                check(canCapture) { "A capture move is available" }

            if (!canCapture && fromPiece is Pawn )
                check(fromPiece.canMove(from, to, this)) { "Pawn invalid move from $from to $to" }

            if (!canCapture && fromPiece is Queen)
                check(fromPiece.canMove(from, to, this)) { "Queen invalid move from $from to $to" }

            val newBoard = if (canCapture) capturePiece(from, to) else movePiece(from, to)
            val nextTurn = if (canCapture && newBoard.canCaptureFrom(to)) turn else turn.other
            val winner = winnerIn(newBoard)

            return when {
                winner != null -> BoardWin(newBoard.moves,winner)
                else -> BoardRun(nextTurn, newBoard.moves)
            }
        }
        is BoardWin -> error("Game is over")
    }
}

/**
 * Verifica se há um vencedor
 */
private fun winnerIn(board: Board): Player? = when {
    board.moves.count { it.value.player == Player.w } == 0 -> Player.b
    board.moves.count { it.value.player == Player.b } == 0 -> Player.w
    else -> null
}

/**
 * Verifica se a peça pode capturar a partir de um square
 */
fun BoardRun.canCaptureFrom(square: Square): Boolean {
    for (to in Square.values) {
        val fromPiece = this.moves[square]
        if (fromPiece is Pawn && fromPiece.canCapture(square, to, this)) return true
        if (fromPiece is Queen && fromPiece.canCapture(square,to,this)) return true
    }
    return false
}

/**
 * Verifica se há captura disponível para todas as peças do jogador
 */
fun BoardRun.hasCaptureAvailable(): Boolean {
    return moves.any { (from, piece) ->
        piece.player == turn && Square.values.any { to ->
            if (piece is Queen) piece.canCapture(from, to, this) && moves[to] == null && opponentsPiece(from, to, this) != 0
            else (piece !is Pawn || piece.isMovingForward(from,to)) && isCaptureMove(from, to) && moves[to] == null && opponentsPiece(from, to, this) != 0
        }
    }
}

/**
 * Captura a peça adversária
 */
fun BoardRun.capturePiece(from: Square, to: Square): BoardRun {
    val middleSquares = middleSquares(from, to)
    val piece = moves[from] ?: throw IllegalStateException("No piece found at $from")
    val opponentPieceSquare = middleSquares.firstOrNull { square -> moves[square]?.player == turn.other }
        ?: throw IllegalStateException("No opponent piece to capture between $from and $to")
    return BoardRun(
        turn = turn,
        moves = moves - from - opponentPieceSquare + (to to piece.promoteIfNeeded(to)) // verifica se fica a rainha
    )
}

/**
 * Move a peça
 */
fun BoardRun.movePiece(from: Square, to: Square): BoardRun {
    val piece = moves[from] ?: throw IllegalStateException("No piece found at $from")
    return BoardRun(
        turn = turn.other,
        moves = moves - from + (to to piece.promoteIfNeeded(to))
    )
}
