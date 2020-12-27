package tictactoe

fun main() {
    Game.init()
}

object Game {
    var s = "         ".toCharArray()
    var next = 'X'

    fun init() {
        makeMove()
    }
    private fun printField() {
        val field = """
            ---------
            | ${s[0]} ${s[1]} ${s[2]} |
            | ${s[3]} ${s[4]} ${s[5]} |
            | ${s[6]} ${s[7]} ${s[8]} |
            ---------
        """.trimIndent()
        println(field)
    }
    private fun makeMove() {
        print("Enter the coordinates:")
        val move = readLine()!!.split(" ").map {
            var c = it.toIntOrNull()
            when (c) {
                !is Int -> {
                    println("You should enter numbers!")
                    makeMove()
                    return
                }
                !in 1..3 -> {
                    println("Coordinates should be from 1 to 3!")
                    makeMove()
                    return
                }
                else -> c - 1
            }
        }.toTypedArray()
        val x = move[1]
        val y = move[0]

        if (s[x + 3 * y] != ' ') {
            println("This cell is occupied! Chose another one!")
            makeMove()
            return
        } else {
            s[x + 3 * y] = next
            next = if (next == 'X') 'O' else 'X'
            if (!isEnded()) makeMove()
        }
    }

    private fun isEnded():Boolean {
        
        if ((s[0] != ' ' && s[0] == s[1] && s[0] == s[2]) ||
                (s[0] != ' ' && s[0] == s[3] && s[0] == s[6]) ||
                (s[0] != ' ' && s[0] == s[4] && s[4] == s[8]))  {
            printField()
            println("${s[0]} wins")
            return true
        }

        if ((s[1] != ' ' && s[1] == s[4] && s[1] == s[7])) {
            printField()
            println("${s[1]} wins")
            return true
        }

        if ((s[2] != ' ' && s[2] == s[5] && s[2] == s[8])  ||
                (s[2] != ' ' && s[2] == s[4] && s[4] == s[6])) {
            printField()
            println("${s[2]} wins")
            return true
        }

        if (' ' !in s) {
            printField()
            println("Draw!")
            return true
        }

        printField()
        return false
    }
}