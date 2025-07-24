package isel.leic.tds.checkers.model

import isel.leic.tds.storage.*

object GameSerializer: Serializer<Game> {
    override fun serialize(data: Game) = buildString {
        appendLine(data.started)
        appendLine(data.firstPlayer)
        data.board?.let{ appendLine(BoardSerializer.serialize(it)) }
    }
    override fun deserialize(text: String): Game {
        val parts = text.split("\n")
        return Game(
            board = if (parts.size==2) null else BoardSerializer.deserialize(parts[2]),
            firstPlayer =  Player.valueOf(parts[1]),
            started = parts[0].toInt()
        )
    }
}

object BoardSerializer: Serializer<Board> {
    override fun serialize(data: Board): String {
        val boardType = when (data) {
            is BoardRun -> "RUN ${data.turn}"
            is BoardWin -> "WIN ${data.winner}"
        }
        val moves = data.moves.entries.joinToString(" ") { (pos, piece) ->
            val pieceType = when (piece) {
                is Pawn -> "Pawn"
                is Queen -> "Queen"
                else -> error("Unknown piece type")
            }
            "$pos:${piece.player},$pieceType"
        }
        return "$boardType | $moves"
    }

    override fun deserialize(text: String): Board {
        val (left, right) = text.split(" | ")
        val moves = if (right.isEmpty()) emptyMap<Square, Piece>() else right
            .split(" ").map { it.split(":") }
            .associate { (pos, pieceInfo) ->
                val (player, pieceType) = pieceInfo.split(",")
                val square = pos.toSquare()
                val piece = when (pieceType) {
                    "Pawn" -> Pawn(Player.valueOf(player), square)
                    "Queen" -> Queen(Player.valueOf(player), square)
                    else -> error("Unknown piece type $pieceType")
                }
                square to piece
            }
        val (type, player) = left.split(" ")
        return when (type) {
            "RUN" -> BoardRun(Player.valueOf(player), moves)
            "WIN" -> BoardWin(moves, Player.valueOf(player))
            else -> error("Illegal board type $type")
        }
    }
}