package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.DefaultMarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

import isel.leic.tds.checkers.model.*

@Composable
fun StatusBar(board: Board?, sidePlayer: Player?) {
    Box(
        modifier = Modifier
            .height(BORDER_WIDTH * 2)
            .width(GRID_WIDTH + BORDER_WIDTH * 2)
            .background(Color(ORANGE))
    ) {
        val state = when (board) {
            is BoardRun -> "Player: $sidePlayer"
            is BoardWin -> "Winner: ${board.winner}"
            null -> "No board"
        }
        val turn = when (board) {
            is BoardRun -> if (board.turn == sidePlayer) "Your turn" else "Opponent's turn"
            else -> ""
        }

        Row(
            modifier = Modifier.matchParentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Game: Demo", fontSize = fontSize, fontWeight = FontWeight.Bold, modifier = Modifier.offset(x = BORDER_WIDTH))
            Text(text = turn, fontSize = fontSize, fontWeight = FontWeight.Bold, modifier = Modifier.offset(x = -BORDER_WIDTH))
        }

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = state, fontSize = fontSize, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
@Preview
fun StatusBarPreview() {
    StatusBar(BoardRun(Player.b), Player.w)
}