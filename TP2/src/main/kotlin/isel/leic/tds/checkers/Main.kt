package isel.leic.tds.checkers

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.*
import isel.leic.tds.checkers.ui.*

import isel.tds.storage.MongoDriver

@Composable
@Preview
private fun CheckersApp(vm: CheckersVM) {
    MaterialTheme {
        Column {
            Grid(vm.board, onClickCell = { pos -> vm.play(pos) }, vm.square , vm.possibleMoves, vm.showTargets)
            StatusBar(vm.board, vm.sidePlayer)
        }
    }
    vm.action?.let { NameEdit(vm::cancelAction, vm::doAction ) }
    vm.message?.let { ErrorDialog(it, vm::hideError) }
    if (vm.isWaiting) Waiting()
}

@Composable
fun FrameWindowScope.CheckersMenu(vm: CheckersVM, onExit: ()->Unit) {
    MenuBar {
        Menu("Game") {
            Item("Start", onClick = vm::start)

            Item("Refresh", enabled = vm.hasClash, onClick = vm::refresh)
            Item("Exit", onClick = onExit )
        }
        Menu("Options") {
            Item("Show targets (${vm.toogleONOFF(vm.showTargets)})", enabled = vm.hasClash, onClick = vm::toggleShowTargets )
            Item("Auto-refresh (${vm.toogleONOFF(vm.autorefresh)})", enabled = vm.hasClash, onClick = vm::toggleAutoRefresh )
        }
    }
}

fun main() =
    MongoDriver("games").use { driver ->
    application(exitProcessOnExit = false) {
        val scope = rememberCoroutineScope()
        val vm = remember { CheckersVM(scope, driver) }
        val onExit = { vm.exit(); exitApplication() }
        Window(
            onCloseRequest = onExit,
            state = WindowState(size = DpSize.Unspecified),
            title = "Checkers"
        ) {
            CheckersMenu(vm, onExit)
            CheckersApp(vm)
        }
    }
}