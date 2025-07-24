package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.model.Player

@Composable
fun Player(piece: Piece?, onClick: () -> Unit = {}, modifier: Modifier = Modifier.size(100.dp)) {
    if (piece == null) {
        Box(modifier.clickable(onClick = onClick))
    } else {
        val file = when {
            piece is Queen && piece.player == Player.w -> "piece_wk"
            piece is Queen && piece.player == Player.b -> "piece_bk"
            piece is Pawn && piece.player == Player.w -> "piece_w"
            piece is Pawn && piece.player == Player.b -> "piece_b"
            else -> ""
        }
        Image(
            painter = painterResource("$file.png"),
            contentDescription = "Player $file",
            modifier = modifier
        )
    }
}

@Composable
@Preview
fun PlayerXPreview() {
    val square = Square(Row(0), Column(0))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Player(Pawn(Player.b, square), modifier = Modifier.background(Color.Yellow).size(100.dp))
    }
}
