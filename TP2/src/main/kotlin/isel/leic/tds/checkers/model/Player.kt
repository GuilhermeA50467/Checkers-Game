package isel.leic.tds.checkers.model

enum class Player {
    b, w;
    val other get() = if (this== b) w else b
}