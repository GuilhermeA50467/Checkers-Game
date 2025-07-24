package isel.leic.tds.checkers.model

enum class Direction(val rowStep: Int, val colStep: Int) {
    UP_RIGHT(-1, 1),
    UP_LEFT(-1, -1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);
}

fun getDirection(from: Square, to: Square): Direction = when {
        from.row.index < to.row.index && from.column.index < to.column.index -> Direction.DOWN_RIGHT
        from.row.index < to.row.index && from.column.index > to.column.index -> Direction.DOWN_LEFT
        from.row.index > to.row.index && from.column.index < to.column.index -> Direction.UP_RIGHT
        else -> Direction.UP_LEFT
    }
