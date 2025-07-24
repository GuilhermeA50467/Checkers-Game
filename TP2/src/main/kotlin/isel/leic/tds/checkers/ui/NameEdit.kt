package isel.leic.tds.checkers.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import isel.leic.tds.checkers.model.Name

enum class Action(val text: String) {
    START("Start"),
    JOIN("Join")
}

@Composable
fun NameEdit(
    onCancel: ()->Unit,
    onAction: (Name)->Unit
) {
    var txt by remember{ mutableStateOf("") }
    AlertDialog(
        title = { Text("Name to Start/Join", style = MaterialTheme.typography.h4) },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                enabled = Name.isValid(txt),
                onClick = { onAction(Name(txt)) }
            ) { Text("Start/Join") }
        },
        dismissButton = {
            TextButton(onClick = onCancel) { Text("Cancel") }
        },
        text = {
            OutlinedTextField(txt, onValueChange = { txt = it }, label = { Text("Name") })
        }
    )
}