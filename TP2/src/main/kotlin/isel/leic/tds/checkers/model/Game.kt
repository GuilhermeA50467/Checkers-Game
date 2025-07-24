package isel.leic.tds.checkers.model

data class Game(
    val board: Board? = null,
    val firstPlayer: Player = Player.entries.first(),
)

fun Game.new(): Game = Game(
    board = BoardRun(turn = Player.w),
    firstPlayer = firstPlayer.other
)

fun Game.play(from: Square, to: Square): Game {
    checkNotNull(board) { "No board" }
    val board = board.play(from, to)
    return copy(board = board)
}
