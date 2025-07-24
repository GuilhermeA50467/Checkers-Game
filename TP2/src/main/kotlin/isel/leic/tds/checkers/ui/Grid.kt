package isel.leic.tds.checkers.ui

import isel.leic.tds.checkers.model.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

const val DARK_GREEN = 0xFF006400
const val ORANGE = 0xFFFF7F00

val CELL_SIZE = 70.dp
val GRID_WIDTH = CELL_SIZE * BOARD_DIM
val BORDER_WIDTH = CELL_SIZE / 3
val fontSize = 18.sp

@Composable
@Preview
fun Grid(board: Board?, onClickCell: (Square) -> Unit, selectedSquare: Square?, possibleMoves: List<Square>, show: Boolean = false) {
    Box(
        modifier = Modifier
            .size(GRID_WIDTH + BORDER_WIDTH * 2, GRID_WIDTH + BORDER_WIDTH)
            .background(Color(ORANGE))
    ) {
        Column {
            Row {
                Spacer(modifier = Modifier.width(BORDER_WIDTH))
                (0 until BOARD_DIM).forEach { colIndex ->
                    Box(
                        modifier = Modifier
                            .width(CELL_SIZE)
                            .height(BORDER_WIDTH),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ('a' + colIndex).toString(),
                            fontSize = fontSize,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(BORDER_WIDTH))
            }
            Row{
                Column(
                    modifier = Modifier
                        .height(GRID_WIDTH)
                        .width(BORDER_WIDTH),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    (BOARD_DIM downTo 1).forEach { number ->
                        Box(
                            modifier = Modifier
                                .height(CELL_SIZE)
                                .width(BORDER_WIDTH),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = number.toString(), fontSize = fontSize, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
                GridBoard(board, onClickCell, selectedSquare, possibleMoves, show)
            }
        }
    }
}

@Composable
fun GridBoard(board: Board?, onClickCell: (Square) -> Unit, selectedSquare: Square?, possibleMoves: List<Square>, show: Boolean) {
    Column(
        modifier = Modifier
            .width(GRID_WIDTH)
    ) {
        for (row in 0 until BOARD_DIM) {
            Row(modifier = Modifier.height(CELL_SIZE)) {
                for (col in 0 until BOARD_DIM) {
                    val square = Square(Row(row), Column(col))
                    val piece = board?.moves?.get(square)
                    val isSelected = selectedSquare == square
                    val isPossibleMove = possibleMoves.contains(square)
                    Box(
                        modifier = Modifier
                            .size(CELL_SIZE)
                            .background(if (square.black) Color.DarkGray else Color.LightGray)
                            .border(4.dp, if (isSelected && show) Color.Red else Color.Transparent)
                            .clickable { onClickCell(square) }
                    ) {
                        if (isPossibleMove && show) {
                            Box(
                                modifier = Modifier
                                    .size(CELL_SIZE - 12.dp)
                                    .background(Color(DARK_GREEN), shape = CircleShape)
                                    .align(Alignment.Center)
                            )
                        }
                        piece?.let {
                            Player(
                                piece = it,
                                onClick = { onClickCell(square) },
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun GridPreview() {
    Grid(BoardRun(Player.b), {}, null, emptyList())
}
