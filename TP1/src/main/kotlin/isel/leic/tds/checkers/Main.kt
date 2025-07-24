package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.ui.*
import isel.leic.tds.storage.*
import readLineCommand

fun main() {
    val storage = TextFileStorage<Name,Game>("games",GameSerializer)
    var clash = Clash(storage)
    val cmds: Map<String, Command> = getCommands()
    while (true) {
        val (name, args) = readLineCommand()
        val cmd = cmds[name]
        if (cmd == null) println("Unknown command $name")
        else try {
            clash = cmd.execute(args, clash)
            if (cmd.toTerminate) break
            (clash as ClashRun).show()
        } catch (e: IllegalStateException) {
            println(e.message)
        } catch (e: IllegalArgumentException) {
            println("${e.message}. Use: $name ${cmd.syntaxArgs}")
        }
    }
}



