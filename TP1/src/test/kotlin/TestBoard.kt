import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.ui.show
import kotlin.test.*

class TestBoard {
    private fun playSequence(moves: String): Board {
        var board: Board = BoardRun(turn = Player.w)
        val moveList = moves.split(", ")
        for (move in moveList) {
            val (from, to) = move.split("-").map { it.toSquare() }
            board = board.play(from, to)
        }
        return board
    }

    @Test
    fun `test moves and captures`(){
        val board = playSequence(
            "3A-4B, 6D-5C, 4B-6D, 7E-5C"
        )
        board.show()
    }

    @Test
    fun `test avoiding capture`(){
        var board = playSequence(
            "3A-4B, 6D-5C"
        )
        assertFailsWith<IllegalStateException>{ board = board.play("4B".toSquare(), "5A".toSquare()) }
        board.show()
    }

    @Test
    fun `move two diagonal squares and to a none diagonal Square`(){
        var board: Board = BoardRun(turn = Player.w)
        assertFailsWith<IllegalStateException>{ board = board.play("3A".toSquare(), "5C".toSquare()) }
        board.show()
        assertFailsWith<IllegalStateException>{ board = board.play("3A".toSquare(), "5A".toSquare()) }
        board.show()
    }

    @Test
    fun `test double capture`(){
        var board = playSequence(
            "3A-4B, 6D-5C, 4B-6D, 7E-5C, 2B-3A, 7C-6D, 3G-4H, 5C-4D, 3E-5C"
        )
        board.show()
        assertEquals((board as BoardRun).turn, Player.w)
        board = board.play("5C".toSquare(), "7E".toSquare())
        board.show()
        assertEquals((board as BoardRun).turn, Player.b)
    }

    @Test
    fun `test Queen`(){
        var board = playSequence(
            "3A-4B, 6D-5C, 4B-6D, 7E-5C, 2B-3A, 7C-6D, 3G-4H, 5C-4D, 3E-5C, 5C-7E, 8F-6D, 4H-5G, 6F-4H, 2H-3G, "
                    + "6B-5A, 3G-4F, 8D-7C, 2D-3E, 5A-4B, 3A-5C, 5C-7E, 7C-6B, 7E-8D, 7G-6F"
        )
        board.show()
        assertFailsWith<IllegalStateException> { board = board.play("8D".toSquare(), "7C".toSquare()) }
        assertFailsWith<IllegalStateException> { board = board.play("1A".toSquare(), "2B".toSquare()) }
        board = board.play("8D".toSquare(), "5A".toSquare())
        board.show()
    }

    @Test
    fun `Board Win`(){
        val board = playSequence(
            "3A-4B, 6D-5C, 4B-6D, 7E-5C, 2B-3A, 7C-6D, 3G-4H, 5C-4D, 3E-5C, 5C-7E, 8F-6D, 4H-5G, 6F-4H, 2H-3G, "
                    + "6B-5A, 3G-4F, 8D-7C, 2D-3E, 5A-4B, 3A-5C, 5C-7E, 7C-6B, 7E-8D, 7G-6F, 8D-5A, 8H-7G, 1E-2D, "
                    + "6H-5G, 4F-6H, 6H-8F, 4H-3G, 2F-4H, 7A-6B, 5A-8D, 8D-5G, 8B-7C, 5G-8D, 7C-6B, 8D-5A"

        )
        board.show()
        assertTrue { board is BoardWin }
    }
}