package isel.leic.tds.checkers.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.model.Diagonal.possibleMoves
import isel.leic.tds.storage.MongoStorage
import isel.leic.tds.storage.TextFileStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import isel.tds.storage.MongoDriver
import isel.tds.storage.deleteDocument
import isel.tds.storage.getAllDocuments
import isel.tds.storage.getDocument

class CheckersVM(val scope: CoroutineScope, driver:MongoDriver) {
    //val storage: TextFileStorage<Name, Game> = TextFileStorage("games", GameSerializer)
    val storage = MongoStorage<Name,Game>("games",driver,GameSerializer)

    // Model State
    private var clash: Clash by mutableStateOf(Clash(storage))
    var square: Square? by mutableStateOf(null)

    val hasClash: Boolean get() = clash is ClashRun
    val board: Board? get() = (clash as? ClashRun)?.game?.board
    val possibleMoves: List<Square> get() = square?.let { possibleMoves(it, board!!) } ?: emptyList()
    val sidePlayer: Player? get() = (clash as? ClashRun)?.sidePlayer
    private val isSideTurn get() = clash.isSideTurn

    fun play(pos: Square) {
        if (board !is BoardRun) return

        val currentBoard = board as BoardRun
        val selectedPiece = currentBoard.moves[pos]

        if (square == null || (selectedPiece != null)) {
            if (selectedPiece?.player == sidePlayer) {
                square = pos
                hideError()
            } else square = null
        } else square?.let { exec { play(it,pos) } }.also { square = null }
        waitForOther()
    }

    // UI State
    var message: String? by mutableStateOf(null)
        private set
    var showTargets: Boolean by mutableStateOf(true)
    var autorefresh: Boolean by mutableStateOf(true)

    fun hideError() { message = null }
    fun toggleShowTargets() { showTargets = !showTargets }
    fun toggleAutoRefresh() { autorefresh = !autorefresh }
    fun toogleONOFF(const: Boolean) =
        if(const) "ON" else "OFF"

    var action: Action? by mutableStateOf(null)
        private set

    fun refresh() = exec ( Clash::refresh )
    fun exit() {
        cancelWaiting()
        clash.exit()
    }
    fun start() { action = if (storage.docs.getAllDocuments().isNotEmpty()) Action.JOIN else Action.START }

    fun cancelAction() { action = null }
    fun doAction(name: Name) {
        exec{ when(action as Action) {
            Action.START -> start(name)
            Action.JOIN -> join(name)
        } }
        action = null
    }

    private fun exec( fx: Clash.()->Clash ) {
        try { clash = clash.fx() }
        catch(ex: Exception) {
            manageException(ex)
        }
    }
    private fun manageException(ex: Exception) {
        if (ex is IllegalArgumentException || ex is IllegalStateException) {
            message = ex.message
            if (ex is GameDeletedException)
                clash = Clash(storage)
        }
        else throw ex
    }

    private var waitingJob by mutableStateOf<Job?>(null)
    val isWaiting get() = waitingJob!=null

    private fun cancelWaiting() {
        waitingJob?.cancel()
        waitingJob = null
    }

    private fun waitForOther() {
        if (isSideTurn)  return
        waitingJob = scope.launch{
            while (autorefresh) {
                delay(5000)
                try {
                    clash = clash.refresh()
                    if (isSideTurn) break
                }
                catch (ex: NoModificationException) { /* ignore */ }
                catch (ex: Exception) { manageException(ex); break }
            }
            waitingJob = null
        }
    }
}