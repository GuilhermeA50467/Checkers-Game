package isel.leic.tds.checkers.model

data class Game(
    val board: Board? = null,
    val firstPlayer: Player = Player.entries.first(),
    val started : Int = 0
)

fun Game.new(): Game = Game(
    board = BoardRun(turn = firstPlayer),
    firstPlayer = firstPlayer.other,
    started = 1
)

fun Game.play(from: Square, to: Square): Game {
    checkNotNull(board) { "No board" }
    val board = board.play(from, to)
    return copy(
        board = board,
    )
}
