package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorDialog(
    message: String,
    onClose: ()->Unit,
) = AlertDialog(
    onDismissRequest = onClose,
    confirmButton = {
        TextButton(onClick = onClose) { Text("Ok") }
    },
    text = { Text(message, style = MaterialTheme.typography.body1) }
)


@Composable
@Preview
fun ErrorDialogPreview() {
    ErrorDialog("This is an error message", {})
}