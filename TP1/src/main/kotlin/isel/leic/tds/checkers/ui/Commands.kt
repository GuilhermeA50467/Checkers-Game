package isel.leic.tds.checkers.ui
import isel.leic.tds.checkers.model.*

class Command(
    val syntaxArgs: String = "",
    val toTerminate: Boolean = false,
    val execute: (args: List<String>, clash: Clash)-> Clash = { _,clash -> clash }
)

val playCommand = Command("<from> <to>") { args, clash ->
    val from = requireNotNull(args.firstOrNull()) { "Missing <from> argument" }
    val to = requireNotNull(args.getOrNull(1)) { "Missing <to> argument" }
    clash.play(from.toSquare(), to.toSquare())
}

fun nameCmd(fx: Clash.(Name)-> Clash) = Command("<name>") { args, clash ->
    val arg = requireNotNull(args.firstOrNull()) { "Missing name" }
    clash.fx(Name(arg))
}

fun getCommands() = mapOf(
    "START" to nameCmd { name -> if (st.read(name) == null) start(name).newBoard() else join(name) },
    "PLAY" to playCommand,
    "EXIT" to Command(toTerminate = true) { _, clash ->
        if (clash is ClashRun) {
            val updatedGame = clash.game.copy(started = clash.game.started - 1)
            clash.st.update(clash.id, updatedGame)
        }
        clash.also { println("Bye!") }
    },
    "REFRESH" to Command { _, clash -> clash.refresh() },
    "GRID" to Command { _, clash -> clash.refresh() }
)